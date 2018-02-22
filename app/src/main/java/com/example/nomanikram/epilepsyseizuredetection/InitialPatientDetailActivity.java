package com.example.nomanikram.epilepsyseizuredetection;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.nomanikram.epilepsyseizuredetection.models.Patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InitialPatientDetailActivity extends AppCompatActivity {

    private AppCompatEditText txt_name;
    private AppCompatEditText txt_age;
    private AppCompatEditText txt_weight;
    private AppCompatEditText txt_height;

    private TextInputLayout textInputLayout_name;
    private TextInputLayout textInputLayout_age;
    private TextInputLayout textInputLayout_weight;
    private TextInputLayout textInputLayout_height;

    private RadioGroup radioGroup;
    private RadioButton check_radiobutton;

    private RadioButton radioButton_male;
    private RadioButton radioButton_female;

    private AppCompatButton btn_save;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    FirebaseDatabase database;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_patient_detail);

        txt_name =(AppCompatEditText) findViewById(R.id.txt_name);
        txt_age = (AppCompatEditText) findViewById(R.id.txt_age);
        txt_weight = (AppCompatEditText) findViewById(R.id.txt__weight);
        txt_height =(AppCompatEditText) findViewById(R.id.txt_height);

        textInputLayout_name = (TextInputLayout) findViewById(R.id.textInputLayout_name);
        textInputLayout_age  = (TextInputLayout) findViewById(R.id.textInputLayout_age);
        textInputLayout_weight = (TextInputLayout) findViewById(R.id.textInputLayout_weight);
        textInputLayout_height = (TextInputLayout) findViewById(R.id.textInputLayout_height);

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup_gender);
        radioButton_male = (AppCompatRadioButton) findViewById(R.id.radio_male);
        radioButton_female = (AppCompatRadioButton) findViewById(R.id.radio_female);

        btn_save = (AppCompatButton) findViewById(R.id.btn_save) ;

        mAuth = FirebaseAuth.getInstance();

//        setupAuthStateListener();
//        login();

        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int radioButton_id = radioGroup.getCheckedRadioButtonId();
                check_radiobutton = findViewById(radioButton_id);

                boolean is_fields_empty = txt_name.getText().toString().isEmpty()   ||
                                          txt_age.getText().toString().isEmpty()    ||
                                          txt_height.getText().toString().isEmpty() ||
                                          txt_weight.getText().toString().isEmpty() ||
                        (check_radiobutton != radioButton_male && check_radiobutton != radioButton_female);

                // check if Name Field is empty
                if(txt_name.getText().toString().isEmpty()){
                    textInputLayout_name.setErrorEnabled(true);
                    textInputLayout_name.setError("Name field is empty");
                }
                else
                {
                    textInputLayout_name.setErrorEnabled(false);
                }

                // check of Age Field is empty
                if(txt_age.getText().toString().isEmpty()){
                    textInputLayout_age.setErrorEnabled(true);
                    textInputLayout_age.setError("Age field is empty");
                }
                else
                {
                    textInputLayout_age.setErrorEnabled(false);
                }

                // check if Height Field is empty
                if(txt_name.getText().toString().isEmpty()){
                    textInputLayout_height.setErrorEnabled(true);
                    textInputLayout_height.setError("Height field is empty");
                }
                else
                {
                    textInputLayout_height.setErrorEnabled(false);
                }

                // check if Weight Field is empty
                if(txt_weight.getText().toString().isEmpty()){
                    textInputLayout_weight.setErrorEnabled(true);
                    textInputLayout_weight.setError("Weight field is empty");
                }
                else
                {
                    textInputLayout_weight.setErrorEnabled(false);
                }

                // check if any Radio Button for Gender is selected
                if(check_radiobutton != radioButton_male && check_radiobutton != radioButton_female)
                    Toast.makeText(getApplicationContext(),"Gender not Selected",Toast.LENGTH_SHORT).show();

                // Check if all fields are filled
                if(!is_fields_empty) {
                    Toast.makeText(getApplicationContext(), "Not Empty", Toast.LENGTH_SHORT).show();

                    /* Write Working Code Here */

                    Patient patient =  new Patient();

                    patient.setName(txt_name.getText().toString());
                    patient.setAge(txt_age.getText().toString());
                    patient.setGender(check_radiobutton.getText().toString());
                    patient.setHeight(txt_height.getText().toString());
                    patient.setWeight(txt_weight.getText().toString());

                    DatabaseReference user_reference = mRef.child("users").child(""+mAuth.getCurrentUser().getUid());
                    DatabaseReference patient_reference = user_reference.child("Patient");

                    patient_reference.child("name").setValue(patient.getName());
                    patient_reference.child("age").setValue(patient.getAge());
                    patient_reference.child("gender").setValue(patient.getGender());
                    patient_reference.child("height").setValue(patient.getHeight());
                    patient_reference.child("weight").setValue(patient.getHeight());

                    mAuth.signOut();

                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });
    }


}
