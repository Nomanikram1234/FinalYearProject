package com.example.nomanikram.epilepsyseizuredetection.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.nomanikram.epilepsyseizuredetection.R;

/**
 * Created by nomanikram on 04/03/2018.
 */

public class view_holder_devices  extends RecyclerView.ViewHolder  {

    TextView name;
    TextView address;

    public view_holder_devices(View itemView) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.txt_device_name);
        address = (TextView) itemView.findViewById(R.id.txt_device_address);

    }
}
