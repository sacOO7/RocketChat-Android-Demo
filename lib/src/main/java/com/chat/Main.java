package com.chat;

import com.chat.Models.MessageObject;
import com.chat.Models.RemovedRoom;
import com.chat.Models.RoomObject;
import com.chat.Models.SubscriptionObject;
import com.chat.ServerMethods.PresenceMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by SAC on 11-03-2017.
 */
public class Main {


    public static void main(final String args[])  {

        //Setting end-pint for connection
        final Socket socket=new Socket("wss://demo.rocket.chat/websocket");

        //Setting credentials to be sent to server
        socket.setCredentials("username","password");

        //Connect event to server
        try {
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Login to server
        socket.login(new Callback() {
            public void call(JSONObject jsonObject) {
                System.out.println("Successful login"+jsonObject.toString());
            }
        });


        //Setting status
        socket.setDefaultStatus(PresenceMethods.Status.OFFLINE, new Callback() {
            public void call(JSONObject jsonObject) {
                System.out.println("I have set status "+jsonObject.toString());
            }
        });

        //Getting rooms in JSON format, need to convert it into room objects
        //In future scenario callback will directly return room objects

        socket.getRooms(new Callback() {
            public void call(JSONObject jsonObject) {
                System.out.println("Room info "+jsonObject.toString());

                List<RoomObject> rooms=new ArrayList<RoomObject>();
                List<RemovedRoom> removedRooms=new ArrayList<RemovedRoom>();

                try {
                    JSONArray update= jsonObject.getJSONObject("result").getJSONArray("update");
                    JSONArray remove=jsonObject.getJSONObject("result").getJSONArray("remove");

                    for (int i=0;i<update.length();i++){
                        rooms.add(new RoomObject(update.getJSONObject(i)));
                    }

                    for (int i=0;i<remove.length();i++){
                        removedRooms.add(new RemovedRoom(remove.getJSONObject(i)));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (RoomObject room :rooms){
                    System.out.println("Room name is "+room.getRoomName());
                }
            }
        });

        //Getting user roles in JSON format
        socket.getUserRoles(new Callback() {
            public void call(JSONObject jsonObject) {
                System.out.println("user roles "+jsonObject.toString());
            }
        });

        //Getting subscriptions in JSON format, need to convert it into objects
        //In future scenario callback will directly return room objects
        socket.getSubscriptions(new Callback() {
            public void call(JSONObject jsonObject) {
                List<SubscriptionObject> roomObjects=new ArrayList<SubscriptionObject>();
                try {
                    JSONArray array=jsonObject.getJSONArray("result");
                    for (int i=0;i<array.length();i++){
                        roomObjects.add(new SubscriptionObject(array.getJSONObject(i)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (SubscriptionObject room :roomObjects){
                    System.out.println("Names of room are:"+room.getRoomName()+" type : "+room.getRoomType()+" id is "+room.getRoomId());
                }
            }
        });

        //Sending message to particular room
        socket.sendMessage("adfsnjksnbssc", "72WRGznH7ALgTjqqb", "Sender is sachin shinde once again", new Callback() {
            public void call(JSONObject jsonObject) {
                try {
                    MessageObject messageObject=new MessageObject(jsonObject.getJSONObject("result"));
                    System.out.println("Message is sent by"+messageObject.getSender().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        //Typing event is set
        socket.setTyping("85gESD9jaHkaxWhT8", true, "sachin.shinde", new Callback() {
            public void call(JSONObject jsonObject) {
                System.out.println("I'm currently typing in gsoc"+jsonObject.toString());
            }
        });


        //History API 
        socket.getHistory("85gESD9jaHkaxWhT8", 50, new Date(), new Callback() {
            public void call(JSONObject jsonObject) {
                System.out.println("Messages are "+jsonObject);
                List <MessageObject> messages=new ArrayList<MessageObject>();
                try {
                    JSONArray result=jsonObject.getJSONObject("result").getJSONArray("messages");
                    for (int i=0;i<result.length();i++){
                        messages.add(new MessageObject(result.getJSONObject(i)));
                        System.out.println("Message is "+messages.get(i).getMessage());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}


//        Names of room are:general type : c id is GENERAL
//        Names of room are:gsoc17 type : c id is 85gESD9jaHkaxWhT8
//        Names of room are:rafael.kellermann type : d id is MHNCPQyQnzdjRPiuRg9vNPgp6C4nDpSp8W
//        Names of room are:stormbuster type : d id is aBTkzJN2TTs75Yb4Xg9vNPgp6C4nDpSp8W
//        Names of room are:demosachin type : c id is 72WRGznH7ALgTjqqb
//        Names of room are:jaykay12 type : d id is g9vNPgp6C4nDpSp8WrmSkBppPsEJ66kkGk
//        Names of room are:sing.li type : d id is 2urrp3DyDkLxoMAd3g9vNPgp6C4nDpSp8W
//        Names of room are:tiago.cunha type : d id is S8hWtdhDxSPgZDHqXg9vNPgp6C4nDpSp8W
