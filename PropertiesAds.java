package com.basim.outfitters.single_classes;

import android.graphics.Color;
import android.widget.TextView;

/**
 * Created by Basim on 28/07/2018.
 */

public class PropertiesAds {


    public void MakeNewChangeColor(int m , TextView textView){
        switch (m){
            case 0 :
                textView.setBackgroundColor(Color.RED);
                break;
            case 1 :
                textView.setBackgroundColor(Color.parseColor("#D9E900"));
                break;
            case 2:
                textView.setBackgroundColor(Color.parseColor("#3500D2"));
                break;
            case 3:
                textView.setBackgroundColor(Color.parseColor("#F57700"));
                break;
            case 4 :
                textView.setBackgroundColor(Color.parseColor("#F2009D"));
        }
    }
}
