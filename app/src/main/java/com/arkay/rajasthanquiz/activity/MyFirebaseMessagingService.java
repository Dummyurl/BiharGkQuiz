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
//            case CURRENT_AFFAIR:
//                intent = new Intent(getApplicationContext(), YoungTalentActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0, intent,
//                        PendingIntent.FLAG_ONE_SHOT);
//
//                Uri defaultSoundUri2 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.logo_25)
//                        .setLargeIcon(bitmapLohana)
//                        .setContentTitle(getResources().getString(R.string.str_btn1))
//                        .setContentText(messageBody)
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri2)
//                        .setContentIntent(pendingIntent2);
//
//                NotificationManager notificationManager2 =
//                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//                notificationManager2.notify(0, notificationBuilder.build());
//                break;
//
//            case GK_NEWS:
//                intent = new Intent(getApplicationContext(), SabashAchievementActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                PendingIntent pendingIntent3 = PendingIntent.getActivity(this, 0, intent,
//                        PendingIntent.FLAG_ONE_SHOT);
//
//                Uri defaultSoundUri3 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                NotificationCompat.Builder notificationBuilder3 = new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.logo_25)
//                        .setLargeIcon(bitmapLohana)
//                        .setContentTitle(getResources().getString(R.string.str_btn2))
//                        .setContentText(messageBody)
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri3)
//                        .setContentIntent(pendingIntent3);
//
//                NotificationManager notificationManager3 =
//                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//                notificationManager3.notify(0, notificationBuilder3.build());
//                break;
//
//            case FAMOUS_PERSON:
//                intent = new Intent(getApplicationContext(), SamajPropertyActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                PendingIntent pendingIntent4 = PendingIntent.getActivity(this, 0, intent,
//                        PendingIntent.FLAG_ONE_SHOT);
//
//                Uri defaultSoundUri4 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                NotificationCompat.Builder notificationBuilder4 = new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.logo_25)
//                        .setLargeIcon(bitmapLohana)
//                        .setContentTitle(getResources().getString(R.string.str_btn3))
//                        .setContentText(messageBody)
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri4)
//                        .setContentIntent(pendingIntent4);
//
//                NotificationManager notificationManager4 =
//                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//                notificationManager4.notify(0, notificationBuilder4.build());
//                break;
//
//            case FAMOUS_PLACE:
//                intent = new Intent(getApplicationContext(), TrusteeInfoActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                PendingIntent pendingIntent5 = PendingIntent.getActivity(this, 0, intent,
//                        PendingIntent.FLAG_ONE_SHOT);
//
//                Uri defaultSoundUri5 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                NotificationCompat.Builder notificationBuilder5 = new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.logo_25)
//                        .setLargeIcon(bitmapLohana)
//                        .setContentTitle(getResources().getString(R.string.str_btn4))
//                        .setContentText(messageBody)
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri5)
//                        .setContentIntent(pendingIntent5);
//
//                NotificationManager notificationManager5 =
//                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//                notificationManager5.notify(0, notificationBuilder5.build());
//                break;
//
//            case PRATHNA_SABHA:
//                intent = new Intent(getApplicationContext(), PrathnaSabhaActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                PendingIntent pendingIntent6 = PendingIntent.getActivity(this, 0, intent,
//                        PendingIntent.FLAG_ONE_SHOT);
//
//                Uri defaultSoundUri6 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                NotificationCompat.Builder notificationBuilder6 = new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.logo_25)
//                        .setLargeIcon(bitmapLohana)
//                        .setContentTitle(getResources().getString(R.string.str_btn5))
//                        .setContentText(messageBody)
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri6)
//                        .setContentIntent(pendingIntent6);
//
//                NotificationManager notificationManager6 =
//                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//                notificationManager6.notify(0, notificationBuilder6.build());
//                break;
//
//            case GALLERY:
//                intent = new Intent(getApplicationContext(), GallaryPhotoActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                PendingIntent pendingIntent7 = PendingIntent.getActivity(this, 0, intent,
//                        PendingIntent.FLAG_ONE_SHOT);
//
//                Uri defaultSoundUri7 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                NotificationCompat.Builder notificationBuilder7 = new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.logo_25)
//                        .setLargeIcon(bitmapLohana)
//                        .setContentTitle(getResources().getString(R.string.str_btn8))
//                        .setContentText(messageBody)
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri7)
//                        .setContentIntent(pendingIntent7);
//
//                NotificationManager notificationManager7 =
//                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//                notificationManager7.notify(0, notificationBuilder7.build());
//                break;
//
//            case BIRTHDAY:
//                intent = new Intent(getApplicationContext(), BirthdayActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                PendingIntent pendingIntent8 = PendingIntent.getActivity(this, 0, intent,
//                        PendingIntent.FLAG_ONE_SHOT);
//
//                Uri defaultSoundUri8 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                NotificationCompat.Builder notificationBuilder8 = new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.logo_25)
//                        .setLargeIcon(bitmapLohana)
//                        .setContentTitle(getResources().getString(R.string.str_btn11))
//                        .setContentText(messageBody)
//                        .setAutoCancel(true)
//                        .setSound(defaultSoundUri8)
//                        .setContentIntent(pendingIntent8);
//
//                NotificationManager notificationManager8 =
//                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//                notificationManager8.notify(0, notificationBuilder8.build());
//                break;


            default:
                break;
        }
    }
}