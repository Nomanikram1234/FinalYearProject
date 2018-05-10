package com.example.nomanikram.epilepsyseizuredetection;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nomanikram.epilepsyseizuredetection.MainActivity;
import com.example.nomanikram.epilepsyseizuredetection.models.Data;
import com.example.nomanikram.epilepsyseizuredetection.models.Patient;
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
import java.util.Iterator;
import java.util.UUID;

public class MyBluetoothService extends Service {
    //    ListView listViewPairedDevice;
//    BluetoothAdapter bluetoothAdapter;
    private Intent senddata;
    private static final int REQUEST_ENABLE_BT = 1;
//    String textInfo;

    private UUID myUUID;
    private final String UUID_STRING_WELL_KNOWN_SPP =
            "00001101-0000-1000-8000-00805F9B34FB";
    private String textStatus;
    private ThreadConnectBTdevice myThreadConnectBTdevice;
    private String textByteCnt, pulse;
    private ThreadConnected myThreadConnected;

    private String temp;
    private static String activity;

    private Date d;

    private static boolean first_run = true;

    // declare variable to store format for date and time
    private static SimpleDateFormat date;
    private static SimpleDateFormat time;

    // declare variable for firebase auth state and database ref
    private FirebaseAuth mAuth;
    private DatabaseReference database;

    private static DatabaseReference user_reference;
    private static DatabaseReference record_ref;

    private static ArrayList<String> numbers;

    static int age;

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
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Intent notificationIntent = new Intent(this, MyBluetoothService.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);

            Notification notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Sensor Monitoring")
                    .setContentText("Doing some work...")
                    .setOngoing(true)
                    .setContentIntent(pendingIntent).build();
            startForeground(1337, notification);
        }
    }

    @Override
    public int onStartCommand(Intent tent, int flags, int id) {
        try {

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
                    Log.w("", "DS: " + dataSnapshot);
                    if (dataSnapshot.getValue() == null) {
                        count = 0;
                        first_run = false;
                    } else {
                        count = Integer.parseInt("" + dataSnapshot.getValue());
                        Log.w("", "DataSnapShot Data: " + dataSnapshot.getValue());
                        first_run = false;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            numbers = new ArrayList<String>();

            Log.w("", "Service started: " + device);


            myUUID = UUID.fromString(UUID_STRING_WELL_KNOWN_SPP);
            myThreadConnectBTdevice = new MyBluetoothService.ThreadConnectBTdevice(device);
            //goes to class ThreadConnectBTdevice and runs its threads start method to connect device and start data sending
            myThreadConnectBTdevice.start();
            senddata = new Intent(getApplicationContext(), MainActivity.class);


            senddata.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(senddata);
        } catch (Exception ex) {

        }
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
                textStatus = "bluetoothSocket: \n" + bluetoothSocket;
                Log.w("Tag", "BLUETOOTH Sockey:" + bluetoothSocket);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void destroy() {
            super.destroy();


            if (myThreadConnectBTdevice != null) {
                myThreadConnectBTdevice.cancel();
//                stopSelf();

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
            if (success) {
                // object of class thread connected
                startThreadConnected(bluetoothSocket);
            }
            // if connection failed
            else {

            }
        }

        public void cancel() {
            Toast.makeText(getApplicationContext(),
                    "close bluetoothSocket",
                    Toast.LENGTH_LONG).show();
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private void startThreadConnected(BluetoothSocket socket) {

        myThreadConnected = new MyBluetoothService.ThreadConnected(socket);
        myThreadConnected.start();
        if (myThreadConnected != null) {
            String i = "1";
            byte[] bytesToSend = i.toString().getBytes();
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
                try {

                    //****ADDED SLEEP HERE SO THE BUFFER CAN FILL UP AND WE GET WHOLE DATA TOGETHER
                    //****

                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                    }
                    bytes = connectedInputStream.read(buffer);
                    final String strReceived = new String(buffer, 0, bytes);
                    final String strByteCnt = String.valueOf(bytes) + " bytes received.\n";


                    textStatus = strReceived;

                    try {
                        String s = textStatus;
                        String tem = "", pul = "", act = "";
                        try {
                            tem = s.substring(s.indexOf("(") + 1);
                            tem = tem.substring(0, tem.indexOf(")"));

                            temp = tem;

                        } catch (Exception e) {
                            temp = "n/a";
                        }
                            /*
                             Add try catch for accelrometer here


                            */


                        try {
                            act = s.substring(s.indexOf("p"), s.indexOf("g") + 1);
                            activity = act;

                        } catch (Exception e) {
                            try {
                                act = s.substring(s.indexOf("L"), s.indexOf("W") + 1);
                                activity = act;

                            } catch (Exception ex) {
                                try {
                                    act = s.substring(s.indexOf("M"), s.indexOf("U") + 2);
                                    activity = act;

                                } catch (Exception exe) {
                                    try {
                                        act = s.substring(s.indexOf("H"), s.indexOf("G") + 2);
                                        activity = act;

                                    } catch (Exception excep) {

                                    }
                                }
                            }
                        }


//
//try {
//    if (s.substring(s.indexOf("p"), s.indexOf("g") + 1).contains("person not moving"))
//        activity = "no activity";
//    if (s.substring(s.indexOf("L"), s.indexOf("W") + 1).contains("LOW"))
//        activity = "low";
//    if (s.substring(s.indexOf("M"), s.indexOf("I") + 3).contains("MEDIUM"))
//        activity = "medium";
//    if (s.substring(s.indexOf("H"), s.indexOf("G") + 2).contains("HIGH"))
//        activity = "high";
//}
//                           catch (Exception ex) {
//                               activity = "n/a";
//                           }

//                            try
//                            {
//
//                                    act = s.substring(s.indexOf("p"), s.indexOf("g")+1);
//                                    activity = act;
//
//                            }
//                            catch(Exception e)
//                            {
////                                activity = "l n/a";
//
//try {
//    activity = s.substring(s.indexOf("p"), s.indexOf("g")+1);;
//}catch (Exception ex){
//
//}
//
//
//                            }
//
//                            try
//                            {
//                                act= s.substring(s.indexOf("L"), s.indexOf("W")+1);
//                                activity = act;
//
//                            }
//                            catch(Exception e)
//                            {
//                                activity = "m n/a";
//
//                                act= s.substring(s.indexOf("L"), s.indexOf("W")+1);
//                                activity = act;
//                            }
//
//                            try
//                            {
//                                act = s.substring(s.indexOf("M"), s.indexOf("I")+3);
//                                activity = act;
//
//                            }
//                            catch(Exception e)
//                            {
//                                activity = "h n/a";
//
//                                act = s.substring(s.indexOf("M"), s.indexOf("I")+3);
//                                activity = act;
//                            }
//
//                            try
//                            {
//                                act = s.substring(s.indexOf("H"), s.indexOf("G")+2);
//                                activity = act;
//
//                            }
//                            catch(Exception e)
//                            {
//                                activity = "n n/a";
//
//                                act = s.substring(s.indexOf("H"), s.indexOf("G")+2);
//                                activity = act;
//                            }







                        /* END Test Code */
                        try {
                            pul = s.substring(s.indexOf("{") + 1);
                            pul = pul.substring(0, pul.indexOf("}"));
                            pulse = pul;
                        } catch (Exception e) {
                            pulse = "Pulse not Received";
                        }
                        try {
                            int temp1 = Integer.parseInt(temp);
                            int pul1 = Integer.parseInt(pulse);

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
                            if (temp1 < 37) {
                                textStatus = "37";
                            }
                            if (temp1 > 38) {
                                textStatus = "38";
                            }
                            if (pul1 < 100) {
                                pul1 = pul1;
                            }
                            if (pul1 > 120) {
                                pulse = "100";
                            }
                            //AFTER SUBTRACTING NEW VALUE OF PUL DONE CUZ OF CHEAP SENSOR

                        } catch (Exception e) {
                            pulse = "0";
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    textByteCnt = strByteCnt;


                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                    final String msgConnectionLost = "Connection lost:\n"
                            + e.getMessage();

                }

//                if(temp.equals("Arduino external interrupt 0"))
//                    temp= "--";

                // data obtained from sensors is stored in the obt object
                Data obt = new Data();
                obt.temp = temp;
                obt.pulse = pulse;
                obt.activity = activity;

                checkCondition(obt.pulse, obt.temp, obt.activity);

                // setting he format
                time = new SimpleDateFormat("HH:mm:ss");
                date = new SimpleDateFormat("dd/MM/yyyy");

                d = new Date();

                // storing the data to database
                record_ref.child("count").setValue(count);
                count++;

                record_ref.child("record " + count).child("pulse").setValue("" + obt.pulse);

                record_ref.child("record " + count).child("temperture").setValue("" + obt.temp);
                record_ref.child("record " + count).child("accelerometer").setValue(obt.activity);
                record_ref.child("record " + count).child("time").setValue("" + time.format(d));
                record_ref.child("record " + count).child("date").setValue("" + date.format(d));

                Log.w("Accelerometer", "Activity: " + activity);

                /********************
                 ************************/
//
//                Query queryC = database.child("users").child(mAuth.getCurrentUser().getUid()).child("Patient").child("Contact");
//                queryC.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
////                Log.w("","Duckey: "+dataSnapshot);
//                        Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
//
//                        while (items.hasNext()) {
//                            DataSnapshot item = items.next();
//                            try {
//
////                        Log.w("", "Donkey: " + item.child("Number").getValue().toString());
//                                numbers.add(item.child("Number").getValue().toString());
//
//                            } catch (Exception ex) {
//                                Log.w("", "--|--");
//                            }
//
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });

                getContacts();
                /********************
                 ************************/

                Log.w("TAG", "MyBluetoothService\n" + "temp: " + temp + "\npulse: " + pulse + "\nactivity: " + activity);
                // Broadcasting the data to Home class -> Sensor Receiver class
                Intent intent = new Intent(getApplicationContext(), HomeFragment.SensorReceiver.class);
                intent.putExtra("MyObject", obt);
                sendBroadcast(intent);

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

        public void cancel() {
            try {
                connectedBluetoothSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


    }

    private int average_bpm(int providedAge) {
//        Scanner input = new Scanner(System.in);

        int age = providedAge;
        int weight, height;

        double max;

        System.out.println("Enter age:");
//        age = input.nextInt();

        // Max Heart rate
        max = 211 - 0.64 * age;

        // Training
        int TMax = (int) (max - 6);
        int TMin = (int) (max - 26);

        // Normal
        int NMax = (int) (max - 44);
        int NMin = (int) (max - 64);

//        System.out.println("\nMax Heart Rate:" + max + "\n\n" + "[TRAINING]\nMax: " + TMax + " to Min: " + TMin + "\n");

//        System.out.println("[NORMAL]\nMax: " + NMax + " to Min: " + NMin);

        return NMax;
    }

    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS snt", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "NOT snt", Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList<String> getContacts() {


        Query queryC = database.child("users").child(mAuth.getCurrentUser().getUid()).child("Patient").child("Contact");
        queryC.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                Log.w("","Duckey: "+dataSnapshot);
                Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();

                while (items.hasNext()) {
                    DataSnapshot item = items.next();
                    try {

//                        Log.w("", "Donkey: " + item.child("Number").getValue().toString());
                        numbers.add(item.child("Number").getValue().toString());

                    } catch (Exception ex) {
                        Log.w("", "--|--");
                    }


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return numbers;
    }

    private int getAge() {
        Query age_query = database.child("users").child(mAuth.getCurrentUser().getUid()).child("Patient");

        age_query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                age = Integer.parseInt(dataSnapshot.child("age").getValue().toString());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return age;
    }

    private void checkCondition(String pulse, String temp, String activity) {

        ArrayList<String> contacts = getContacts();
        for (int j = 0; j < contacts.size(); j++) {
            Log.w("", "Numbers " + j + ": " + contacts.get(j));

            int avg = average_bpm(getAge());
            if (Double.parseDouble(temp) > 37) {
                if (Integer.parseInt(pulse) > avg) {
                    if (activity.equalsIgnoreCase("HIGH") || activity.equalsIgnoreCase("MEDIUM") || activity.equalsIgnoreCase("LOW")) {
                        getContacts();

                        for (int i = 0; i < contacts.size(); i++) {
                            sendSMS(contacts.get(i), "Person is having seizure");
                        }

                    }
                }
            }

        }

    }
}