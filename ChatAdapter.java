package com.basim.outfitters.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.basim.outfitters.R;
import com.basim.outfitters.image_classes.ActivityFullScreen;
import com.basim.outfitters.modiles.ChatMessage_Model;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Basim on 18/07/2018.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolderChat> {
    private ArrayList<ChatMessage_Model> arrayList = new ArrayList<>();
    private Context context ;
    private String myId ;
    private String uriMyImage ;
    private String uriUserImage ;



    public ChatAdapter(ArrayList<ChatMessage_Model> arrayList, Context context, String myId, String uriMyImage, String uriUserImage) {
        this.arrayList = arrayList;
        this.context = context;
        this.myId = myId;
        this.uriMyImage = uriMyImage;
        this.uriUserImage = uriUserImage;
    }

    @Override
    public ViewHolderChat onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == 0){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user_message_two,parent,false);

        }
        else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user_message,parent,false);
        }

        return new ViewHolderChat(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderChat holder, final int position) {


        if (!arrayList.get(position).getId().equals(myId) ){
            Glide.with(context).load(uriMyImage).into(holder.imageViewUser);
        }else{
            Glide.with(context).load(uriUserImage).into(holder.imageViewUser);
        }
        if (arrayList.get(position).getText_message().equals("")){
            holder.textViewMessage.setPadding(0,0,0,0);
            holder.textViewMessage.setVisibility(View.INVISIBLE);
        }else{
            holder.textViewMessage.setText(arrayList.get(position).getText_message());
        }

        // this code fixed repeat data in recycler
        holder.setIsRecyclable(false);

        Glide.with(context).load(arrayList.get(position).getImage()).into(holder.imageViewMessage);

        holder.imageViewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToFullScreen = new Intent(context, ActivityFullScreen.class);
                intentToFullScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentToFullScreen.putExtra("uri",arrayList.get(position).getImage());
                context.startActivity(intentToFullScreen);

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        String id = arrayList.get(position).getId();
        if (! id.equals(myId)){
            position = 0;
        }else {
            position = 1 ;
        }
        return position ;
    }

    class ViewHolderChat extends RecyclerView.ViewHolder{
        public CircleImageView imageViewUser ;
        public ImageView imageViewMessage ;
        public TextView textViewMessage ;

        public ViewHolderChat(View itemView) {
            super(itemView);
            imageViewUser = (CircleImageView) itemView.findViewById(R.id.image_user);
            imageViewMessage =(ImageView) itemView.findViewById(R.id.image_Message);
            textViewMessage = (TextView)itemView.findViewById(R.id.text_message);

        }
    }
}
