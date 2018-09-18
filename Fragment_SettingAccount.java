package com.basim.outfitters;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_SettingAccount extends Fragment {
    private LinearLayout linearEditName , linearEditAge , linearEditDescreption ;
    private TextView txtChaneName , txtChangeAge , txtChangeDescreption;
    private ImageView imgChaneName , imgChangeAge , imgChangeDescreption;


    public Fragment_SettingAccount() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.StyleFragmentMessages);

        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        View view  = localInflater.inflate(R.layout.fragment__setting_account, container, false);


        linkViews(view);


        txtChaneName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeName();
            }
        });

        imgChaneName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeName();
            }
        });


        txtChangeAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAge();
            }
        });

        imgChangeAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAge();
            }
        });

        txtChangeDescreption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDiscreption();
            }
        });

        imgChangeDescreption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeDiscreption();
            }
        });



        return view ;
    }




    public void changeName(){
        Intent intentTo = new Intent(getActivity(),PropertiesOfEdit.class);
        intentTo.putExtra("key" , "name");
        startActivity(intentTo);
    }

    public void changeAge(){
        Intent intentTo = new Intent(getActivity(),PropertiesOfEdit.class);
        intentTo.putExtra("key" , "age");
        startActivity(intentTo);
    }

    public void changeDiscreption(){
        Intent intentTo = new Intent(getActivity(),PropertiesOfEdit.class);
        intentTo.putExtra("key" , "disk");
        startActivity(intentTo);
    }


    public void linkViews(View view){
        txtChaneName = (TextView)view.findViewById(R.id.txtEditName);
        txtChangeAge = (TextView)view.findViewById(R.id.txtEditAge);
        txtChangeDescreption = (TextView)view.findViewById(R.id.txtViewDescreption);
        imgChaneName = (ImageView) view.findViewById(R.id.imgEditName);
        imgChangeAge = (ImageView) view.findViewById(R.id.imgEditAge);
        imgChangeDescreption = (ImageView) view.findViewById(R.id.imgEditDesk);
    }
}
