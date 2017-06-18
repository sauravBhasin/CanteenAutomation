package com.example.hp.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import  android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Button;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;


class Custom_Adapter3 extends ArrayAdapter<String>{
    SQLiteDatabase db;
    ArrayList<String> Items=new ArrayList<String>();
    public Custom_Adapter3(Context context, ArrayList<String> ItemList) {
        super(context,R.layout.custom_admin_myorders_layout,ItemList);
        Items=ItemList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater myInflater=LayoutInflater.from(getContext());
        View myView=myInflater.inflate(R.layout.custom_admin_myorders_layout,parent,false);
        String singleItem=getItem(position);
        TextView OrderPlacer=(TextView)myView.findViewById(R.id.LV_OrderPlacer);
        TextView OrderItems=(TextView)myView.findViewById(R.id.LV_OrderItems);
        TextView OrderPrice=(TextView)myView.findViewById(R.id.LV_OrderPrice);
        Button Confirm=(Button)myView.findViewById((R.id.LV_Confirm));
        Confirm.setTag(position);
        final int first_index=singleItem.indexOf("+");
        final int second_index=singleItem.lastIndexOf("+");
        int timeStampIndex=singleItem.indexOf("#");
        final String orderPlacerName=singleItem.substring(0,first_index);
        final String orderItems=singleItem.substring(first_index+1,second_index);
        final String orderPrice="Rs "+singleItem.substring(second_index+1,timeStampIndex);
        final String timeStamp=singleItem.substring(timeStampIndex+1);
        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=(Integer)v.getTag();
                db=getContext().openOrCreateDatabase("PersonDB", Context.MODE_PRIVATE, null);
                //db.execSQL("DELETE FROM myplacedorders WHERE username = " + "\""+orderPlacerName+"\" AND timestamp=" + "\"" + timeStamp + "\";");
                db.execSQL("UPDATE myplacedorders SET status= \'prepared\' WHERE username = " + "\""+orderPlacerName+"\" AND timestamp=" + "\"" + timeStamp + "\";");
                Items.remove(pos);
                notifyDataSetChanged();
            }
        });

        OrderPlacer.setText(orderPlacerName);
        OrderItems.setText(orderItems);
        OrderPrice.setText(orderPrice+","+timeStamp);
        return myView;
    }


}
