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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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

    AppCompatButton btn_add_manually;
    AppCompatButton btn_add_from_contacts;

    AlertDialog alertDialog;
    AlertDialog.Builder builder;

    static List<String> names,numbers;
    static List<Contact> contacts;

    Cursor phones;

    RecyclerView recycleview;

    FirebaseAuth mAuth;
    DatabaseReference database;

   static ListView listView ;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment_view=  inflater.inflate(R.layout.fragment_contact, container, false);

        btn_add_manually = (AppCompatButton) fragment_view.findViewById(R.id.btn_add_manually);
        btn_add_from_contacts = (AppCompatButton) fragment_view.findViewById(R.id.btn_add_from_contacts);

        recycleview = (RecyclerView) fragment_view.findViewById(R.id.recycler_contact);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        names  = new ArrayList<String>();
        numbers = new ArrayList<String>();
        contacts = new ArrayList<Contact>();

        List<Contact> users = new ArrayList<Contact>();



        Contact c1 = new Contact();
        c1.contact_name = "Noman Ikram";
        c1.contact_no = "03485007570XXX";

        Contact c2 = new Contact();
        c2.contact_name = "Raja Waqas";
        c2.contact_no = "0044121771069";


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
        Log.d("TAG",c1+"\n"+c1);


        btn_add_manually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(getActivity());
                View view_f = getLayoutInflater().inflate(R.layout.layout_add_contact_manually, null);
                builder.setView(view_f);

                AppCompatButton btn_reset = (AppCompatButton) view_f.findViewById(R.id.btn_reset_password);

                AppCompatEditText txt_number = (AppCompatEditText) view_f.findViewById(R.id.txt_contactno);
                AppCompatEditText txt_name = (AppCompatEditText) view_f.findViewById(R.id.txt_contact_name);

                alertDialog = builder.create();
                alertDialog.show();

                if(!txt_number.getText().toString().isEmpty() || !txt_name.getText().toString().isEmpty()){

                }
            }
        });

        btn_add_from_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ContactListActivity.class);
                startActivity(intent);


            }
        });

        DatabaseReference user_reference = database.child("users").child("" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        final DatabaseReference contact_ref = database.child("users").child("" + mAuth.getCurrentUser().getUid()).child("Patient").child("Contact");
        Query query = contact_ref;

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for( DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Log.w("", "snapshot: " + dataSnapshot.getChildrenCount());

                        if(snapshot.child("Name").getValue() == null && snapshot.child("Number").getValue() == null)
                        continue;

                        Contact C = new Contact();
                        C.contact_name =(String) snapshot.child("Name").getValue();
                        C.contact_no = (String) snapshot.child("Number").getValue();
////
//                        Log.w("","C Name: "+C.contact_name);
//                        Log.w("","C Number: "+C.contact_no);
//
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

}
