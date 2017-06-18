package com.example.hp.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class AdminLoggedIn1Activity extends AppCompatActivity {
    SQLiteDatabase db;
    Cursor c;
    ListView listView;
    ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_logged_in1);
        openDatabase();
        displayDatabase();
    }

    public void yourMenuClicked(View view){
        Intent intent=new Intent(this,AdminLoggedInActivity.class);
        startActivity(intent);
    }

    public void openDatabase()
    {
        db=openOrCreateDatabase("PersonDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS myplacedorders (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,itemname VARCHAR,itemprice VARCHAR,username VARCHAR,useremail VARCHAR,status VARCHAR,timestamp TEXT);");
    }

    public void displayDatabase() {
        ArrayList<String> Name = new ArrayList<String>();
        String sqlQuery = "SELECT * FROM myplacedorders WHERE status=\'placed\'";
        String orderPlacerName, orderPrice, orderItems = "",timeStamp="";
        int OrderPrice = 0;
        c = db.rawQuery(sqlQuery, null);
        if (!c.isAfterLast()) {
            c.moveToFirst();
            String nameGroupIdentifier = c.getString(c.getColumnIndex("username"));
            String timeStampIdentifier= c.getString(c.getColumnIndex("timestamp"));
            while (true) {
                orderPlacerName = c.getString(c.getColumnIndex("username"));
                timeStamp=c.getString(c.getColumnIndex("timestamp"));
                if (nameGroupIdentifier.equals(orderPlacerName)&&timeStampIdentifier.equals(timeStamp)) {
                    orderItems = orderItems + "," + c.getString(c.getColumnIndex("itemname"));
                    orderPrice = c.getString(c.getColumnIndex("itemprice"));
                    OrderPrice = OrderPrice + Integer.valueOf(orderPrice);
                    timeStamp=c.getString(c.getColumnIndex("timestamp"));
                    c.moveToNext();
                } else {
                    Name.add(nameGroupIdentifier + "+" + orderItems.substring(1) + "+" + OrderPrice + "#" + timeStampIdentifier);
                    nameGroupIdentifier = orderPlacerName;
                    timeStampIdentifier=timeStamp;
                    orderItems = "";
                    OrderPrice = 0;
                    orderItems = orderItems + "," + c.getString(c.getColumnIndex("itemname"));
                    orderPrice = c.getString(c.getColumnIndex("itemprice"));
                    OrderPrice = OrderPrice + Integer.valueOf(orderPrice);
                    timeStamp=c.getString(c.getColumnIndex("timestamp"));
                    c.moveToNext();
                }
                if (c.isAfterLast()) {
                    Name.add(nameGroupIdentifier + "+" + orderItems.substring(1) + "+" + OrderPrice + "#" + timeStampIdentifier);
                    break;
                }
            }
            listAdapter = new Custom_Adapter3(this, Name);
            listView = (ListView) findViewById(R.id.MyOrdersListView);
            listView.setAdapter(listAdapter);
        }
        else
        {
            TextView OrderTextView=(TextView)findViewById(R.id.YourOrdersTextView);
            OrderTextView.setText("No Orders have been place till now");
        }
    }

    //action bar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.mymainview);
        switch (item.getItemId())
        {
            case R.id.actionpassword:
                return true;
            case R.id.actionsignout:
                SharedPreferences sharedPreferences=getSharedPreferences("adminLoginInfo",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent=new Intent(this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
        }
        return true;
    }
}
