package io.rocket.chat.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.chat.Models.SubscriptionObject;

import java.util.ArrayList;

import io.rocket.chat.Activities.ChatActivity;
import io.rocket.chat.R;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by sachin on 17/3/17.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<SubscriptionObject> roomObjects;
    Context context;

    public CustomAdapter(ArrayList<SubscriptionObject> roomObjects, Context context){
        this.roomObjects=roomObjects;
        this.context=context;
    }

    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);
        MyViewHolder viewHolder=new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomAdapter.MyViewHolder holder, final int position) {
        ColorGenerator generator = ColorGenerator.MATERIAL;
        final String roomName=roomObjects.get(position).getRoomName();
        int color = generator.getColor(roomName);
        TextDrawable drawable=TextDrawable.builder()
                .buildRound((String.valueOf(roomName.charAt(0)).toUpperCase()), color);
        holder.imageView.setImageDrawable(drawable);
        holder.textView.setText(roomName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ChatActivity.class);
                intent.putExtra("roomid",roomObjects.get(position).getRoomId());
                intent.putExtra("roomname",roomName);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        String roomType=roomObjects.get(position).getRoomType().toUpperCase();
        if (roomType.equals("D") || roomType.equals("P")) {
            TextDrawable type = TextDrawable.builder()
                    .buildRoundRect(roomType, Color.parseColor("#E91E63"), 10);
            holder.Type.setImageDrawable(type);
        }else{
            TextDrawable type = TextDrawable.builder()
                    .buildRoundRect(roomType, Color.parseColor("#009688"), 10);
            holder.Type.setImageDrawable(type);
        }

    }

    @Override
    public int getItemCount() {
        return roomObjects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        ImageView Type;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView= (TextView) itemView.findViewById(R.id.channel);
            imageView= (ImageView) itemView.findViewById(R.id.image_view);
            Type= (ImageView) itemView.findViewById(R.id.type);
        }
    }
}
