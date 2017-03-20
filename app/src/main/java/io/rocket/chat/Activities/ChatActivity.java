package io.rocket.chat.Activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chat.Callback;
import com.chat.Models.MessageObject;
import com.chat.Socket;
import com.chat.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import io.rocket.chat.Adapters.ChatAdapter;
import io.rocket.chat.MyApplication;
import io.rocket.chat.R;

public class ChatActivity extends AppCompatActivity {

    private Socket socket;
    private ArrayList<MessageObject> messages;
    private ProgressDialog dialog;
    private String roomId;
    private String roomName;


    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    RelativeLayout rootView;

    //Emoji
    EmojiconEditText emojiconEditText;
    ImageView emojiButton;
    ImageView submitButton;
    EmojIconActions emojIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        roomId=getIntent().getStringExtra("roomid");
        roomName=getIntent().getStringExtra("roomname");

        getSupportActionBar().setTitle(roomName);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#CC1D1D")));

        MyApplication application= (MyApplication) getApplication();
        socket=application.getmSocket();

        recyclerView= (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        rootView = (RelativeLayout) findViewById(R.id.activity_chat);
        emojiButton = (ImageView) findViewById(R.id.emoji_btn);
        submitButton = (ImageView) findViewById(R.id.submit_btn);
        emojiconEditText = (EmojiconEditText) findViewById(R.id.emojicon_edit_text);
        emojIcon=new EmojIconActions(this,rootView,emojiconEditText,emojiButton,"#495C66","#DCE1E2","#E6EBEF");
        emojIcon.ShowEmojIcon();
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                recyclerView.scrollToPosition(messages.size() - 1);
                Log.e("Keyboard","open");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("Keyboard","close");
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitButton.setClickable(false);

                socket.sendMessage(Utils.shortUUID(), roomId, emojiconEditText.getText().toString(), new Callback() {
                    public void call(JSONObject jsonObject) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                emojiconEditText.setText("");
                                submitButton.setClickable(true);
                            }
                        });
//                        try {
//                            MessageObject messageObject=new MessageObject(jsonObject.getJSONObject("result"));
//                            messages.add(messageObject);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                adapter.notifyDataSetChanged();
//                                recyclerView.scrollToPosition(messages.size() - 1);
//                                emojiconEditText.setText("");
//                            }
//                        });
                    }
                });
            }
        });

        dialog=new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        dialog.setIndeterminate(true);
        dialog.setMessage("Fetching messages...");
        dialog.show();

        socket.subscribeRoom(new Callback() {
            public void call(JSONObject jsonObject) {
                Log.i ("Success","Message "+jsonObject);
                JSONObject fieldObject=jsonObject.optJSONObject("fields");
                String roomid=fieldObject.optString("eventName");
                if (fieldObject.opt("args")!=null && roomid.equals(roomId)){
                    JSONArray array=fieldObject.optJSONArray("args");
                    for (int i=0;i<array.length();i++){
                        try {
                            messages.add(new MessageObject(array.getJSONObject(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyItemInserted(messages.size() - 1);
                            recyclerView.scrollToPosition(messages.size() - 1);
                        }
                    });
                }
            }
        });

        socket.getHistory(roomId, 50, new Date(), new Callback() {
            public void call(JSONObject jsonObject) {
                messages=new ArrayList<MessageObject>();
                try {
                    JSONArray result=jsonObject.getJSONObject("result").getJSONArray("messages");
                    for (int i=result.length()-1;i>=0;i--){
                        messages.add(new MessageObject(result.getJSONObject(i)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                for (MessageObject messageObject:messages){
//                    Log.i ("success","Message is "+messageObject.getMessage());
//                }
                adapter=new ChatAdapter(messages,getApplicationContext());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        recyclerView.setAdapter(adapter);
                        recyclerView.scrollToPosition(messages.size() - 1);
                    }
                });
            }
        });
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
