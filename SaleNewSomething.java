package com.basim.outfitters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.basim.outfitters.firebase_use.FireBaseVarConnect;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;

public class SaleNewSomething extends AppCompatActivity {
    private Button btn_sale_now;
    private ImageView imageView_add_photo_1, imageView_add_photo_2;
    private EditText editText_product_name, editText_product_price, editText_product_colors, edit_price_all,
            editText_product_more, editText_phone;
    private FireBaseVarConnect FBO = new FireBaseVarConnect();
    private String MyName, MyEmail, Image_1, Image_2, mUId;
    private ProgressDialog progressDialog;
    HashMap<String, String> hashMap = new HashMap<String, String>();
    byte[] image_byte;
    private Toolbar toolbarSale ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_new_something);


        GetMyInfo();

        mUId = FBO.bCurrentUser.getUid();

        btn_sale_now = (Button) findViewById(R.id.btn_sale_finish);
        imageView_add_photo_1 = (ImageView) findViewById(R.id.image_add_1);
        imageView_add_photo_2 = (ImageView) findViewById(R.id.image_add_2);
        editText_product_name = (EditText) findViewById(R.id.edit_product_name);
        editText_product_price = (EditText) findViewById(R.id.edit_product_price);
        editText_product_colors = (EditText) findViewById(R.id.edit_product_colors);
        editText_product_more = (EditText) findViewById(R.id.edit_product_more);
        editText_phone = (EditText) findViewById(R.id.edit_phone_num);
        edit_price_all = (EditText) findViewById(R.id.edit_price_all);
        toolbarSale = (Toolbar)findViewById(R.id.toolBarSale);


        setSupportActionBar(toolbarSale);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("");



        btn_sale_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(SaleNewSomething.this);
                progressDialog.setTitle(getString(R.string.massageproduct));
                progressDialog.setMessage(getString(R.string.massagewait));
                progressDialog.show();




                if (imageView_add_photo_2.getDrawable() != null
                        && imageView_add_photo_2.getDrawable() != getResources().getDrawable(R.drawable.add_photo_dark)) {

                    String productName = editText_product_name.getText().toString().trim();
                    String productPrice = editText_product_price.getText().toString().trim();
                    String productColors = editText_product_colors.getText().toString().trim();
                    String moreAboutProduct = editText_product_more.getText().toString().trim();
                    String allPrice = edit_price_all.getText().toString().trim();
                    String PhoneNum = editText_phone.getText().toString().trim();


                    if (!productName.isEmpty() && !productPrice.isEmpty() && !productColors.isEmpty() && !moreAboutProduct.isEmpty() && !allPrice.isEmpty() && !PhoneNum.isEmpty()) {
                        SendPost(productName, productPrice, productColors, moreAboutProduct, allPrice, PhoneNum);
                    } else {
                        progressDialog.hide();
                        Toast.makeText(SaleNewSomething.this, getString(R.string.addmoredetiles), Toast.LENGTH_SHORT).show();
                    }


                } else {
                    progressDialog.hide();
                    Toast.makeText(SaleNewSomething.this, getString(R.string.addImageagain), Toast.LENGTH_SHORT).show();
                }
            }
        });


        imageView_add_photo_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent galleryint = new Intent(Intent.ACTION_PICK);
                galleryint.setType("image/*");
                startActivityForResult(galleryint, 10);


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == RESULT_OK) {


            Uri uri = data.getData();


            try {
                ContentResolver cr = this.getContentResolver();
                InputStream inputStream = cr.openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Bitmap res = Bitmap.createScaledBitmap(bitmap, 500, 650, false);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                res.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                image_byte = baos.toByteArray();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            Calendar calender = Calendar.getInstance();
            long num = calender.getTimeInMillis();

            final StorageReference riversRef = FBO.bStorageReference.child("posts/" + FBO.bCurrentUser.getUid() + num + ".jpg");

            riversRef.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if (task.isSuccessful()) {
                        UploadTask uploadTask = riversRef.putBytes(image_byte);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                imageView_add_photo_2.setVisibility(View.VISIBLE);
                                // Get a URL to the uploaded content
                                if (imageView_add_photo_2.getDrawable() != null) {
                                    Image_2 = taskSnapshot.getDownloadUrl().toString();
                                    hashMap.put("image_2", Image_2);
                                    Glide.with(SaleNewSomething.this).load(Image_2).placeholder(R.drawable.loading_2).into(imageView_add_photo_1);
                                } else {
                                    Image_1 = taskSnapshot.getDownloadUrl().toString();
                                    hashMap.put("image_1", Image_1);
                                    Glide.with(SaleNewSomething.this).load(Image_1).placeholder(R.drawable.loading_2).into(imageView_add_photo_2);
                                }



                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Toast.makeText(SaleNewSomething.this, getString(R.string.tryUpload), Toast.LENGTH_LONG).show();
                                        // ...
                                    }
                                });
                    }
                }
            });


            if (imageView_add_photo_1.getDrawable() != null &&
                    imageView_add_photo_1.getDrawable() != getResources().getDrawable(R.drawable.add_photo_dark) && imageView_add_photo_2.getDrawable() != null) {
                imageView_add_photo_1.setEnabled(false);
            }


        }

    }

    public void SendPost(String productName, String productPrice, final String productColors, String moreAboutProduct, String allPrice, String phone) {

        hashMap.put("name", productName);
        hashMap.put("price", productPrice);
        hashMap.put("colors", productColors);
        hashMap.put("more", moreAboutProduct);
        hashMap.put("myName", MyName);
        hashMap.put("myEmail", MyEmail);
        hashMap.put("allPrice", allPrice);
        hashMap.put("mobile", phone);
        hashMap.put("user_id", mUId);


        FBO.bDataBaseposts.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(SaleNewSomething.this, getString(R.string.published), Toast.LENGTH_LONG).show();
                    editText_product_more.setText("");
                    editText_product_colors.setText("");
                    editText_product_price.setText("");
                    editText_product_name.setText("");
                    imageView_add_photo_1.setImageDrawable(null);
                    imageView_add_photo_2.setImageDrawable(null);
                    editText_phone.setText("");
                    edit_price_all.setText("");

                    startActivity(new Intent(SaleNewSomething.this,MainActivity.class));





                } else {
                    progressDialog.hide();
                    Toast.makeText(SaleNewSomething.this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void GetMyInfo() {
        FBO.bDataBaseUsers.child(FBO.bCurrentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MyName = dataSnapshot.child("name").getValue().toString();
                MyEmail = dataSnapshot.child("email").getValue().toString();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
