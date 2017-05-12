package com.arkay.rajasthanquiz.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.arkay.rajasthanquiz.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by INDIA on 07-01-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    Bitmap bitmap, bitmapLohana;

    String msg_type ;


    public static final String CURRENT_AFFAIR = "1";
    public static final String GK_NEWS = "2";
    public static final String FAMOUS_PERSON = "3";
    public static final String FAMOUS_PLACE = "4";



    public static final String UPC = "5";
    int product_id = 0;
    Intent intent;
    String id,title;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Data: " + remoteMessage.getData());

        msg_type = remoteMessage.getData().get("msg_type");
        id = remoteMessage.getData().get("id");
       // title = remoteMessage.getData().get("title");

        System.out.println("msg : "+msg_type);
//        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
//
//        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            title = remoteMessage.getNotification().getBody();
        }
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Data: " + remoteMessage.getData());

        sendNotification(title);
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody) {


        switch (msg_type) {

            case CURRENT_AFFAIR:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("FROM_PUSH", true);
                intent.putExtra("MSG_TYPE", msg_type);
                intent.putExtra("ID", id);

                Log.i("INFO","Msg Type: "+msg_type);
                Log.i("INFO","Msg ID: "+id);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent1 = PendingIntent.getActivity(this, 0, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                Uri defaultSoundUri1 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder1 = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setLargeIcon(bitmapLohana)
                        .setContentTitle(getResources().getString(R.string.str_btn7))
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri1)
                        .setContentIntent(pendingIntent1);

                NotificationManager notificationManager1 =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager1.notify(0, notificationBuilder1.build());
                break;

            default:
                break;
        }
    }
}