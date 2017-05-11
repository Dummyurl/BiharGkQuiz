package com.arkay.rajsthanquiz.application;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import com.arkay.rajsthanquiz.handler.DatabaseHelper;


/**
 * Created by Ishan4452 on 5/12/2015.
 */

public class MainApplication extends Application
{
    public static final String TAG = MainApplication.class
            .getSimpleName();


    public static final String PREFS_NAME = "preferences";
    public static DatabaseHelper databaseHelper;


    private RequestQueue mRequestQueue;
    private static MainApplication mInstance;


    public static synchronized MainApplication getInstance() {
        return mInstance;
    }


    public static DatabaseHelper getInstance(Context context) {
        if (databaseHelper!=null)
            return databaseHelper;

         databaseHelper=new DatabaseHelper(context);
            return databaseHelper;
    }
    @Override
    public void onCreate() {
        super.onCreate();

         getInstance(this);
        mInstance = this;

    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);

    }

    public int getRequestionSequanceNo(){
        return getRequestQueue().getSequenceNumber();
    }
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
