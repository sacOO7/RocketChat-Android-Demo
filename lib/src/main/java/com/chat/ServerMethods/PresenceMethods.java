package com.chat.ServerMethods;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by SAC on 13-03-2017.
 */
public class PresenceMethods {

    public enum Status{
        ONLINE,
        BUSY,
        AWAY,
        OFFLINE
    }

    public static String getdefaultpresence(String status, int integer){
        return "{" +
                "    \"msg\": \"method\",\n" +
                "    \"method\": \"UserPresence:setDefaultStatus\",\n" +
                "    \"id\": \""+integer+"\",\n" +
                "    \"params\": [ \""+status+"\" ]\n" +
                "}";
    }

    public static String gettemporarystatus(String status, int integer){
        return "{" +
                "    \"msg\": \"method\",\n" +
                "    \"method\": \"UserPresence:"+status+"\",\n" +
                "    \"id\": \""+integer+"\",\n" +
                "    \"params\":[]\n" +
                "}";
    }
}
