package com.chat;

import com.chat.Models.TokenObject;
import com.chat.ServerMethods.BasicMethods;
import com.chat.ServerMethods.HistoryMethod;
import com.chat.ServerMethods.PresenceMethods;
import com.chat.ServerMethods.SendMessage;
import com.chat.ServerMethods.SubscriptionMethods;
import com.chat.ServerMethods.TypingMethod;
import com.chat.Utils.Utils;
import com.neovisionaries.ws.client.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by SAC on 13-03-2017.
 */
public class Socket {

    String username;
    String password;
    TokenObject tokenObject;
    String url;
    public static boolean roomsSubscribed=false;
    String sessionId;
    WebSocketListener listener;
    public static Boolean isconnected;
    private WebSocketFactory factory;
    private WebSocket ws;
    AtomicInteger integer;
    ConcurrentHashMap <Long,Callback> callbacks;

    public static String defaultURL="wss://demo.rocket.chat/websocket";

    public Socket(String url){
        this.url=url;
        listener=getListener();
        integer=new AtomicInteger(2);
        callbacks=new ConcurrentHashMap<Long, Callback>();
        isconnected=false;
    }

    public TokenObject getTokenObject() {
        return tokenObject;
    }

    public String getSessionId() {
        return sessionId;
    }

    /**
     * Setting credentials to be sent to the server
     * @param username
     * @param password
     */
    public void setCredentials(String username, String password){
        this.username=username;
        this.password=password;
    }


    /**
     * Function for logging in into server using username and password
     * @param callback
     * Callback gives response JSON from server
     */
    public void login(final Callback callback){

        EventThread.exec(new Runnable() {
            public void run() {
                callbacks.put((long) 1,callback);
                ws.sendText(BasicMethods.getLoginObject(username,password));
            }
        });
    }

    /**
     * Function for logging in by using AuthToken
     * @param callback
     * Callback gives response JSON from server
     */
    public void resumeLogin(final String token, final Callback callback){
        EventThread.exec(new Runnable() {
            public void run() {
                callbacks.put((long) 1,callback);
                ws.sendText(BasicMethods.getlogintokenobject(token));
            }
        });
    }

    /**
     * Function for getting user roles
     * @param callback
     * Callback gives response JSON from server
     */
    public void getUserRoles(final Callback callback){
        EventThread.exec(new Runnable() {
            public void run() {
                int uniqueID=integer.getAndIncrement();
                callbacks.put((long) uniqueID,callback);
                ws.sendText(BasicMethods.getuserroleobject(uniqueID));
            }
        });
    }

    /**
     *
     * Function for getting all subscriptions from server
     * @param callback
     * Callback gives response JSON from server
     */
    public void getSubscriptions(final Callback callback){
        EventThread.exec(new Runnable() {
            public void run() {
                int uniqueID=integer.getAndIncrement();
                callbacks.put((long) uniqueID,callback);
                ws.sendText(BasicMethods.getsubscriptions(uniqueID));
            }
        });
    }

    /**
     *Function for getting all rooms from server
     * @param callback
     * Callback gives response JSON from server
     */
    public void getRooms(final Callback callback){
        EventThread.exec(new Runnable() {
            public void run() {
                int uniqueID=integer.getAndIncrement();
                callbacks.put((long) uniqueID,callback);
                ws.sendText(BasicMethods.getrooms(uniqueID));
            }
        });
    }

    /**
     * Function for setting default status on server
     * @param status
     * ONLINE,BUSY,AWAY,OFFLINE
     * @param callback
     * Callback gives response JSON from server
     *
     */
    public void setDefaultStatus(PresenceMethods.Status status, final Callback callback){
        String currentstatus = null;
        switch (status) {
            case ONLINE:
                currentstatus="online";
                break;
            case BUSY:
                currentstatus="busy";
                break;
            case AWAY:
                currentstatus="away";
                break;
            case OFFLINE:
                currentstatus="offline";
                break;
        }
        final String finalCurrentstatus = currentstatus;
        EventThread.exec(new Runnable() {
            public void run() {
                int uniqueID=integer.getAndIncrement();
                callbacks.put((long) uniqueID,callback);
                ws.sendText(PresenceMethods.getdefaultpresence(finalCurrentstatus,uniqueID));
            }
        });

    }

    /**
     * Function for setting temporary status on server
     * @param status
     * ONLINE,AWAY
     * @param callback
     * Callback gives response JSON from server
     */

    public void setTemporaryStatus(PresenceMethods.Status status, final Callback callback){
        String currentstatus = null;
        switch (status) {
            case ONLINE:
                currentstatus="online";
                break;
            case AWAY:
                currentstatus="away";
                break;
        }
        final String finalCurrentstatus = currentstatus;
        EventThread.exec(new Runnable() {
            public void run() {
                int uniqueID=integer.getAndIncrement();
                callbacks.put((long) uniqueID,callback);
                ws.sendText(PresenceMethods.gettemporarystatus(finalCurrentstatus,uniqueID));
            }
        });
    }

    /**
     * Function for sending typing event
     * @param roomId
     * RoomId to which typing event need to be sent
     * @param istyping
     * Boolean: It enable or disables typing event
     * @param username
     * Username to be shown for typing event
     * @param callback
     * Callback gives response JSON from server
     */
    public void setTyping(final String roomId, final Boolean istyping, final String username, final Callback callback){
        EventThread.exec(new Runnable() {
            public void run() {
                int uniqueID=integer.getAndIncrement();
                callbacks.put((long) uniqueID,callback);
                ws.sendText(TypingMethod.gettypingobject(uniqueID,roomId,istyping,username));
            }
        });
    }

    /**
     * Function for sending message to any subscribed room
     * @param msgid
     * Set unique message_id for sending message
     * @param roomId
     * Set roomId to which message to be sent
     * @param message
     * Text message to be sent
     * @param callback
     * Callback gives response JSON from server
     */
    public void sendMessage( final String msgid, final String roomId, final String message,final Callback callback){
        EventThread.exec(new Runnable() {
            public void run() {
                int uniqueID=integer.getAndIncrement();
                callbacks.put((long) uniqueID,callback);
                ws.sendText(SendMessage.getsendmessageobject(uniqueID,msgid,roomId,message));
            }
        });
    }

    /**
     * Function for getting history from server
     * @param roomId
     * History for particular room
     * @param count
     * Number of messages to be received for particular room
     * @param lastTimestamp
     *  the date of the last time the client got data for the room
     * @param callback
     * Callback gives response JSON from server
     */
    public void getHistory(final String roomId, final Integer count, final Date lastTimestamp, final Callback callback){
        EventThread.exec(new Runnable() {
            public void run() {
                int uniqueID=integer.getAndIncrement();
                callbacks.put((long) uniqueID,callback);
                ws.sendText(HistoryMethod.getHistory(uniqueID,roomId,count,lastTimestamp));
            }
        });
    }

    public void subscribeRoom(final String room_id){
        EventThread.exec(new Runnable() {
            public void run() {
                int uniqueID=integer.getAndIncrement();
                ws.sendText(SubscriptionMethods.getroommessages(uniqueID,room_id));
            }
        });
    }

    public void subscribeRoom(Callback callback){
        if (callbacks.get((long) -1)!=null){
            callbacks.remove((long) -1);
        }
        callbacks.put((long) -1,callback);
    }

    public void subscribeRoom(final String room_id, final Callback callback){
        EventThread.exec(new Runnable() {
            public void run() {
                int uniqueID=integer.getAndIncrement();
                if (callbacks.get((long) -1)!=null){
                    callbacks.remove((long) -1);
                }
                callbacks.put((long) -1,callback);
                ws.sendText(SubscriptionMethods.getroommessages(uniqueID,room_id));
            }
        });
    }

    /**
     * Setting basic listeners for websocket events
     * Most of the callbacks are unused, might be required in future
     * @return
     */
    WebSocketListener getListener(){
        return new WebSocketListener() {
            public void onStateChanged(WebSocket websocket, WebSocketState newState) throws Exception {
//                System.out.println("on state changed");
            }

            public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                integer.set(2);
                isconnected=true;
                System.out.println("Connected to server");
            }

            public void onConnectError(WebSocket websocket, WebSocketException cause) throws Exception {
                System.out.println("got connect error");
            }

            public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                System.out.println("Disconnected to server");
                isconnected=false;
            }

            public void onFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
//                System.out.println("Got frame");
            }

            public void onContinuationFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
                System.out.println("on continuation frame");
            }

            public void onTextFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
             //   System.out.println("On text frame");
            }

            public void onBinaryFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
                System.out.println("on binary frame");
            }

            public void onCloseFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
                System.out.println("On close frame");
            }

            public void onPingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
                System.out.println("On ping frame"+frame.getPayloadText());
            }

            public void onPongFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
                System.out.println("on pong frame");
            }

            public void onTextMessage(WebSocket websocket, String text) throws Exception {
                JSONObject object=new JSONObject(text);

                if (object.has("server_id")) {
                    websocket.sendText(BasicMethods.getConnectObject());
                    callbacks.get((long)0).call(null);
                }else{

                    if (object.optString("msg").equals("ping")){
                        websocket.sendText("{\"msg\":\"pong\"}");
                    }
                    else if (object.optString("msg").equals("connected")){
                        sessionId=object.optString("session");
//                        System.out.println("session id is "+sessionId);
                    }
                    else if (object.optString("id").equals("1")){
                        if (object.opt("error")!=null){
                            callbacks.get((long)1).call(object.optJSONObject("error"));
                        }else {
                            tokenObject = new TokenObject(object.optJSONObject("result"));
                            callbacks.get((long)1).call(null);
                        }
                    }else if(object.optString("id").equals("id")){
                        //Callback for messages
                        callbacks.get((long)-1).call(object);

                    }else if (Utils.isInteger(object.optString("id"))){
                        callbacks.get(Long.valueOf(object.optString("id"))).call(object);
                    }
//
                    System.out.println("Message is "+text);
                }
//                websocket.sendText(getLoginObject("sachin.shinde","sachin9922"));
            }

            public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {
                System.out.println("on binary message");
            }

            public void onSendingFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
//                System.out.println("on sending frame");
            }

            public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception {
//                System.out.println("on frame set "+frame.getPayloadText());
            }

            public void onFrameUnsent(WebSocket websocket, WebSocketFrame frame) throws Exception {
                System.out.println("on frame unsent");
            }

            public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
                System.out.println("On error");
            }

            public void onFrameError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {
                System.out.println("On frame error");
            }

            public void onMessageError(WebSocket websocket, WebSocketException cause, List<WebSocketFrame> frames) throws Exception {
                System.out.println("On message error");
            }

            public void onMessageDecompressionError(WebSocket websocket, WebSocketException cause, byte[] compressed) throws Exception {
                System.out.println("on message decompression error");
            }

            public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data) throws Exception {
                System.out.println("on text message error");
            }

            public void onSendError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {
                System.out.println("on send error");
            }

            public void onUnexpectedError(WebSocket websocket, WebSocketException cause) throws Exception {
                System.out.println("on unexpected error");
            }

            public void handleCallbackError(WebSocket websocket, Throwable cause) throws Exception {
                System.out.println("handle callback error");
            }

            public void onSendingHandshake(WebSocket websocket, String requestLine, List<String[]> headers) throws Exception {
//                System.out.println("On sending handshake");
            }
        };

    }


    public void connectasync(){
        factory = new WebSocketFactory();

        // Create a WebSocket with a socket connection timeout value.
        try {
            ws = factory.createSocket(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ws.addListener(listener);
        ws.connectAsynchronously();

    }
    /**
     * Function for connecting to server
     * @throws IOException
     */
    public void connect() throws IOException {

        factory = new WebSocketFactory();

        // Create a WebSocket with a socket connection timeout value.
        ws = factory.createSocket(url);

        ws.addListener(listener);

        try
        {
            // Connect to the server and perform an opening handshake.
            // This method blocks until the opening handshake is finished.
            ws.connect();
        }
        catch (OpeningHandshakeException e)
        {
            // A violation against the WebSocket protocol was detected
            // during the opening handshake.
            StatusLine sl = e.getStatusLine();
            System.out.println("=== Status Line ===");
            System.out.format("HTTP Version  = %s\n", sl.getHttpVersion());
            System.out.format("Status Code   = %d\n", sl.getStatusCode());
            System.out.format("Reason Phrase = %s\n", sl.getReasonPhrase());

            // HTTP headers.
            Map<String, List<String>> headers = e.getHeaders();
            System.out.println("=== HTTP Headers ===");
            for (Map.Entry<String, List<String>> entry : headers.entrySet())
            {
                // Header name.
                String name = entry.getKey();

                // Values of the header.
                List<String> values = entry.getValue();

                if (values == null || values.size() == 0)
                {
                    // Print the name only.
                    System.out.println(name);
                    continue;
                }

                for (String value : values)
                {
                    // Print the name and the value.
                    System.out.format("%s: %s\n", name, value);
                }
            }
        }
        catch (WebSocketException e)
        {
            System.out.println("Got websocket exception "+e.getMessage());
            // Failed to establish a WebSocket connection.
        }
    }

    public void connect(Callback callback){
        callbacks.put((long) 0,callback);
        factory = new WebSocketFactory();

        // Create a WebSocket with a socket connection timeout value.
        try {
            ws = factory.createSocket(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ws.addListener(listener);
        ws.connectAsynchronously();

    }
}
