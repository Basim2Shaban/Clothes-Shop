package com.basim.outfitters.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.basim.outfitters.Fragment_Messages;
import com.basim.outfitters.R;
import com.basim.outfitters.chat_classes.ChatWithSomeOne;
import com.basim.outfitters.firebase_use.FireBaseGetData;
import com.basim.outfitters.firebase_use.FireBaseVarConnect;
import com.basim.outfitters.modiles.Model_UsersChatWithThem;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Basim on 20/07/2018.
 */

public class AdapterChatWithThem extends RecyclerView.Adapter<AdapterChatWithThem.ViewHolderViews> {
    private Context mContext ;
    private ArrayList<Model_UsersChatWithThem> arrayList = new ArrayList<>() ;

    private FireBaseGetData fireBaseGetData = new FireBaseGetData();
    private FireBaseVarConnect varConnect = new FireBaseVarConnect() ;



    public AdapterChatWithThem(Context mContext, ArrayList<Model_UsersChatWithThem> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }





    @Override
    public ViewHolderViews onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chating_with , parent,false);
        return new ViewHolderViews(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderViews holder, int position) {
        String mId = varConnect.bCurrentUser.getUid();
        final String user_id = arrayList.get(position).getUserid();

        holder.textViewName.setText(arrayList.get(position).getUsername());

        fireBaseGetData.getLastMessage(mContext,mId,user_id , holder.textViewLastMessa);

        fireBaseGetData.getUserStateConn(mContext,user_id,holder.textViewState);

        String uri = arrayList.get(position).getUserimage();
        Picasso.get().load(uri).into(holder.imageView);










        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChatWithSomeOne.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("user_id",user_id);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolderViews extends RecyclerView.ViewHolder{
        CircleImageView imageView ;
        TextView textViewName , textViewLastMessa , textViewState ;

        public ViewHolderViews(View itemView) {
            super(itemView);
            imageView = (CircleImageView)itemView.findViewById(R.id.image_userIn_chatWith);
            textViewName = (TextView)itemView.findViewById(R.id.text_username_chatWith);
            textViewLastMessa = (TextView)itemView.findViewById(R.id.textLastMessageChatWith);
            textViewState = (TextView)itemView.findViewById(R.id.textStateUserChatWith);
        }
    }
}
