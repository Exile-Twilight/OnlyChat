package com.example.onlychat.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlychat.ChatActivity;
import com.example.onlychat.Msg;
import com.example.onlychat.R;
import com.example.onlychat.util.Util;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.MsgHolder> {
    public MsgAdapter(List<Msg> msgList) {
        this.msgList = msgList;
    }


    private final List<Msg> msgList;

    @NonNull
    @Override
    public MsgHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item_recycler, parent, false);
        MsgHolder msgHolder = new MsgHolder(view);

        return msgHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MsgHolder holder, int position) {

        Msg msg = msgList.get(position);

//        if (msg.getSender_id().equals(ChatActivity.getUsername()))
//        {
//            holder.linearLayoutLeft.setVisibility(View.GONE);
//            holder.linearLayoutRight.setVisibility(View.VISIBLE);
//            if (msg.getContentstype().equalsIgnoreCase("text"))
//            {
//                holder.contextRight.setText(msg.getContents());
//                holder.imageViewRight.setVisibility(View.GONE);
//            }else {
//                holder.imageViewRight.setImageBitmap(Util.base64ToBitmap(msg.getContents()));
//                holder.contextRight.setVisibility(View.GONE);
//            }
//        }else {
//            holder.linearLayoutRight.setVisibility(View.GONE);
//            holder.linearLayoutLeft.setVisibility(View.VISIBLE);
//            if (msg.getContentstype().equalsIgnoreCase("text"))
//            {
//                holder.contextLeft.setText(msg.getContents());
//                holder.imageViewLeft.setVisibility(View.GONE);
//            }else {
//                holder.imageViewLeft.setImageBitmap(Util.base64ToBitmap(msg.getContents()));
//                holder.contextLeft.setVisibility(View.GONE);
//            }
//        }

        switch (msg.getContentstype().toLowerCase()) {
            case "text":
                if (msg.getSender_id().equals(ChatActivity.getUsername())) {
                    holder.linearLayoutRight.setVisibility(View.VISIBLE);
                    holder.linearLayoutLeft.setVisibility(View.GONE);
                    holder.imageViewRight.setVisibility(View.GONE);
                    holder.contextRight.setText(msg.getContents());
                } else {
                    holder.linearLayoutLeft.setVisibility(View.VISIBLE);
                    holder.linearLayoutRight.setVisibility(View.GONE);
                    holder.imageViewLeft.setVisibility(View.GONE);
                    holder.contextLeft.setText(msg.getContents());

                }
                break;
            case "image":
                if (msg.getSender_id().equals(ChatActivity.getUsername())) {
                    holder.linearLayoutRight.setVisibility(View.VISIBLE);
                    holder.linearLayoutLeft.setVisibility(View.GONE);
                    holder.contextRight.setText("\n---来自" + msg.getSender());
                    holder.imageViewRight.setImageBitmap(Util.base64ToBitmap(msg.getContents()));
                } else {
                    holder.linearLayoutRight.setVisibility(View.GONE);
                    holder.linearLayoutLeft.setVisibility(View.VISIBLE);
                    holder.contextLeft.setText("\n---来自" + msg.getSender());
                    holder.imageViewLeft.setImageBitmap(Util.base64ToBitmap(msg.getContents()));
                }
                break;
            default:
                holder.linearLayoutRight.setVisibility(View.GONE);
                holder.linearLayoutLeft.setVisibility(View.GONE);
                break;
        }
    }


    @Override
    public int getItemCount() {
        return msgList.size();
    }


    public class MsgHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayoutLeft;
        LinearLayout linearLayoutRight;
        TextView contextLeft;
        ImageView imageViewLeft;
        TextView contextRight;
        ImageView imageViewRight;

        public MsgHolder(@NonNull View itemView) {
            super(itemView);
            linearLayoutLeft = itemView.findViewById(R.id.left_layout);
            linearLayoutRight = itemView.findViewById(R.id.right_layout);
            contextLeft = itemView.findViewById(R.id.msg_left);
            contextRight = itemView.findViewById(R.id.msg_right);
            imageViewLeft = itemView.findViewById(R.id.image_left);
            imageViewRight = itemView.findViewById(R.id.image_right);

        }
    }

}
