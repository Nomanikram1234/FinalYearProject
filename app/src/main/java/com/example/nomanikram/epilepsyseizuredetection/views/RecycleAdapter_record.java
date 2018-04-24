package com.example.nomanikram.epilepsyseizuredetection.views;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nomanikram.epilepsyseizuredetection.R;
import com.example.nomanikram.epilepsyseizuredetection.RecordFragment;
import com.example.nomanikram.epilepsyseizuredetection.models.Contact;
import com.example.nomanikram.epilepsyseizuredetection.models.Record;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nomanikram on 12/02/2018.
 */

public class RecycleAdapter_record extends RecyclerView.Adapter<view_holder_record> {

    // declaring the variables for alertDialog and its builder
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;

    FirebaseAuth mAuth;
    DatabaseReference database;


    private List<Record> record ;
    Context context;

    GraphView graph;

   static ArrayList<String> pulse_rates_list;

    public RecycleAdapter_record(List<Record> record ,Context context){
        this.record = record;
        this.context = context;

        mAuth = FirebaseAuth.getInstance();
        database =FirebaseDatabase.getInstance().getReference();

        pulse_rates_list = new ArrayList<String>();
    }


    @Override
    public view_holder_record onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_record,parent,false);





        return new view_holder_record(view);
    }

    @Override
    public void onBindViewHolder(view_holder_record holder, final int position) {
        Record samplerecord = record.get(position);

        holder.pulse.setText(samplerecord.heartbeat);
        holder.temp.setText(samplerecord.temp);
        holder.activity_status.setText(samplerecord.activity_status);
        holder.date.setText(samplerecord.date);
        holder.time.setText(samplerecord.time);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // building alert dialog for getting contact info
                builder = new AlertDialog.Builder(context);
                View view_f = LayoutInflater.from(context).inflate(R.layout.layout_graph_alertbox, null);
                builder.setView(view_f);

//                AppCompatButton btn_add_contact_manually = (AppCompatButton) view_f.findViewById(R.id.btn_add_contact);

                DatabaseReference ref = database.child("users").child(mAuth.getCurrentUser().getUid()).child("Patient").child("Record");


                // Dummy Data User at the moment
                 graph = (GraphView) view_f.findViewById(R.id.graph);

                // query to read the patient node
                Query query = database.child("users").child(mAuth.getCurrentUser().getUid()).child("Patient").child("Record");



                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        for( DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            Log.w("", "snapshot R: " + dataSnapshot.getChildrenCount());

                            if(!snapshot.hasChild("pulse"))
                                continue;

//                            Record R = new Record();
//                            R.heartbeat = (String) snapshot.child("pulse").getValue()+"bpm";
//                            R.temp = (String) snapshot.child("temperture").getValue()+"˚C";
//                            R.activity_status = (String) snapshot.child("accelerometer").getValue();
//                            R.time = (String) snapshot.child("time").getValue();
//                            R.date = (String) snapshot.child("date").getValue();

                            pulse_rates_list.add(snapshot.child("pulse").getValue().toString());
                            Log.w("","SIZE inside: "+pulse_rates_list.size());
                          //  Log.w("", "Temperature over Record: " + R.temp);

                            Log.w("","position inside\n  "+pulse_rates_list);


                        }

                        Log.w("","CHECKING: "+pulse_rates_list);


                        if(pulse_rates_list.size() == 1){
                            if (position == 0) {
                                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                                        new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position)))
                                });

                                graph.addSeries(series);
                            }
                        }


                        if(pulse_rates_list.size() == 2){
                            if (position == 0) {
                                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                                        new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position)))
                                });

                                graph.addSeries(series);
                            }
                            if (position == 1) {
                                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{

                                        new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position - 1))),
                                        new DataPoint(1, Integer.parseInt(pulse_rates_list.get(position)))
                                });

                                graph.addSeries(series);
                            }

                        }


                        if(pulse_rates_list.size() == 3){
                            if (position == 0) {
                                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                                        new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position)))
                                });

                                graph.addSeries(series);
                            }
                            if (position == 1) {
                                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{

                                        new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position - 1))),
                                        new DataPoint(1, Integer.parseInt(pulse_rates_list.get(position)))
                                });

                                graph.addSeries(series);
                            }

                            if (position == 2) {
                                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{

                                        new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position - 2))),
                                        new DataPoint(1, Integer.parseInt(pulse_rates_list.get(position - 1))),
                                        new DataPoint(2, Integer.parseInt(pulse_rates_list.get(position)))
                                });

                                graph.addSeries(series);
                            }

                        }


                        if(pulse_rates_list.size() == 4){
                            if (position == 0) {
                                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                                        new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position)))
                                });

                                graph.addSeries(series);
                            }
                            if (position == 1) {
                                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{

                                        new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position - 1))),
                                        new DataPoint(1, Integer.parseInt(pulse_rates_list.get(position)))
                                });

                                graph.addSeries(series);
                            }

                            if (position == 2) {
                                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{

                                        new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position - 2))),
                                        new DataPoint(1, Integer.parseInt(pulse_rates_list.get(position - 1))),
                                        new DataPoint(2, Integer.parseInt(pulse_rates_list.get(position)))
                                });

                                graph.addSeries(series);
                            }

                            if (position == 3) {
                                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{

                                        new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position - 3))),
                                        new DataPoint(1, Integer.parseInt(pulse_rates_list.get(position - 2))),
                                        new DataPoint(2, Integer.parseInt(pulse_rates_list.get(position - 1))),
                                        new DataPoint(3, Integer.parseInt(pulse_rates_list.get(position)))
                                });

                                graph.addSeries(series);
                            }

                        }

                        if(pulse_rates_list.size() == 5){
                            if (position == 0) {
                                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                                        new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position)))
                                });

                                graph.addSeries(series);
                            }
                            if (position == 1) {
                                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{

                                        new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position - 1))),
                                        new DataPoint(1, Integer.parseInt(pulse_rates_list.get(position)))
                                });

                                graph.addSeries(series);
                            }

                            if (position == 2) {
                                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{

                                        new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position - 2))),
                                        new DataPoint(1, Integer.parseInt(pulse_rates_list.get(position - 1))),
                                        new DataPoint(2, Integer.parseInt(pulse_rates_list.get(position)))
                                });

                                graph.addSeries(series);
                            }

                            if (position == 3) {
                                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{

                                        new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position - 3))),
                                        new DataPoint(1, Integer.parseInt(pulse_rates_list.get(position - 2))),
                                        new DataPoint(2, Integer.parseInt(pulse_rates_list.get(position - 1))),
                                        new DataPoint(3, Integer.parseInt(pulse_rates_list.get(position)))
                                });

                                graph.addSeries(series);
                            }

                            if (position == 4) {
                                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{

                                        new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position - 4))),
                                        new DataPoint(1, Integer.parseInt(pulse_rates_list.get(position - 3))),
                                        new DataPoint(2, Integer.parseInt(pulse_rates_list.get(position - 2))),
                                        new DataPoint(3, Integer.parseInt(pulse_rates_list.get(position - 1))),
                                        new DataPoint(4, Integer.parseInt(pulse_rates_list.get(position)))
                                });

                                graph.addSeries(series);
                            }

                        }


                        // Size == 6
                        if(pulse_rates_list.size() >= 6) {
                            if (position == 0) {
                                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                                        new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position)))
                                });

                                graph.addSeries(series);
                            }
                            if (position == 1) {
                                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{

                                        new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position - 1))),
                                        new DataPoint(1, Integer.parseInt(pulse_rates_list.get(position)))
                                });

                                graph.addSeries(series);
                            }

                            if (position == 2) {
                                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{

                                        new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position - 2))),
                                        new DataPoint(1, Integer.parseInt(pulse_rates_list.get(position - 1))),
                                        new DataPoint(2, Integer.parseInt(pulse_rates_list.get(position)))
                                });

                                graph.addSeries(series);
                            }

                            if (position == 3) {
                                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{

                                        new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position - 3))),
                                        new DataPoint(1, Integer.parseInt(pulse_rates_list.get(position - 2))),
                                        new DataPoint(2, Integer.parseInt(pulse_rates_list.get(position - 1))),
                                        new DataPoint(3, Integer.parseInt(pulse_rates_list.get(position)))
                                });

                                graph.addSeries(series);
                            }

                            if (position == 4) {
                                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{

                                        new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position - 4))),
                                        new DataPoint(1, Integer.parseInt(pulse_rates_list.get(position - 3))),
                                        new DataPoint(2, Integer.parseInt(pulse_rates_list.get(position - 2))),
                                        new DataPoint(3, Integer.parseInt(pulse_rates_list.get(position - 1))),
                                        new DataPoint(4, Integer.parseInt(pulse_rates_list.get(position)))
                                });

                                graph.addSeries(series);
                            }

                           if(position == 5) {
                                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
//
                                        new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position - 5))),
                                        new DataPoint(1, Integer.parseInt(pulse_rates_list.get(position - 4))),
                                        new DataPoint(2, Integer.parseInt(pulse_rates_list.get(position - 3))),
                                        new DataPoint(3, Integer.parseInt(pulse_rates_list.get(position - 2))),
                                        new DataPoint(4, Integer.parseInt(pulse_rates_list.get(position - 1))),
                                        new DataPoint(5, Integer.parseInt(pulse_rates_list.get(position)))
                                });
                               Log.w("Error","Position 5");
                                graph.addSeries(series);
                            }

                            if(position >= 6)
                            {
                                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                                        new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position - 5))),
                                        new DataPoint(1, Integer.parseInt(pulse_rates_list.get(position - 4))),
                                        new DataPoint(2, Integer.parseInt(pulse_rates_list.get(position - 3))),
                                        new DataPoint(3, Integer.parseInt(pulse_rates_list.get(position - 2))),
                                        new DataPoint(4, Integer.parseInt(pulse_rates_list.get(position - 1))),
                                        new DataPoint(5, Integer.parseInt(pulse_rates_list.get(position)))
                                });
                                Log.w("Error","Position 6");
                                graph.addSeries(series);
                            }


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Log.w("","SIZE 1: "+pulse_rates_list.size());

//
//                if(pulse_rates_list.size() == 1){
//                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
//                            new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position)))
//                    });
//                    graph.addSeries(series);
//                }
//                if(pulse_rates_list.size() == 2){
//                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
//
//                            new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position-1))),
//                            new DataPoint(1, Integer.parseInt(pulse_rates_list.get(position)))
//
//                    });
//                    graph.addSeries(series);
//                }
//
//                if(pulse_rates_list.size() == 3){
//                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
//
//                            new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position-2))),
//                            new DataPoint(1, Integer.parseInt(pulse_rates_list.get(position-1))),
//                            new DataPoint(2, Integer.parseInt(pulse_rates_list.get(position)))
//
//
//
//
//                    });
//
//
//
//
//                    graph.addSeries(series);
//                }
//                if(pulse_rates_list.size() == 4){
//                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
//
//                            new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position-3))),
//                            new DataPoint(1, Integer.parseInt(pulse_rates_list.get(position-2))),
//                            new DataPoint(2, Integer.parseInt(pulse_rates_list.get(position-1))),
//                            new DataPoint(3, Integer.parseInt(pulse_rates_list.get(position)))
//
//                    });
//
//
//
//
//                    graph.addSeries(series);
//                }
//
//                if(pulse_rates_list.size() == 5){
//                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
//
//                            new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position-4))),
//                            new DataPoint(1, Integer.parseInt(pulse_rates_list.get(position-3))),
//                            new DataPoint(2, Integer.parseInt(pulse_rates_list.get(position-2))),
//                            new DataPoint(3, Integer.parseInt(pulse_rates_list.get(position-1))),
//                            new DataPoint(4, Integer.parseInt(pulse_rates_list.get(position)))
//
//                    });
//
//
//
//
//                    graph.addSeries(series);
//                }
//
//                if(pulse_rates_list.size() >= 6){
//                    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
//                            new DataPoint(0, Integer.parseInt(pulse_rates_list.get(position))),
//                            new DataPoint(1, Integer.parseInt(pulse_rates_list.get(position))),
//                            new DataPoint(2, Integer.parseInt(pulse_rates_list.get(position))),
//                            new DataPoint(3, Integer.parseInt(pulse_rates_list.get(position))),
//                            new DataPoint(4, Integer.parseInt(pulse_rates_list.get(position))),
//                            new DataPoint(5, Integer.parseInt(pulse_rates_list.get(position)))
//
//
//
//                    });
//
//                    graph.addSeries(series);
//                }

//                for(int i =0 ; i< pulse_rates_list.size() ;i++)
                    Log.w("","position\n  "+pulse_rates_list);


//
//                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
//
//                  //////////////////////////////////////////
//
//
//                  //////////////////////////////////////////
////                      for(int i=0 ; i <pulse_rates_list.size() ;i++)
////                      new DataPoint(i, Integer.parseInt(pulse_rates_list.get(i)));
//
//                        new DataPoint(0, 132),
//                        new DataPoint(1, 160),
//                        new DataPoint(2, 123),
//                        new DataPoint(3, 140),
//                        new DataPoint(4, 150),
//                        new DataPoint(5, 150),
//                        new DataPoint(6, 190),
//                        new DataPoint(7, 170),
//                        new DataPoint(8, 140)
//                });
//                graph.addSeries(series);

                alertDialog = builder.create();
                alertDialog.show();

//                pulse_rates_list.clear();

            }
        });


    }

    @Override
    public int getItemCount() {
        return record.size();

    }

    private void drawGraph(){

    }
}
