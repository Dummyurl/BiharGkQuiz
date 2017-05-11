package com.arkay.rajasthanquiz.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.arkay.rajasthanquiz.R;
import com.arkay.rajasthanquiz.adapter.CurrentAffairAdapter;
import com.arkay.rajasthanquiz.application.MainApplication;
import com.arkay.rajasthanquiz.beans.CurrentAffairLevel;
import com.arkay.rajasthanquiz.customviews.SwipeRefreshLayoutBottom;
import com.arkay.rajasthanquiz.handler.CurrentAffairQuestionsDAO;
import com.arkay.rajasthanquiz.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by India on 13-04-2017.
 */

public class CurrentAffairFragment extends Fragment implements SwipeRefreshLayoutBottom.OnRefreshListener{


    ListView listview;
    CurrentAffairAdapter currentAffairAdapter;
    private CurrentAffairLevel level;
    TextView txtAppTitle;
    private SwipeRefreshLayoutBottom swipeRefreshLayout;
    Typeface tp;
    private Handler mHandler;
    Listener mListener = null;
    private List<CurrentAffairLevel> dataArrayList;
    private CurrentAffairQuestionsDAO currentAffairQuestionsDAO;
    public static String TAG = MainApplication.class.getSimpleName();
    private int offSet = 1;
    AdapterView.OnItemClickListener listClickListener;
    //private ProgressDialog progress;

    public static CurrentAffairFragment newInstance(Bundle bundle) {
        CurrentAffairFragment fragment = new CurrentAffairFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_current_level, container, false);
    }

    public void setListener(Listener l) {
        mListener = l;
    }


    public interface Listener {

        void playCurrentAffairQuiz(int currentAffairLevelID);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.detail_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
         }

        level = new CurrentAffairLevel();
        mHandler = new Handler();
        tp = Typeface.createFromAsset(getActivity().getAssets(),
                "MarkoOne-Regular.ttf");
        txtAppTitle = (TextView) view.findViewById(R.id.txtAppTitle);
        txtAppTitle.setText(getResources().getString(R.string.current_affair_level));

        dataArrayList = new ArrayList<CurrentAffairLevel>();
        // Show the Up button in the action bar.
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        currentAffairQuestionsDAO  = new CurrentAffairQuestionsDAO(getActivity());
        dataArrayList = currentAffairQuestionsDAO.getCurrentAffairLevels();

        dataArrayList = currentAffairQuestionsDAO.getCurrentAffairLevels();
        offSet = currentAffairQuestionsDAO.getTotalNoOfLevel();
        offSet = offSet+1;
        Log.i(TAG," Off set: "+offSet);

        listview = (ListView) view.findViewById(R.id.listview);
        currentAffairAdapter = new CurrentAffairAdapter(getActivity(),dataArrayList);
        listview.setAdapter(currentAffairAdapter);

        tp = Typeface.createFromAsset(getActivity().getAssets(),
                "MarkoOne-Regular.ttf");
        View v = new View(getActivity());
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,1));

        swipeRefreshLayout = (SwipeRefreshLayoutBottom) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        Bundle bundle = getArguments();
        Log.i("IS From PUSH: ", "" + bundle.getBoolean("isfrompush"));
        boolean isFromPush = bundle.getBoolean("isfrompush");

        if(isFromPush){
            level.setCurrentAffairLevelID(bundle.getInt(Constants.CURRENT_AFFAIR_LEVEL_ID));
        }else{
            level.setCurrentAffairLevelID(bundle.getInt(Constants.CURRENT_AFFAIR_LEVEL_ID));
        }

        listClickListener = new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int levelNo = dataArrayList.get(position).getCurrentAffairLevelID();
                mListener.playCurrentAffairQuiz(levelNo);
             }
        };
        listview.setOnItemClickListener(listClickListener);

        if(dataArrayList.size() <= 1){
            swipeRefreshLayout.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            swipeRefreshLayout.setRefreshing(true);

                                            fetchMovies();
                                        }
                                    }
            );
        }
    }

    @Override
    public void onRefresh() {
        listview.setOnItemClickListener(null);
        getActivity().setProgressBarIndeterminateVisibility(true);
        Log.i(TAG,"Refresh Call");
        //fetchMovies();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"Fetch Movie Call");
                swipeRefreshLayout.setRefreshing(false);
                fetchMovies();
                int index = listview.getFirstVisiblePosition();
                View v = listview.getChildAt(0);
                int top = (v == null) ? 0 : v.getTop();
                listview.setSelectionFromTop(index, top);
            }
        }, 2000);
    }


    private void fetchMovies() {
//        progress = new ProgressDialog(getActivity());
//        progress.setTitle("Please Wait!!");
//        progress.setMessage("Data Loading..");
//        progress.setCancelable(true);
//        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progress.show();

        offSet = currentAffairQuestionsDAO.getTotalNoOfLevel();
        System.out.println("offset : "+offSet);
        offSet = offSet+1;
        Log.i(TAG," Off set after Refresh: "+offSet);
        // appending offset to url
        String url = getResources().getString(R.string.get_current_affair_level) + offSet;

        Log.i(TAG, "New URL: " + url);

        // Volley's json array request object
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        if (response.length() > 0) {

                            // looping through json and adding to movies list
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject movieObj = response.getJSONObject(i);

                                    int rank = movieObj.getInt("levelid");
                                    String title = movieObj.getString("lvlname");

                                    CurrentAffairLevel m = new CurrentAffairLevel(rank, title,false,0);
//
                                    dataArrayList.add(m);
                                    currentAffairQuestionsDAO.addCurrentAffairLevel(m);
                                } catch (JSONException e) {
                                    Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                                }
                            }
//                            if(progress.isShowing()) {
//                                progress.cancel();
//                            }

                            currentAffairAdapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(getActivity().getApplicationContext(), "There are no any data",
                                    Toast.LENGTH_SHORT).show();
                            //swipeRefreshLayout.setRefreshing(false);
                            listview.setOnItemClickListener(listClickListener);
                        }

                        // stopping swipe refresh
                        swipeRefreshLayout.setRefreshing(false);
                        listview.setOnItemClickListener(listClickListener);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Server Error: " + error.getMessage());

                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
                listview.setOnItemClickListener(listClickListener);
            }
        });

        // Adding request to request queue
        MainApplication.getInstance().addToRequestQueue(req);
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacksAndMessages(null);
        super.onPause();
    }
}
