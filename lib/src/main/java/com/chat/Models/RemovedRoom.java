package com.chat.Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by SAC on 13-03-2017.
 */
public class RemovedRoom {
    Date deletedAt;
    String roomId;

    public RemovedRoom(JSONObject object){
        try {
            deletedAt=new Date(new Timestamp(object.getJSONObject("_deletedAt").getLong("$date")).getTime());
            roomId=object.getString("_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
