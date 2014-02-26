package com.messaggi.messaging.external;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.messaggi.domain.Device;
import com.messaggi.messages.SendMessageRequest;

@XmlRootElement(name = "")
public class AndroidSendMessageRequest
{
    @XmlTransient
    public final SendMessageRequest request;

    /**
     * A string array with the list of devices (registration IDs) receiving the
     * message. It must contain at least 1 and at most 1000 registration IDs.
     * Note that a regID represents a particular Android application running on
     * a particular device.
     * 
     * @return
     */
    @XmlElement(name = "registration_ids")
    public String[] registrationIds;

    /**
     * A string that maps a single user to multiple registration IDs associated
     * with that user. This allows a 3rd-party server to send a single message
     * to multiple app instances (typically on multiple devices) owned by a
     * single user. A 3rd-party server can use notification_key as the target
     * for a message instead of an individual registration ID (or array of
     * registration IDs). The maximum number of members allowed for a
     * notification_key is 10.
     * 
     * @return
     */
    @XmlElement(name = "notification_key")
    public String notificationKey;

    /**
     * A JSON object whose fields represents the key-value pairs of the
     * message's payload data. If present, the payload data it will be included
     * in the Intent as application data, with the key being the extra's name.
     * For instance, "data":{"score":"3x1"} would result in an intent extra
     * named score whose value is the string 3x1. There is no limit on the
     * number of key/value pairs, though there is a limit on the total size of
     * the message (4kb). The values could be any JSON object, but we recommend
     * using strings, since the values will be converted to strings in the GCM
     * server anyway.
     * 
     * @return
     */
    @XmlElement(name = "data")
    public Map<String, String> data;

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
     * sending a message. Optional. The default value is false, and must be a
     * JSON boolean.
     * 
     * @return
     */
    @XmlElement(name = "dry_run")
    public boolean dryRun;

    public AndroidSendMessageRequest()
    {
        this(null);
    }

    public AndroidSendMessageRequest(SendMessageRequest request)
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
