package com.arkay.rajsthanquiz.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arkay.rajsthanquiz.R;
import com.arkay.rajsthanquiz.adapter.ViewPagerAdapter;
import com.arkay.rajsthanquiz.beans.GkInfoDetail;
import com.arkay.rajsthanquiz.util.ConnectionDetector;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ARKAY 05 on 17-10-2016.
 */
public class FragmentGkInDetail extends Fragment {

    private View v;
    ViewPager viewPagerBanner;
    ViewPagerAdapter adapter;
    ConnectionDetector conn;
   // NewsData newsData;
    int news_id;
    int type;
    String r;
    int id1 = 0;
    ArrayList<String> myListBack;
    private ArrayList<GkInfoDetail> recentNewses;
    private FloatingActionButton btnNext;
    private ImageButton imgLocation;
    private PlaceMapFragment projectMapFragment;
    public static String LATITUDE = "latitude";
    public static String LOGITUDE = "logitude";
    public static String TITLE = "title";
    Listener mListener = null;


    public static FragmentGkInDetail newInstance(Bundle bundle) {
        FragmentGkInDetail fragment = new FragmentGkInDetail();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recent_news_in_detail_test, container, false);
    }

    public void setListener(Listener l) {
        mListener = l;
    }


    public interface Listener {
        public  void  setPlaceMap(Bundle bundle,String Lat,String Log,String name);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        btnNext = (FloatingActionButton) view.findViewById(R.id.btnNext);
        imgLocation = (ImageButton) view.findViewById(R.id.imgLocation);
        news_id = getArguments().getInt(FragmentGKListRefresh.NEWS_ID);
        type = getArguments().getInt(FragmentGKListRefresh.TYPE);
        recentNewses = new ArrayList<>();
        viewPagerBanner = (ViewPager) view.findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getActivity(), recentNewses,type);
        fetchActivity();

        if(type == 2)
            imgLocation.setVisibility(View.VISIBLE);

        myListBack = new ArrayList<>();



        viewPagerBanner.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        viewPagerBanner.setCurrentItem(4);
        int id = viewPagerBanner.getCurrentItem();

        viewPagerBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                System.out.println("id : "+position);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPagerBanner.setCurrentItem(viewPagerBanner.getCurrentItem() + 1, true);

            }
        });

        imgLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle1 = new Bundle();
                String lattitude = recentNewses.get(viewPagerBanner.getCurrentItem()).getLatitude();
                String logitude = recentNewses.get(viewPagerBanner.getCurrentItem()).getLongitude();
                String name = recentNewses.get(viewPagerBanner.getCurrentItem()).getTitle();

                mListener.setPlaceMap(bundle1,lattitude,logitude,name);
            }
        });
    }



    public void fetchActivity(){

        String url="";
        if(type == 1)
            url = getResources().getString(R.string.get_news_greater_than_id);
        else if(type == 2)
            url = getResources().getString(R.string.get_famous_place_greater_than_id);
        else if(type == 3)
            url = getResources().getString(R.string.get_famous_person_greater_than_id);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    JSONArray jsonResponse = null;
                    JSONObject json = null;
                    @Override
                    public void onResponse(String response) {
                        if (response.length() > 0) {
                            try {
                                jsonResponse = new JSONObject(response).getJSONArray("news_by_id");
                                for (int i = 0; i < jsonResponse.length(); i++) {
                                    json = (JSONObject) jsonResponse.get(i);
                                    final Gson gson = new Gson();
                                    GkInfoDetail activity = gson.fromJson(String.valueOf(json), GkInfoDetail.class);
                                    recentNewses.add(activity);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        )

        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                Log.i("id : ",""+news_id);
                params.put("newsid",""+news_id);
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(postRequest);
    }
}
