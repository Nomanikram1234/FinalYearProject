package com.example.nomanikram.epilepsyseizuredetection.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nomanikram.epilepsyseizuredetection.R;

import com.example.nomanikram.epilepsyseizuredetection.models.Device;

import java.util.List;

/**
 * Created by nomanikram on 04/03/2018.
 */

public class RecycleAdapter_devices extends RecyclerView.Adapter<view_holder_devices> {

    List<Device> device;

    public RecycleAdapter_devices(List<Device> device){
        this.device = device;
    }

    @Override
    public view_holder_devices onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_device,parent,false);
        return new view_holder_devices(view);
    }

    @Override
    public void onBindViewHolder(view_holder_devices holder, int position) {
        Device devices = device.get(position);

        holder.name.setText(devices.name);
        holder.address.setText(devices.address);

    }

    @Override
    public int getItemCount() {
        return device.size();
    }
}
