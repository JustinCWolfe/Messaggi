package com.messaggi.messaging.external;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.messaggi.messages.SendMessageException;
import com.messaggi.messages.SendMessageRequest;

@XmlRootElement(name = "")
public class AppleSendMessageRequest
{
    @XmlTransient
    public final SendMessageRequest request;

    /**
     * An arbitrary, opaque value that identifies this notification. This
     * identifier is used for reporting errors to your server (4 bytes).
     */
    @XmlTransient
    public final byte[] notificationId = new byte[4];

    @XmlTransient
    public byte[] notificationFormat;

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
    @XmlElement(name = "aps")
    public AppleSendMessagePayload payload;

    @XmlRootElement(name = "")
    public static class AppleSendMessagePayload
    {
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
        public String getAlert()
        {
            // If the alertDictionary is not null, manually marshal it 
            // and return it as alert string.  Otherwise return the alertString.
            return (alertDictionary != null) ? null : alertString;
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

    public AppleSendMessageRequest() throws SendMessageException
    {
        this(null);
    }

    public AppleSendMessageRequest(SendMessageRequest request) throws SendMessageException
    {
        this.request = request;
        ThreadLocalRandom.current().nextBytes(this.notificationId);
    }

    public byte[] toByteArray() throws IOException
    {
        return new Notification(this).toByteArray();
    }

    private static class Notification
    {
        private static final int SEND_MESSAGE_COMMAND = 2;

        private static final int FRAME_DATA_SIZE = 4;

        private static final int DEVICE_TOKEN_ITEM_INDEX = 1;

        private static final int DEVICE_TOKEN_ITEM_SIZE = 32;

        private static final int PAYLOAD_ITEM_INDEX = 2;

        private static final int NOTIFICATION_IDENTIFIER_ITEM_INDEX = 3;

        private static final int NOTIFICATION_IDENTIFIER_ITEM_SIZE = 4;

        private static final int EXPIRATION_DATE_ITEM_INDEX = 4;

        private static final int EXPIRATION_DATE_ITEM_SIZE = 4;

        private static final int PRIORITY_ITEM_INDEX = 5;

        private static final int PRIORITY_ITEM_SIZE = 1;

        private static final int SEND_MESSAGE_IMMEDIATELY = 10;

        private final AppleSendMessageRequest appleRequest;

        Notification(AppleSendMessageRequest request)
        {
            this.appleRequest = request;
        }

        /**
         * The device token in binary form, as was registered by the device.
         */
        private Frame.Item getDeviceTokenItem() throws IOException
        {
            return new Frame.Item(DEVICE_TOKEN_ITEM_INDEX, DEVICE_TOKEN_ITEM_SIZE,
                    appleRequest.request.to[0].getCodeAsBinary());
        }

        /**
         * The JSON-formatted payload. The payload must not be null-terminated.
         * Variable length and is <= 256 bytes.
         */
        private Frame.Item getPayloadItem() throws IOException
        {
            //TODO: dump payload to binary.
            return new Frame.Item(PAYLOAD_ITEM_INDEX, 32, appleRequest.request.to[0].getCodeAsBinary());
        }

        /**
         * An arbitrary, opaque value that identifies this notification. This
         * identifier is used for reporting errors to your server.
         */
        private Frame.Item getNotificationIdentifierItem() throws IOException
        {
            return new Frame.Item(NOTIFICATION_IDENTIFIER_ITEM_INDEX, NOTIFICATION_IDENTIFIER_ITEM_SIZE,
                    appleRequest.notificationId);
        }

        /**
         * A UNIX epoch date expressed in seconds (UTC) that identifies when the
         * notification is no longer valid and can be discarded. If this value
         * is non-zero, APNs stores the notification tries to deliver the
         * notification at least once. Specify zero to indicate that the
         * notification expires immediately and that APNs should not store the
         * notification at all.
         */
        private Frame.Item getExpirationDateItem() throws IOException
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(appleRequest.request.requestDate);
            // Message should be kept for 4 weeks to behave similarly to GCM.
            calendar.add(Calendar.WEEK_OF_MONTH, 4);
            return new Frame.Item(EXPIRATION_DATE_ITEM_INDEX, EXPIRATION_DATE_ITEM_SIZE, calendar.getTimeInMillis());
        }

        /**
         * The notification’s priority. Provide one of the following values: 10
         * - The push message is sent immediately. The push notification must
         * trigger an alert, sound, or badge on the device. It is an error to
         * use this priority for a push that contains only the content-available
         * key; 5 - The push message is sent at a time that conserves power on
         * the device receiving it.
         */
        private Frame.Item getPriorityItem() throws IOException
        {
            return new Frame.Item(PRIORITY_ITEM_INDEX, PRIORITY_ITEM_SIZE, SEND_MESSAGE_IMMEDIATELY);
        }

        /**
         * Frame data (variable length) - The frame contains the body,
         * structured as a series of items.
         */
        private Frame getFrameData() throws IOException
        {
            Frame.Item deviceTokenItem = getDeviceTokenItem();
            Frame.Item payloadItem = getPayloadItem();
            Frame.Item notificationIdentifierItem = getNotificationIdentifierItem();
            Frame.Item expirationDateItem = getExpirationDateItem();
            Frame.Item priorityItem = getPriorityItem();
            return new Frame(deviceTokenItem, payloadItem, notificationIdentifierItem, expirationDateItem, priorityItem);
        }

        byte[] toByteArray() throws IOException
        {
            ByteArrayOutputStream notificationStream = new ByteArrayOutputStream();
            // Command field (1 byte)
            notificationStream.write(SEND_MESSAGE_COMMAND);
            // Frame length (4 bytes) - The size of the frame data.
            byte[] frameData = getFrameData().toByteArray();
            notificationStream.write(ByteBuffer.allocate(FRAME_DATA_SIZE).putLong(frameData.length).array());
            // Frame data (variable length) - The frame contains the body, structured as a series of items.        
            notificationStream.write(frameData);
            return notificationStream.toByteArray();
        }

        private static class Frame
        {
            private final Item[] items;

            Frame(Item... items)
            {
                this.items = items;
            }

            byte[] toByteArray() throws IOException
            {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                for (Item item : items) {
                    stream.write(item.toByteArray());
                }
                return stream.toByteArray();
            }

            private static class Item
            {
                private final int id;

                private final int length;

                private final byte[] data;

                Item(int id, int length, long data)
                {
                    this(id, length, ByteBuffer.allocate(length).putLong(length).array());
                }

                Item(int id, int length, byte[] data)
                {
                    this.id = id;
                    this.length = data.length;
                    this.data = data;
                }

                byte[] toByteArray() throws IOException
                {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // 1 byte
                    stream.write(id);
                    // 2 bytes
                    ByteBuffer.allocate(2).putInt(length).array();
                    // Variable length
                    stream.write(data);
                    return stream.toByteArray();
                }
            }
        }
    }
}

