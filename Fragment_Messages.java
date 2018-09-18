package com.basim.outfitters;


import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.basim.outfitters.adapters.AdapterChatWithThem;
import com.basim.outfitters.firebase_use.FireBaseVarConnect;
import com.basim.outfitters.modiles.Model_UsersChatWithThem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Fragment_Messages extends Fragment {
    private FireBaseVarConnect varConnect = new FireBaseVarConnect();
    private String mId ;
    private ArrayList<Model_UsersChatWithThem> arrayList = new ArrayList<>();

    private RecyclerView recyclerView ;
    LinearLayoutManager layoutManager;

    public Fragment_Messages() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_messages,container, false);



        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_chatingWith);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        mId = varConnect.bCurrentUser.getUid();


        getUsersChatWith();

        sendToken();







        return view ;
    }



    public void getUsersChatWith(){
        varConnect.bChatWith.child(mId).child("my_friends").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                onGetUsers(dataSnapshot.getValue(Model_UsersChatWithThem.class),getActivity());
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

    public void onGetUsers(Model_UsersChatWithThem chatWithThem , Context context){
        arrayList.add(chatWithThem);
        AdapterChatWithThem adapterChatWithThem = new AdapterChatWithThem(context,arrayList);
        recyclerView.setAdapter(adapterChatWithThem);
    }



    public void sendToken(){
        Map hashMap = new HashMap<>();
        String token = FirebaseInstanceId.getInstance().getToken();
        hashMap.put("token",token);

        varConnect.bDataBaseUsers.child(mId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                //  editTextEdit.setText("");
            }
        });
    }
}
