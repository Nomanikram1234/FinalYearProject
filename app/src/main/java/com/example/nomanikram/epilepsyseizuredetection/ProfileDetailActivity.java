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

    // declaring the variables for button
    private AppCompatButton btn_done;

    // declaring the variables for radio button and radiogroup
    private RadioGroup radioGroup;
    private AppCompatRadioButton check_radio_button;

    // declaring the variables for edittext
    private AppCompatEditText txt_name;
    private AppCompatEditText txt_age;
    private AppCompatEditText txt_gender;
    private AppCompatEditText txt_contactno;

    // declaring the variables for inputlayout
    private TextInputLayout textInputLayout_name;
    private TextInputLayout textInputLayout_age;
    private TextInputLayout textInputLayout_contactno;

    // declare the variable for firebase auth state
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // declare variable for firebase database reference
    private FirebaseDatabase database;
    private DatabaseReference mRef;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);

        // initialing the ref to button
        btn_done = (AppCompatButton) findViewById(R.id.btn_done);

        // initialing the ref to edit texts
        txt_name = (AppCompatEditText) findViewById(R.id.txt_name);
        txt_age  = (AppCompatEditText) findViewById(R.id.txt_age);
    //  txt_gender = (AppCompatEditText) findViewById(R.id.txt_gender);
        txt_contactno =(AppCompatEditText) findViewById(R.id.txt_contactno);

        // initialing the ref to inputlayout
        textInputLayout_name = (TextInputLayout) findViewById(R.id.textInputLayout_name);
        textInputLayout_age = (TextInputLayout) findViewById(R.id.textInputLayout_age);
        textInputLayout_contactno = (TextInputLayout) findViewById(R.id.textInputLayout_contactno);

        // initialing the ref to radio group
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup_gender);

        // initializing instance of firebase auth
        mAuth = FirebaseAuth.getInstance();

        // initializing the reference
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getting id of currently checked button
                int radioButton_id = radioGroup.getCheckedRadioButtonId();

                // if check then assign ref
                 if (R.id.radio_male == radioButton_id) {
                     check_radio_button= findViewById(R.id.radio_male);
//                     Log.w("TAG", "Male:");
                 }
                 if (R.id.radio_female == radioButton_id){
                     check_radio_button= findViewById(R.id.radio_female);
//                     Log.w("TAG", "FEMale:");
                 }
                   //  Log.w("TAG", "FEMale:");

                // check if all fields are filled and radio buttion is selected
                boolean is_textfield_empty = txt_name.getText().toString().isEmpty() ||
                        txt_age.getText().toString().isEmpty() ||
                        txt_contactno.getText().toString().isEmpty() ||
                        ( R.id.radio_male != radioButton_id &&
                        R.id.radio_female != radioButton_id) ;




                // name field can not be empty
                if(txt_name.getText().toString().isEmpty())
                {
                  textInputLayout_name.setErrorEnabled(true);
                  textInputLayout_name.setError("Name field is empty");
                }
                else
                  textInputLayout_name.setErrorEnabled(false);


                int age=0;

                // check if age field is not empty then parse to int
                if(!txt_age.getText().toString().isEmpty())
                age =Integer.parseInt(txt_age.getText().toString());

                // age field can not be empty
                if(txt_age.getText().toString().isEmpty())
                {
                    textInputLayout_age.setErrorEnabled(true);
                    textInputLayout_age.setError("Age field is empty");
                }
                else
                {
                    textInputLayout_age.setErrorEnabled(false);

                    // age validation : age can only be between
                    if(!(age >= 0 && age <=150))
                    {
                        textInputLayout_age.setErrorEnabled(true);
                        textInputLayout_age.setError("Invalid Age");
                    }
                    else
                        textInputLayout_age.setErrorEnabled(false);

                }


                // contact field can not be empty
                if(txt_contactno.getText().toString().isEmpty())
                {
                    textInputLayout_contactno.setErrorEnabled(true);
                    textInputLayout_contactno.setError("Contact no field is empty");
                }
                else
                {
                    textInputLayout_contactno.setErrorEnabled(false);

                    // contact number can be 11 to 15 digit long
                    if(!(txt_contactno.getText().toString().length() >= 11 && txt_contactno.getText().toString().length() <= 15))
                    {
                        textInputLayout_contactno.setErrorEnabled(true);
                        textInputLayout_contactno.setError("Contact no can be 11 to 15 digits long");
                        Log.w("","Length: "+txt_contactno.getText().toString().length() );
                    }
                    else
                    {
                        textInputLayout_contactno.setErrorEnabled(false);

                        // contact number cannot contain any symbol
                        if(!(txt_contactno.getText().toString().contains(",") || !txt_contactno.getText().toString().contains(".")))
                        {
                            textInputLayout_contactno.setErrorEnabled(true);
                            textInputLayout_contactno.setError("Contact no cannot contain any symbol");
                        }
                        else
                            textInputLayout_contactno.setErrorEnabled(false);
                    }
                }



                // if radio button is not selected
                if(! (radioButton_id == R.id.radio_male || radioButton_id == R.id.radio_female))
                    Toast.makeText(getApplicationContext(),"Gender not Selected",Toast.LENGTH_SHORT).show();

                // if fields are not empty
                if (!is_textfield_empty  ) {
                    // if age is between 1 - 150
                    if(age >= 0 && age <=150) {
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

                        Intent intent = new Intent(getApplicationContext(), InitialPatientDetailActivity.class);
                        startActivity(intent);
                        finish();
                    }
               }
                else
                    Log.w("Tag","Empty Fields");
            }

        });

    }

}
