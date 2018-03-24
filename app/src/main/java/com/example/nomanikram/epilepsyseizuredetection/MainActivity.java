package com.example.nomanikram.epilepsyseizuredetection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nomanikram.epilepsyseizuredetection.models.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottom ;
//    Intent intent;
//    AppCompatTextView txt;

    FirebaseAuth mAuth;
    DatabaseReference database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        txt_pulse = (TextView) findViewById(R.id);


        bottom = (BottomNavigationView) findViewById(R.id.bottom);

        mAuth =FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        Log.w("","UID: "+mAuth.getCurrentUser().getUid());

        HomeFragment fragment = new HomeFragment();
        setFragment(fragment);


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        Query query1 = database.child("users").equalTo(mAuth.getCurrentUser().getUid());
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren())
                    Log.w("CHECKING: ",""+singleSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.nav_home){

                    HomeFragment fragment = new HomeFragment();
                    setFragment(fragment);


                    Toast.makeText(MainActivity.this,"Home",Toast.LENGTH_LONG);
                    Log.w("TAG","Home");}

             else   if(id == R.id.nav_patient) {

                    PatientFragment fragment = new PatientFragment();
                    setFragment(fragment);

                    Log.w("TAG", "Patient");

                    Toast.makeText(MainActivity.this, "Patient", Toast.LENGTH_LONG);
                }

                else if(id == R.id.nav_contact){
                    ContactFragment fragment = new ContactFragment();
                    setFragment(fragment);
                    Log.w("TAG","Contact");

                }
                else if(id == R.id.nav_records){

                    RecordFragment fragment = new RecordFragment();
                    setFragment(fragment);

                    Log.w("TAG","Record");
                }
                else if(id == R.id.nav_setting){

                    SettingFragment fragment = new SettingFragment();
                    setFragment(fragment);

                    Log.w("TAG","Setting");
                }

                return true;
            }
        });
    }

    // Setting Fragment on View
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.context,fragment,"FragmentName");
        fragmentTransaction.commit();
    }

    public static class SensorReceiver extends BroadcastReceiver {

        //        static Data data;
        @Override
        public void onReceive(Context context, Intent intent) {
            Data  data = (Data) intent.getSerializableExtra("MyObject");

            String txt_pulse = data.pulse;
            String txt_temp = data.temp;

            Log.w("TAG","MainActivity\n"+"temp: "+txt_temp+"\npulse: "+txt_pulse);

//            doublev.setText(txt_temp);
//            pulse1.setText(txt_pulse);


//           Name.setText("    Noman ");
//
//            Intent intent_d = new Intent(context,MainActivity.class);
//            intent_d.putExtra("MyHealth,data",data);


        }
    }

}
