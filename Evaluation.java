package com.basim.outfitters;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.basim.outfitters.firebase_use.FireBaseVarConnect;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Created by Basim on 13/07/2018.
 */

public class Evaluation {
    String StateClick;

    public void Start_Dialog(final Button button, final Context context, final String key) {
        final FireBaseVarConnect fireBaseVarConnect = new FireBaseVarConnect();
        final ActivityGet_ItemDetails itemDetails = new ActivityGet_ItemDetails();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_evaluation);


                final ImageView imageView_1 = dialog.findViewById(R.id.imageView_1);
                final ImageView imageView_2 = dialog.findViewById(R.id.imageView_2);
                final ImageView imageView_3 = dialog.findViewById(R.id.imageView_3);
                final TextView evaluationWillShow = dialog.findViewById(R.id.t_eva_state);
                Button btn_evaluation_now = dialog.findViewById(R.id.btn_dialog_eval);
                dialog.show();


                imageView_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (imageView_1.getDrawable() == imageView_1.getResources().getDrawable(R.drawable.normalstar)) {
                            imageView_1.setImageResource(R.drawable.halfstar);
                        } else {
                            imageView_1.setImageResource(R.drawable.halfstar);
                            imageView_2.setImageResource(R.drawable.normalstar);
                            imageView_3.setImageResource(R.drawable.normalstar);
                        }
                        StateClick = "bad";
                        evaluationWillShow.setText(context.getString(R.string.Bad));
                    }
                });


                imageView_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        imageView_1.setImageResource(R.drawable.fullstar);
                        imageView_2.setImageResource(R.drawable.fullstar);
                        imageView_3.setImageResource(R.drawable.normalstar);
                        StateClick = "good";
                        evaluationWillShow.setText(context.getString(R.string.Good));
                    }
                });

                imageView_3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        imageView_1.setImageResource(R.drawable.fullstar);
                        imageView_2.setImageResource(R.drawable.fullstar);
                        imageView_3.setImageResource(R.drawable.fullstar);
                        StateClick = "very_good";
                        evaluationWillShow.setText(context.getString(R.string.VeryGood));
                    }
                });


                btn_evaluation_now.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int Value = 0;

                        if (StateClick.equals("bad")){
                            Value = itemDetails.bad + 1 ;
                        }
                        else if (StateClick.equals("good")){
                            Value = itemDetails.good + 1 ;
                        }
                        else if (StateClick.equals("very_good")){
                            Value = itemDetails.vgood + 1 ;
                        }


                        final int finalValue = Value;

                        fireBaseVarConnect.bDataBaseposts.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.hasChild("evaluation")){
                                    fireBaseVarConnect.bDataBaseposts.child(key).child("evaluation").child(StateClick)
                                            .setValue(finalValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isComplete()){
                                                dialog.dismiss();
                                                Toast.makeText(context, context.getString(R.string.toastTnxForE), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });


                                }else {
                                    HashMap<String,Integer> hashMap = new HashMap<String, Integer>();
                                    hashMap.put("bad",1);
                                    hashMap.put("good",1);
                                    hashMap.put("very_good",1);
                                    fireBaseVarConnect.bDataBaseposts.child(key).child("evaluation")
                                            .setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isComplete()){
                                                dialog.dismiss();
                                            }
                                        }
                                    });
                                }
                            }



                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });

            }
        });


    }


}
