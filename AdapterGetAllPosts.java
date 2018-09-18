package com.basim.outfitters.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.basim.outfitters.ActivityGet_ItemDetails;
import com.basim.outfitters.R;
import com.basim.outfitters.firebase_use.FireBaseGetData;
import com.basim.outfitters.modiles.Model_GetPosts;
import com.basim.outfitters.single_classes.PropertiesItemPost;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Basim on 03/06/2018.
 */

public class AdapterGetAllPosts extends RecyclerView.Adapter<AdapterGetAllPosts.ViewHolderA> {
    private ArrayList<Model_GetPosts> arrayList = new ArrayList<>();
    private Context context;
    private static int countClicked = 0 ;

    FireBaseGetData fireBaseGetData = new FireBaseGetData();



    public AdapterGetAllPosts(ArrayList<Model_GetPosts> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public ViewHolderA onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder mViewHolder = null;
        View view = null;
     //  if (viewType == 0){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_posts, parent, false);
        return new ViewHolderA(view);
           /*
       }else if (viewType == 1){
           view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_numbers, parent, false);
           mViewHolder = new ViewHolderNumbers(view);
       }
       /*/


   //     return mViewHolder;
    }



    @Override
    public void onBindViewHolder(final ViewHolderA holder, final int position) {

        holder.itemView.setTag(arrayList.get(position));
            PropertiesItemPost propertiesItemPost = new PropertiesItemPost();
            propertiesItemPost.setViewCorners(holder.itemView,holder.imageView);
            propertiesItemPost.setWidthAndHeightTo(position,holder.itemView,holder.imageView);

            Glide.with(context).load(arrayList.get(position).getImage_1()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imageView);
            holder.T_productName.setText(arrayList.get(position).getName());
            holder.btn_viewPrice.setText(arrayList.get(position).getPrice() + " " + context.getString(R.string.LE));
            holder.setIsRecyclable(false);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    countClicked++ ;
                    String Key_Item = arrayList.get(position).getKeyItem();
                    Intent intent = new Intent(view.getContext(), ActivityGet_ItemDetails.class);
                    intent.putExtra("key", Key_Item);
                    intent.putExtra("count",countClicked);
                    view.getContext().startActivity(intent);

                }
            });

        }


   // }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolderA extends RecyclerView.ViewHolder {
        private TextView T_productName;
        private ImageView imageView;
        private TextView btn_viewPrice;


        public ViewHolderA(View itemView) {
            super(itemView);
            T_productName = (TextView) itemView.findViewById(R.id.dis_name_of_product);
            imageView = (ImageView) itemView.findViewById(R.id.dis_image_item_fromAll);
            btn_viewPrice = (TextView) itemView.findViewById(R.id.txt_View_price);

        }
    }

/*
    public class ViewHolderNumbers extends RecyclerView.ViewHolder{
      public Button btnNext , btnBack ;
        public ViewHolderNumbers(View itemView) {
            super(itemView);

            btnNext = (Button)itemView.findViewById(R.id.btnNext);
            btnBack = (Button)itemView.findViewById(R.id.btnBack);
        }
    }
    */

}
