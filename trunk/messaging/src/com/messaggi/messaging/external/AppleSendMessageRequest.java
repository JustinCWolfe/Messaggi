package com.messaggi.messaging.external;

import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.annotation.XmlRootElement;

import com.messaggi.messages.SendMessageException;
import com.messaggi.messages.SendMessageRequest;
import com.messaggi.messaging.external.exception.AppleSendMessageException.AppleInvalidPayloadException;
import com.notnoop.apns.PayloadBuilder;

@XmlRootElement(name = "")
public class AppleSendMessageRequest
{
    private static final String PAYLOAD_TOO_LONG_POSTFIX = "...";

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
            PayloadBuilder builder = PayloadBuilder.newPayload().alertBody(request.getDefaultMessage()).clearBadge();
            if (builder.isTooLong()) {
                if (builder.shrinkBody(PAYLOAD_TOO_LONG_POSTFIX).isTooLong()) {
                    throw new AppleInvalidPayloadException(this, null);
                }
            }
            this.payloadBytes = builder.buildBytes();
        }
    }
}

