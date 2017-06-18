package com.example.hp.project;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.app.NotificationManager;
import android.app.PendingIntent;

public class MyService extends Service {
    private static final String TAG="com.example.hp.project";
    SQLiteDatabase db;
    Cursor cursor;
    int NotificationId=0;
    String userName,userEmail;
    NotificationCompat.Builder notification;
    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         userName=intent.getStringExtra("userName");
         userEmail=intent.getStringExtra("userEmail");
        db=openOrCreateDatabase("PersonDB", Context.MODE_PRIVATE, null);
        String status="prepared";
        final String sqlQuery="SELECT * FROM myplacedorders WHERE username=" + "\""+userName+"\" and useremail=" + "\"" + userEmail + "\" and status=" + "\"" + status +"\" ;";
        notification =new NotificationCompat.Builder(getApplicationContext());
        notification.setAutoCancel(true);
        Log.i(TAG,"Service started "+ userName+" "+userEmail);
        Runnable r=new Runnable() {
            String timeStampIdent="",timeStamp,status;
            @Override
            public void run() {
                cursor=db.rawQuery(sqlQuery,null);
                if(!cursor.isAfterLast()) {
                    cursor.moveToFirst();
                    String OrderPrice = "";
                    int orderprice = 0;
                    timeStampIdent = cursor.getString(cursor.getColumnIndex("timestamp"));
                    while (!cursor.isAfterLast()) {
                        String timestamp=cursor.getString(cursor.getColumnIndex("timestamp"));
                        if(timestamp.equals(timeStampIdent)){
                            orderprice=orderprice+Integer.valueOf(cursor.getString(cursor.getColumnIndex("itemprice")));
                            OrderPrice=Integer.toString(orderprice);
                            cursor.moveToNext();
                        }
                        else {
                            BuildNotification(OrderPrice);

                            timeStampIdent=timestamp;
                            orderprice=0;
                            orderprice=orderprice+Integer.valueOf(cursor.getString(cursor.getColumnIndex("itemprice")));
                            OrderPrice=Integer.toString(orderprice);
                            cursor.moveToNext();

                        }
                        if(cursor.isAfterLast())
                        {
                            BuildNotification(OrderPrice);
                            break;
                        }
                    }
                    String Status="prepared";
                    String sqlQuery="UPDATE myplacedorders SET status=\'prepared and serviced\' WHERE username =" + "\"" + userName+ "\"  and useremail=" + "\"" + userEmail + "\" and status=" + "\"" + Status + "\";";
                    db.execSQL(sqlQuery);
                }
            }
        };



        Thread thread=new Thread(r);
        thread.start();

        return START_NOT_STICKY;
    }
    public void BuildNotification(String orderPrice)
    {
        notification.setSmallIcon(R.drawable.mainpic);
        notification.setTicker("this is a ticker");
        notification.setWhen(System.currentTimeMillis());
        notification.setContentTitle("Canteen Automation System");
        notification.setContentText("Hello "+ userName + " Your order worth Rs "+orderPrice+" has been prepared");
        Intent intent1 = new Intent(this, MainActivity.class);
        intent1.putExtra("FromNotification","yes");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(++NotificationId, notification.build());

    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"Service Stopped");

    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }
}
