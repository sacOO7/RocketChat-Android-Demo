package com.chat.Models;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by SAC on 13-03-2017.
 */
public class TokenObject {
    String userId;
    String AuthToken;
    Date Expiry;

    public TokenObject(String userId, String authToken, Date expiry) {
        this.userId = userId;
        AuthToken = authToken;
        Expiry = expiry;
    }
    public TokenObject(JSONObject object) throws JSONException {
        userId =object.optString("id");
        AuthToken=object.optString("token");
        Expiry=new Date(new Timestamp(object.optJSONObject("tokenExpires").getLong("$date")).getTime());
    }

    public String getUserId() {
        return userId;
    }

    public String getAuthToken() {
        return AuthToken;
    }

    public Date getExpiry() {
        return Expiry;
    }
}
