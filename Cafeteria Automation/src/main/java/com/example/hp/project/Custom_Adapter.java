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


class Custom_Adapter extends ArrayAdapter<String>{
    SQLiteDatabase db;

ArrayList<String> Items=new ArrayList<String>();
    public Custom_Adapter(Context context, ArrayList<String> ItemList) {
        super(context,R.layout.cutom_layout,ItemList);
        Items=ItemList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater myInflater=LayoutInflater.from(getContext());
        View myView=myInflater.inflate(R.layout.cutom_layout,parent,false);

        String singleItem=getItem(position);
        TextView ItemName=(TextView)myView.findViewById(R.id.LV_ItemName);
        TextView ItemPrice=(TextView)myView.findViewById(R.id.LV_Price);
        Button Delete=(Button)myView.findViewById((R.id.LV_Delete));
        Delete.setTag(position);
        int index=singleItem.lastIndexOf("+");
        final String itemName=singleItem.substring(0,index);
        String itemPrice=singleItem.substring(index+1);
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos=(Integer)v.getTag();
                db=getContext().openOrCreateDatabase("PersonDB", Context.MODE_PRIVATE, null);
                db.execSQL("DELETE FROM mymenu WHERE itemname =" + "\""+itemName+"\";");
                Items.remove(pos);

                notifyDataSetChanged();

            }
        });

        ItemName.setText(itemName);
        ItemPrice.setText(itemPrice);
        return myView;
    }


}
