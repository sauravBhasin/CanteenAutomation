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


class Custom_Adapter4 extends ArrayAdapter<String>{
    SQLiteDatabase db;
    ArrayList<String> Items=new ArrayList<String>();
    public Custom_Adapter4(Context context, ArrayList<String> ItemList) {
        super(context,R.layout.custom_layout_4,ItemList);
        Items=ItemList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater myInflater=LayoutInflater.from(getContext());
        View myView=myInflater.inflate(R.layout.custom_layout_4,parent,false);
        String singleItem=getItem(position);
        TextView OrderStatus=(TextView)myView.findViewById(R.id.placedOrderStatus);
        TextView OrderItems=(TextView)myView.findViewById(R.id.placedOrderItems);
        TextView OrderPrice=(TextView)myView.findViewById(R.id.placedOrderPrice);

        final int first_index=singleItem.indexOf("+");
        final int second_index=singleItem.lastIndexOf("+");

        final String orderItems=singleItem.substring(0,first_index);
        final String orderPrice=singleItem.substring(first_index+1,second_index);
        final String orderStatus=singleItem.substring(second_index+1);
        OrderItems.setText(orderItems);
        OrderPrice.setText("Rs "+orderPrice);
        OrderStatus.setText(orderStatus);
        return myView;
    }


}
