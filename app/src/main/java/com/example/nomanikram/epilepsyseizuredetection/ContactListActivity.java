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

    // Declaring list to store names and corresponding mobile number
    private static List<String> names,numbers;

    // declaring the listview to display the list of contacts
    private ListView listView ;

    // Declaring the cursor to go through the whole list of contacts
    private Cursor phones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        // Initiazling the listview
        listView = (ListView) findViewById(R.id.listview_contacts);

        // Initializing the arraylists
        names = new ArrayList<String>();
        numbers= new ArrayList<String>();

                /* ********************* */
        // cursor to move through the whole list of contacts and getting the display names
        phones = getApplication().getContentResolver().query
                (ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

        // variable declared to get name and phone numbers from the cursor -> phone
        String name,phoneNumber;

        // phone the items in contact list exists
        while(phones.moveToNext()) {

            // getting names and numbers stored from contacts
            name= phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phoneNumber=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            // getting the above fetched info stored in the arraylist
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

                // getting the storing the click contact in obj
                Contact c = new Contact();
                c.contact_name =  names.get(position);
                c.contact_no  = numbers.get(position);

                // sending the data in the form of obj and thus, start the sctivity
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("MyObject",c);
                startActivity(intent);

                Toast.makeText(getApplicationContext(),"Name:"+names.get(position),Toast.LENGTH_SHORT);
                Log.w("TAG","Name:"+names.get(position)+" Number: "+c.contact_no);
            }
        });


    }
}
