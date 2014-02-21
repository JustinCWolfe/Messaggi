package com.messaggi.messaging.external;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.messaggi.domain.ApplicationPlatform;
import com.messaggi.domain.Device;
import com.messaggi.external.MessagingServiceConnection;
import com.messaggi.messages.SendMessageException;
import com.messaggi.messages.SendMessageRequest;
import com.messaggi.messages.SendMessageResponse;

public class AppleConnection implements MessagingServiceConnection
{
    // APNs production host - this is set to a different host for unit testing (via a mock object).
    protected static String APPLE_PUSH_NOTIFICATION_HOST = "gateway.push.apple.com";

    private static final int APPLE_PUSH_NOTIFICATION_PORT = 2195;

    private ApplicationPlatform applicationPlatform;

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

    private void getAPNsServerCertificate()
    {

    }

    private void validateAPNsServerCertificate()
    {

    }

    private void sendProviderCertificatetoAPNs()
    {

    }

    @Override
    public void connect() throws Exception
    {
        // When a provider authenticates itself to APNs, it sends its topic to the APNs server, which identifies the 
        // application for which it’s providing data. The topic is currently the bundle identifier of the target application.

        // Connection trust between a provider and APNs is also established through TLS peer-to-peer authentication. 
        // The provider initiates a TLS connection, gets the server certificate from APNs, and validates that certificate. 
        // Then the provider sends its provider certificate to APNs, which validates it on its end. Once this procedure 
        // is complete, a secure TLS connection has been established; APNs is now satisfied that the connection has been 
        // made by a legitimate provider. 
        getAPNsServerCertificate();
        validateAPNsServerCertificate();
        sendProviderCertificatetoAPNs();
    }

    @Override
    public SendMessageResponse sendMessage(SendMessageRequest request) throws SendMessageException
    {

        return new SendMessageResponse();
    }

    @XmlRootElement(name = "")
    public static class AppleSendMessageRequest
    {
        @XmlTransient
        public final SendMessageRequest request;

        /**
         * A string array with the list of devices (registration IDs) receiving
         * the message. It must contain at least 1 and at most 1000 registration
         * IDs. Note that a regID represents a particular Apple application
         * running on a particular device.
         * 
         * @return
         */
        @XmlElement(name = "registration_ids")
        public String[] registrationIds;

        /**
         * A string that maps a single user to multiple registration IDs
         * associated with that user. This allows a 3rd-party server to send a
         * single message to multiple app instances (typically on multiple
         * devices) owned by a single user. A 3rd-party server can use
         * notification_key as the target for a message instead of an individual
         * registration ID (or array of registration IDs). The maximum number of
         * members allowed for a notification_key is 10.
         * 
         * @return
         */
        @XmlElement(name = "notification_key")
        public String notificationKey;

        /**
         * The payload contains information about how the system should alert
         * the user as well as any custom data you provide. The maximum size
         * allowed for a notification payload is 256 bytes; Apple Push
         * Notification Service refuses any notification that exceeds this
         * limit. For each notification, compose a JSON dictionary object (as
         * defined by RFC 4627). This dictionary must contain another dictionary
         * identified by the key aps. The aps dictionary contains one or more
         * properties that specify the following actions: • An alert message to
         * display to the user • A number to badge the application icon with • A
         * sound to play
         * 
         * @return
         */
        @XmlElement(name = "data")
        public Map<String, String> data;

        @XmlRootElement(name = "")
        public static class AppleSendMessagePayload
        {

        }

        /**
         * A string containing the package name of your application. When set,
         * messages will only be sent to registration IDs that match the package
         * name.
         * 
         * @return
         */
        @XmlElement(name = "restricted_package_name")
        public String restrictedPackageName;

        /**
         * If included, allows developers to test their request without actually
         * sending a message. Optional. The default value is false, and must be
         * a JSON boolean.
         * 
         * @return
         */
        @XmlElement(name = "dry_run")
        public boolean dryRun;

        public AppleSendMessageRequest()
        {
            this(null);
        }

        public AppleSendMessageRequest(SendMessageRequest request)
        {
            this.request = request;
            if (request != null) {
                List<String> recipients = new ArrayList<>();
                for (Device toDevice : request.to) {
                    recipients.add(toDevice.getCode());
                }
                registrationIds = recipients.toArray(new String[recipients.size()]);
                data = request.messageMap;
                dryRun = request.isDebug;
            }
        }
    }
}

