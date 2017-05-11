package com.arkay.rajsthanquiz.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arkay.rajsthanquiz.R;
import com.arkay.rajsthanquiz.customviews.CircularButton;

/**
 * Created by India on 13-04-2017.
 */

public class CategoryScreenFragment extends Fragment implements View.OnClickListener{


    Listener mListener = null;
    Typeface tp,tpHindi;
    TextView txtAppTitle;
    int category;
    TextView txtLoginName1,txtLoginName2,txtLoginName3,txtLoginName4,txtLoginName5,txtLoginName6,txtLoginName7,txtLoginName8,txtLoginName9,txtLoginName10,txtLoginName11,txtLoginName12;

    ImageButton btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn10,btn11,btn12;
    public static CategoryScreenFragment newInstance(Bundle bundle) {
        CategoryScreenFragment fragment = new CategoryScreenFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.category_activity, container, false);
    }

    public void setListener(Listener l) {
        mListener = l;
    }


    public interface Listener {

        public void playCategoryQuiz(int catId);
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
        tp = Typeface.createFromAsset(getActivity().getAssets(),
                "MarkoOne-Regular.ttf");

        tpHindi = Typeface.createFromAsset(getActivity().getAssets(),
                "olivier_demo.ttf");
        txtAppTitle = (TextView) view.findViewById(R.id.txtAppTitle);
        txtAppTitle.setText(getResources().getString(R.string.select_category));

        // Show the Up button in the action bar.
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        btn1 = (ImageButton) view.findViewById(R.id.btnb1);
        btn2 = (ImageButton) view.findViewById(R.id.btnb2);
        btn3 = (ImageButton) view.findViewById(R.id.btnb3);
        btn4 = (ImageButton) view.findViewById(R.id.btnb4);
        btn5 = (ImageButton) view.findViewById(R.id.btnb5);
        btn6 = (ImageButton) view.findViewById(R.id.btnb6);
        btn7 = (ImageButton) view.findViewById(R.id.btnb7);
        btn8 = (ImageButton) view.findViewById(R.id.btnb8);
        btn9 = (ImageButton) view.findViewById(R.id.btnb9);
        btn10 = (ImageButton) view.findViewById(R.id.btnb10);
        btn11 = (ImageButton) view.findViewById(R.id.btnb11);
        btn12 = (ImageButton) view.findViewById(R.id.btnb12);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn10.setOnClickListener(this);
        btn11.setOnClickListener(this);
        btn12.setOnClickListener(this);
        txtLoginName1 = (TextView) view.findViewById(R.id.txtLoginName1);
        txtLoginName2 = (TextView) view.findViewById(R.id.txtLoginName2);
        txtLoginName3 = (TextView) view.findViewById(R.id.txtLoginName3);
        txtLoginName4 = (TextView) view.findViewById(R.id.txtLoginName4);
        txtLoginName5 = (TextView) view.findViewById(R.id.txtLoginName5);
        txtLoginName6 = (TextView) view.findViewById(R.id.txtLoginName6);
        txtLoginName7 = (TextView) view.findViewById(R.id.txtLoginName7);
        txtLoginName8 = (TextView) view.findViewById(R.id.txtLoginName8);
        txtLoginName9 = (TextView) view.findViewById(R.id.txtLoginName9);
        txtLoginName10 = (TextView) view.findViewById(R.id.txtLoginName10);
        txtLoginName11 = (TextView) view.findViewById(R.id.txtLoginName11);
        txtLoginName12 = (TextView) view.findViewById(R.id.txtLoginName12);

        txtLoginName1.setTypeface(tpHindi);
        txtLoginName2.setTypeface(tpHindi);
        txtLoginName3.setTypeface(tpHindi);
        txtLoginName4.setTypeface(tpHindi);
        txtLoginName5.setTypeface(tpHindi);
        txtLoginName6.setTypeface(tpHindi);
        txtLoginName7.setTypeface(tpHindi);
        txtLoginName8.setTypeface(tpHindi);
        txtLoginName9.setTypeface(tpHindi);
        txtLoginName10.setTypeface(tpHindi);
        txtLoginName11.setTypeface(tpHindi);
        txtLoginName12.setTypeface(tpHindi);

    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnb1:
                category = 1;
                break;
            case R.id.btnb2:
                category = 2;
                break;
            case R.id.btnb3:
                category = 3;
                break;
            case R.id.btnb4:
                category = 4;
                break;
            case R.id.btnb5:
                category = 5;
                break;
            case R.id.btnb6:
                category = 6;
                break;
            case R.id.btnb7:
                category = 7;
                break;
            case R.id.btnb8:
                category = 8;
                break;
            case R.id.btnb9:
                category = 9;
                break;
            case R.id.btnb10:
                category = 10;
                break;
            case R.id.btnb11:
                category = 11;
                break;
            case R.id.btnb12:
                category = 12;
                break;

            default:
                break;

        }
        mListener.playCategoryQuiz(category);
    }
}

