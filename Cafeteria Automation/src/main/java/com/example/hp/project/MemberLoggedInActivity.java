package com.example.hp.project;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.database.Cursor;
import android.widget.AdapterView;
import java.util.ArrayList;

public class MemberLoggedInActivity extends AppCompatActivity implements ChangeItemInterface {

    SQLiteDatabase db;
    Cursor c,c1;
    ListAdapter listAdapter,listAdapter1;
    ListView listView,listView1;
    TextView finalPriceView;
    String finalPrice="";
    int FinalPrice=0;
    String userName,userEmail;
    Button ConfirmButton,placedOrderButton;
    final ArrayList<String> orderPasser=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_logged_in);
        Bundle USERNAME = getIntent().getExtras();
        userName = USERNAME.getString("userName");
        userEmail=USERNAME.getString("userEmail");
        Intent intent=new Intent(this,MyService.class);
        intent.putExtra("userName",userName);
        intent.putExtra("userEmail",userEmail);
        startService(intent);
        TextView textView = (TextView) findViewById(R.id.userName);
        textView.setText("Welcome " + userName);
        finalPriceView=(TextView)findViewById(R.id.TotalPriceText);
        ConfirmButton=(Button) findViewById(R.id.OrderConfirmButton);

        final String STATUS="selected";
        ConfirmButton.setOnClickListener(
                new Button.OnClickListener(){
                    public void onClick(View v)
                    {
                        String finalPrice=finalPriceView.getText().toString();

                        if(finalPrice.equals("Rs 0"))
                        {
                            Toast.makeText(getApplicationContext(), "Cannot Accept Blank Order", Toast.LENGTH_LONG).show();
                            return;
                        }
                        else{
                            String sqlQuery="UPDATE myplacedorders SET status=\'placed\', timestamp ="+"\""+getDateTime()+"\" WHERE username =" + "\"" + userName+ "\"  and useremail=" + "\"" + userEmail + "\" and status=" + "\"" + STATUS + "\";";
                            db.execSQL(sqlQuery);
                            Intent intent=getIntent();
                            startActivity(intent);
                        }
                    }
                }
        );
        openDatabase();
        String sqlQuery1="SELECT * FROM myplacedorders WHERE username=" + "\""+userName+"\" and useremail=" + "\"" + userEmail + "\";";
        c1=db.rawQuery(sqlQuery1,null);
        c1.moveToFirst();
        String LeftOverTotalPrice="";
        int leftPrice=0;
        while (!c1.isAfterLast()) {
            String status=c1.getString(c1.getColumnIndex("status"));
            if(status.equals("selected")) {
                String name = c1.getString(c1.getColumnIndex("itemname"));
                String price = c1.getString(c1.getColumnIndex("itemprice"));
                orderPasser.add(name + "+" + price);
                leftPrice=leftPrice+Integer.valueOf(price);
                LeftOverTotalPrice=String.valueOf(leftPrice);
            }
            c1.moveToNext();
        }
        if(leftPrice==0) {
            finalPriceView.setText("Rs 0");
        }
        else {
            finalPriceView.setText("Rs " + LeftOverTotalPrice);
        }
        FinalPrice=leftPrice;
        displayDatabase();
        DisplayMyOrderView();
    }

    public void openDatabase() {
        db = openOrCreateDatabase("PersonDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS myplacedorders (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,itemname VARCHAR,itemprice VARCHAR,username VARCHAR,useremail VARCHAR,status VARCHAR,timestamp TEXT);");
    }

    public void displayDatabase()
    {
        ArrayList<String> Name=new ArrayList<String>();
        String sqlQuery="SELECT * FROM mymenu WHERE 1";
        String name,price,id;
        c=db.rawQuery(sqlQuery,null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            id=c.getString(c.getColumnIndex("id"));
            name=c.getString(c.getColumnIndex("itemname"));
            price=c.getString(c.getColumnIndex("itemprice"));
            Name.add(name+"+"+price);
            c.moveToNext();
        }
        listAdapter=new Custom_Adapter1(this,Name);
        listView=(ListView)findViewById(R.id.myMenyLV1);
        listView.setAdapter(listAdapter);
        listView.setBackgroundColor(Color.TRANSPARENT);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String CurrentItem = String.valueOf(parent.getItemAtPosition(position));
                        int index = CurrentItem.lastIndexOf("+");
                        String CurrentItemName = CurrentItem.substring(0, index);
                        String CurrentItemRate = CurrentItem.substring(index + 4);
                        CurrentItemName.trim();
                        orderPasser.add(CurrentItemName+"+"+CurrentItemRate);
                        int CurrentItemPrice = Integer.valueOf(CurrentItemRate);
                        String currentDisplayedPrice=finalPriceView.getText().toString();
                        currentDisplayedPrice=currentDisplayedPrice.substring(3);
                        FinalPrice=Integer.valueOf(currentDisplayedPrice);
                        FinalPrice = FinalPrice + CurrentItemPrice;
                        finalPrice = "Rs " + Integer.toString(FinalPrice);
                        finalPriceView.setText(finalPrice);
                        String status="selected";
                        String sql_query="INSERT INTO myplacedorders (itemname,itemprice,username,useremail,status,timestamp) VALUES('" + CurrentItemName +  "','" + CurrentItemRate + "','" + userName + "','" + userEmail + "','" + status + "','" + getDateTime() + "');";
                        db.execSQL(sql_query);
                        DisplayMyOrderView();
                    }
                }
        );
    }

    public void DisplayMyOrderView()
    {
        listAdapter1=new Custom_Adapter2(this,orderPasser,this);
        listView1=(ListView)findViewById(R.id.MyListView2);
        listView1.setAdapter(listAdapter1);
    }

    //Interface Method Implementation
    @Override
    public void doChange(String item_price) {
        int price=Integer.valueOf(item_price);
        String current=finalPriceView.getText().toString().trim().substring(3);
        int price_current=Integer.valueOf(current);
        current=String.valueOf(price_current-price);
        finalPriceView.setText("Rs "+current);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_member,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.myMemberView);
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
            case R.id.actionorderhistory:
                Intent intent1=new Intent(this,MyPlacedOrders.class);
                intent1.putExtra("userName",userName);
                intent1.putExtra("userEmail",userEmail);
                startActivity(intent1);
                return true;
        }
        return true;
    }
}
