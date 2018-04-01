package com.example.nomanikram.epilepsyseizuredetection;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nomanikram.epilepsyseizuredetection.MainActivity;
import com.example.nomanikram.epilepsyseizuredetection.models.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class MyBluetoothService extends Service  {
//    ListView listViewPairedDevice;
//    BluetoothAdapter bluetoothAdapter;
    Intent senddata;
    private static final int REQUEST_ENABLE_BT = 1;
//    String textInfo;

    private UUID myUUID;
    private final String UUID_STRING_WELL_KNOWN_SPP =
            "00001101-0000-1000-8000-00805F9B34FB";
    String textStatus;
    ThreadConnectBTdevice myThreadConnectBTdevice;
    String textByteCnt,pulse;
    ThreadConnected myThreadConnected;

    String temp;

    Date d;

    static boolean first_run = true;

    // declare variable to store format for date and time
    static SimpleDateFormat date;
    static SimpleDateFormat time;

    // declare variable for firebase auth state and database ref
    private FirebaseAuth mAuth;
    private DatabaseReference database;

    private static DatabaseReference user_reference;
    private static DatabaseReference record_ref;

    // declare counter variable
    static int count;

    // decalre variable for query
    static Query query;

    public MyBluetoothService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent tent, int flags, int id) {

        Bundle b = tent.getExtras();
        BluetoothDevice device = b.getParcelable("data");

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        user_reference = database.child("users").child("" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        record_ref = database.child("users").child("" + mAuth.getCurrentUser().getUid()).child("Patient").child("Record");

        query = record_ref.child("count");


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w("","DS: "+dataSnapshot);
                if (dataSnapshot.getValue() == null  )
                {
                    count = 0;
                    first_run=false;
                }else
                    {
                    count = Integer.parseInt("" + dataSnapshot.getValue());
                    Log.w("", "DataSnapShot Data: " + dataSnapshot.getValue());
                    first_run = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        Log.w("","Service started: "+device);


        myUUID = UUID.fromString(UUID_STRING_WELL_KNOWN_SPP);
        myThreadConnectBTdevice = new MyBluetoothService.ThreadConnectBTdevice(device);
        //goes to class ThreadConnectBTdevice and runs its threads start method to connect device and start data sending
        myThreadConnectBTdevice.start();
        senddata=new Intent(getApplicationContext(),MainActivity.class);


        senddata.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(senddata);

        return flags;

    }

    private class ThreadConnectBTdevice extends Thread {

        private BluetoothSocket bluetoothSocket = null;
        private final BluetoothDevice bluetoothDevice;


        private ThreadConnectBTdevice(BluetoothDevice device) {
            bluetoothDevice = device;

            try {
                // a soket for bluetooth device is reserveed
                bluetoothSocket = device.createRfcommSocketToServiceRecord(myUUID);
                textStatus="bluetoothSocket: \n" + bluetoothSocket;
                Log.w("Tag","BLUETOOTH Sockey:"+bluetoothSocket );
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            boolean success = false;
            try {
                bluetoothSocket.connect();
                success = true;
            } catch (IOException e) {
                e.printStackTrace();

                final String eMessage = e.getMessage();


                try {
                    bluetoothSocket.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }

            // if connected successfully
            if(success){
                // object of class thread connected
                startThreadConnected(bluetoothSocket);
            }
            // if connection failed
            else
            {

            }
        }

        public void cancel()
        {
            Toast.makeText(getApplicationContext(),
                    "close bluetoothSocket",
                    Toast.LENGTH_LONG).show();
            try
            {
                bluetoothSocket.close();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
    private void startThreadConnected(BluetoothSocket socket){

        myThreadConnected = new MyBluetoothService.ThreadConnected(socket);
        myThreadConnected.start();
        if(myThreadConnected!=null){
            String i="1";
            byte[] bytesToSend =i.toString().getBytes();
            myThreadConnected.write(bytesToSend);
            //byte[] NewLine = "\n".getBytes();
            //myThreadConnected.write(NewLine);
        }
    }
    private class ThreadConnected extends Thread {
        private final BluetoothSocket connectedBluetoothSocket;
        private final InputStream connectedInputStream;
        private final OutputStream connectedOutputStream;

        public ThreadConnected(BluetoothSocket socket) {
            connectedBluetoothSocket = socket;
            InputStream in = null;
            OutputStream out = null;

            try {
                in = socket.getInputStream();
                out = socket.getOutputStream();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            connectedInputStream = in;
            connectedOutputStream = out;

        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            String strRx = "";

            while (true) {
                try
                {

                    //****ADDED SLEEP HERE SO THE BUFFER CAN FILL UP AND WE GET WHOLE DATA TOGETHER
                    //****

                    try
                    {
                        Thread.sleep(8000);}catch (Exception e){}
                        bytes = connectedInputStream.read(buffer);
                        final String strReceived = new String(buffer, 0, bytes);
                        final String strByteCnt = String.valueOf(bytes) + " bytes received.\n";


                        textStatus=strReceived;

                        try
                        {
                            String s=textStatus;
                            String tem="",pul="";
                            try
                            {
                                tem = s.substring(s.indexOf("(") + 1);
                                tem = tem.substring(0, tem.indexOf(")"));

                                temp = tem;

                        }
                        catch(Exception e)
                        {
                            temp="Temperature not received";}
                            try
                            {
                                pul = s.substring(s.indexOf("{") + 1);
                                pul = pul.substring(0, pul.indexOf("}"));
                                pulse=pul;
                            }
                            catch(Exception e)
                            {
                                pulse="Pulse not Received";}
                                try
                                {
                                    int temp1 = Integer.parseInt(temp);
                                    int pul1=Integer.parseInt(pulse);

                            //doublev.setText("t"+temp1);

                            ///********TEMP ALARM CONDITION BELOW****
                            ////********
//                                    if(temp1>38){
//                                        if(pul1>100) {
//                                            StartAlarm();
//                                        }
//                                    }
//                                    if(temp1>50){
//                                        temp1=temp1-18;
//                                        doublev.setText(temp1);
//                                    }
                                if(temp1<37)
                                {
                                textStatus="37";
                                }
                                if(temp1>38)
                                {
                                textStatus="38";
                                }
                                if(pul1<100)
                                {
                                pul1=pul1;
                                }
                                if(pul1>120)
                                {
                                pulse="100";
                                }
                                //AFTER SUBTRACTING NEW VALUE OF PUL DONE CUZ OF CHEAP SENSOR

                                }
                                catch(Exception e)
                                {
                                    pulse="--";
                                }

                        }catch (Exception e)
                        {
                        e.printStackTrace();
                        }

                        textByteCnt=strByteCnt;


                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                    final String msgConnectionLost = "Connection lost:\n"
                            + e.getMessage();

                }

                if(temp.equals("Arduino external interrupt 0"))
                    temp= "--";

                // data obtained from sensors is stored in the obt object
                Data obt = new Data();
                obt.temp = temp;
                obt.pulse= pulse;

                // setting he format
                time = new SimpleDateFormat("HH:mm:ss");
                date = new SimpleDateFormat("dd/MM/yyyy");

                d = new Date();

                // storing the data to database
                    record_ref.child("count").setValue(count);
                    count++;
                    record_ref.child("record "+count).child("pulse").setValue(""+obt.pulse);
                    record_ref.child("record "+count).child("temperture").setValue(""+obt.temp);
                    record_ref.child("record "+count).child("acceleromenter").setValue("N/A");
                    record_ref.child("record "+count).child("time").setValue(""+time.format(d));
                    record_ref.child("record "+count).child("date").setValue(""+date.format(d));


                Log.w("TAG","MyBluetoothService\n"+"temp: "+temp+"\npulse: "+pulse);
                // Broadcasting the data to Home class -> Sensor Receiver class
                Intent inteni = new Intent(getApplicationContext(),HomeFragment.SensorReceiver.class);
                inteni.putExtra("MyObject",obt);
                sendBroadcast(inteni);

            }
        }
        public void write(byte[] buffer) {
            try {
                connectedOutputStream.write(buffer);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

}
