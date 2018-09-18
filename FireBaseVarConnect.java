package com.basim.outfitters.firebase_use;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Basim on 17/04/2018.
 */

public class FireBaseVarConnect {
    public DatabaseReference bDataBase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser bCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    public DatabaseReference mAdsShowNow = FirebaseDatabase.getInstance().getReference().child("ads_now");
    public DatabaseReference mCurrentPage = FirebaseDatabase.getInstance().getReference().child("current_page");


    public DatabaseReference bDataBaseUsers = bDataBase.child("users");
    public DatabaseReference bDataBaseposts = bDataBase.child("posts");
    public DatabaseReference bChat = bDataBase.child("chat");
    public DatabaseReference bChatWith = bDataBase.child("chat_with");
    public DatabaseReference bUserStateConn = bDataBase.child("state_conn");
    public DatabaseReference bReports = bDataBase.child("reports");
    public DatabaseReference bAdLottery = bDataBase.child("adLottery");
    public StorageReference bStorageReference = FirebaseStorage.getInstance().getReference();
    public FirebaseAuth bAuth = FirebaseAuth.getInstance();

}
