package com.basim.outfitters.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.basim.outfitters.R;
import com.basim.outfitters.image_classes.PropertiesImage;
import com.bumptech.glide.Glide;

/**
 * Created by Basim on 16/07/2018.
 */

public class CustomPagerAdapter extends PagerAdapter {
    String listImage [] ;
    Context mContext;
    LayoutInflater mLayoutInflater;


    public CustomPagerAdapter(String[] listImage, Context mContext, LayoutInflater mLayoutInflater) {
        this.listImage = listImage;
        this.mContext = mContext;
        this.mLayoutInflater = mLayoutInflater;
    }

    @Override
    public int getCount() {
        return listImage.length;
    }



    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        final ImageView imageView = (ImageView) itemView.findViewById(R.id.img_viewpager);
        Glide.with(mContext).load(listImage[position]).into(imageView);

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PropertiesImage propertiesImage = new PropertiesImage();
                propertiesImage.AlertDialog_ButtonS(imageView,mContext);
                return true;
            }
        });


        container.addView(itemView);

        return itemView;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }


}
