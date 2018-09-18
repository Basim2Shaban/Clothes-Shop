package com.basim.outfitters;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basim.outfitters.firebase_use.FireBaseVarConnect;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class PropertiesOfEdit extends AppCompatActivity {
    public String getIntent , mId  ;
    String myName , myAge , myDisk ;
    private TextView textViewEdit ;
    private EditText editTextEdit ;
    private Button buttonSaveChange ;
    FireBaseVarConnect varConnect = new FireBaseVarConnect();
    Map hashMap = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.properties_of_edit);


        textViewEdit = (TextView)findViewById(R.id.Text_Edit);
        editTextEdit = (EditText)findViewById(R.id.Edit_Change);
        buttonSaveChange = (Button)findViewById(R.id.btnSaveChange);


        mId = varConnect.bCurrentUser.getUid();


        Intent intent = getIntent() ;
        getIntent = intent.getStringExtra("key");






        editTextEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextEdit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (editTextEdit.getText().length() >= 1){
                            buttonSaveChange.setEnabled(true);
                            buttonSaveChange.setBackgroundColor(Color.BLUE);

                        }else{
                            buttonSaveChange.setBackgroundColor(Color.GRAY);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (editTextEdit.getText().length() >= 1){
                            buttonSaveChange.setBackgroundColor(Color.BLUE);
                        }else{
                            buttonSaveChange.setBackgroundColor(Color.GRAY);
                        }
                    }


                });
            }
        });



        switch (getIntent){
            case "name":
                GetName();
                buttonSaveChange.setBackgroundColor(Color.GRAY);
                buttonSaveChange.setEnabled(false);

                editTextEdit.setFilters(new InputFilter[] {new InputFilter.LengthFilter(12)});
                textViewEdit.setText(getString(R.string.editName));
                editTextEdit.setHint("write name..");
                buttonSaveChange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveName();
                    }
                });
                break;

            case "age":
                GetAge();
                buttonSaveChange.setBackgroundColor(Color.GRAY);
                buttonSaveChange.setEnabled(false);

                textViewEdit.setText(getString(R.string.EditAge));
                editTextEdit.setHint("write Age..");
                editTextEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
                buttonSaveChange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveAge();
                    }
                });
                break;


            case "disk":
                GetDisk();
                buttonSaveChange.setBackgroundColor(Color.GRAY);
                buttonSaveChange.setEnabled(false);

                textViewEdit.setText(getString(R.string.EditDescreption));
                editTextEdit.setHint("write Discreption..");
                buttonSaveChange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        saveDiscreption();
                    }
                });

                break;
        }
    }

    public void saveName(){
        String getEditText = editTextEdit.getText().toString().trim();
        hashMap.put("name",getEditText);

        varConnect.bDataBaseUsers.child(mId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
              //  editTextEdit.setText("");
                buttonSaveChange.setBackgroundColor(Color.GRAY);
                Toast.makeText(PropertiesOfEdit.this, "Update Name is Done", Toast.LENGTH_LONG).show();
            }
        });

    }
    public void saveAge(){
        String getEditText = editTextEdit.getText().toString().trim();
        hashMap.put("age",getEditText);
        varConnect.bDataBaseUsers.child(mId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
            //    editTextEdit.setText("");
                buttonSaveChange.setBackgroundColor(Color.GRAY);
                Toast.makeText(PropertiesOfEdit.this, "Update Age is Done", Toast.LENGTH_LONG).show();
            }
        });

    }
    public void saveDiscreption(){
        String getEditText = editTextEdit.getText().toString().trim();
        hashMap.put("discreption",getEditText);
        varConnect.bDataBaseUsers.child(mId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
          //      editTextEdit.setText("");
                buttonSaveChange.setBackgroundColor(Color.GRAY);
                Toast.makeText(PropertiesOfEdit.this, "Update discreption is Done", Toast.LENGTH_LONG).show();
            }
        });

    }



    public void GetName(){
        varConnect.bDataBaseUsers.child(mId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myName = dataSnapshot.child("name").getValue().toString();
                editTextEdit.setText(myName);
                buttonSaveChange.setEnabled(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void GetAge(){
        varConnect.bDataBaseUsers.child(mId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    myAge = dataSnapshot.child("age").getValue().toString();
                    editTextEdit.setText(myAge);
                    buttonSaveChange.setEnabled(false);
                }catch (Exception e){}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void GetDisk(){
        varConnect.bDataBaseUsers.child(mId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    myDisk = dataSnapshot.child("discreption").getValue().toString();
                    editTextEdit.setText(myDisk);
                    buttonSaveChange.setEnabled(false);

                }catch (Exception e){}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}

