package com.example.nomanikram.epilepsyseizuredetection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.nomanikram.epilepsyseizuredetection.models.Device;
import com.example.nomanikram.epilepsyseizuredetection.models.Record;
import com.example.nomanikram.epilepsyseizuredetection.views.RecycleAdapter_devices;
import com.example.nomanikram.epilepsyseizuredetection.views.RecycleAdapter_record;

import java.util.ArrayList;
import java.util.List;

public class BluetoothConnectionActivity extends AppCompatActivity {

    RecyclerView recycleview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connection);

        recycleview = (RecyclerView) findViewById(R.id.recycleview_devicelist);
        List<Device> devices = new ArrayList<Device>();

        Device d2 = new Device();
        d2.name = "Hadi";
        d2.address = "Fdwdasd";

        Device d1 = new Device();
        d1.name = "Noman Ikram";
        d1.address = "Fawqe123";


        devices.add(d2);
        devices.add(d1);


        Toast.makeText(getApplicationContext(),"Size: "+ devices.size(),Toast.LENGTH_SHORT).show();

        LinearLayoutManager linear = new LinearLayoutManager(getApplicationContext());
        recycleview.setLayoutManager(linear);

        recycleview.setHasFixedSize(false);
        recycleview.setAdapter(new RecycleAdapter_devices(devices));



    }
}
