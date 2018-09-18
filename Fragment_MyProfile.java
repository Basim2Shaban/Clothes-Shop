package com.basim.outfitters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.basim.outfitters.firebase_use.FireBaseVarConnect;
import com.basim.outfitters.image_classes.ActivityFullScreen;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_MyProfile extends Fragment {

    private ImageView imageViewCover, imageViewChangeCov;
    private CircleImageView cImageViewProfile;
    private TextView textViewChangeImageP, textViewPName , txtAge , txtDisc;
    private FireBaseVarConnect varConnect = new FireBaseVarConnect();
    private String mId, myImageP, myImageCover, myName , myAge , myDisk;
    private byte byteImage[];


    public Fragment_MyProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__my_profile, container, false);

        mId = varConnect.bCurrentUser.getUid();

        linkViews(view);

        getImagesAndName(getActivity());
        getAge();
        getDisk();


        textViewChangeImageP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentOpenGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentOpenGallery, 100);
            }
        });


        imageViewChangeCov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentOpenGallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentOpenGallery, 10);
            }
        });

        cImageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentTo = new Intent(getActivity(), ActivityFullScreen.class);
                intentTo.putExtra("uri", myImageP);
                startActivity(intentTo);
            }
        });


        imageViewCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentTo = new Intent(getActivity(), ActivityFullScreen.class);
                intentTo.putExtra("uri", myImageCover);
                startActivity(intentTo);
            }
        });





        return view;

    }


    public void linkViews(View view) {
        imageViewChangeCov = (ImageView) view.findViewById(R.id.change_cover);
        imageViewCover = (ImageView) view.findViewById(R.id.image_cover);
        cImageViewProfile = (CircleImageView) view.findViewById(R.id.image_myProfile);
        textViewChangeImageP = (TextView) view.findViewById(R.id.text_change_imageProfile);
        textViewPName = (TextView) view.findViewById(R.id.text_ProfileName);
        txtDisc = (TextView) view.findViewById(R.id.txt_descreption);
        txtAge = (TextView) view.findViewById(R.id.txt_age);
    }


    public void getImagesAndName(final Context context) {
        varConnect.bDataBaseUsers.child(mId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myName = dataSnapshot.child("name").getValue().toString();
                textViewPName.setText(myName);
                try {
                myImageP = dataSnapshot.child("image").getValue().toString();
                Glide.with(context).load(myImageP).into(cImageViewProfile);


                    myImageCover = dataSnapshot.child("image_cover").getValue().toString();
                    Glide.with(getActivity()).load(myImageCover).into(imageViewCover);
                } catch (Exception e) {
                    Log.e("There Or not : ", e.toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getAge(){
        varConnect.bDataBaseUsers.child(mId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    myAge = dataSnapshot.child("age").getValue().toString();
                    txtAge.setText(myAge + getString(R.string.year));
                }catch (Exception e){}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void getDisk(){
        varConnect.bDataBaseUsers.child(mId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    myDisk = dataSnapshot.child("discreption").getValue().toString();
                    txtDisc.setText(myDisk);

                }catch (Exception e){}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == getActivity().RESULT_OK) {

            imageViewCover.setImageDrawable(null);
            Uri uri = data.getData();


            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                byteImage = baos.toByteArray();


            } catch (IOException e) {
                e.printStackTrace();
            }


            final StorageReference filePath = varConnect.bStorageReference.child("Cover_users").child(mId + ".jpg");

            filePath.putBytes(byteImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        myImageCover = task.getResult().getDownloadUrl().toString();
                        Map hashMap = new HashMap();
                        hashMap.put("image_cover", myImageCover);
                        varConnect.bDataBaseUsers.child(mId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Glide.with(getActivity()).load(myImageCover).into(imageViewCover);
                                }
                            }
                        });
                    }
                }
            });


        } else if (requestCode == 100 && resultCode == getActivity().RESULT_OK) {
            cImageViewProfile.setImageDrawable(null);

            Uri uri = data.getData();


            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                byteImage = baos.toByteArray();


            } catch (IOException e) {
                e.printStackTrace();
            }


            final StorageReference filePath = varConnect.bStorageReference.child("profile_images").child(mId + ".jpg");

            filePath.putBytes(byteImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        myImageP = task.getResult().getDownloadUrl().toString();
                        Map hashMap = new HashMap();
                        hashMap.put("image", myImageP);
                        varConnect.bDataBaseUsers.child(mId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Glide.with(getActivity()).load(myImageP).into(cImageViewProfile);
                                }
                            }
                        });
                    }
                }
            });

        }
    }


}
