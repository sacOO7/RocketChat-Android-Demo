package com.chat.ServerMethods;


import com.chat.Models.TokenObject;

import static com.chat.Utils.Utils.getDigest;

/**
 * Created by SAC on 13-03-2017.
 */
public class
BasicMethods {

    public static String getConnectObject(){
        return "{\"msg\":\"connect\",\"version\":\"1\",\"support\":[\"1\",\"pre2\",\"pre1\"]}";
    }

    public static String getLoginObject(String username, String password){
        return "{\n" +
                "    \"msg\": \"method\",\n" +
                "    \"method\": \"login\",\n" +
                "    \"id\":\"1\",\n" +
                "    \"params\":[\n" +
                "        {\n" +
                "            \"user\": { \"username\": \""+username+"\" },\n" +
                "            \"password\": {\n" +
                "                \"digest\": \""+ getDigest(password)+"\",\n" +
                "                \"algorithm\":\"sha-256\"\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}";
    }

    public static String getlogintokenobject(String token){
        return "{" +
                "    \"msg\": \"method\",\n" +
                "    \"method\": \"login\",\n" +
                "    \"id\": \"1\",\n" +
                "    \"params\":[\n" +
                "        { \"resume\": \""+token+"\" }\n" +
                "    ]\n" +
                "}";
    }

    public static String getuserroleobject(int integer){

        return "{\n" +
                "        \"msg\": \"method\",\n" +
                "        \"method\": \"getUserRoles\",\n" +
                "        \"id\": \""+integer+"\",\n" +
                "        \"params\": []\n" +
                "    }";
    }

    public static String getsubscriptions(int integer){
        return "{" +
                "    \"msg\": \"method\",\n" +
                "    \"method\": \"subscriptions/get\",\n" +
                "    \"id\": \""+integer+"\",\n" +
                "    \"params\": [ ]\n" +
                "}";
    }

    public static String getrooms(int integer){
        return "{" +
                "    \"msg\": \"method\",\n" +
                "    \"method\": \"rooms/get\",\n" +
                "    \"id\": \""+integer+"\",\n" +
                "    \"params\": [ { \"$date\": 0 }]\n" +
                "}";
    }

}
