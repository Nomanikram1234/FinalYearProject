package com.example.nomanikram.epilepsyseizuredetection.views;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.nomanikram.epilepsyseizuredetection.BluetoothConnectionActivity;
import com.example.nomanikram.epilepsyseizuredetection.MyBluetoothService;
import com.example.nomanikram.epilepsyseizuredetection.R;

import com.example.nomanikram.epilepsyseizuredetection.models.Device;

import java.util.List;

/**
 * Created by nomanikram on 04/03/2018.
 */

public class RecycleAdapter_devices extends RecyclerView.Adapter<view_holder_devices> {

    List<BluetoothDevice> device;
    LinearLayout linearLayout;

    public RecycleAdapter_devices(List<BluetoothDevice> device){
        this.device = device;
    }

    @Override
    public view_holder_devices onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_device,parent,false);

        return new view_holder_devices(view);
    }

    @Override
    public void onBindViewHolder(view_holder_devices holder, final int position) {
        final BluetoothDevice devices = device.get(position);

        holder.name.setText(devices.getName());
        holder.address.setText(devices.getAddress());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.w("TAG","DEVICES: "+devices.getName());



//                (BluetoothDevice) parent.getItemAtPosition(position);
//                Toast.makeText(bluetoothlist.this,
//                        "Name: " + devices.getName() + "\n"
//                                + "Address: " + devices.getAddress() + "\n"
//                                + "BondState: " + devices.getBondState() + "\n"
//                                + "BluetoothClass: " + devices.getBluetoothClass() + "\n"
//                                + "Class: " + device.getClass(),
//                        Toast.LENGTH_LONG).show();

//                textStatus="start ThreadConnectBTdevice";
                Log.w(" ","DEDEDEDEDEDD: "+devices);

               Intent intent = new Intent(BluetoothConnectionActivity.context_push,MyBluetoothService.class);
                Bundle b = new Bundle();
                b.putParcelable("data",devices);
                intent.putExtras(b);
                BluetoothConnectionActivity.context_push.startService(intent);
            }
        });

        Log.w("TAG", "List: "+devices.getName());

    }

    @Override
    public int getItemCount() {
        return device.size();
    }
}
