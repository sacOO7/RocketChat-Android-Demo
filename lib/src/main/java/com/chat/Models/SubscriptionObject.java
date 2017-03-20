package com.chat.Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by SAC on 13-03-2017.
 */
public class SubscriptionObject {
    String roomType;
    Date roomCreated;
    Date lastSeen;
    String roomName;
    String roomId;
    JSONObject userInfo;
    Boolean open;
    Boolean alert;
    Integer unread;
    Date updatedAt;
    String subscriptionId;

    public SubscriptionObject(JSONObject object)  {

        try {
            roomType=object.getString("t");
            if (object.optJSONObject("ts")!=null) {
                roomCreated = new Date(new Timestamp(object.getJSONObject("ts").getLong("$date")).getTime());
            }
            lastSeen = new Date(new Timestamp(object.getJSONObject("ls").getLong("$date")).getTime());
            roomName = object.getString("name");
            roomId = object.getString("rid");
            userInfo = object.getJSONObject("u");
            open = object.getBoolean("open");
            alert = object.getBoolean("alert");
            unread = object.getInt("unread");
            updatedAt = new Date(new Timestamp(object.getJSONObject("_updatedAt").getLong("$date")).getTime());
            subscriptionId = object.getString("_id");
        }catch (JSONException e) {
            e.printStackTrace();
            System.out.println("name is "+roomName);
        }
    }

    public String getRoomType() {
        return roomType;
    }

    public Date getRoomCreated() {
        return roomCreated;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getRoomId() {
        return roomId;
    }

    public JSONObject getUserInfo() {
        return userInfo;
    }

    public Boolean getOpen() {
        return open;
    }

    public Boolean getAlert() {
        return alert;
    }

    public Integer getUnread() {
        return unread;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }
}
