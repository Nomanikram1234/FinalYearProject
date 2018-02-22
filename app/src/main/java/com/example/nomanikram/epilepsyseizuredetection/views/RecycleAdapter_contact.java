package com.example.nomanikram.epilepsyseizuredetection.views;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nomanikram.epilepsyseizuredetection.R;
import com.example.nomanikram.epilepsyseizuredetection.models.Contact;
import com.example.nomanikram.epilepsyseizuredetection.models.User;

import java.util.List;

/**
 * Created by nomanikram on 09/02/2018.
 */

public class RecycleAdapter_contact extends RecyclerView.Adapter<view_holder_contacts>{

    private List<Contact> users;

    public RecycleAdapter_contact(List<Contact> users){
          this.users = users;
    }

    @Override
    public view_holder_contacts onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_contact,parent,false);
        return new view_holder_contacts(view);
    }

    @Override
    public void onBindViewHolder(view_holder_contacts holder, int position) {
        Contact sampleuser = users.get(position);

        holder.name.setText(sampleuser.contact_name);
        holder.contact.setText(sampleuser.contact_no);
      //  holder.img.setImageResource(1);
    }

    @Override
    public int getItemCount() {

        return users.size();
    }
}
