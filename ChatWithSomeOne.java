package com.basim.outfitters.chat_classes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.basim.outfitters.R;
import com.basim.outfitters.adapters.ChatAdapter;
import com.basim.outfitters.firebase_use.FireBaseGetData;
import com.basim.outfitters.firebase_use.FireBaseVarConnect;
import com.basim.outfitters.modiles.ChatMessage_Model;
import com.basim.outfitters.modiles.Model_GetPosts;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import id.zelory.compressor.Compressor;

public class ChatWithSomeOne extends AppCompatActivity {
    private Button btn_SendMessage;
    private EditText edit_WriteMessage;
    private String getEditMessage, user_id, userImage, userName, mYID, imageChatUri = "default";
    private String myImage, myName;
    private TextView textView_NameU;
    private ImageView  chooseImage, displayImageBeforeSend;
    private boolean dateNotEmpty = false;
    byte[] byteImage;
    private RecyclerView recyclerViewMessages;
    public FireBaseVarConnect varConnect = new FireBaseVarConnect();
    private boolean knowWhenQuer;
    ArrayList<ChatMessage_Model> arrayList = new ArrayList<ChatMessage_Model>();
    ChatAdapter chatAdapter;
    String firstItemKey;
    private int LimitToGetMessage = 30;
    private int LoadMore = 20;
    LinearLayoutManager layoutManager;
    boolean mCanLoadMore = true;
    DatabaseReference databaseReference;

    FireBaseGetData fireBaseGetData = new FireBaseGetData();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_with_some_one);


        Intent getIntent = getIntent();
        user_id = getIntent.getStringExtra("user_id");
        mYID = varConnect.bCurrentUser.getUid();


        btn_SendMessage = (Button) findViewById(R.id.button_sendMessage);
        edit_WriteMessage = (EditText) findViewById(R.id.edittext_writeMessage);
        textView_NameU = (TextView) findViewById(R.id.text_nameUser);
        chooseImage = (ImageView) findViewById(R.id.image_choose);
        displayImageBeforeSend = (ImageView) findViewById(R.id.display_imageBefore);

        recyclerViewMessages = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        recyclerViewMessages.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerViewMessages.setLayoutManager(layoutManager);


        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChatUri = "default";
                Intent intentOpenGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentOpenGallery, 10);
            }
        });


        btn_SendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });


        recyclerViewMessages.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0 && mCanLoadMore
                        && arrayList.size() >= LimitToGetMessage) {
                    if (knowWhenQuer) {
                        databaseReference = varConnect.bChat.child(user_id).child(mYID);
                    } else {
                        databaseReference = varConnect.bChat.child(mYID).child(user_id);
                    }
                    Query query = databaseReference.orderByKey().endAt(firstItemKey).limitToLast(LoadMore + 1);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() <= LoadMore)
                                mCanLoadMore = false;
                            List<ChatMessage_Model> newScrolledItems = new ArrayList<>();
                            Iterator<DataSnapshot> itr = dataSnapshot.getChildren().iterator();

                            for (int i = 1; i < dataSnapshot.getChildrenCount(); i++) {
                                DataSnapshot item = itr.next();
                                if (newScrolledItems.size() == 0) {
                                    firstItemKey = item.getKey();
                                }
                                newScrolledItems.add(item.getValue(ChatMessage_Model.class));
                            }

                            arrayList.addAll(0, newScrolledItems);
                            chatAdapter.notifyItemRangeInserted(0, newScrolledItems.size());


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }
        });


        getUserInfo();
        getMyInfo();
        checkFromWhenMessageAndGetChat();
        putChatWith();



    }


    public void getUserInfo() {
        varConnect.bDataBaseUsers.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userName = dataSnapshot.child("name").getValue().toString();
                userImage = dataSnapshot.child("image").getValue().toString();

                textView_NameU.setText(userName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getMyInfo() {
        varConnect.bDataBaseUsers.child(mYID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myImage = dataSnapshot.child("image").getValue().toString();
                myName = dataSnapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
// get data from method get data from views and sent them to database
    public void sendMessage() {
        getDataFromViews();
        if (dateNotEmpty) {
            final HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("text_message", getEditMessage);
            hashMap.put("image", imageChatUri);
            hashMap.put("id", mYID);

            varConnect.bChat.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(mYID)) {
                        varConnect.bChat.child(user_id).child(mYID).push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                edit_WriteMessage.setText("");
                                displayImageBeforeSend.requestLayout();
                                displayImageBeforeSend.getLayoutParams().width = 0;
                                displayImageBeforeSend.getLayoutParams().height = 0;
                                displayImageBeforeSend.setImageDrawable(null);
                                imageChatUri = "default";
                                recyclerViewMessages.smoothScrollToPosition(recyclerViewMessages.getAdapter().getItemCount());

                            }
                        });
                    } else {
                        varConnect.bChat.child(mYID).child(user_id).push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                edit_WriteMessage.setText("");
                                displayImageBeforeSend.requestLayout();
                                displayImageBeforeSend.getLayoutParams().width = 0;
                                displayImageBeforeSend.getLayoutParams().height = 0;
                                displayImageBeforeSend.setImageDrawable(null);
                                imageChatUri = "";
                                recyclerViewMessages.smoothScrollToPosition(recyclerViewMessages.getAdapter().getItemCount());

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(this, "please add some data !!", Toast.LENGTH_SHORT).show();
        }
    }
// to get data from edittext and image
    public void getDataFromViews() {
        getEditMessage = edit_WriteMessage.getText().toString();
        if (!getEditMessage.equals("")) {
            dateNotEmpty = true;
        } else if (!imageChatUri.equals("default")) {
            dateNotEmpty = true;
        } else {
            dateNotEmpty = false;
        }
    }
// to see when variable chat and do method chat in it
    public void checkFromWhenMessageAndGetChat() {
        varConnect.bChat.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(mYID)) {
                    getChatAndSendToAdapter(varConnect.bChat.child(user_id).child(mYID).limitToLast(LimitToGetMessage));
                    knowWhenQuer = true;
                } else {
                    getChatAndSendToAdapter(varConnect.bChat.child(mYID).child(user_id).limitToLast(LimitToGetMessage));
                    knowWhenQuer = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
 // to get all chat items and set them to adapter
    public void getChatAndSendToAdapter(Query query) {
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (firstItemKey == null) {
                    firstItemKey = dataSnapshot.getKey();
                }
                StatementRecycler(dataSnapshot.getValue(ChatMessage_Model.class));
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
// to see if recycler has data or not and see when user scrolling
    public void StatementRecycler(ChatMessage_Model chatMessageModel) {
        arrayList.add(chatMessageModel);


        if (recyclerViewMessages.getAdapter() == null) {
            chatAdapter = new ChatAdapter(arrayList, getApplicationContext(), mYID, userImage, myImage);
            recyclerViewMessages.setAdapter(chatAdapter);
        } else {
            chatAdapter.notifyItemInserted(chatAdapter.getItemCount() - 1);
        }
    }

// to put users do content to u in database chat with
    public void putChatWith() {
        varConnect.bChatWith.child(mYID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.child("my_friends").hasChild(user_id)) {

                } else {
                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("username", userName);
                    hashMap.put("userimage", userImage);
                    hashMap.put("userid", user_id);
                    varConnect.bChatWith.child(mYID).child("my_friends").child(user_id).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                hashMap.put("username", myName);
                                hashMap.put("userimage", userImage);
                                hashMap.put("userid", mYID);
                                varConnect.bChatWith.child(user_id).child("my_friends").child(mYID).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                        }
                                    }
                                });
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == RESULT_OK) {

            displayImageBeforeSend.requestLayout();
            displayImageBeforeSend.getLayoutParams().width = 160;
            displayImageBeforeSend.getLayoutParams().height = 100;
            Glide.with(getApplicationContext()).load(R.drawable.loading_2).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade();

            Uri uri = data.getData();


            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                byteImage = baos.toByteArray();


            } catch (IOException e) {
                e.printStackTrace();
            }


            Calendar calendar = Calendar.getInstance();
            long timeMilli = calendar.getTimeInMillis();


            final StorageReference filePath = varConnect.bStorageReference.child("chat_image").child(timeMilli + ".jpg");

            filePath.putBytes(byteImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        imageChatUri = task.getResult().getDownloadUrl().toString();
                        Glide.with(getApplicationContext()).load(imageChatUri).into(displayImageBeforeSend);

                        getDataFromViews();
                        /*
                        if (dateNotEmpty) {
                            btn_SendMessage.setBackgroundColor(Color.BLUE);
                            btn_SendMessage.setTextColor(Color.WHITE);
                        }
                        */
                    }
                }
            });


        }
    }
}
