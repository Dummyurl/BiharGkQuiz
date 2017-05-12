package com.arkay.rajasthanquiz.fragment;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.arkay.rajasthanquiz.R;
import com.arkay.rajasthanquiz.activity.MainActivity;
import com.arkay.rajasthanquiz.beans.CurrentAffairQuestion;
import com.arkay.rajasthanquiz.util.Constants;

import java.util.ArrayList;
import java.util.List;


public class FragmentScoreBoard extends Fragment implements
        View.OnClickListener{

    private Listener mListener = null;
    private LinearLayout main;
    private TextView txtQuestion, txtOption1, txtOption2, txtOption3, txtOption4, rightAns, txtLevelHeading;
    private TextView txtTotalTrue, txtTotalFalse, txtTotalScoreThisLevel, txtSkipQuestion;
    private Button btnHome, btnPlayAgain;
    private ImageView imgCircle;
    private List<View> scoreQuestionList = new ArrayList<View>();
    boolean isLastLetLearn =false;
    private SharedPreferences settings;
    private Typeface tp,tpHindi;

    public interface Listener {
        public void displyHomeScreen();
        public ArrayList<CurrentAffairQuestion> getCAScoreBoardQuestion();
        public void playQuizLeatsLearnMode(int selectedIndexOrCategoryID);
        public void playCurrentAffairQuiz(int currentAffairLevelID);
    }


    public static FragmentScoreBoard newInstance(Bundle bundle){
        FragmentScoreBoard fragment = new FragmentScoreBoard();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    public void setListener(Listener l) {
        mListener = l;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_score_board, container, false);
        settings = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);

        tpHindi = Typeface.createFromAsset(getActivity().getAssets(),
                "olivier_demo.ttf");
        tp = Typeface.createFromAsset(getActivity().getAssets(),
                "Roboto-Regular.ttf");


        txtLevelHeading = (TextView) v.findViewById(R.id.txtLevelHeading);
        txtLevelHeading.setText("");


        btnHome = (Button) v.findViewById(R.id.btnHome);
        btnHome.setOnClickListener(this);
        btnPlayAgain = (Button)v.findViewById(R.id.btnPlayAgain);
        btnPlayAgain.setOnClickListener(this);

        int trueQuestion = settings.getInt(MainActivity.LAST_LEVE_TRUE_ANS,0);

        int incurrentQuestion = settings.getInt(MainActivity.LAST_LEVE_FALSE_ANS,0);
        int skillQuestin = 10 - (trueQuestion+incurrentQuestion);

        txtTotalTrue = (TextView) v.findViewById(R.id.txtTotalTrue);
        txtTotalTrue.setText("" + trueQuestion + "/10");
        txtTotalFalse = (TextView) v.findViewById(R.id.txtTotalFalse);
        txtTotalFalse.setText(""+incurrentQuestion+"/10" );

        isLastLetLearn = settings.getBoolean(MainActivity.IS_LETS_LEARN_LEVEL_PLAY,false);
        Log.i("Gk Last",""+isLastLetLearn);
        if(isLastLetLearn) {
            TextView lblSkipQuestion = (TextView) v.findViewById(R.id.lblSkipQuestion);
            lblSkipQuestion.setText("Skip Question:");

            txtSkipQuestion = (TextView) v.findViewById(R.id.txtSkipQuestion);
            txtSkipQuestion.setText(""+skillQuestin);
        }else{
            TextView lblSkipQuestion = (TextView) v.findViewById(R.id.lblSkipQuestion);
            lblSkipQuestion.setText("");

            txtSkipQuestion = (TextView) v.findViewById(R.id.txtSkipQuestion);
            txtSkipQuestion.setText("");
        }



        txtTotalScoreThisLevel = (TextView) v.findViewById(R.id.txtTotalScoreThisLevel);
        txtTotalScoreThisLevel.setText(""+settings.getInt(MainActivity.LAST_LEVEL_SCORE, 0));

        boolean islevelcomplted = settings.getBoolean(MainActivity.IS_LAST_LEVEL_COMPLETED, false);

        //if(!isCategoryPlayQuiz) {
        int levelNo = settings.getInt(MainActivity.LET_LERAN_LAST_LEVEL_COMPLETED, 1);
        if (islevelcomplted) {
            levelNo--;
            txtLevelHeading.setText(getActivity().getString(R.string.level) + " " + levelNo + " " + getActivity().getResources().getString(R.string.finished));
            btnPlayAgain.setText("Play Next");
        } else {
            txtLevelHeading.setText(getActivity().getString(R.string.level) + " " + levelNo + " " + getActivity().getResources().getString(R.string.not_completed));
            btnPlayAgain.setText("Play Again");
        }

        main = (LinearLayout) v.findViewById(R.id.linearLayout1);
        main.removeAllViews();
        main.removeView(v);
        scoreQuestionList.clear();

        Log.i("Info: ", mListener.getCAScoreBoardQuestion().toString());
        for(int i=0;i<mListener.getCAScoreBoardQuestion().size(); i++){
            View view = getActivity().getLayoutInflater().inflate(
                    R.layout.score_board_question, main, false);


            txtQuestion = (TextView) view.findViewById(R.id.quizQuestion);
            int temInd = i;
            temInd++;
            txtQuestion.setText("Question No "+temInd+" : "+mListener.getCAScoreBoardQuestion().get(i).getQuestion());

            int ansIndex = mListener.getCAScoreBoardQuestion().get(i).getAnsIndex();

            txtOption1 = (TextView) view.findViewById(R.id.option1);
            txtOption1.setText("A: "+mListener.getCAScoreBoardQuestion().get(i).getOptions().get(0));

            txtOption2 = (TextView) view.findViewById(R.id.option2);
            txtOption2.setText("B: "+mListener.getCAScoreBoardQuestion().get(i).getOptions().get(1));

            txtOption3 = (TextView) view.findViewById(R.id.option3);
            txtOption3.setText("C: "+mListener.getCAScoreBoardQuestion().get(i).getOptions().get(2));

            txtOption4 = (TextView) view.findViewById(R.id.option4);
            txtOption4.setText("D: "+mListener.getCAScoreBoardQuestion().get(i).getOptions().get(3));

            rightAns = (TextView) view.findViewById(R.id.rightAns);
            rightAns.setText("Right Ans: "+mListener.getCAScoreBoardQuestion().get(i).getTrueAns());

            imgCircle = (ImageView) view.findViewById(R.id.imgCircle);
          //  txtQuestionTrue = (TextView) view.findViewById(R.id.txtQuestionTrue);

            int trueAnsIndex = 0;
            if(mListener.getCAScoreBoardQuestion().get(i).getOptions().get(0).equalsIgnoreCase(mListener.getCAScoreBoardQuestion().get(i).getTrueAns())) {
                txtOption1.setTextColor(getResources().getColor(R.color.score_board_green));
                trueAnsIndex=0;
            }
            if(mListener.getCAScoreBoardQuestion().get(i).getOptions().get(1).equalsIgnoreCase(mListener.getCAScoreBoardQuestion().get(i).getTrueAns())) {
                txtOption2.setTextColor(getResources().getColor(R.color.score_board_green));
                trueAnsIndex=1;
            }
            if(mListener.getCAScoreBoardQuestion().get(i).getOptions().get(2).equalsIgnoreCase(mListener.getCAScoreBoardQuestion().get(i).getTrueAns())) {
                txtOption3.setTextColor(getResources().getColor(R.color.score_board_green));
                trueAnsIndex=2;
            }
            if(mListener.getCAScoreBoardQuestion().get(i).getOptions().get(3).equalsIgnoreCase(mListener.getCAScoreBoardQuestion().get(i).getTrueAns())) {
                txtOption4.setTextColor(getResources().getColor(R.color.score_board_green));
                trueAnsIndex=3;
            }

            if(mListener.getCAScoreBoardQuestion().get(i).isQuestionTrue()){
                imgCircle.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_right));
            }else{
                imgCircle.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_wrong));
            }

            switch(ansIndex){
                case 0:
                    if(trueAnsIndex!=0) {
                        txtOption1.setTextColor(getResources().getColor(R.color.score_board_red));
                    }
                    break;
                case 1:
                    if(trueAnsIndex!=1) {
                        txtOption2.setTextColor(getResources().getColor(R.color.score_board_red));
                    }
                    break;
                case 2:
                    if(trueAnsIndex!=2) {
                        txtOption3.setTextColor(getResources().getColor(R.color.score_board_red));
                    }
                    break;
                case 3:
                    if(trueAnsIndex!=3) {
                        txtOption4.setTextColor(getResources().getColor(R.color.score_board_red));
                    }
                    break;
            }


            scoreQuestionList.add(view);
        }

        for (int j = 0; j < scoreQuestionList.size(); j++) {
            main.addView(scoreQuestionList.get(j));
        }
       // txtLevelHeading.setTypeface(tpHindi);
        txtOption1.setTypeface(tpHindi);
        txtOption2.setTypeface(tpHindi);
        txtOption3.setTypeface(tpHindi);
        txtOption4.setTypeface(tpHindi);
        //txtQuestion.setTypeface(tpHindi);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPlayAgain:
                getActivity().getSupportFragmentManager().popBackStack();
                if(isLastLetLearn) {
                    boolean islevelcomplted = settings.getBoolean(MainActivity.IS_LAST_LEVEL_COMPLETED, false);
                    int levelNo = settings.getInt(MainActivity.LET_LERAN_LAST_LEVEL_COMPLETED, 1);
                    if (islevelcomplted) {
                        levelNo++;
                        mListener.playQuizLeatsLearnMode(levelNo);
                    } else {
                        mListener.playQuizLeatsLearnMode(levelNo);
                    }
                }else{
                    int lastLevel = settings.getInt(MainActivity.LAST_TIME_CURRENT_AFAIR_PLAY, 1);
                    mListener.playCurrentAffairQuiz(lastLevel);
                }
                // implemet play again current affair
                break;
            case R.id.btnHome:
                getActivity().getSupportFragmentManager().popBackStack();
                mListener.displyHomeScreen();
                break;
            default:
                break;
        }
    }
}

