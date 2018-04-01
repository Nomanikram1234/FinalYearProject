package com.example.nomanikram.epilepsyseizuredetection;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.nomanikram.epilepsyseizuredetection.models.Contact;
import com.example.nomanikram.epilepsyseizuredetection.views.RecycleAdapter_contact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    // declaring variables for button
    AppCompatButton btn_add_manually;
    AppCompatButton btn_add_from_contacts;

    // declaring variables for edit text
    static AppCompatEditText txt_number;
    static  AppCompatEditText txt_name;

    // declaring the variables for alertDialog and its builder
    AlertDialog alertDialog;
    AlertDialog.Builder builder;

    // declaring the list for name,number,contact
    static List<String> names,numbers;
    static List<Contact> contacts;

    // Declaring cursor
    Cursor phones;

    // declaring the variable for recycle view
    RecyclerView recycleview;

    // declaring the variable for authentication state
    FirebaseAuth mAuth;

    //declaring variable for storing database reference
    DatabaseReference database;

    // database reference to user and contact
    static DatabaseReference user_reference;
    static DatabaseReference contact_ref;


    static ListView listView ;

    // declaring the variables that used as counter for number of contacts already available in the databse
    static int no;
    private static String total_no;

    static boolean first_run = false;

    // declaring the object for contact
    static Contact contact;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment_view=  inflater.inflate(R.layout.fragment_contact, container, false);

        // Initizaling the buttons
        btn_add_manually = (AppCompatButton) fragment_view.findViewById(R.id.btn_add_manually);
        btn_add_from_contacts = (AppCompatButton) fragment_view.findViewById(R.id.btn_add_from_contacts);

        // Initializing the recycleview
        recycleview = (RecyclerView) fragment_view.findViewById(R.id.recycler_contact);

        // Initializing the auth state and data
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        // initizaling the arraylist for name,number and contact
        names  = new ArrayList<String>();
        numbers = new ArrayList<String>();
        contacts = new ArrayList<Contact>();

        // initialization users list
        List<Contact> users = new ArrayList<Contact>();

        // initializing the database references
        user_reference = database.child("users").child("" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        contact_ref = database.child("users").child("" + mAuth.getCurrentUser().getUid()).child("Patient").child("Contact");

//
//        Contact c1 = new Contact();
//        c1.contact_name = "Noman Ikram";
//        c1.contact_no = "03485007570XXX";
//
//        Contact c2 = new Contact();
//        c2.contact_name = "Raja Waqas";
//        c2.contact_no = "0044121771069";
//

//        Toast.makeText(getActivity().getApplicationContext(),"contacts size: "+contacts.size(),Toast.LENGTH_SHORT);
//        for(int i=0 ; i< contacts.size() ; i++) {
////            Contact C =;
//            Log.w("","Names: "+contacts.get(i).contact_name);
//            users.add(contacts.get(i));
//        }
//        users.add(c2);



        Log.d("TAG","Size: "+users.size());
        LinearLayoutManager linear = new LinearLayoutManager(this.getContext());
        recycleview.setLayoutManager(linear);

        recycleview.setHasFixedSize(true);
//        recycleview.setAdapter(new RecycleAdapter_contact(users));
//        Log.d("TAG",c1+"\n"+c1);


        btn_add_manually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // building alert dialog for getting contact info
                builder = new AlertDialog.Builder(getActivity());
                View view_f = getLayoutInflater().inflate(R.layout.layout_add_contact_manually, null);
                builder.setView(view_f);

                AppCompatButton btn_add_contact_manually = (AppCompatButton) view_f.findViewById(R.id.btn_add_contact);
                contact = new Contact();
                txt_number = (AppCompatEditText) view_f.findViewById(R.id.txt_contact_no);
                txt_name = (AppCompatEditText) view_f.findViewById(R.id.txt_contact_name);

                alertDialog = builder.create();
                alertDialog.show();

                btn_add_contact_manually.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       // checks if the input fields of alert dialog are not empty
                       if(!txt_number.getText().toString().isEmpty() || !txt_name.getText().toString().isEmpty()){

                /* ************************************** */
//
//                          user_reference = database.child("users").child("" + FirebaseAuth.getInstance().getCurrentUser().getUid());
//                          contact_ref = database.child("users").child("" + mAuth.getCurrentUser().getUid()).child("Patient").child("Contact");

                           // store the inputs the object
                           contact.contact_name = txt_name.getText().toString();
                           contact.contact_no = txt_number.getText().toString();

                           // query to read the data change at contact ref node in the database reference
                           Query query = contact_ref;

                               query.addListenerForSingleValueEvent((new ValueEventListener() {
                                   @Override
                                   public void onDataChange(DataSnapshot dataSnapshot) {

                                       // checking either size node exists or not, if not the initialize the counter variables to 0
                                       if (!dataSnapshot.child("Size").exists())
                                       {
                                           no = 0;
                                           total_no="0";

                                           // storing data in the database
                                           contact_ref.child("Size").setValue("" + total_no);
                                           contact_ref.child("contact " + total_no).child("Name").setValue("" + contact.contact_name);
                                           contact_ref.child("contact " + total_no).child("Number").setValue("" + contact.contact_no);

                                         update_recycleview();                                    }

                                       // otherwise read the current value of counter variable from database and initizaling those value to counter variable
                                         else
                                       {
                                           Log.w("","total_no: "+total_no);
                                           total_no = (String) dataSnapshot.child("Size").getValue();
                                           no = Integer.parseInt(total_no);

                                           no++;
                                           total_no = ""+no;

                                           // storing data in the database
                                           contact_ref.child("Size").setValue("" + total_no);
                                           contact_ref.child("contact " + total_no).child("Name").setValue("" + contact.contact_name);
                                           contact_ref.child("contact " + total_no).child("Number").setValue("" + contact.contact_no);

                                           update_recycleview();

                                       }
//                                            update_recycleview();

                                   }

                                   @Override
                                   public void onCancelled(DatabaseError databaseError) {

                                   }
                               }));

                            /* ************************************** */

                       }


                   }
               });
            }
        });

        btn_add_from_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ContactListActivity.class);
                startActivity(intent);


            }
        });


        Query query = contact_ref;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for( DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        Log.w("", "snapshot: " + dataSnapshot.getChildrenCount());

                        if(snapshot.child("Name").getValue() == null && snapshot.child("Number").getValue() == null)
                        continue;

                        Contact C = new Contact();
                        C.contact_name =(String) snapshot.child("Name").getValue();
                        C.contact_no = (String) snapshot.child("Number").getValue();

                        contacts.add(C);
                }
                recycleview.setAdapter(new RecycleAdapter_contact(contacts));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return fragment_view;
    }


    private void update_recycleview(){

        Query query = contact_ref;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for( DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Log.w("", "snapshot: " + dataSnapshot.getChildrenCount());

                    if(snapshot.child("Name").getValue() == null && snapshot.child("Number").getValue() == null)
                        continue;

                    Contact C = new Contact();
                    C.contact_name =(String) snapshot.child("Name").getValue();
                    C.contact_no = (String) snapshot.child("Number").getValue();




                    contacts.add(C);
                }
                recycleview.setAdapter(new RecycleAdapter_contact(contacts));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
