package com.example.nomanikram.epilepsyseizuredetection.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.nomanikram.epilepsyseizuredetection.R;

/**
 * Created by nomanikram on 12/02/2018.
 */

public class view_holder_record extends RecyclerView.ViewHolder{

    TextView pulse;
    TextView temp;
    TextView activity_status;
    TextView date;
    TextView time;

    public view_holder_record(View itemView) {
        super(itemView);

        pulse = (TextView) itemView.findViewById(R.id.txt_heartbeat);
        temp = (TextView) itemView.findViewById(R.id.txt_body_temp);
        activity_status = (TextView) itemView.findViewById(R.id.txt_activity_status);

        date = (TextView) itemView.findViewById(R.id.txt_date);
        time = (TextView) itemView.findViewById(R.id.txt_time);


    }
}
