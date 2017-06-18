package com.example.hp.project;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText Email;
    private EditText Password;
    private Button Login;
    private String sql_query="SELECT * FROM userecords WHERE 1";
    private SQLiteDatabase db;
    private Cursor c;
    private String email;
    private String password;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        openDatabase();
        Email=(EditText)findViewById(R.id.LoginEmail);
        Password=(EditText)findViewById(R.id.LoginPassword);
        Login=(Button) findViewById(R.id.SignInButton);
        Login.setOnClickListener(this);
        c = db.rawQuery(sql_query, null);
        c.moveToFirst();
    }

    public void openDatabase(){
        db = openOrCreateDatabase("PersonDB", Context.MODE_PRIVATE, null);
    }

    public void onClick(View view){
        saveSharedPreferences();
        email=Email.getText().toString().trim();
        password=Password.getText().toString().trim();
        String storedEmail,storedPassword;
        int r=0;
        if(email.equals("")||password.equals(""))
        {
            Toast.makeText(getApplicationContext(), "Please Fill in Email and Password", Toast.LENGTH_LONG).show();
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
        if(email.equalsIgnoreCase("ADMIN")&&password.equalsIgnoreCase("ADMIN"))
        {
            r=1;
        }
        else
        {
            while (!c.isAfterLast()) {
                storedEmail = c.getString(c.getColumnIndex("email"));
                storedPassword = c.getString(c.getColumnIndex("password"));
                userName=c.getString(c.getColumnIndex("name"));

                if (email.equalsIgnoreCase(storedEmail) && password.equals(storedPassword)) {
                    //Login Confirmed, Send Intent to Login page with user details
                    r = 2;
                    Toast.makeText(getApplicationContext(), "Login Successfull", Toast.LENGTH_LONG).show();
                    break;
                } else
                    c.moveToNext();
            }
        }
        if(r==0)//Login failed
        {
            Toast.makeText(getApplicationContext(),"Email and Password do not match", Toast.LENGTH_LONG).show();
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
        else if(r==2){
            saveSharedPreferences();
            Intent intent=new Intent(this,MemberLoggedInActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);;
            intent.putExtra("userName",userName);
            intent.putExtra("userEmail",email);
            startActivity(intent);
        }
        else if(r==1)
        {
            saveSharedPreferencesAdmin();
            Intent intent=new Intent(this,AdminLoggedInActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);;
            startActivity(intent);
        }
        db.close();
    }

    public void saveSharedPreferences() {
        SharedPreferences sharedPreferences=getSharedPreferences("userLoginInfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putString("userEmail",email);
        editor.putString("userPassword",password);
        editor.putString("userName",userName);
        editor.apply();
    }

    public void saveSharedPreferencesAdmin()
    {
        SharedPreferences sharedPreferences=getSharedPreferences("adminLoginInfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("adminEmail",email);
        editor.putString("adminPass",password);
        editor.apply();
    }
}
