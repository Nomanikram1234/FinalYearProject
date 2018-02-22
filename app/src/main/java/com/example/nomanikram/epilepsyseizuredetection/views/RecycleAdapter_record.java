package com.example.nomanikram.epilepsyseizuredetection.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nomanikram.epilepsyseizuredetection.R;
import com.example.nomanikram.epilepsyseizuredetection.models.Contact;
import com.example.nomanikram.epilepsyseizuredetection.models.Record;

import java.util.List;

/**
 * Created by nomanikram on 12/02/2018.
 */

public class RecycleAdapter_record extends RecyclerView.Adapter<view_holder_record> {

    private List<Record> record ;

    public RecycleAdapter_record(List<Record> record){
        this.record = record;
    }
    @Override
    public view_holder_record onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_record,parent,false);





        return new view_holder_record(view);
    }

    @Override
    public void onBindViewHolder(view_holder_record holder, int position) {
        Record samplerecord = record.get(position);

        holder.pulse.setText(samplerecord.heartbeat);
        holder.temp.setText(samplerecord.temp);
        holder.activity_status.setText(samplerecord.activity_status);
        holder.date.setText(samplerecord.date);
        holder.time.setText(samplerecord.time);
    }

    @Override
    public int getItemCount() {
        return record.size();

    }
}
