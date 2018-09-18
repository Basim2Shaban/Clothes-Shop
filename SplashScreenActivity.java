package com.basim.outfitters;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {
    TextView textViewTitle , textViewBy ,textViewCompliteBy ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



        textViewTitle = (TextView)findViewById(R.id.textTitleSplash);
        textViewBy = (TextView)findViewById(R.id.textBy);
        textViewCompliteBy = (TextView)findViewById(R.id.textComplite);

        Typeface custom_typeface = Typeface.createFromAsset(getAssets(),"fonts/Courgette-Regular.ttf");
        textViewTitle.setTypeface(custom_typeface);
        textViewBy.setTypeface(custom_typeface);
        textViewCompliteBy.setTypeface(custom_typeface);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intentToMain = new Intent(SplashScreenActivity.this,MainActivity.class);
                startActivity(intentToMain);


            }
        },2000);
    }
}
