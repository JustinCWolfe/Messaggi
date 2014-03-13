package com.messaggi.messaging.external;

import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.messaggi.messages.SendMessageException;
import com.messaggi.messages.SendMessageRequest;
import com.messaggi.messaging.external.AppleSendMessageRequest.AppleSendMessagePayload.ContentAvailable;
import com.messaggi.messaging.external.exception.AppleSendMessageException.AppleInvalidPayloadException;
import com.messaggi.util.EncodeHelper;
import com.messaggi.util.JAXBHelper;

@XmlRootElement(name = "")
public class AppleSendMessageRequest
{
    public final SendMessageRequest request;

    /**
     * An arbitrary, opaque value that identifies this notification. This
     * identifier is used for reporting errors to your server (4 bytes).
     */
    public int notificationId;

    /**
     * A UNIX epoch date expressed in seconds (UTC) that identifies when the
     * notification is no longer valid and can be discarded. If this value is
     * non-zero, APNs stores the notification tries to deliver the notification
     * at least once. Specify zero to indicate that the notification expires
     * immediately and that APNs should not store the notification at all (4
     * bytes).
     */
    public int expirationDate;

    /**
     * The device token in binary form, as was registered by the device.
     */
    public byte[] deviceToken;

    /**
     * The JSON-formatted payload. The payload must not be null-terminated.
     * Variable length and is <= 256 bytes.
     */
    //public byte[] payload;

    /**
     * The payload contains information about how the system should alert the
     * user as well as any custom data you provide. The maximum size allowed for
     * a notification payload is 256 bytes; Apple Push Notification Service
     * refuses any notification that exceeds this limit. For each notification,
     * compose a JSON dictionary object (as defined by RFC 4627). This
     * dictionary must contain another dictionary identified by the key aps. The
     * aps dictionary contains one or more properties that specify the following
     * actions: 1. An alert message to display to the user; 2. A number to badge
     * the application icon with; 3. A sound to play
     */
    public AppleSendMessagePayload payload;

    public byte[] payloadBytes;

    public AppleSendMessageRequest() throws SendMessageException
    {
        this(null);
    }

    public AppleSendMessageRequest(SendMessageRequest request) throws SendMessageException
    {
        this.request = request;
        if (request != null) {
            this.notificationId = ThreadLocalRandom.current().nextInt();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(request.requestDate);
            // Message should be kept for 4 weeks to behave similarly to GCM.
            calendar.add(Calendar.WEEK_OF_MONTH, 4);
            this.expirationDate = (int) TimeUnit.MILLISECONDS.toSeconds(calendar.getTimeInMillis());

            this.deviceToken = request.to[0].getCodeAsBinary();

            if (request.messageMap.size() != 1) {
                throw new AppleInvalidPayloadException(this, null);
            }
            this.payload = new AppleSendMessagePayload(request.getDefaultMessage(), 0, ContentAvailable.No);
            try {
                String payloadJSON = JAXBHelper.objectToJSON(payload);
                this.payloadBytes = EncodeHelper.encodeBase64Image(payloadJSON);
            } catch (Exception e) {
                throw new AppleInvalidPayloadException(this, null, e);
            }
        }
    }

    @XmlRootElement(name = "aps")
    static class AppleSendMessagePayload
    {
        enum ContentAvailable {
            No(0), Yes(0);

            private final int value;

            private ContentAvailable(int value)
            {
                this.value = value;
            }
        }
        
        /**
         * If this property is included, the system displays a standard alert.
         * You may specify a string as the value of alert or a dictionary as its
         * value. If you specify a string, it becomes the message text of an
         * alert with two buttons: Close and View. If the user taps View, the
         * application is launched. Alternatively, you can specify a dictionary
         * as the value of alert.
         */
        @XmlTransient
        public String alertString;

        @XmlTransient
        public Alert alertDictionary;

        /**
         * Note: If you want the device to display the message text as-is in an
         * alert that has both the Close and View buttons, then specify a string
         * as the direct value of alert. Don’t specify a dictionary as the value
         * of alert if the dictionary only has the body property.
         */
        @XmlElement(name = "alert")
        public String getAlert() throws Exception
        {
            return (alertDictionary != null) ? JAXBHelper.objectToJSON(alertDictionary) : alertString;
        }

        /**
         * The number to display as the badge of the application icon. If this
         * property is absent, the badge is not changed. To remove the badge,
         * set the value of this property to 0.
         */
        @XmlElement(name = "badge")
        public int badge;

        /**
         * The name of a sound file in the application bundle. The sound in this
         * file is played as an alert. If the sound file doesn’t exist or
         * default is specified as the value, the default alert sound is played.
         * The audio must be in one of the audio data formats that are
         * compatible with system sounds; see“Preparing Custom Alert Sounds” for
         * details.
         */
        @XmlElement(name = "sound")
        public String sound;

        /**
         * Provide this key with a value of 1 to indicate that new content is
         * available. This is used to support Newsstand apps and background
         * content downloads.
         */
        @XmlElement(name = "content-available")
        public int getContentAvailableValue()
        {
            return contentAvailable.value;
        }

        public ContentAvailable  contentAvailable;

        AppleSendMessagePayload(String alert, int badge, ContentAvailable contentAvailable)
        {
            this.alertString = alert;
            this.badge = badge;
            this.contentAvailable = contentAvailable;
        }

        @XmlRootElement(name = "")
        public static class Alert
        {
            /**
             * The text of the alert message.
             */
            @XmlElement(name = "body")
            public String body;

            /**
             * If a string is specified, the system displays an alert with two
             * buttons, whose behavior is described in Table 3-1. The string is
             * used as a key to get a localized string in the current
             * localization to use for the right button’s title instead of
             * “View”.
             */
            @XmlElement(name = "action-loc-key")
            public String actionLocKey;

            /**
             * A key to an alert-message string in a Localizable.strings file
             * for the current localization (which is set by the user’s language
             * preference). The key string can be formatted with %@ and %n$@
             * specifiers to take the variables specified in loc-args.
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
             * The filename of an image file in the application bundle; it may
             * include the extension or omit it. The image is used as the launch
             * image when users tap the action button or move the action slider.
             * If this property is not specified, the system either uses the
             * previous snapshot,uses the image identified by
             * theUILaunchImageFile key in the application’s Info.plist file, or
             * falls back to Default.png. This property was added in iOS 4.0.
             */
            @XmlElement(name = "launch-image")
            public String launchImage;
        }
    }
}

