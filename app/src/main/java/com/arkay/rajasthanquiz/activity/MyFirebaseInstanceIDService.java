package com.arkay.rajasthanquiz.activity;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by INDIA on 07-01-2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    SharedPreferences settings;
    int uid = 0;


    @Override
    public void onTokenRefresh() {
        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putString(MainActivity.TOKEN, "" + refreshedToken);
        prefEditor.commit();
        //sendRegistrationToServer(refreshedToken);

    }


}