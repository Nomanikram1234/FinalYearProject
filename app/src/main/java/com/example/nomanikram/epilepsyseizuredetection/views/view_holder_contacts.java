package com.example.nomanikram.epilepsyseizuredetection.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nomanikram.epilepsyseizuredetection.R;

/**
 * Created by nomanikram on 09/02/2018.
 */

public class view_holder_contacts extends RecyclerView.ViewHolder {


    ImageView img;
    TextView name;
    TextView contact;

    public view_holder_contacts(View itemView) {

        super(itemView);

       img = (ImageView) itemView.findViewById(R.id.imageview_id);
        name = (TextView) itemView.findViewById(R.id.txt_name);
        contact = (TextView) itemView.findViewById(R.id.txt_contactno);
    }
}
