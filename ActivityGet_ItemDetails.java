package com.basim.outfitters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basim.outfitters.chat_classes.ChatWithSomeOne;
import com.basim.outfitters.firebase_use.FireBaseVarConnect;
import com.basim.outfitters.modiles.Model_GetPosts;
import com.basim.outfitters.image_classes.ActivityFullScreen;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ActivityGet_ItemDetails extends AppCompatActivity {
    private FireBaseVarConnect fireBaseVariable = new FireBaseVarConnect();
    private TextView mTextNameProduct, mTextColorProduct, mTextPriceOne,
            mTextPriceTotal, mTextMore, mTextMyName, mTexMyEmail, mTextMyPhone;
    private ImageView fImageDisplayBeg, fImageDisplaySmall, fImage_1, fImage_2, fImage_3;
    private Button bBtnEvaluation, btn_SendMessage , btn_Report , btn_adLottery;
    private static String user_id, imageUri_1, imageUri_2;
    private static String GetState_eva , mYID;
    private String name, price, allPrice, colors, more, gPhone, myName, myEmail;
    private ArrayList<Model_GetPosts> arrayListGetData = new ArrayList<>();
    private Evaluation evaluationClass = new Evaluation();
    String KeyThisItem ;
    private InterstitialAd mInterstitialAd;



    boolean state  ;

    public static int bad = 0;
    public static int good = 0;
    public static int vgood = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get__item_details);

        //this key of the product
        Intent GetIntent = getIntent();
        KeyThisItem = GetIntent.getStringExtra("key");

        int countClicking = GetIntent.getIntExtra("count",0);

       mYID = fireBaseVariable.bCurrentUser.getUid();


        startFullAds(countClicking);


        LinkViews();
        Get_Evaluation(KeyThisItem);
        evaluationClass.Start_Dialog(bBtnEvaluation, this, KeyThisItem);
        GetValueToSingleItem(KeyThisItem);
        adLotteryState();



        btn_Report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityGet_ItemDetails.this);
                builder.setMessage("هل تريد الابلاغ عن هذا المنتج")
                        .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                setReport();
                            }
                        })
                        .setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });



        btn_adLottery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAdLottery();
            }
        });





    }


    // link the views together
    public void LinkViews() {
        mTextNameProduct = (TextView) findViewById(R.id.text_nameP);
        mTextColorProduct = (TextView) findViewById(R.id.text_color);
        mTextPriceOne = (TextView) findViewById(R.id.text_priceOne);
        mTextPriceTotal = (TextView) findViewById(R.id.text_priceAll);
        mTextMore = (TextView) findViewById(R.id.text_more);
        mTextMyName = (TextView) findViewById(R.id.text_nameOfSaler);
        mTexMyEmail = (TextView) findViewById(R.id.text_Email);
        mTextMyPhone = (TextView) findViewById(R.id.text_phoneNumper);
        fImageDisplayBeg = (ImageView) findViewById(R.id.imageview_disBeg);
        fImageDisplaySmall = (ImageView) findViewById(R.id.imageView_disSmall);
        fImage_1 = (ImageView) findViewById(R.id.imageView_One);
        fImage_2 = (ImageView) findViewById(R.id.imageView_Two);
        fImage_3 = (ImageView) findViewById(R.id.imageView_Three);
        bBtnEvaluation = (Button) findViewById(R.id.btn_evaluation);
        btn_SendMessage = (Button) findViewById(R.id.btn_SendMessage);
        btn_adLottery = (Button) findViewById(R.id.btn_addLottery);
        btn_Report = (Button) findViewById(R.id.btnReport);
    }

    //Get all value from key and display them
    public void GetValueToSingleItem(String Key) {
        fireBaseVariable.bDataBaseposts.child(Key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Model_GetPosts modelGetPosts = dataSnapshot.getValue(Model_GetPosts.class);
                price = modelGetPosts.getPrice().toString();
                mTextPriceOne.setText(price + " ج.م ");
                colors = modelGetPosts.getColors().toString();
                mTextColorProduct.setText(colors);
                myEmail = modelGetPosts.getMyEmail().toString();
                mTexMyEmail.setText(myEmail);
                name = modelGetPosts.getName().toString();
                mTextNameProduct.setText(name);
                try {
                    gPhone = modelGetPosts.getMobile().toString();
                    mTextMyPhone.setText(gPhone);
                } catch (Exception e) {

                }

                imageUri_1 = modelGetPosts.getImage_1().toString();
                Glide.with(ActivityGet_ItemDetails.this).load(imageUri_1).into(fImageDisplayBeg);

                try {
                    Glide.with(ActivityGet_ItemDetails.this).load(imageUri_2).into(fImageDisplaySmall);
                } catch (Exception e) {
                    System.out.print(e);
                }
                allPrice = modelGetPosts.getAllPrice().toString();
                mTextPriceTotal.setText(allPrice + " ج.م ");
                myName = modelGetPosts.getMyName().toString();
                mTextMyName.setText(myName);
                more = modelGetPosts.getMore().toString();
                mTextMore.setText(more);
                user_id = modelGetPosts.getUser_id().toString();
                if (user_id.equals(mYID)){
                    btn_SendMessage.setLayoutParams(new LinearLayout.LayoutParams(0,0));
                    btn_SendMessage.setVisibility(View.INVISIBLE);
                    btn_Report.setLayoutParams(new LinearLayout.LayoutParams(0,0));
                    btn_Report.setVisibility(View.INVISIBLE);
                }else{
                    btn_adLottery.setVisibility(View.INVISIBLE);
                }

                MakeImage_FullAndS(fImageDisplayBeg, imageUri_1, imageUri_2);

                if (fImageDisplaySmall.getDrawable() == null){
                    fImageDisplaySmall.setVisibility(View.INVISIBLE);
                    fImageDisplaySmall.setEnabled(false);
                }else {
                    fImageDisplaySmall.setVisibility(View.VISIBLE);
                    fImageDisplaySmall.setEnabled(true);
                }

                MakeImage_FullAndS(fImageDisplaySmall, imageUri_2, imageUri_1);

                btn_SendMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentToMessages = new Intent(ActivityGet_ItemDetails.this, ChatWithSomeOne.class);
                        intentToMessages.putExtra("user_id",user_id);
                        startActivity(intentToMessages);

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //get evaluation fron key item
    public void Get_Evaluation(final String key) {
        final FireBaseVarConnect fireBaseVarConnect = new FireBaseVarConnect();

        fireBaseVarConnect.bDataBaseposts.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("evaluation")) {

                    fireBaseVarConnect.bDataBaseposts.child(key).child("evaluation").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                bad = Integer.parseInt(dataSnapshot.child("bad").getValue().toString());
                                good = Integer.parseInt(dataSnapshot.child("good").getValue().toString());
                                vgood = Integer.parseInt(dataSnapshot.child("very_good").getValue().toString());


                                //to cheek how it's put the image in evaluation
                                if (bad <= 0 && good <= 0 && vgood <= 0) {
                                    CheckOfStateEva(bad, good, vgood);
                                } else {
                                    CheckOfStateEva(bad, good, vgood);
                                }

                            } catch (Exception e) {

                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    // To start dialog evaluation and send what u choees to firebase
    public void CheckOfStateEva(int b, int g, int vg) {

        // i am doing that if and else to know anyone have fullstar and halfstar and when it need that

        if (b >= g && b >= vg) {
            GetState_eva = "bad";


            if (bad >= 3) {
                fImage_1.setImageResource(R.drawable.halfstar);
                fImage_2.setImageResource(R.drawable.normalstar);
                fImage_3.setImageResource(R.drawable.normalstar);

            } else {
                fImage_1.setImageResource(R.drawable.fullstar);
                fImage_2.setImageResource(R.drawable.normalstar);
                fImage_3.setImageResource(R.drawable.normalstar);
            }

        } else if (g >= b && g >= vg) {
            GetState_eva = "good";


            if (good >= 3) {
                fImage_1.setImageResource(R.drawable.fullstar);
                fImage_2.setImageResource(R.drawable.fullstar);
                fImage_3.setImageResource(R.drawable.normalstar);

            } else {
                fImage_1.setImageResource(R.drawable.fullstar);
                fImage_2.setImageResource(R.drawable.halfstar);
                fImage_3.setImageResource(R.drawable.normalstar);
            }


        } else if (vg >= b && vg >= g) {
            GetState_eva = "vgood";


            if (vgood >= 3) {
                fImage_1.setImageResource(R.drawable.fullstar);
                fImage_2.setImageResource(R.drawable.fullstar);
                fImage_3.setImageResource(R.drawable.fullstar);
            } else {
                fImage_1.setImageResource(R.drawable.fullstar);
                fImage_2.setImageResource(R.drawable.fullstar);
                fImage_3.setImageResource(R.drawable.halfstar);
            }


        }

    }

    // this to make image full screen and small
    public void MakeImage_FullAndS(final ImageView imageView, final String uri, final String uri2) {

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setImageFullScreen = new Intent(ActivityGet_ItemDetails.this, ActivityFullScreen.class);
                setImageFullScreen.putExtra("uri", uri);
                setImageFullScreen.putExtra("uri2", uri2);
                startActivity(setImageFullScreen);
            }
        });

    }




    public void setReport(){
        fireBaseVariable.bReports.push().child("key").setValue(KeyThisItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ActivityGet_ItemDetails.this, "Thank u For Roport Us about this", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void setAdLottery(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        final int dateToday = Integer.parseInt(sdf.format(new Date()));
        if (adLotteryState()){
            if (adLotteryState()){
                fireBaseVariable.bAdLottery.child(String.valueOf(dateToday)).child(myName).child("key").setValue(KeyThisItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        btn_adLottery.setEnabled(false);
                        btn_adLottery.setText("تم الاشتراك في قرعه الاعلان المجاني");
                        btn_adLottery.setBackgroundColor(Color.GRAY);
                        Toast.makeText(ActivityGet_ItemDetails.this, "تم الاشتراك في قرعه الاعلان المجاني بهذا المنتج", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        }


    }
    public boolean adLotteryState(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        final int dateToday = Integer.parseInt(sdf.format(new Date()));
        fireBaseVariable.bAdLottery.child(String.valueOf(dateToday)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(myName)){
                    state = false ;
                    btn_adLottery.setEnabled(false);
                    btn_adLottery.setText("تم الاشتراك في قرعه الاعلان المجاني من قبل");
                    btn_adLottery.setBackgroundColor(Color.GRAY);
                }else if (dataSnapshot.getChildrenCount() == 19){
                    state = false ;
                    btn_adLottery.setEnabled(false);
                    btn_adLottery.setText("تم الوصول للحد الاقصي للاشتراك هذا اليوم");
                    btn_adLottery.setBackgroundColor(Color.GRAY);
                }
                else {
                    state = true ;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return state ;
    }

    public void startFullAds(int count){
        if (count == 3){
            MobileAds.initialize(ActivityGet_ItemDetails.this , "ca-app-pub-2166371604316477~5185775435");
            mInterstitialAd = new InterstitialAd(ActivityGet_ItemDetails.this);
            mInterstitialAd.setAdUnitId("ca-app-pub-2166371604316477/5540998653");
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    // Load the next interstitial.
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }

            });
        }
    }


}



