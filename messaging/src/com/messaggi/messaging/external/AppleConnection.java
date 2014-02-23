package com.messaggi.messaging.external;

import java.io.ByteArrayOutputStream;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.messaggi.domain.ApplicationPlatform;
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

    /**
     * Note: To establish a TLS session with APNs, an Entrust Secure CA root
     * certificate must be installed on the provider’s server. If the server is
     * running OS X, this root certificate is already in the keychain. On other
     * systems, the certificate might not be available. You can download this
     * certificate from the Entrust SSL Certificates website.
     */
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

        @XmlTransient
        public byte[] notificationFormat;

        /**
         * The payload contains information about how the system should alert
         * the user as well as any custom data you provide. The maximum size
         * allowed for a notification payload is 256 bytes; Apple Push
         * Notification Service refuses any notification that exceeds this
         * limit. For each notification, compose a JSON dictionary object (as
         * defined by RFC 4627). This dictionary must contain another dictionary
         * identified by the key aps. The aps dictionary contains one or more
         * properties that specify the following actions: 1. An alert message to
         * display to the user; 2. A number to badge the application icon with;
         * 3. A sound to play
         */
        @XmlElement(name = "aps")
        public AppleSendMessagePayload payload;

        @XmlRootElement(name = "")
        public static class AppleSendMessagePayload
        {
            /**
             * If this property is included, the system displays a standard
             * alert. You may specify a string as the value of alert or a
             * dictionary as its value. If you specify a string, it becomes the
             * message text of an alert with two buttons: Close and View. If the
             * user taps View, the application is launched. Alternatively, you
             * can specify a dictionary as the value of alert.
             */
            @XmlTransient
            public String alertString;

            @XmlTransient
            public Alert alertDictionary;

            /**
             * Note: If you want the device to display the message text as-is in
             * an alert that has both the Close and View buttons, then specify a
             * string as the direct value of alert. Don’t specify a dictionary
             * as the value of alert if the dictionary only has the body
             * property.
             */
            @XmlElement(name = "alert")
            public String getAlert()
            {
                // If the alertDictionary is not null, manually marshal it 
                // and return it as alert string.  Otherwise return the alertString.
                return (alertDictionary != null) ? null : alertString;
            }

            /**
             * The number to display as the badge of the application icon. If
             * this property is absent, the badge is not changed. To remove the
             * badge, set the value of this property to 0.
             */
            @XmlElement(name = "badge")
            public int badge;

            /**
             * The name of a sound file in the application bundle. The sound in
             * this file is played as an alert. If the sound file doesn’t exist
             * or default is specified as the value, the default alert sound is
             * played. The audio must be in one of the audio data formats that
             * are compatible with system sounds; see“Preparing Custom Alert
             * Sounds” for details.
             */
            @XmlElement(name = "sound")
            public String sound;

            /**
             * Provide this key with a value of 1 to indicate that new content
             * is available. This is used to support Newsstand apps and
             * background content downloads.
             */
            @XmlElement(name = "content-available")
            public int contentAvailable;
            
            @XmlRootElement(name = "")
            public static class Alert
            {
                /**
                 * The text of the alert message.
                 */
                @XmlElement(name = "body")
                public String body;

                /**
                 * If a string is specified, the system displays an alert with
                 * two buttons, whose behavior is described in Table 3-1. The
                 * string is used as a key to get a localized string in the
                 * current localization to use for the right button’s title
                 * instead of “View”.
                 */
                @XmlElement(name = "action-loc-key")
                public String actionLocKey;

                /**
                 * A key to an alert-message string in a Localizable.strings
                 * file for the current localization (which is set by the user’s
                 * language preference). The key string can be formatted with %@
                 * and %n$@ specifiers to take the variables specified in
                 * loc-args.
                 */
                @XmlElement(name = "loc-key")
                public String locKey;

                /**
                 * Variable string values to appear in place of the format
                 * specifiers in loc-key.
                 */
                @XmlElement(name = "loc-args")
                public String[] locArgs;

                /**
                 * The filename of an image file in the application bundle; it
                 * may include the extension or omit it. The image is used as
                 * the launch image when users tap the action button or move the
                 * action slider. If this property is not specified, the system
                 * either uses the previous snapshot,uses the image identified
                 * by theUILaunchImageFile key in the application’s Info.plist
                 * file, or falls back to Default.png. This property was added
                 * in iOS 4.0.
                 */
                @XmlElement(name = "launch-image")
                public String launchImage;
            }
        }

        public AppleSendMessageRequest()
        {
            this(null);
        }

        public AppleSendMessageRequest(SendMessageRequest request)
        {
            this.request = request;
            if (request != null) {
                ByteArrayOutputStream notificationStream = new ByteArrayOutputStream();
                // Command field (1 byte)
                notificationStream.write(2);
                // Frame length (4 bytes) - The size of the frame data.
                // Frame data (variable length) - The frame contains the body, structured as a series of items.        
                
                // Device token item.
                notificationStream.write(2);
                
                // Payload item.

                // Notification identifier item.

                // Expiration date item.

                // Priority item.

                // Item ID (1 byte) - The item identifier. For example, the item number of the payload is 2.
                // Item data length (2 bytes) - The size of the item data.
                // Item data (variable length) - The frame contains the body, structured as a series of items.
            }
        }
    }
}

