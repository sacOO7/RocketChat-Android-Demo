package io.rocket.chat.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chat.Models.MessageObject;
import com.chat.Models.TokenObject;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import io.rocket.chat.MyApplication;
import io.rocket.chat.R;

/**
 * Created by sachin on 17/3/17.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MessageObject> messages;
    Context context;
    TokenObject token;
    private static final int SELF=0;
    public static final int OTHER=1;

    public ChatAdapter(ArrayList<MessageObject> messages, Context context){
        this.messages=messages;
        this.context=context;
        token=((MyApplication)context).getmSocket().getTokenObject();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case SELF:
                View v1 = inflater.inflate(R.layout.chat_user2_item, parent, false);
                viewHolder = new ChatViewHolder2(v1);
                break;
            case OTHER:
                View v2 = inflater.inflate(R.layout.chat_user1_item, parent, false);
                viewHolder = new ChatViewHolder1(v2);
                break;
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageObject object=messages.get(position);
        String username = "";
        try {
            username=object.getSender().getString("username");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatDate = new SimpleDateFormat("hh:mm a");
        String message=object.getMessage();
        String time=formatDate.format(object.getMsgTimestamp());
        switch (holder.getItemViewType()){
            case SELF:
                ChatViewHolder2 holder2= (ChatViewHolder2) holder;
                holder2.messageTextView.setText(message);
                holder2.timeTextView.setText(time);
                break;
            case OTHER:
                ChatViewHolder1 holder1= (ChatViewHolder1) holder;
                holder1.UserName.setText(username);
                holder1.messageTextView.setText(message);
                holder1.timeTextView.setText(time);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        MessageObject object=messages.get(position);
        String userid = null,username;
        try {
            userid = object.getSender().getString("_id");
            username=object.getSender().getString("username");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (userid.equals(token.getUserId())){
            return SELF;
        }else{
            return OTHER;
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ChatViewHolder1 extends RecyclerView.ViewHolder {
        public EmojiconTextView messageTextView;
        public TextView timeTextView;
        public TextView UserName;

        public ChatViewHolder1(View itemView) {
            super(itemView);
            messageTextView= (EmojiconTextView) itemView.findViewById(R.id.message_text);
            timeTextView= (TextView) itemView.findViewById(R.id.time_text);
            UserName= (TextView) itemView.findViewById(R.id.chat_company_reply_author);
        }
    }

    public class ChatViewHolder2 extends RecyclerView.ViewHolder {
        public ImageView messageStatus;
        public EmojiconTextView messageTextView;
        public TextView timeTextView;

        public ChatViewHolder2(View itemView) {
            super(itemView);
            messageTextView= (EmojiconTextView) itemView.findViewById(R.id.message_text);
            timeTextView= (TextView) itemView.findViewById(R.id.time_text);
            messageStatus= (ImageView) itemView.findViewById(R.id.user_reply_status);
        }

    }
}
