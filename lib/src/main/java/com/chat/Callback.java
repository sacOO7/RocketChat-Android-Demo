package com.chat;

import org.json.JSONObject;

/**
 * Created by SAC on 13-03-2017.
 */
public interface Callback {
    void call(JSONObject jsonObject);
}
