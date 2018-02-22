package com.example.nomanikram.epilepsyseizuredetection;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nomanikram.epilepsyseizuredetection.models.Contact;
import com.example.nomanikram.epilepsyseizuredetection.views.RecycleAdapter_contact;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends Fragment {

    RecyclerView recycleview;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_contact, container, false);

        recycleview = (RecyclerView) view.findViewById(R.id.recycler_contact);

        List<Contact> users = new ArrayList<Contact>();

        Contact c1 = new Contact();
        c1.contact_name = "Noman Ikram";
        c1.contact_no = "03485007570XXX";

        Contact c2 = new Contact();
        c2.contact_name = "Raja Waqas";
        c2.contact_no = "0044121771069";

        users.add(c1);
        users.add(c2);

        Log.d("TAG","Size: "+users.size());
        LinearLayoutManager linear = new LinearLayoutManager(this.getContext());
        recycleview.setLayoutManager(linear);

        recycleview.setHasFixedSize(true);
        recycleview.setAdapter(new RecycleAdapter_contact(users));
        Log.d("TAG",c1+"\n"+c1);
        return view;
    }

}
