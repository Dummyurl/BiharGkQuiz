package com.arkay.rajasthanquiz.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;

import com.arkay.rajasthanquiz.R;


/**
 * Created by arkayapps on 14/04/16.
 */

public class HomeFragment extends Fragment implements View.OnClickListener{


    public static final String TAG = HomeFragment.class.getSimpleName();;

    Animation growFromBottom;//, animZoomIn1, animZoomIn2, animZoomIn3, animZoomIn4, animZoomIn5, animZoomIn6;

    DrawerLayout drawer;

    TextView txtLetsLearn,txtGkNews,txtPlayQuiz,txtCurrentAffair,txtCategory,txtFamous,txtFamousPlace;
    ImageButton imgLetslearn,imgGk,imgDrawer,imgPlayQuiz,imgCurrent,imgCategory,imgFamousPlace,imgFamousPerson;

    LetsLearnLevelFragment levelFragment;
    Typeface tp;

    public interface Listener {
        public void LetsLearnLevel();
        public void CategoryScreen();
        public void CurrentAffair();
        public void GkLevel();
        public void clickOnPlayQuiz();
        public void clickOnFamousPlace();
        public void clickOnFamousPerson();
    }
    Listener mListener = null;
    private Context context;

    public static HomeFragment newInstance(Bundle bundle) {
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setListener(Listener l) {
        mListener = l;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_home, container, false);
        this.context = getActivity();

        drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        tp = Typeface.createFromAsset(getActivity().getAssets(),
                "MarkoOne-Regular.ttf");
        imgDrawer = (ImageButton) v.findViewById(R.id.imgDrawer);
        txtLetsLearn = (TextView) v.findViewById(R.id.txtLetsLearn);
        txtGkNews = (TextView) v.findViewById(R.id.txtGkNews);
        txtPlayQuiz = (TextView) v.findViewById(R.id.txtPlayQuiz);
        txtCurrentAffair = (TextView) v.findViewById(R.id.txtCurrentAffair);
        txtCategory = (TextView) v.findViewById(R.id.txtCategory);
        txtFamousPlace = (TextView) v.findViewById(R.id.txtFamousPlace);
        txtFamous = (TextView) v.findViewById(R.id.txtFamousPerson);

        imgLetslearn = (ImageButton) v.findViewById(R.id.imgLetsLearn);
        imgGk = (ImageButton) v.findViewById(R.id.imgGk);
        imgPlayQuiz = (ImageButton) v.findViewById(R.id.imgPlayQuiz);
        imgCurrent = (ImageButton) v.findViewById(R.id.imgCurrent);
        imgCategory = (ImageButton) v.findViewById(R.id.imgCategory);
        imgFamousPlace = (ImageButton) v.findViewById(R.id.imgFamousPlace);
        imgFamousPerson = (ImageButton) v.findViewById(R.id.imgFamousPerson);

        txtCategory.setTypeface(tp);
        txtGkNews.setTypeface(tp);
        txtPlayQuiz.setTypeface(tp);
        txtCurrentAffair.setTypeface(tp);
        txtLetsLearn.setTypeface(tp);
        txtFamous.setTypeface(tp);
        txtFamousPlace.setTypeface(tp);

        imgDrawer.setOnClickListener(this);
        imgLetslearn.setOnClickListener(this);
        imgGk.setOnClickListener(this);
        imgPlayQuiz.setOnClickListener(this);
        imgCurrent.setOnClickListener(this);
        imgCategory.setOnClickListener(this);
        imgFamousPlace.setOnClickListener(this);
        imgFamousPerson.setOnClickListener(this);

        return v;
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.imgDrawer:
                drawer.openDrawer(GravityCompat.START);
                break;

            case R.id.imgLetsLearn:
                mListener.LetsLearnLevel();
                break;

            case R.id.imgPlayQuiz:
                mListener.clickOnPlayQuiz();
                break;

            case R.id.imgGk:
                mListener.GkLevel();
                break;

            case R.id.imgCurrent:
               mListener.CurrentAffair();
                break;

            case R.id.imgCategory:
               mListener.CategoryScreen();
                break;

            case R.id.imgFamousPlace:
                mListener.clickOnFamousPlace();
                break;
            case R.id.imgFamousPerson:
                mListener.clickOnFamousPerson();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
