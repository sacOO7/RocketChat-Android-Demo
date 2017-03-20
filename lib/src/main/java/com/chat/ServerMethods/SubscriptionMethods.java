package com.chat.ServerMethods;

/**
 * Created by sachin on 18/3/17.
 */

public class SubscriptionMethods {
    public static String getroommessages(int uniqueid, String room_id){
        return "{\n" +
                "    \"msg\": \"sub\",\n" +
                "    \"id\": \""+uniqueid+"\",\n" +
                "    \"name\": \"stream-room-messages\",\n" +
                "    \"params\":[\n" +
                "        \""+room_id+"\",\n" +
                "        true\n" +
                "    ]\n" +
                "}";
    }
}
