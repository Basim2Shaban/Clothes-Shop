package com.basim.outfitters;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.basim.outfitters.firebase_use.FireBaseVarConnect;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class Dialog_UploadImageAndContinue extends AppCompatActivity {

    private ImageView imageView_choeesImage;
    private CircleImageView circleImageView_dis_image;
    private EditText editTextPassword ;
    private Button btn_continue;
    private Bitmap thump_bitmap ;
    Intent getintent;
    ProgressDialog mprogressdialogg;
    private String Name, Email, Password, Radio, Country, ImageUri , token;
    FireBaseVarConnect varConnect = new FireBaseVarConnect();
    int IntentValue = 100;
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog__upload_image_and_continue);

        mStorageRef = FirebaseStorage.getInstance().getReference();

        getintent = getIntent();

        Name = getintent.getStringExtra("name");
        Email = getintent.getStringExtra("email");
        Password = getintent.getStringExtra("password");
        Radio = getintent.getStringExtra("radio");
        Country = getintent.getStringExtra("country");
        token = FirebaseInstanceId.getInstance().getToken();



        imageView_choeesImage = (ImageView) findViewById(R.id.image_choees_image);
        circleImageView_dis_image = (CircleImageView) findViewById(R.id.c_image_dis_image);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        editTextPassword = (EditText) findViewById(R.id.editTextPasswordD);
        btn_continue.setEnabled(false);


        imageView_choeesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryint = new Intent(Intent.ACTION_PICK);
                galleryint.setType("image/*");
                startActivityForResult(galleryint, IntentValue);
            }
        });

        EditStatue(editTextPassword);
        editTextPassword.setFilters(new InputFilter[] {new InputFilter.LengthFilter(8)});




        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextPassword.getText().toString().isEmpty()){
                    Toast.makeText(Dialog_UploadImageAndContinue.this, "يرجي تاكيد كلمه السر", Toast.LENGTH_SHORT).show();
                }else{
                    if (editTextPassword.getText().toString().equals(Password)){
                        CreateProgressDialogAndUploadData();
                    }else{
                        Toast.makeText(Dialog_UploadImageAndContinue.this, "كمله السر غير متطابقه !", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentValue || resultCode == RESULT_OK) {
            Uri uri = data.getData();
            CropAndConvertImage(uri, data, requestCode, resultCode);


        }
    }


    public void CropAndConvertImage(Uri uri, Intent data, int requestCode, int resultCode) {


        CropImage.activity(uri)
                // .setAspectRatio(1, 1)
                //.setMinCropWindowSize(200,200)
                .start(this);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            final CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mprogressdialogg = new ProgressDialog(Dialog_UploadImageAndContinue.this);
                mprogressdialogg.setTitle("تعديل الصوره");
                mprogressdialogg.setMessage("جار قص صورتك...");
                mprogressdialogg.setCanceledOnTouchOutside(false);
                mprogressdialogg.show();

                Uri resulturi = result.getUri();


                final File thump_filepatch = new File(resulturi.getPath());
                //111111111

                String current_user_id = varConnect.bCurrentUser.getUid();


                thump_bitmap = new Compressor(this) //00000000000
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(70)
                        .compressToBitmap(thump_filepatch);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thump_bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                final byte[] thump_byte = baos.toByteArray();


                // StorageReference filepatch = mStorageRef.child("profile_images").child(current_user_id+".jpg");
                final StorageReference thump_filepath = mStorageRef.child("profile_images").child(current_user_id + ".jpg");


                thump_filepath.putFile(resulturi).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {


                        if (task.isSuccessful()) {
                            UploadTask uploadTask = thump_filepath.putBytes(thump_byte); //000000000000
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thump_task) {


                                    if (thump_task.isSuccessful()) {
                                        ImageUri = thump_task.getResult().getDownloadUrl().toString();
                                        circleImageView_dis_image.setBackground(null);
                                        imageView_choeesImage.setVisibility(View.INVISIBLE);
                                        Glide.with(Dialog_UploadImageAndContinue.this).load(ImageUri).into(circleImageView_dis_image);
                                        mprogressdialogg.dismiss();
                                        btn_continue.setBackgroundColor(getResources().getColor(R.color.BTN_True));
                                        btn_continue.setEnabled(true);


                                    } else {
                                        Toast.makeText(Dialog_UploadImageAndContinue.this, "خطا اثناء التحميل", Toast.LENGTH_SHORT).show();
                                        mprogressdialogg.dismiss();
                                        btn_continue.setEnabled(false);
                                    }


                                }
                            });


                        } else {
                            Toast.makeText(Dialog_UploadImageAndContinue.this, "خطا اثناء التحميل", Toast.LENGTH_SHORT).show();
                            mprogressdialogg.dismiss();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }


    public void EditStatue(final EditText edit){
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (edit.getText().length() >= 1){
                            btn_continue.setEnabled(true);
                            btn_continue.setBackgroundColor(Color.parseColor("#1741a3"));

                        }else{
                            btn_continue.setBackgroundColor(Color.parseColor("#686666"));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (edit.getText().length() >= 1){
                            btn_continue.setBackgroundColor(Color.parseColor("#1741a3"));
                        }else{
                            btn_continue.setBackgroundColor(Color.parseColor("#686666"));                        }
                    }


                });
            }
        });
    }

    public void CreateProgressDialogAndUploadData(){
        mprogressdialogg = new ProgressDialog(Dialog_UploadImageAndContinue.this);
        mprogressdialogg.setTitle("انشاء حساب جديد");
        mprogressdialogg.setMessage("يرجي الانتظار , جار انشاء حسابك");
        mprogressdialogg.show();


        HashMap<String, String> SetData = new HashMap<String, String>();
        SetData.put("name", Name);
        SetData.put("email", Email);
        SetData.put("password", Password);
        SetData.put("type", Radio);
        SetData.put("country", Country);
        SetData.put("image", ImageUri);
        SetData.put("token",token);

        varConnect.bDataBaseUsers.child(varConnect.bCurrentUser.getUid()).setValue(SetData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    mprogressdialogg.hide();
                    Intent Main = new Intent(Dialog_UploadImageAndContinue.this, MainActivity.class);
                    startActivity(Main);
                } else {
                    mprogressdialogg.dismiss();
                    Toast.makeText(Dialog_UploadImageAndContinue.this, "حاول مجددا", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
