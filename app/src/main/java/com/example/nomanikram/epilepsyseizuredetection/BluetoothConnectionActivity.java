package com.example.nomanikram.epilepsyseizuredetection;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nomanikram.epilepsyseizuredetection.models.Device;
import com.example.nomanikram.epilepsyseizuredetection.models.Record;
import com.example.nomanikram.epilepsyseizuredetection.views.RecycleAdapter_devices;
import com.example.nomanikram.epilepsyseizuredetection.views.RecycleAdapter_record;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothConnectionActivity extends AppCompatActivity {

    RecyclerView recycleview;

    public  static  Context context_push;

    ListView listViewPairedDevice;
    static  BluetoothAdapter bluetoothAdapter;

    public static Object bluetoothdevice;

    private UUID myUUID;
    private final String UUID_STRING_WELL_KNOWN_SPP =
            "00001101-0000-1000-8000-00805F9B34FB";
    private static final int REQUEST_ENABLE_BT = 1;
    String textInfo;

    ArrayList<BluetoothDevice> pairedDeviceArrayList;
    ArrayAdapter<BluetoothDevice> pairedDeviceAdapter;

    String textStatus;



    Intent intent;
    BluetoothDevice device;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connection);

        context_push = getApplicationContext();

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
//        recycleview.setAdapter(new RecycleAdapter_devices(devices));

        /* *************************************************************************** */
//        listViewPairedDevice = (ListView)findViewById(R.id.pairedlist);
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)){
            Toast.makeText(this,
                    "FEATURE_BLUETOOTH NOT support",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        //using the well-known SPP UUID


        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this,
                    "Bluetooth is not supported on this hardware platform",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        String stInfo = bluetoothAdapter.getName() + "\n" +
                bluetoothAdapter.getAddress();
        textInfo=stInfo;

    }


    @Override
    protected void onStart() {
        super.onStart();

        //Turn ON BlueTooth if it is OFF
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        setup();
    }
    private void setup() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            pairedDeviceArrayList = new ArrayList<BluetoothDevice>();

            for (BluetoothDevice device : pairedDevices) {
                pairedDeviceArrayList.add(device);
                Log.w("","DEVICES BT: "+device);
            }

            pairedDeviceAdapter = new ArrayAdapter<BluetoothDevice>(this,
                    android.R.layout.simple_list_item_1, pairedDeviceArrayList);
            recycleview.setAdapter(new RecycleAdapter_devices(pairedDeviceArrayList));

//            recycleview.OnItemTouchListener(new )

//            listViewPairedDevice.setAdapter(pairedDeviceAdapter);

//            listViewPairedDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view,
//                                        int position, long id) {
//                    device =
//                            (BluetoothDevice) parent.getItemAtPosition(position);
//                    Toast.makeText(getApplicationContext(),
//                            "Name: " + device.getName() + "\n"
//                                    + "Address: " + device.getAddress() + "\n"
//                                    + "BondState: " + device.getBondState() + "\n"
//                                    + "BluetoothClass: " + device.getBluetoothClass() + "\n"
//                                    + "Class: " + device.getClass(),
//                            Toast.LENGTH_LONG).show();
//
//                    textStatus="start ThreadConnectBTdevice";
//                    intent = new Intent(getApplicationContext(),MyBluetoothService.class);
//                    Bundle b = new Bundle();
//                    b.putParcelable("data",device);
//                    intent.putExtras(b);
//                    startService(intent);
//
//                }
//            });
        }
    }
}
