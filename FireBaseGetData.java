package com.basim.outfitters.firebase_use;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basim.outfitters.R;
import com.basim.outfitters.adapters.AdapterGetAllPosts;
import com.basim.outfitters.modiles.ChatMessage_Model;
import com.basim.outfitters.modiles.Model_GetPosts;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.threeten.bp.Duration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Basim on 05/06/2018.
 */

public class FireBaseGetData {
    public static String image, text;
    private static  String  keyNext , keyBack;
    private ArrayList<Model_GetPosts> ArrayGetPosts = new ArrayList<>();
    ArrayList<Model_GetPosts> arrayGetPostsAndClear = new ArrayList<>();
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private AdapterGetAllPosts adapterGetAllPosts;
    private int TOTAL_ITEM_EACH_LOAD = 40 , CurrentPage = 1 ;


    private static FireBaseVarConnect fireBaseVarConnect = new FireBaseVarConnect();


    public void getArrayAllPosts(final RecyclerView recyclerView_allPosts, final Context context ) {
        if (ArrayGetPosts.size() <= 0){
            Query query = fireBaseVarConnect.bDataBaseposts.limitToLast(TOTAL_ITEM_EACH_LOAD);
            getPostsByQuery(recyclerView_allPosts,context , query );
        }

    }


    public static void getLastMessage(final Context context, final String mId, final String user_id, final TextView textView) {
        fireBaseVarConnect.bChat.child(mId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user_id)) {
                    assistGetLastMessage(fireBaseVarConnect.bChat.child(mId).child(user_id).orderByKey().limitToLast(1), textView, context);
                } else {
                    assistGetLastMessage(fireBaseVarConnect.bChat.child(user_id).child(mId).orderByKey().limitToLast(1), textView, context);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //to get message in the moment
    private static void assistGetLastMessage(Query databaseReference, final TextView textView, final Context context) {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessage_Model chatMessageModel = dataSnapshot.getValue(ChatMessage_Model.class);
                image = chatMessageModel.getImage();
                text = chatMessageModel.getText_message();

                if (!text.equals("")) {
                    textView.setText(text);
                } else {
                    textView.setText(context.getString(R.string.Image));
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static void getUserStateConn(final Context context, String id, final TextView textView) {
        fireBaseVarConnect.bUserStateConn.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long lastSeenTime = dataSnapshot.child("state").getValue(Long.class);
                if (lastSeenTime == -1) {
                    textView.setText("متصل ");
                } else {

                    org.threeten.bp.Instant current = org.threeten.bp.Instant.now();
                    org.threeten.bp.Instant last = org.threeten.bp.Instant.ofEpochMilli(lastSeenTime);
                    Duration duration = Duration.between(last, current);

                    if (duration.toMinutes() <= 60) {

                        textView.setText(duration.toMinutes() + context.getString(R.string.m));
                    } else {
                        if (duration.toHours() != 0) {
                            textView.setText(duration.toHours() + context.getString(R.string.h));
                            if (duration.toHours() >= 24) {
                                Date date = new Date(lastSeenTime);
                                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                String formatted = formatter.format(date);
                                textView.setText(formatted);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void OnScrollRec(RecyclerView recyclerView  , final LinearLayout linearLayout , final Button next , Button back , final Context context) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int mSpanCount = 2;
                int[] into = new int[mSpanCount];
                if (staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(into)[0] == ArrayGetPosts.size()-2 ||
                        staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(into)[0] == ArrayGetPosts.size()-3 ||
                        staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(into)[0] == ArrayGetPosts.size()-4) {
                    linearLayout.setVisibility(View.VISIBLE);
                }else{
                    linearLayout.setVisibility(View.INVISIBLE);
                   }


                 if (ArrayGetPosts.size() < TOTAL_ITEM_EACH_LOAD - 6){
                    linearLayout.setVisibility(View.VISIBLE);
                    next.setVisibility(View.INVISIBLE);
                }else {
                     next.setVisibility(View.VISIBLE);
                 }


            }
        });
    }


    private void getPostsByQuery(final RecyclerView recyclerView , final Context context , Query query){
        ArrayGetPosts.clear();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Model_GetPosts modelGetPosts = dataSnapshot1.getValue(Model_GetPosts.class);
                    modelGetPosts.setKeyItem(dataSnapshot1.getRef().getKey());
                    ArrayGetPosts.add(modelGetPosts);
                    keyNext = ArrayGetPosts.get(0).getKeyItem();
                    keyBack = ArrayGetPosts.get(ArrayGetPosts.size()-1).getKeyItem();
                }
                Collections.reverse(ArrayGetPosts);

                staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
                // staggeredGridLayoutManager = new GridLayoutManager(context,2);
                recyclerView.setLayoutManager(staggeredGridLayoutManager);


                adapterGetAllPosts = new AdapterGetAllPosts(ArrayGetPosts, context);
                recyclerView.setAdapter(adapterGetAllPosts);
                adapterGetAllPosts.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onClickBtnNext(final RecyclerView recyclerView, Button btn_next, final Context context , final LinearLayout lin){
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 //   Toast.makeText(context, keyNext, Toast.LENGTH_SHORT).show();
                    Query query = fireBaseVarConnect.bDataBaseposts.orderByKey().limitToFirst(TOTAL_ITEM_EACH_LOAD).endAt(keyNext);
                    getPostsByQuery(recyclerView,context,query);
                    // OnScrollRec(recyclerView,lin);
                CurrentPage++ ;


            }
        });
    }
    public void onClickBtnBack(final RecyclerView recyclerView, Button btn_next, final Context context , final LinearLayout lin){
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query query ;
                if (CurrentPage == 1){
                    Toast.makeText(context, "انت مازلت في الصفحه الاولي !", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (CurrentPage == 2){
                        query = fireBaseVarConnect.bDataBaseposts.limitToLast(TOTAL_ITEM_EACH_LOAD);
                    }else {
                        query = fireBaseVarConnect.bDataBaseposts.orderByKey().limitToFirst(TOTAL_ITEM_EACH_LOAD).startAt(keyBack);
                    }
                    getPostsByQuery(recyclerView,context,query);
                    CurrentPage -- ;
                }


            }
        });
    }
}