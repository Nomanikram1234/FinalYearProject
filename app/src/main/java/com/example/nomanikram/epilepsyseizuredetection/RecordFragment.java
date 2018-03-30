package com.example.nomanikram.epilepsyseizuredetection;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nomanikram.epilepsyseizuredetection.models.Contact;
import com.example.nomanikram.epilepsyseizuredetection.models.Record;
import com.example.nomanikram.epilepsyseizuredetection.views.RecycleAdapter_record;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecordFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference database;

    private static DatabaseReference user_reference;
    private static DatabaseReference record_ref;

    static Query query;

   static List<Record> recorder;

    RecyclerView recycleview;

    public RecordFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record, container, false);

        recycleview = (RecyclerView) view.findViewById(R.id.recycler_record);
         recorder = new ArrayList<Record>();

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        user_reference = database.child("users").child("" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        record_ref = database.child("users").child("" + mAuth.getCurrentUser().getUid()).child("Patient").child("Record");

        query = record_ref;



        Record r1 = new Record();
        r1.heartbeat = "85";
        r1.activity_status="mild";
        r1.date="12/12/2018";
        r1.temp="100";
        r1.id="12:00";


        Record r2 = new Record();
        r2.heartbeat = "8";
        r2.activity_status="serve";
        r2.date="12/12/2018";
        r2.temp="200";
        r2.id="12:00";


        Record r3 = new Record();


//        recorder.add(r1);
//        recorder.add(r2);
//        recorder.add(r3);

        Log.d("TAG","Size: "+ recorder.size());
        LinearLayoutManager linear = new LinearLayoutManager(this.getContext());
        recycleview.setLayoutManager(linear);

        recycleview.setHasFixedSize(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for( DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Log.w("", "snapshot R: " + dataSnapshot.getChildrenCount());

                    if(!snapshot.hasChild("pulse"))
                        continue;

                    Record R = new Record();
                    R.heartbeat = (String) snapshot.child("pulse").getValue()+"bpm";
                    R.temp = (String) snapshot.child("temperture").getValue()+"˚C";
                    R.activity_status = (String) snapshot.child("accelerometer").getValue();
                    R.time = (String) snapshot.child("time").getValue();
                    R.date = (String) snapshot.child("date").getValue();


                    Log.w("", "Temperature over Record: " + R.temp);


                    recorder.add(R);

                }
                recycleview.setAdapter(new RecycleAdapter_record(recorder));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        recycleview.setAdapter(new RecycleAdapter_record(recorder));
        Log.d("TAG",r1+"\n"+r1);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


    }
}
