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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.arkay.rajasthanquiz.R;
import com.arkay.rajasthanquiz.activity.MainActivity;
import com.arkay.rajasthanquiz.adapter.GkAdapter;
import com.arkay.rajasthanquiz.application.MainApplication;
import com.arkay.rajasthanquiz.beans.GKInfo;
import com.arkay.rajasthanquiz.customviews.SwipeRefreshLayoutBottom;
import com.arkay.rajasthanquiz.util.ConnectionDetector;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by arkayapps on 08/08/15.
 */
public class FragmentGKListRefresh extends Fragment implements SwipeRefreshLayoutBottom.OnRefreshListener {

    private String TAG = FragmentGKListRefresh.class.getSimpleName();


    private SwipeRefreshLayoutBottom swipeRefreshLayout;
    private ListView listView;
    private GkAdapter adapter;
    private List<GKInfo> dataArrayList;
    private View v;
    // initially offset will be 0, later will be updated while parsing the json
    private int offSet = 1;
    Listener mListener = null;
    AdapterView.OnItemClickListener listClickListener;
    TextView txtAppTitle;
    Typeface tp;
    private int type;
    //ProgressDialog progress;
    public static final String NEWS_ID = "news_id";
    public static final String TYPE = "type";

    private Tracker mTracker;

    public interface Listener {
        public void setGKDetailScreen(Bundle bundle,int id,int type);
    }

    public void setListener(Listener l) {
        mListener = l;
    }

    public static FragmentGKListRefresh newInstance(Bundle bundle){
        FragmentGKListRefresh fragment = new FragmentGKListRefresh();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Obtain the shared Tracker instance.
        MainApplication application = (MainApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_gk_news, container, false);
        this.v = v;

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.detail_toolbar);
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
        tp = Typeface.createFromAsset(getActivity().getAssets(),
                "MarkoOne-Regular.ttf");
        txtAppTitle = (TextView) v.findViewById(R.id.txtAppTitle);
        type = getArguments().getInt(MainActivity.TYPE);
        if(type == 1) {
            txtAppTitle.setText(getResources().getString(R.string.gk_news));
          }else if(type == 2){
            txtAppTitle.setText(getResources().getString(R.string.famous_place));
        }else if(type == 3){
            txtAppTitle.setText(getResources().getString(R.string.famous_person));
        }



        // Show the Up button in the action bar.
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        listView = (ListView) v.findViewById(R.id.listview);
        swipeRefreshLayout = (SwipeRefreshLayoutBottom) v.findViewById(R.id.swipe_refresh_layout);
        //gKinfoDAO  = new GKinfoDAO(getActivity());
       // dataArrayList = gKinfoDAO.getGKInfo();
        dataArrayList = new ArrayList<>();

        Log.i(TAG," Off set: "+offSet);
        swipeRefreshLayout.setOnRefreshListener(this);

        // if(dataArrayList.size() <= 1){
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        fetchMovies();
                                    }
                                }
        );

        adapter = new GkAdapter(getActivity(), dataArrayList,type);
        listView.setAdapter(adapter);
        offSet = adapter.getCount();
        offSet = offSet+1;


      //  }

        listClickListener = new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int levelNo = dataArrayList.get(position).getNewsid();
                Log.i("INFO", "Display you tube: " + levelNo);
                Log.i("GKInfo in Gujarati", "");
                Bundle bundle = new Bundle();
                mListener.setGKDetailScreen(bundle,levelNo,type);
            }
        };
        listView.setOnItemClickListener(listClickListener);

        ConnectionDetector connectionDetector = new ConnectionDetector(getActivity().getApplicationContext());

        if(connectionDetector.isConnectingToInternet()) {
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mTracker.setScreenName("Rajasthan Quiz GK News");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        listView.setOnItemClickListener(null);
        getActivity().setProgressBarIndeterminateVisibility(true);
        Log.i(TAG,"Refresh Call");
        //fetchMovies();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"Fetch Movie Call");
                swipeRefreshLayout.setRefreshing(false);
                fetchMovies();
                int index = listView.getFirstVisiblePosition();
                View v = listView.getChildAt(0);
                int top = (v == null) ? 0 : v.getTop();
                listView.setSelectionFromTop(index, top);
            }
        }, 2000);
    }

    /**
     * Fetching movies json by making http call
     */
    private void fetchMovies() {

        offSet = adapter.getCount();
        offSet = offSet+1;

        String url="";
//        progress = new ProgressDialog(getActivity());
//        progress.setTitle("Please Wait!!");
//        progress.setMessage("Data Loading..");
//        progress.setCancelable(true);
//        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progress.show();
        Log.i(TAG," Off set after Refresh: "+offSet);
        // showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);

        // appending offset to url
        if(type == 1) {
            url = getResources().getString(R.string.get_gk_news_url) + offSet;
            txtAppTitle.setText(getResources().getString(R.string.gk_news));
        }else if(type == 2){
            url = getResources().getString(R.string.get_famous_place_url) + offSet;
            txtAppTitle.setText(getResources().getString(R.string.famous_place));
        }else if(type == 3){
            url = getResources().getString(R.string.get_famous_person_url) + offSet;
            txtAppTitle.setText(getResources().getString(R.string.famous_person));
        }


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
                                    int rank = movieObj.getInt("newsid");
                                    Log.i(TAG,"New ID: "+rank);
                                    String title = movieObj.getString("title");
                                    String desc = movieObj.getString("desc");
                                    String imgUrl = movieObj.getString("imagepath");
                                    String postBy = movieObj.getString("uname");
                                    String imagePostDate = movieObj.getString("date");
                                    String imageCredit = movieObj.getString("imgcredit");
                                    String lat = movieObj.getString("latitude");
                                    String log = movieObj.getString("longitude");

                                    GKInfo m = new GKInfo(rank, title,desc,imagePostDate,postBy,lat,log,imageCredit,imgUrl);
                                    dataArrayList.add(m);

                                } catch (JSONException e) {
                                    Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(getActivity().getApplicationContext(), "There are no any data",
                                    Toast.LENGTH_SHORT).show();
                        }
                        if(adapter!=null) {
                            adapter.notifyDataSetChanged();
                        }
                        // stopping swipe refresh
                        swipeRefreshLayout.setRefreshing(false);
                        listView.setOnItemClickListener(listClickListener);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Server Error: " + error.getMessage());


                // stopping swipe refresh
                swipeRefreshLayout.setRefreshing(false);
                listView.setOnItemClickListener(listClickListener);
            }
        });

        // Adding request to request queue
        MainApplication.getInstance().addToRequestQueue(req);
    }
}
