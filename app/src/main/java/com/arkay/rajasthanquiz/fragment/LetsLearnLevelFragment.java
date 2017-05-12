package com.arkay.rajasthanquiz.fragment;


import android.os.Bundle;
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

import com.arkay.rajasthanquiz.R;
import com.arkay.rajasthanquiz.adapter.AllListAdaptor;
import com.arkay.rajasthanquiz.beans.GameData;
import com.arkay.rajasthanquiz.beans.LearnListData;
import com.arkay.rajasthanquiz.beans.LetLeranScore;
import com.arkay.rajasthanquiz.handler.QuestionsDAO;

import java.util.ArrayList;

/**
 * Created by India on 10-04-2017.
 */

public class LetsLearnLevelFragment extends Fragment {

    private ListView listview;
    private AllListAdaptor levelAdapter;
    private TextView txtAppTitle;
    private Listener mListener = null;
    private ArrayList<LearnListData> dataArrayList;

    public static LetsLearnLevelFragment newInstance(Bundle bundle) {
        LetsLearnLevelFragment fragment = new LetsLearnLevelFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_level, container, false);
    }

    public void setListener(Listener l) {
        mListener = l;
    }

    public interface Listener {
        public void playQuizLeatsLearnMode(int selectedIndexOrCategoryID);
        public GameData getGameData();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        dataArrayList = getAllInfoData();
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

        txtAppTitle = (TextView) view.findViewById(R.id.txtAppTitle);
        txtAppTitle.setText(getResources().getString(R.string.select_level));

        // Show the Up button in the action bar.
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        listview = (ListView) view.findViewById(R.id.listview);
        levelAdapter = new AllListAdaptor(getActivity(), dataArrayList);
        listview.setAdapter(levelAdapter);

        View v = new View(getActivity());
        v.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.playQuizLeatsLearnMode(dataArrayList.get(position).getId());
            }
        });
    }

    private ArrayList<LearnListData> getAllInfoData() {
        QuestionsDAO questionDAO = new QuestionsDAO(getActivity());
        dataArrayList = new ArrayList<LearnListData>();

        int size = questionDAO.getTotalSingleAnswareQuestionLevel();
        for (int i = 1; i <= size; i++) {
            LetLeranScore letTem = mListener.getGameData().getLetLearnScoreByLevelID(i);
            Log.i("Score ", "" + letTem);
            dataArrayList.add(new LearnListData(i, " सामान्य ज्ञान ", 10, letTem));
        }
        return dataArrayList;
    }
}
