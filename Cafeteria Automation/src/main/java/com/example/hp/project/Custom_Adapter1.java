package com.example.hp.project;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import  android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
public class Custom_Adapter1 extends ArrayAdapter<String> {

    SQLiteDatabase db;
    ArrayList<String> Items = new ArrayList<String>();

    public Custom_Adapter1(Context context, ArrayList<String> ItemList) {
        super(context, R.layout.custom_layout_member, ItemList);
        Items = ItemList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View myView = myInflater.inflate(R.layout.custom_layout_member, parent, false);
        String singleItem = getItem(position);
        TextView ItemName = (TextView) myView.findViewById(R.id.LV_ItemName1);
        TextView ItemPrice = (TextView) myView.findViewById(R.id.LV_Price1);
        int index=singleItem.lastIndexOf("+");
        final String itemName=singleItem.substring(0,index);
        String itemPrice=singleItem.substring(index+1);
        ItemName.setText(itemName);
        ItemPrice.setText(itemPrice);
        return myView;
    }

}
