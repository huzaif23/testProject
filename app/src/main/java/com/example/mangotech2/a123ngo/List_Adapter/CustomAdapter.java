package com.example.mangotech2.a123ngo.List_Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mangotech2.a123ngo.Model.TripModel;
import com.example.mangotech2.a123ngo.R;

import java.util.ArrayList;

/**
 * Created by Sameer on 12/8/2017.
 */

public class CustomAdapter extends ArrayAdapter<TripModel> implements View.OnClickListener{
    private ArrayList<TripModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtstatus;
        TextView txtdate;
        TextView txtprice;
        TextView txtpickupaddress;
        TextView txtdropoffaddress;
    }

    public CustomAdapter(ArrayList<TripModel> data, Context context) {
        super(context, R.layout.tripfragment, data);
        this.dataSet = data;
        this.mContext=context;

    }



    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        TripModel dataModel=(TripModel) object;
/*
        switch (v.getId())
        {
            case R.id.item_info:
                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }
  */
    }




    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TripModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.tripfragment, parent, false);
            viewHolder.txtdate = (TextView) convertView.findViewById(R.id.tvusertripdate);
            viewHolder.txtstatus = (TextView) convertView.findViewById(R.id.tvusertripstatus);
            viewHolder.txtprice = (TextView) convertView.findViewById(R.id.tvusertripprice);
            viewHolder.txtpickupaddress = (TextView) convertView.findViewById(R.id.tvusertrippickuploc);
            viewHolder.txtdropoffaddress = (TextView) convertView.findViewById(R.id.tvusertripdropoffloc);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtstatus.setText(dataModel.getstatus());
        viewHolder.txtprice.setText(dataModel.getprice());
        viewHolder.txtdate.setText(dataModel.getdate());
        viewHolder.txtpickupaddress.setText(dataModel.getpickupaddress());
        viewHolder.txtdropoffaddress.setText(dataModel.getdropoffaddress());
  //      viewHolder.info.setOnClickListener(this);
   //     viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }


}