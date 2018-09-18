package com.basim.outfitters;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basim.outfitters.firebase_use.FireBaseVarConnect;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class StartActivity extends AppCompatActivity {
    private Button Btn_Sign_in , Btn_Create_New_Account ;
    private EditText Edit_Sign_Email , Edit_Sign_Password ;
    private TextView Text_Forget_password ;
    private FireBaseVarConnect FBO = new FireBaseVarConnect();
    private String get_email , get_password ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);


        Btn_Sign_in =(Button)findViewById(R.id.button_sign_in);
        Btn_Create_New_Account =(Button)findViewById(R.id.button_create_new_account);
        Edit_Sign_Email = (EditText)findViewById(R.id.edit_sign_email);
        Edit_Sign_Password = (EditText)findViewById(R.id.edit_sign_password);
      //  Text_Forget_password = (TextView)findViewById(R.id.text_forget_password);







        //Moved to register //
        Btn_Create_New_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_to_register = new Intent(StartActivity.this,RegisterActivity.class);
                startActivity(intent_to_register);
            }
        });




            Btn_Sign_in.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    get_email = Edit_Sign_Email.getText().toString().trim();
                    get_password = Edit_Sign_Password.getText().toString().trim();

                    if (!get_email.isEmpty() || !get_password.isEmpty()) {

                        final ProgressDialog progressDialog = new ProgressDialog(StartActivity.this);
                        progressDialog.setTitle("تسجـيل الدخـول");
                        progressDialog.setMessage("برجاء الانتظار , جار تسجيل الدخول");
                        progressDialog.show();
                        FBO.bAuth.signInWithEmailAndPassword(get_email, get_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                                    intent.putExtra("login","login");
                                    startActivity(intent);
                                    progressDialog.dismiss();
                                } else {
                                    Toast.makeText(StartActivity.this, "حدث خطا , يرجي التاكد من بياناتك ", Toast.LENGTH_SHORT).show();
                                    progressDialog.hide();
                                }
                            }
                        });

                    }else {
                        Toast.makeText(StartActivity.this, "يرجي كتابه بيانات الدخول ؟", Toast.LENGTH_SHORT).show();
                    }


                }
            });

    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
