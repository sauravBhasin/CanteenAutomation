package com.example.hp.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent_main=getIntent();
        //Auto login
        SharedPreferences sharedPreferences=getSharedPreferences("userLoginInfo", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences1=getSharedPreferences("adminLoginInfo", Context.MODE_PRIVATE);
        String userEmail=sharedPreferences.getString("userEmail","");
        String userPassword=sharedPreferences.getString("userPassword","");
        String adminEmail=sharedPreferences1.getString("adminEmail","");
        String adminPassword=sharedPreferences1.getString("adminPassword","");
        if(adminEmail.equalsIgnoreCase("admin")){
            Intent intent=new Intent(this,AdminLoggedInActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);;
            startActivity(intent);
        }
        else if(!userEmail.equals("")&&!userPassword.equals("")) {
            if(!intent_main.hasExtra("FromNotification")) {

                String userName = sharedPreferences.getString("userName", "");
                Intent intent = new Intent(this, MemberLoggedInActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userName", userName);
                intent.putExtra("userEmail", userEmail);
                startActivity(intent);
            }
            else
            {
                String userName = sharedPreferences.getString("userName", "");
                Intent intent = new Intent(this, MyPlacedOrders.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("userName", userName);
                intent.putExtra("userEmail", userEmail);
                startActivity(intent);
            }
        }
        else if(intent_main.hasExtra("FromNotification")&&userEmail.equals(""))
        {
            Toast.makeText(getApplicationContext(),"You have Logged Out of your account. Please login to continue.", Toast.LENGTH_LONG).show();
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
        else {
            setContentView(R.layout.activity_main);
        }
    }
    public void Register(View view){
        Intent intent=new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
    public void Signin(View view){
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
}
