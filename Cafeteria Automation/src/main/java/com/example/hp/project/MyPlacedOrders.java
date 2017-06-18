package com.example.hp.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class MyPlacedOrders extends AppCompatActivity {

    SQLiteDatabase db;
    ListAdapter listAdapter;
    ListView listView;
    String userName,userEmail;
    TextView username;
    Cursor c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_placed_orders);
        Bundle USERNAME = getIntent().getExtras();
        userName = USERNAME.getString("userName");
        userEmail=USERNAME.getString("userEmail");
        username=(TextView)findViewById(R.id.userNameTV);
        username.setText("Welcome "+ userName+" your order history:");
        db = openOrCreateDatabase("PersonDB", Context.MODE_PRIVATE, null);
        displayDatabase();
    }

    public void displayDatabase()
    {
        ArrayList<String> Name=new ArrayList<String>();
        String sqlQuery="SELECT * FROM myplacedorders WHERE username=" + "\""+userName+"\" and useremail=" + "\"" + userEmail + "\";";
        String Items="",price,timeStamp,status="";
        int Price=0;
        String setStatus="";
        c=db.rawQuery(sqlQuery,null);
        c.moveToFirst();
        if(!c.isAfterLast()) {
            String timeStampIdentifier = c.getString(c.getColumnIndex("timestamp"));
            while (true) {
                timeStamp = c.getString(c.getColumnIndex("timestamp"));
                if (timeStampIdentifier.equals(timeStamp)) {
                    Items = Items + "," + c.getString(c.getColumnIndex("itemname"));
                    price = c.getString(c.getColumnIndex("itemprice"));
                    Price = Price + Integer.valueOf(price);
                    status=c.getString(c.getColumnIndex("status"));
                    c.moveToNext();
                } else {
                    if(status.equals("prepared and serviced"))
                        status="Order Ready";
                    else if(status.equals("placed"))
                        status="Processing";
                    Name.add(Items.substring(1) + "+" + Price + "+" + status);
                    timeStampIdentifier = timeStamp;
                    Items = "";
                    Price = 0;
                    Items = Items + "," + c.getString(c.getColumnIndex("itemname"));
                    price = c.getString(c.getColumnIndex("itemprice"));
                    Price = Price + Integer.valueOf(price);
                    status=c.getString(c.getColumnIndex("status"));
                    c.moveToNext();

                }
                if(c.isAfterLast())
                {
                    if(status.equals("prepared and serviced"))
                        status="Order Ready";
                    else if(status.equals("placed"))
                        status="Processing";
                    Name.add(Items.substring(1) + "+" + Price + "+" + status);
                    break;
                }

            }

            listAdapter = new Custom_Adapter4(this, Name);
            listView = (ListView) findViewById(R.id.myplacedordersLV);
            listView.setAdapter(listAdapter);
        }
        else{
            // No Orders have been placed
            username.setText("Welcome "+ userName+"\nNo orders have been by you:");
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_member_second,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.actionpassword:
                return true;
            case R.id.actionsignout:
                SharedPreferences sharedPreferences=getSharedPreferences("userLoginInfo",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Intent intent=new Intent(this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            case R.id.actionhome:
                Intent intent1=new Intent(this,MemberLoggedInActivity.class);
                intent1.putExtra("userName",userName);
                intent1.putExtra("userEmail",userEmail);
                startActivity(intent1);
        }
        return true;
    }
}


