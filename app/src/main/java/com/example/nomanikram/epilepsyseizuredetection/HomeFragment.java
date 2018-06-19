package com.example.nomanikram.epilepsyseizuredetection;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.nomanikram.epilepsyseizuredetection.models.Data;
import com.example.nomanikram.epilepsyseizuredetection.models.Patient;
import com.example.nomanikram.epilepsyseizuredetection.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    // Declaring the variable for text view
    static public AppCompatTextView txt_name;
    private AppCompatTextView txt_age;
    private AppCompatTextView txt_weight;
    private AppCompatTextView txt_height;
    private static CircleImageView profileImage;

    // declaring variable for textview, public for use in other class
    public static AppCompatTextView txt_pulse;
    public static AppCompatTextView txt_temp;

    // declared imageview for bluetooth
    private ImageView imageButton_connectivity;

    // declared the variables for Firebase auth state and reference
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    // declared the variable to store the uid of current logged user
    private String userID;
    Uri image;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // initializing the textview
        txt_name = (AppCompatTextView) view.findViewById(R.id.txt_name);
        txt_age = (AppCompatTextView) view.findViewById(R.id.txt_age);
        txt_weight = (AppCompatTextView) view.findViewById(R.id.txt_weight);
        txt_height = (AppCompatTextView) view.findViewById(R.id.txt_height);
        txt_pulse = (AppCompatTextView) view.findViewById(R.id.txt_pulserate);
        txt_temp = (AppCompatTextView) view.findViewById(R.id.txt_temperature);

        profileImage = (CircleImageView) view.findViewById(R.id.profile_image);

        // initializing the bluetooth imageview
        imageButton_connectivity = (ImageView) view.findViewById(R.id.bluetoothConnection);

        // initializing the firebase auth state and get reference to database
        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        // initializing the uid of currently logged user
        userID = mAuth.getCurrentUser().getUid();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("images/patient/" + mAuth.getCurrentUser().getUid());
        image = Uri.parse("https://firebasestorage.googleapis.com/v0/b/fyp-esd.appspot.com/o/images%2Fpatient%2F9cgyFYGxnZhH1pAdhBgNBxHn2fM2?alt=media&token=da712908-0189-4ff0-99c2-cede35bd4706");


        // start bluetooth connection activity
        imageButton_connectivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), BluetoothConnectionActivity.class);
                startActivity(intent);
            }
        });

        // query to read data changes in the database at patient node
        Query query1 = reference.child("users").child(userID).child("Patient");
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Storing data to patient object
                Patient patient = new Patient();
                patient.setName("" + dataSnapshot.child("name").getValue());
                patient.setAge("" + dataSnapshot.child("age").getValue());
                patient.setWeight("" + dataSnapshot.child("weight").getValue());
                patient.setHeight("" + dataSnapshot.child("height").getValue());

//                    Log.w("TAG", "Patient Name: " + patient.getName());
//                    Log.w("TAG", "Patient Age: " + patient.getAge());
//                    Log.w("TAG", "Patient Height: " + patient.getWeight());
//                    Log.w("TAG", "Patient Weight: " + patient.getHeight());

                // displaying patient data from obj to text views
                txt_name.setText(patient.getName());
                txt_age.setText(patient.getAge() + " years old");
                txt_height.setText(patient.getHeight() + "cm");
                txt_weight.setText(patient.getWeight() + "kg");

//                    profileImage.setBackground(image);


                try {
                    if (dataSnapshot.child("image").exists() && dataSnapshot.child("image") != null)
                        Glide.with(getActivity().getApplicationContext()).load(dataSnapshot.child("image").getValue()).into(profileImage);
                    else
                        profileImage.setBackgroundResource(R.drawable.avatar);

                } catch (Exception ex) {
                    Log.w("ERROR", "Glide Error");
                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    // Broadcast listener to receive data from bluetooth service class
    public static class SensorReceiver extends BroadcastReceiver {

        //        static Data data;
        @Override
        public void onReceive(Context context, Intent intent) {
            Data data = (Data) intent.getSerializableExtra("MyObject");

            String pulse = data.pulse;
            String temp = data.temp;

            Log.w("TAG", "HomeFragment\n" + "temp: " + temp + "\npulse: " + pulse);

            txt_pulse.setText(pulse + "bpm");
            txt_temp.setText(temp + "˚C");

        }
    }
}
