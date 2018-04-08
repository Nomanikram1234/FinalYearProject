package com.example.nomanikram.epilepsyseizuredetection.views;

import android.content.Context;
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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

/**
 * Created by nomanikram on 12/02/2018.
 */

public class RecycleAdapter_record extends RecyclerView.Adapter<view_holder_record> {

    // declaring the variables for alertDialog and its builder
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;


    private List<Record> record ;
    Context context;

    public RecycleAdapter_record(List<Record> record ,Context context){
        this.record = record;
        this.context = context;
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


                // Dummy Data User at the moment
                GraphView graph = (GraphView) view_f.findViewById(R.id.graph);
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                        new DataPoint(0, 132),
                        new DataPoint(1, 160),
                        new DataPoint(2, 123),
                        new DataPoint(3, 140),
                        new DataPoint(4, 150),
                        new DataPoint(5, 150),
                        new DataPoint(6, 190),
                        new DataPoint(7, 170),
                        new DataPoint(8, 140)
                });
                graph.addSeries(series);

                alertDialog = builder.create();
                alertDialog.show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return record.size();

    }
}
