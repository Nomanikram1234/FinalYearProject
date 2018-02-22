package com.example.nomanikram.epilepsyseizuredetection;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.util.LogWriter;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.nomanikram.epilepsyseizuredetection.models.Patient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class PatientFragment extends Fragment {

    private AppCompatTextView txt_name;
    private AppCompatTextView txt_age;
    private AppCompatTextView txt_gender;
    private AppCompatTextView txt_height;
    private AppCompatTextView txt_weight;

    FloatingActionButton btn_floating;

    FirebaseAuth mAuth;
    DatabaseReference reference;

    String userID;

    ProgressDialog progressDialog;

    public PatientFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    View view =  inflater.inflate(R.layout.fragment_patient, container, false);

    mAuth =FirebaseAuth.getInstance();
    reference = FirebaseDatabase.getInstance().getReference();

    userID = mAuth.getCurrentUser().getUid();

    txt_name = (AppCompatTextView) view.findViewById(R.id.txt_entered_name);
    txt_age = (AppCompatTextView) view.findViewById(R.id.txt_entered_age);
    txt_gender = (AppCompatTextView) view.findViewById(R.id.txt_entered_gender);
    txt_height = (AppCompatTextView) view.findViewById(R.id.txt_entered_height);
    txt_weight = (AppCompatTextView) view.findViewById(R.id.txt_entered_weight);

    btn_floating = (FloatingActionButton) view.findViewById(R.id.btn_floating);

    Query query1 = reference.child("users").child(userID).child("Patient");

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("Loading");
        progressDialog.show();

    query1.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {


            Patient patient = new Patient();

            patient.setName(""+dataSnapshot.child("name").getValue());
            patient.setAge(""+dataSnapshot.child("age").getValue());
            patient.setGender(""+dataSnapshot.child("gender").getValue());
            patient.setHeight(""+dataSnapshot.child("height").getValue());
            patient.setWeight(""+dataSnapshot.child("weight").getValue());

            txt_name.setText(patient.getName());
            txt_age.setText(patient.getAge());
            txt_gender.setText(patient.getGender());
            txt_height.setText(patient.getHeight()+" cm");
            txt_weight.setText(patient.getWeight()+" kg");

            progressDialog.hide();

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });





    btn_floating.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity().getApplicationContext(),AddPatientDetailActivity.class);
            startActivity(intent);
        }
    });

    return view;
    }

}
