package com.messaggi.messaging.external.connection;

import javax.net.ssl.SSLSocketFactory;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.DefaultPacketExtension;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.external.connection.MessagingServiceConnection;
import com.messaggi.external.message.SendMessageRequest;
import com.messaggi.external.message.SendMessageResponse;
import com.messaggi.external.message.exception.SendMessageException;

public class AndroidConnectionXMPP implements MessagingServiceConnection
{
    private ApplicationPlatform applicationPlatform;

    public static final String GCM_ELEMENT_NAME = "gcm";

    public static final String GCM_NAMESPACE = "google:mobile:data";

    private static final String GCM_SERVER = "http://gcm.googleapis.com";

    private static final int GCM_PORT = 5235;

    private XMPPConnection connection;

    private ConnectionConfiguration config;

    @Override
    public ApplicationPlatform getApplicationPlatform()
    {
        return applicationPlatform;
    }

    @Override
    public void setApplicationPlatform(ApplicationPlatform applicationPlatform)
    {
        this.applicationPlatform = applicationPlatform;
    }

    /**
     * Connections are only needed when using the asynchronous Cloud Connection
     * Service (CCS) XMPP messaging service. Standard HTTP messaging is
     * stateless so no connections need be established.
     * 
     * Connects to GCM Cloud Connection Server using the supplied credentials.
     * 
     * @param username
     *            GCM_SENDER_ID@gcm.googleapis.com
     * @param password
     *            API Key
     */
    @Override
    public void connect() throws Exception
    {
        config = new ConnectionConfiguration(GCM_SERVER, GCM_PORT);
        config.setSecurityMode(SecurityMode.enabled);
        config.setReconnectionAllowed(true);
        config.setRosterLoadedAtLogin(false);
        config.setSendPresence(false);
        config.setSocketFactory(SSLSocketFactory.getDefault());

        // -Dsmack.debugEnabled=true
        XMPPConnection.DEBUG_ENABLED = true;

        connection = new XMPPConnection(config);
        connection.connect();

        connection.addConnectionListener(new ConnectionListener()
        {
            @Override
            public void reconnectionSuccessful()
            {
                //logger.info("Reconnecting..");
            }

            @Override
            public void reconnectionFailed(Exception e)
            {
                //logger.log(Level.INFO, "Reconnection failed.. ", e);
            }

            @Override
            public void reconnectingIn(int seconds)
            {
                //logger.log(Level.INFO, "Reconnecting in %d secs", seconds);
            }

            @Override
            public void connectionClosedOnError(Exception e)
            {
                //logger.log(Level.INFO, "Connection closed on error.");
            }

            @Override
            public void connectionClosed()
            {
                //logger.info("Connection closed.");
            }
        });

        // Log all outgoing packets
        connection.addPacketInterceptor(new PacketInterceptor()
        {
            @Override
            public void interceptPacket(Packet packet)
            {
                //logger.log(Level.INFO, "Sent: {0}", packet.toXML());
            }
        }, new PacketTypeFilter(Message.class));

        //connection.login(username, password);
    }

    @Override
    public void disconnect() throws Exception
    {

    }

    @Override
    public SendMessageResponse sendMessage(SendMessageRequest request) throws SendMessageException
    {
        Packet gcmRequest = new GcmPacketExtension("").toPacket();
        //Packet gcmRequest = new GcmPacketExtension(jsonRequest).toPacket();
        connection.sendPacket(gcmRequest);
        return new SendMessageResponse();
    }

    /**
     * XMPP Packet Extension for GCM Cloud Connection Server.
     */
    private class GcmPacketExtension extends DefaultPacketExtension
    {
        private final String json;

        public GcmPacketExtension(String json)
        {
            super(GCM_ELEMENT_NAME, GCM_NAMESPACE);
            this.json = json;
        }

        @Override
        public String toXML()
        {
            return String.format("<%s xmlns=\"%s\">%s</%s>", GCM_ELEMENT_NAME, GCM_NAMESPACE, json, GCM_ELEMENT_NAME);
        }

        public Packet toPacket()
        {
            return new Message()
            {
                // Must override toXML() because it includes a <body>
                @Override
                public String toXML()
                {
                    StringBuilder buf = new StringBuilder();
                    buf.append("<message");
                    if (getXmlns() != null) {
                        buf.append(" xmlns=\"").append(getXmlns()).append("\"");
                    }
                    if (getLanguage() != null) {
                        buf.append(" xml:lang=\"").append(getLanguage()).append("\"");
                    }
                    if (getPacketID() != null) {
                        buf.append(" id=\"").append(getPacketID()).append("\"");
                    }
                    if (getTo() != null) {
                        buf.append(" to=\"").append(StringUtils.escapeForXML(getTo())).append("\"");
                    }
                    if (getFrom() != null) {
                        buf.append(" from=\"").append(StringUtils.escapeForXML(getFrom())).append("\"");
                    }
                    buf.append(">");
                    buf.append(GcmPacketExtension.this.toXML());
                    buf.append("</message>");
                    return buf.toString();
                }
            };
        }
    }
}

