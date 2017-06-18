package com.example.hp.project;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextPhone;
    private EditText editTextPassword;
    private EditText editTextPassword1;
    TextView errorCode;
    private Button Signup;
    String name,email,phone,password,password1;
    Cursor cursor;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        createDatabase();
        editTextName = (EditText) findViewById(R.id.Name);
        editTextEmail = (EditText) findViewById(R.id.Email);
        editTextPhone = (EditText) findViewById(R.id.Phone);
        editTextPassword = (EditText) findViewById(R.id.Password);
        editTextPassword1 = (EditText) findViewById(R.id.RePassword);
        errorCode=(TextView) findViewById(R.id.errorCode);
        Signup = (Button) findViewById(R.id.SignUp);
        Signup.setOnClickListener(this);
    }


    protected void createDatabase() {
        db = openOrCreateDatabase("PersonDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS userecords(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,name VARCHAR,email VARCHAR,phone VARCHAR,password VARCHAR);");
    }

    protected boolean insertIntoDB() {
        if (name.equals("") || email.equals("") || phone.equals("") || password.equals("") || password1.equals("")) {
            Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
            return false;

        } else if (!password.equals(password1)) {
            Toast.makeText(getApplicationContext(), "Password Field Do Not Match", Toast.LENGTH_LONG).show();
            return false;

        } else {


            String query = "INSERT INTO userecords (name,email,phone,password) VALUES('" + name + "','" + email + "','" + phone + "','" + password + "');";
            db.execSQL(query);
            Toast.makeText(getApplicationContext(), "Saved Successfully", Toast.LENGTH_LONG).show();
            return true;
        }
    }


    @Override
    public void onClick(View v) {
        if (v == Signup) {
            name = editTextName.getText().toString().trim();
            email = editTextEmail.getText().toString().trim();
            phone = editTextPhone.getText().toString().trim();
            password = editTextPassword.getText().toString().trim();
            password1 = editTextPassword1.getText().toString().trim();
            boolean status = checkForEmail();
            boolean status2=checkForPhone();
            if (status == true&&status2==true){
                boolean r = insertIntoDB();
                if (r == true) {
                    Intent intent = new Intent(this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
            else if(status==false&&status2==true)
            {
                errorCode.setText("This Email has already been Registered");
            }
            else if(status==true&&status2==false)
            {
                errorCode.setText("This Phone has already been Registered");
            }
            else if(status==false&&status2==false)
            {
                errorCode.setText("This Email and Phone have already been Registered");
            }

        }
    }
    public boolean checkForEmail()
    {
        String sqlQuery="SELECT * FROM userecords WHERE 1";
        cursor=db.rawQuery(sqlQuery,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            String storedEmail=cursor.getString(cursor.getColumnIndex("email"));
            if(storedEmail.equals(email))
            {
                return false;
            }
            cursor.moveToNext();
        }
        return true;
    }

    public boolean checkForPhone()
    {

        String sqlQuery="SELECT * FROM userecords WHERE 1";
        cursor=db.rawQuery(sqlQuery,null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            String storedPhone=cursor.getString(cursor.getColumnIndex("phone"));
            if(storedPhone.equals(phone))
            {
                return false;
            }
            cursor.moveToNext();
        }
        return true;
    }
}


