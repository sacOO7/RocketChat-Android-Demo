package com.chat.ServerMethods;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by SAC on 13-03-2017.
 */
public class TypingMethod {

    public static String gettypingobject(int integer, String room_id, Boolean istyping, String username){
        return "{\n" +
                "    \"msg\": \"method\",\n" +
                "    \"method\": \"stream-notify-room\",\n" +
                "    \"id\": \""+integer+"\",\n" +
                "    \"params\": [\n" +
                "        \""+room_id+"/typing\",\n" +
                "        \""+username+"\",\n" +
                "        "+istyping+"\n" +
                "    ]\n" +
                "}";
    }
}
