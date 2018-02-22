package com.example.nomanikram.epilepsyseizuredetection;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.nomanikram.epilepsyseizuredetection.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileDetailActivity extends AppCompatActivity {

    private AppCompatButton btn_done;

    private RadioGroup radioGroup;
    private AppCompatRadioButton check_radio_button;

    private AppCompatEditText txt_name;
    private AppCompatEditText txt_age;
    private AppCompatEditText txt_gender;
    private AppCompatEditText txt_contactno;

    private TextInputLayout textInputLayout_name;
    private TextInputLayout textInputLayout_age;
    private TextInputLayout textInputLayout_contactno;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    FirebaseDatabase database;
    DatabaseReference mRef;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);

        btn_done = (AppCompatButton) findViewById(R.id.btn_done);

        txt_name = (AppCompatEditText) findViewById(R.id.txt_name);
        txt_age  = (AppCompatEditText) findViewById(R.id.txt_age);
    //  txt_gender = (AppCompatEditText) findViewById(R.id.txt_gender);
        txt_contactno =(AppCompatEditText) findViewById(R.id.txt_contactno);

        textInputLayout_name = (TextInputLayout) findViewById(R.id.textInputLayout_name);
        textInputLayout_age = (TextInputLayout) findViewById(R.id.textInputLayout_age);
        textInputLayout_contactno = (TextInputLayout) findViewById(R.id.textInputLayout_contactno);

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup_gender);


        mAuth = FirebaseAuth.getInstance();



        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();


        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int radioButton_id = radioGroup.getCheckedRadioButtonId();

                 if (R.id.radio_male == radioButton_id) {
                     check_radio_button= findViewById(R.id.radio_male);
                     Log.w("TAG", "Male:");
                 }
                 if (R.id.radio_female == radioButton_id){
                     check_radio_button= findViewById(R.id.radio_female);
                     Log.w("TAG", "FEMale:");
                 }
                   //  Log.w("TAG", "FEMale:");

                boolean is_textfield_empty = txt_name.getText().toString().isEmpty() ||
                        txt_age.getText().toString().isEmpty() ||
                        txt_contactno.getText().toString().isEmpty() ||
                        ( R.id.radio_male != radioButton_id &&
                        R.id.radio_female != radioButton_id) ;

                if(txt_name.getText().toString().isEmpty())
                {
                  textInputLayout_name.setErrorEnabled(true);
                  textInputLayout_name.setError("Name field is empty");
                }
                else
                  textInputLayout_name.setErrorEnabled(false);

                if(txt_age.getText().toString().isEmpty())
                {
                    textInputLayout_age.setErrorEnabled(true);
                    textInputLayout_age.setError("Age field is empty");
                }
                else
                    textInputLayout_age.setErrorEnabled(false);


                if(txt_contactno.getText().toString().isEmpty())
                {
                    textInputLayout_contactno.setErrorEnabled(true);
                    textInputLayout_contactno.setError("Contact no field is empty");
                }
                else
                    textInputLayout_contactno.setErrorEnabled(false);


                if(! (radioButton_id == R.id.radio_male || radioButton_id == R.id.radio_female))
                    Toast.makeText(getApplicationContext(),"Gender not Selected",Toast.LENGTH_SHORT).show();

                if (!is_textfield_empty) {

                    User user = new User();
                    user.setUser_name(txt_name.getText().toString());
                    user.setUser_age(txt_age.getText().toString());
                    user.setUser_gender(check_radio_button.getText() + "");
                    user.setUser_contactno(txt_contactno.getText().toString());
                    user.setUser_id(mAuth.getCurrentUser().getUid());



                    DatabaseReference user_reference = mRef.child("users").child("" + user.getUser_id());

                    mRef.child("users").child("" + mAuth.getCurrentUser().getUid()).child("name").setValue(user.getUser_name());
                    user_reference.child("contact_no").setValue(user.getUser_contactno());
                    user_reference.child("gender").setValue(user.getUser_gender());
                    user_reference.child("age").setValue(user.getUser_age());

                    Intent intent = new Intent(getApplicationContext(),InitialPatientDetailActivity.class);
                    startActivity(intent);
                    finish();
               }
                else
                    Log.w("Tag","Empty Fields");
            }

        });

    }

}
