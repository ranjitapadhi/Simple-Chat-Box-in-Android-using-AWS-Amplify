package com.blossomgames.simplechatbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.core.Amplify;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<RecyclerData> mMessageList;


    public void AddList(ArrayList<RecyclerData> myList){
        this.mMessageList = myList;
    }

    public MessageListAdapter(Context context, List<RecyclerData> messageList) {
        mContext = context;
        mMessageList = messageList;

        System.out.println("Inside MessageListAdapter");

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == 2) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecyclerData message = (RecyclerData) mMessageList.get(position);
        switch (holder.getItemViewType()) {
            case 1:
                ((SentMessageHolder) holder).bind(message);
                break;
            case 2:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        RecyclerData message = (RecyclerData) mMessageList.get(position);

        if (message.getSender().equals(Amplify.Auth.getCurrentUser().getUsername())) {
            // If the current user is the sender of the message
            return 1;       //Current User Sender
        } else {
            // If some other user sent the message
            return 2;       //Other User Sender
        }
    }

    public void notifyData(ArrayList<RecyclerData> myList) {
        this.mMessageList = myList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        }

        void bind(RecyclerData message) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
            String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());

            timeText.setText(currentDateTimeString);
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
        }

        void bind(RecyclerData message) {
            messageText.setText(message.getMessage());

            String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(currentDateTimeString);

            nameText.setText(message.getSender());

            // Insert the profile image from the URL into the ImageView.
            //Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }
    }
}
