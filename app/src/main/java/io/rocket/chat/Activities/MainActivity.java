package io.rocket.chat.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.chat.Callback;
import com.chat.Models.SubscriptionObject;
import com.chat.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.rocket.chat.Adapters.CustomAdapter;
import io.rocket.chat.MyApplication;
import io.rocket.chat.R;

public class MainActivity extends AppCompatActivity {

    private Socket socket;
    public static int REQUEST_LOGIN=0;
    private ArrayList<SubscriptionObject> roomObjects;
    private ProgressDialog dialog;
    private String token;

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyApplication application= (MyApplication) getApplication();
        socket=application.getmSocket();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#CC1D1D")));



        recyclerView= (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        token=sharedPref.getString("token",null);

        if (token==null) {
//            Log.i ("success Token is","null");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
        }else {

            //Connect event to server
            if (!socket.isconnected) {

                dialog=new ProgressDialog(this,
                        R.style.AppTheme_Dark_Dialog);
                dialog.setIndeterminate(true);
                dialog.setMessage("Fetching rooms...");
                dialog.show();

                socket.connect(new Callback() {
                    @Override
                    public void call(JSONObject jsonObject) {
                        socket.resumeLogin(token, new Callback() {
                            @Override
                            public void call(JSONObject jsonObject) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getSubscribers();
                                    }
                                });
                            }
                        });
                    }
                });
            }else {

                dialog=new ProgressDialog(this,
                        R.style.AppTheme_Dark_Dialog);
                dialog.setIndeterminate(true);
                dialog.setMessage("Fetching rooms...");
                dialog.show();

                socket.resumeLogin(token, new Callback() {
                    @Override
                    public void call(JSONObject jsonObject) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getSubscribers();
                            }
                        });
                    }
                });
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (RESULT_OK!=resultCode){
            finish();
        }
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("token",data.getStringExtra("token"));
        editor.commit();


        dialog=new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        dialog.setIndeterminate(true);
        dialog.setMessage("Fetching rooms...");
        dialog.show();

        getSubscribers();
    }

    public void getSubscribers(){


        socket.getSubscriptions(new Callback() {
            public void call(JSONObject jsonObject) {

                 roomObjects=new ArrayList<SubscriptionObject>();
                try {
                    JSONArray array=jsonObject.getJSONArray("result");
                    for (int i=0;i<array.length();i++){
                        roomObjects.add(new SubscriptionObject(array.getJSONObject(i)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (!Socket.roomsSubscribed) {
                    for (SubscriptionObject object : roomObjects) {
                        socket.subscribeRoom(object.getRoomId());
                    }
                    Socket.roomsSubscribed = true;
                }

                adapter=new CustomAdapter(roomObjects,MainActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        recyclerView.setAdapter(adapter);
                    }
                });
//                for (SubscriptionObject room :roomObjects){
//                    Log.i("Success","Names of room are:"+room.getRoomName()+" type : "+room.getRoomType()+" id is "+room.getRoomId());
//                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }
}
