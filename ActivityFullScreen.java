package com.basim.outfitters.image_classes;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.basim.outfitters.R;
import com.basim.outfitters.adapters.CustomPagerAdapter;

public class ActivityFullScreen extends AppCompatActivity {
    public String array[] ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        array =new String[] {null};


        Intent getUri = getIntent();
        String uriImage = getUri.getStringExtra("uri");
        String uriImage2 = getUri.getStringExtra("uri2");

        if (uriImage != null && uriImage2 != null){
            array = new String[]{uriImage, uriImage2};
        }
        else {
            array = new String[]{uriImage};
        }


        LayoutInflater mLayoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        ImportViewPager(array, this,mLayoutInflater);



    }


    public void ImportViewPager(String arr[] , Context context , LayoutInflater mlayoutInflater){
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        CustomPagerAdapter adapter = new CustomPagerAdapter(arr,context,mlayoutInflater);
        viewPager.setAdapter(adapter);
    }
}
