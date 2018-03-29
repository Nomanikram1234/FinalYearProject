package com.example.nomanikram.epilepsyseizuredetection;

import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nomanikram.epilepsyseizuredetection.R;
import com.example.nomanikram.epilepsyseizuredetection.models.Contact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {

    static List<String> names,numbers;
    ListView listView ;
    Cursor phones;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);



        listView = (ListView) findViewById(R.id.listview_contacts);

        names = new ArrayList<String>();
        numbers= new ArrayList<String>();

                /* ********************* */
        phones = getApplication().getContentResolver().query
                (ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

        String name,phoneNumber;

        while(phones.moveToNext()) {
            name= phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phoneNumber=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


            names.add(""+name);
            numbers.add(""+phoneNumber);
//                    Log.w("","Names: " +name+"\nPhone: "+phoneNumber);

        }

        Log.w("","Names: " +names.get(0)+"\nPhone: "+numbers.get(0));

        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),android.
                R.layout.simple_list_item_1,names);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Contact c = new Contact();
                c.contact_name =  names.get(position);
                c.contact_no  = numbers.get(position);

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("MyObject",c);
                startActivity(intent);

                Toast.makeText(getApplicationContext(),"Name:"+names.get(position),Toast.LENGTH_SHORT);
                Log.w("TAG","Name:"+names.get(position)+" Number: "+c.contact_no);
            }
        });


    }
}
