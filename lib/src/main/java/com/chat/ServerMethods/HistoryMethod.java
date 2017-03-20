package com.chat.ServerMethods;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by SAC on 14-03-2017.
 */
public class HistoryMethod {
    public static String getHistory(int integer, String roomId, Integer count, Date lastTimestamp){
        return "{\n" +
                "    \"msg\": \"method\",\n" +
                "    \"method\": \"loadHistory\",\n" +
                "    \"id\": \""+integer+"\",\n" +
                "    \"params\": [ \""+roomId+"\", null, "+count+", { \"$date\": "+((int)lastTimestamp.getTime()/1000)+" } ]\n" +
                "}";
    }
}
