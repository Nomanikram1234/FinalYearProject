package com.example.nomanikram.epilepsyseizuredetection;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {


    private Button btn_modify_patient_detail,
                            btn_signout,
                            btn_set_patient_image;

    private FirebaseAuth mAuth;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        mAuth = FirebaseAuth.getInstance();

        btn_signout = view.findViewById(R.id.btn_signout);
        btn_modify_patient_detail = view.findViewById(R.id.btn_modify_patient_detail);
        btn_set_patient_image  = view.findViewById(R.id.btn_set_patient_image);

        btn_set_patient_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signout();
            }
        });

        btn_modify_patient_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(),AddPatientDetailActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    public void signout(){
        mAuth.signOut();

        Intent intent = new Intent(getActivity().getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }

    private void set_patient_image(){

    }


}
