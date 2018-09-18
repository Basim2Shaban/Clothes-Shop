package com.basim.outfitters.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.basim.outfitters.ActivityGet_ItemDetails;
import com.basim.outfitters.R;
import com.basim.outfitters.modiles.ModelGetViewPagData;
import com.basim.outfitters.modiles.Model_GetPosts;
import com.basim.outfitters.single_classes.PropertiesAds;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Basim on 27/07/2018.
 */

public class ViewPagerAdsAdapter extends PagerAdapter {
    private ArrayList<ModelGetViewPagData> arrayList ;
    private Context mContext ;
    private LayoutInflater layoutInflater ;
    PropertiesAds propertiesAds = new PropertiesAds();


    public ViewPagerAdsAdapter(ArrayList<ModelGetViewPagData> arrayList, Context mContext, LayoutInflater layoutInflater) {
        this.arrayList = arrayList;
        this.mContext = mContext;
        this.layoutInflater = layoutInflater;
    }




    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = layoutInflater.inflate(R.layout.row_pager_ads ,container ,false);

        ImageView imageViewAds = (ImageView)view.findViewById(R.id.img_ads);
        TextView txtName = (TextView)view.findViewById(R.id.txtAdsName);
        TextView txtPrice = (TextView)view.findViewById(R.id.txtAdsPrice);
        TextView txtNew = (TextView)view.findViewById(R.id.txtNew);

        Glide.with(mContext).load(arrayList.get(position).getImage_1()).into(imageViewAds);
        txtName.setText(arrayList.get(position).getName());
        txtPrice.setText(arrayList.get(position).getPrice() + " " + mContext.getString(R.string.LE));

        propertiesAds.MakeNewChangeColor(position,txtNew);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ActivityGet_ItemDetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("key",arrayList.get(position).getKey());
                mContext.startActivity(intent);
            }
        });



        container.addView(view);
        return view ;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
