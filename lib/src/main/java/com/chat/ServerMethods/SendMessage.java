package com.chat.ServerMethods;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by SAC on 14-03-2017.
 */
public class SendMessage {

    public static String getsendmessageobject(int integer, String msgId, String roomId, String message){
        return "{\n" +
                "    \"msg\": \"method\",\n" +
                "    \"method\": \"sendMessage\",\n" +
                "    \"id\": \""+integer+"\",\n" +
                "    \"params\": [\n" +
                "        {\n" +
                "            \"_id\": \""+msgId+"\",\n" +
                "            \"rid\": \""+roomId+"\",\n" +
                "            \"msg\": \""+message+"\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }
}
