package com.example.nomanikram.epilepsyseizuredetection;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nomanikram.epilepsyseizuredetection.models.Record;
import com.example.nomanikram.epilepsyseizuredetection.views.RecycleAdapter_record;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecordFragment extends Fragment {



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
        List<Record> recorder = new ArrayList<Record>();



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


        recorder.add(r1);
        recorder.add(r2);
//        recorder.add(r3);

        Log.d("TAG","Size: "+ recorder.size());
        LinearLayoutManager linear = new LinearLayoutManager(this.getContext());
        recycleview.setLayoutManager(linear);

        recycleview.setHasFixedSize(true);
        recycleview.setAdapter(new RecycleAdapter_record(recorder));
        Log.d("TAG",r1+"\n"+r1);

        return view;
    }

}
