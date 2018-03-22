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

    AppCompatEditText txt_name;
    AppCompatEditText txt_age;
//    AppCompatEditText txt_gender;
    AppCompatEditText txt_height;
    AppCompatEditText txt_weight;

    RadioGroup radioGroup;

    AppCompatRadioButton radioButton_male;
    AppCompatRadioButton radioButton_female;
    AppCompatRadioButton check_radioButton;

    private TextInputLayout textInputLayout_name;
    private TextInputLayout textInputLayout_age;
    private TextInputLayout textInputLayout_weight;
    private TextInputLayout textInputLayout_height;

    AppCompatButton btn_save;

    FirebaseAuth mAuth;
    FirebaseDatabase database;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient_detail);

        txt_name = (AppCompatEditText) findViewById(R.id.txt_name);
        txt_age = (AppCompatEditText) findViewById(R.id.txt_age);
//        txt_gender = (AppCompatEditText) findViewById(R.id.txt_gender);
        txt_height = (AppCompatEditText) findViewById(R.id.txt_height);
        txt_weight = (AppCompatEditText) findViewById(R.id.txt_weight);

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup_gender);

        radioButton_male = (AppCompatRadioButton) findViewById(R.id.radio_male);
        radioButton_female = (AppCompatRadioButton) findViewById(R.id.radio_female);

        textInputLayout_name = (TextInputLayout) findViewById(R.id.textInputLayout_name);
        textInputLayout_age  = (TextInputLayout) findViewById(R.id.textInputLayout_age);
        textInputLayout_weight = (TextInputLayout) findViewById(R.id.textInputLayout_weight);
        textInputLayout_height = (TextInputLayout) findViewById(R.id.textInputLayout_height);


        btn_save = (AppCompatButton) findViewById(R.id.btn_save);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        userID = mAuth.getCurrentUser().getUid();



        Query query = database.getReference().child("users").child(userID).child("Patient");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Patient patient = new Patient();

                patient.setName(""+dataSnapshot.child("name").getValue());
                patient.setAge(""+dataSnapshot.child("age").getValue());
                patient.setGender(""+dataSnapshot.child("gender").getValue());
                patient.setWeight(""+dataSnapshot.child("weight").getValue());
                patient.setHeight(""+dataSnapshot.child("height").getValue());

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
                int radioButton_id = radioGroup.getCheckedRadioButtonId();
                check_radioButton = findViewById(radioButton_id);

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


                // Check if all fields are filled
                if(!is_fields_empty &&
                        ( Integer.parseInt(txt_weight.getText().toString()) >= 0 && Integer.parseInt(txt_weight.getText().toString()) <= 600 ) &&
                        ( Integer.parseInt(txt_height.getText().toString()) >=0 && Integer.parseInt(txt_height.getText().toString()) <= 300) )

                {

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
