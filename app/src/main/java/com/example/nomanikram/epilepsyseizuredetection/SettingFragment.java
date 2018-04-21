package com.example.nomanikram.epilepsyseizuredetection;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {


    private Button btn_modify_patient_detail,
                            btn_signout,
                            btn_set_patient_image;

    private final int PICK_IMAGE_REQUEST = 1 ;

    private FirebaseAuth mAuth;

    private Uri  mImageUri;

//    private Uri filepath;

    FirebaseStorage storage;
    StorageReference storageReference;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btn_signout = view.findViewById(R.id.btn_signout);
        btn_modify_patient_detail = view.findViewById(R.id.btn_modify_patient_detail);
        btn_set_patient_image  = view.findViewById(R.id.btn_set_patient_image);

        btn_set_patient_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_patient_image();
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
    openFileChoose();

    }

    private void openFileChoose(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    private void uploadImage(Uri image){
        if(image != null){


            StorageReference reference = storageReference.child("images/patient/"+mAuth.getCurrentUser().getUid());
            reference.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String downloadURL = taskSnapshot.getDownloadUrl().toString();

                    DatabaseReference patient_image_ref = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("Patient").child("image");
                    patient_image_ref.setValue(downloadURL);

                }
            });

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();
            Log.w("","mImageUri: "+ data.getData());
            uploadImage(mImageUri);
        }
    }
}
