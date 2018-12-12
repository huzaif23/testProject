package com.example.mangotech2.a123ngo.CustomListAdapter;

/**
 * Created by Sameer on 12/5/2017.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mangotech2.a123ngo.Model.TripModel;
import com.example.mangotech2.a123ngo.MyTrip;
import com.example.mangotech2.a123ngo.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter{
    ArrayList<TripModel> result;
    Context context;
    int [] imageId;
    private static LayoutInflater inflater=null;
    public CustomAdapter(MyTrip myTrip, String prgmNameList, int prgmImages) {
        // TODO Auto-generated constructor stub
    /*    result=prgmNameList;
        context=myTrip;
        imageId=prgmImages;
      */
    inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public CustomAdapter(ArrayList<TripModel> dataModels, MyTrip myTrip) {
        result=new ArrayList<TripModel>(1);
        result=dataModels;
        context=myTrip;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.tripfragment, null);
     /*   holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
        holder.tv.setText(result[position]);
        holder.img.setImageResource(imageId[position]);
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
            }
        });*/
        return rowView;
    }

}