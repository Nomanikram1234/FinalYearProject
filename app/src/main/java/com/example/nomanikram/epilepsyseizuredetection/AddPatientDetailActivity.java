package com.example.nomanikram.epilepsyseizuredetection;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.nomanikram.epilepsyseizuredetection.models.Patient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AddPatientDetailActivity extends AppCompatActivity {

    // Declaring the edit text fields
    AppCompatEditText txt_name;
    AppCompatEditText txt_age;
//    AppCompatEditText txt_gender;
    AppCompatEditText txt_height;
    AppCompatEditText txt_weight;

    // declaring the radiogroup
    RadioGroup radioGroup;

    // declaring the radiobuttons
    AppCompatRadioButton radioButton_male;
    AppCompatRadioButton radioButton_female;
    AppCompatRadioButton check_radioButton;


    // declaring input layout for textfields
    private TextInputLayout textInputLayout_name;
    private TextInputLayout textInputLayout_age;
    private TextInputLayout textInputLayout_weight;
    private TextInputLayout textInputLayout_height;

    // declaring the button
    AppCompatButton btn_save;

    // declaring the variable for Firebase auth state and database reference
    FirebaseAuth mAuth;
    FirebaseDatabase database;

    // declared variable for storing the uid of currently logged user
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient_detail);

        // initializing the edit texts
        txt_name = (AppCompatEditText) findViewById(R.id.txt_name);
        txt_age = (AppCompatEditText) findViewById(R.id.txt_age);
//        txt_gender = (AppCompatEditText) findViewById(R.id.txt_gender);
        txt_height = (AppCompatEditText) findViewById(R.id.txt_height);
        txt_weight = (AppCompatEditText) findViewById(R.id.txt_weight);

        // initializing the radio group
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup_gender);

        // initialing the radio buttons
        radioButton_male = (AppCompatRadioButton) findViewById(R.id.radio_male);
        radioButton_female = (AppCompatRadioButton) findViewById(R.id.radio_female);

        // initialing the layout for input fields
        textInputLayout_name = (TextInputLayout) findViewById(R.id.textInputLayout_name);
        textInputLayout_age  = (TextInputLayout) findViewById(R.id.textInputLayout_age);
        textInputLayout_weight = (TextInputLayout) findViewById(R.id.textInputLayout_weight);
        textInputLayout_height = (TextInputLayout) findViewById(R.id.textInputLayout_height);

        // initialing the button
        btn_save = (AppCompatButton) findViewById(R.id.btn_save);

        // initialing the firebase auth state and database reference
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // initializing the uid of currently logged user
        userID = mAuth.getCurrentUser().getUid();

        // query to read the patient node
        Query query = database.getReference().child("users").child(userID).child("Patient");

        // listerner for data change to patient node
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Patient patient = new Patient();

                // storing the data to firebase
                patient.setName(""+dataSnapshot.child("name").getValue());
                patient.setAge(""+dataSnapshot.child("age").getValue());
                patient.setGender(""+dataSnapshot.child("gender").getValue());
                patient.setWeight(""+dataSnapshot.child("weight").getValue());
                patient.setHeight(""+dataSnapshot.child("height").getValue());

                // displaying the data
                txt_name.setText(patient.getName());
                txt_age.setText(patient.getAge());
                txt_height.setText(patient.getHeight());
                txt_weight.setText(patient.getWeight());

                Log.w("","Gender: "+patient.getGender());

                if(patient.getGender().equalsIgnoreCase("Male")){
                    radioButton_male.setChecked(true);

                }
                if(patient.getGender().equalsIgnoreCase("Female")) {
                    radioButton_female.setChecked(true);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // declaring & initialize the variable with id of currently checked radio button
                int radioButton_id = radioGroup.getCheckedRadioButtonId();
                check_radioButton = findViewById(radioButton_id);

                // check if any field is empty & either radio buton is check or not
                boolean is_fields_empty = txt_name.getText().toString().isEmpty()   ||
                        txt_age.getText().toString().isEmpty()    ||
                        txt_height.getText().toString().isEmpty() ||
                        txt_weight.getText().toString().isEmpty() ||
                        (check_radioButton != radioButton_male && check_radioButton != radioButton_female);


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

                    // checking condition i.e age should be from 0 - 200
                    if( !( Integer.parseInt(txt_age.getText().toString()) >= 0 && Integer.parseInt(txt_age.getText().toString()) <= 200) ){
                        textInputLayout_age.setErrorEnabled(true);
                        textInputLayout_age.setError("Age is Invalid ");
                    }
                    else
                    {
                        textInputLayout_age.setErrorEnabled(false);
                    }
                }



                // check if Height Field is empty
                if(txt_height.getText().toString().isEmpty()){
                    textInputLayout_height.setErrorEnabled(true);
                    textInputLayout_height.setError("Height field is empty");
                }
                else
                {
                    textInputLayout_height.setErrorEnabled(false);

                    // checking the condion i.e height should be from 0 - 300
                    if( !( Integer.parseInt(txt_height.getText().toString()) >=0 && Integer.parseInt(txt_height.getText().toString()) <= 300))
                    {
                        textInputLayout_height.setErrorEnabled(true);
                        textInputLayout_height.setError("Height is invalid i.e Max height = 300");
                    }
                    else
                    {
                        textInputLayout_height.setErrorEnabled(false);
                    }
                }



                // check if Weight Field is empty
                if(txt_weight.getText().toString().isEmpty()){
                    textInputLayout_weight.setErrorEnabled(true);
                    textInputLayout_weight.setError("Weight field is empty");
                }
                else
                {
                    textInputLayout_weight.setErrorEnabled(false);

                    // checking the condion i.e weight should be from 0 - 600
                    if( !( Integer.parseInt(txt_weight.getText().toString()) >= 0 && Integer.parseInt(txt_weight.getText().toString()) <= 600 )){
                        textInputLayout_weight.setErrorEnabled(true);
                        textInputLayout_weight.setError("Weight is Invalid. Max weight = 600");
                    }
                    else
                    {
                        textInputLayout_weight.setErrorEnabled(false);
                    }

                }

                // check if any Radio Button for Gender is selected
                if(check_radioButton != radioButton_male && check_radioButton != radioButton_female)
                    Toast.makeText(getApplicationContext(),"Gender not Selected",Toast.LENGTH_SHORT).show();


                // Check if all fields are filled & conditions for age,height,weight are satisfied
                if(!is_fields_empty &&
                        ( Integer.parseInt(txt_weight.getText().toString()) >= 0 && Integer.parseInt(txt_weight.getText().toString()) <= 600 ) &&
                        ( Integer.parseInt(txt_height.getText().toString()) >=0 && Integer.parseInt(txt_height.getText().toString()) <= 300) )

                {
                    // storing data to database
                    DatabaseReference pRef = database.getReference().child("users").child(userID).child("Patient");
                    pRef.child("name").setValue(txt_name.getText().toString());
                    pRef.child("age").setValue(txt_age.getText().toString());
                    pRef.child("gender").setValue(check_radioButton.getText().toString());
                    pRef.child("height").setValue(txt_height.getText().toString());
                    pRef.child("weight").setValue(txt_weight.getText().toString());

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
     }
}
