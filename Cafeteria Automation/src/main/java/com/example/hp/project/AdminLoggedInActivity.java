package com.example.hp.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Button;
import android.database.Cursor;
import java.util.ArrayList;

public class AdminLoggedInActivity extends AppCompatActivity implements View.OnClickListener{
    EditText itemName;
    EditText itemPrice;
    SQLiteDatabase db;
    Button ADD;
    Cursor c;
    ListAdapter listAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_logged_in);
        ADD=(Button)findViewById(R.id.ADD);
        ADD.setOnClickListener(this);
        openDatabase();
        displayDatabase();//Shows saved Menu
    }

    public void openDatabase()
    {
        db=openOrCreateDatabase("PersonDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS mymenu(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,itemname VARCHAR,itemprice VARCHAR);");
    }

    public void yourOrdersClicked(View view)
    {
        Intent intent=new Intent(this,AdminLoggedIn1Activity.class);
        startActivity(intent);
    }

    public void onClick(View view)
    {
        itemName=(EditText)findViewById(R.id.menuItem);
        itemPrice=(EditText)findViewById(R.id.itemPrice);
        String ItemName=itemName.getText().toString().trim();
        String ItemPrice=itemPrice.getText().toString().trim();
        if(ItemName.equals("")||ItemPrice.equals("")) {
            Toast.makeText(getApplicationContext(), "Item Name and Price Fields Cannot be Left Blank", Toast.LENGTH_LONG).show();
            return;
        }
        ItemPrice="Rs "+ItemPrice;
        String sql_query="INSERT INTO mymenu (itemname,itemprice) VALUES('" + ItemName +  "','" + ItemPrice + "');";
        db.execSQL(sql_query);
        Toast.makeText(getApplicationContext(), "Saved Successfully", Toast.LENGTH_LONG).show();
        itemName.setText("");//set text to blank after user has added the entry
        itemPrice.setText("");
        displayDatabase();
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
        listAdapter=new Custom_Adapter(this,Name);
        listView=(ListView)findViewById(R.id.MyMenuLV);
        listView.setAdapter(listAdapter);
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
            case R.id.actionmanage://Likhna hae abi
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
