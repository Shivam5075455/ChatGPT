package com.example.chatgpt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;



public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{
    List<MessageModel> messageModelList;


    public MessageAdapter(List<MessageModel> list){
        this.messageModelList=list;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder viewHolder, int position){
        MessageModel messageModel = messageModelList.get(position);

        if(messageModel.getSentBy().equals(MessageModel.SEND_BY_BOT)){
            viewHolder.tvBotMessage.setVisibility(View.VISIBLE);
            viewHolder.tvUserMessage.setVisibility(View.GONE);
            viewHolder.tvBotMessage.setText(messageModel.getMessage());
        }else{
            viewHolder.tvBotMessage.setVisibility(View.GONE);
            viewHolder.tvUserMessage.setVisibility(View.VISIBLE);
            viewHolder.tvUserMessage.setText(messageModel.getMessage());
        }

    }

    @Override
    public int getItemCount() {
            return messageModelList.size();
    }


    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvBotMessage, tvUserMessage;
        public MessageViewHolder(@NonNull View itemview){
            super(itemview);

            tvBotMessage = itemview.findViewById(R.id.tvBotMessage);
            tvUserMessage = itemview.findViewById(R.id.tvUserMessage);
        }
    }
}
