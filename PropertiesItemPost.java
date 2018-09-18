package com.basim.outfitters.single_classes;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.basim.outfitters.adapters.AdapterGetAllPosts;

/**
 * Created by Basim on 23/07/2018.
 */

public class PropertiesItemPost {

    public void setWidthAndHeightTo(int position , View view , ImageView imageView){
        if (position % 2 == 0){

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(5,8,7,10);
            view.setLayoutParams(layoutParams);

            FrameLayout.LayoutParams parms = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,150);
            imageView.setLayoutParams(parms);
        }else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(7,8,5,10);
            view.setLayoutParams(layoutParams);
        }
    }


    public void setViewCorners(View view , ImageView imageView){
        view.setClipToOutline(true);
        imageView.setClipToOutline(true);
    }
}
