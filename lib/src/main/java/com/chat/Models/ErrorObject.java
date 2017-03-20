package com.chat.Models;

import org.json.JSONObject;

/**
 * Created by sachin on 17/3/17.
 */
public class ErrorObject {
    String reason;
    String errorType;
    long error;
    String message;

    public ErrorObject(JSONObject object){
        reason=object.optString("reason");
        errorType=object.optString("errorType");
        error=object.optLong("error");
        message=object.optString("message");
    }

    public String getReason() {
        return reason;
    }

    public String getErrorType() {
        return errorType;
    }

    public long getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
