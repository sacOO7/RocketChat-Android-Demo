package io.rocket.chat;

import android.app.Application;

import com.chat.Socket;


/**
 * Created by sachin on 17/3/17.
 */

public class MyApplication extends Application {
    private Socket mSocket;
    @Override
    public void onCreate() {
        super.onCreate();
        mSocket=new Socket(Socket.defaultURL);
    }

    public Socket getmSocket() {
        return mSocket;
    }
}
