package com.arkay.rajsthanquiz.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arkay.rajsthanquiz.R;
import com.arkay.rajsthanquiz.activity.MainActivity;
import com.arkay.rajsthanquiz.application.MainApplication;
import com.arkay.rajsthanquiz.beans.CurrentAffairQuestion;
import com.arkay.rajsthanquiz.beans.GameData;
import com.arkay.rajsthanquiz.beans.LetLeranScore;
import com.arkay.rajsthanquiz.beans.PlayQuizLevel;
import com.arkay.rajsthanquiz.beans.PlayQuizQuestion;
import com.arkay.rajsthanquiz.facebook.AsyncFacebookRunner;
import com.arkay.rajsthanquiz.facebook.Facebook;
import com.arkay.rajsthanquiz.handler.QuestionsDAO;
import com.arkay.rajsthanquiz.util.Constants;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FragmentLetsLearnPlayQuiz extends Fragment
        implements View.OnClickListener{

    private String TAG = FragmentLetsLearnPlayQuiz.class.getSimpleName();


    private PlayQuizLevel level;
    private QuestionsDAO questionDaoNew;
    private int quextionIndex=0;
    private boolean isVibration;

    private ImageButton btnAddToFavourite;

    private int NO_OF_QUESTION = 10;
    private int totalScore=0;
    private int score=0;
    private int correctQuestion=0;
    private int inCorrectQuestion=0;
    private ImageButton btnNext;
    private TextView quizQuestion,   txtTrueQuestion, txtFalseQuestion,txtQestionNo;

    private SharedPreferences settings;
    private Animation animation;
    Animation animationFromRight,animationFromLeft;
    private MediaPlayer rightAnsware, wrongeAnsware;
    private SharedPreferences.Editor editor;
    Animation in;
    Animation out;
    ImageView img1,img2,img3,img4,imgOption1,imgOption2,imgOption3,imgOption4;

    View view;

    int letsLeanLevelID = 0;
    RelativeLayout relative1,relative2,relative3,relative4;

    TextView scoretxt,txtScore,txtOutOfQue,txtQuestion,optionA,optionB,optionC,optionD,txtOption1,txtOption2,txtOption3,txtOption4;
    Typeface tp,tpHindi;


    private Facebook mFacebook    = null;
    private AsyncFacebookRunner mAsyncRunner = null;

    public interface Listener {
        public GameData getGameData();
        public FragmentScoreBoard getQuizFragmentScoreBoard();
        public void setPlayQuizCAQuestionsScoreBoard(ArrayList<CurrentAffairQuestion> playQuizQuestions);
        public void saveDataToCloud();
    }
    Listener mListener = null;

    public static FragmentLetsLearnPlayQuiz newInstance(Bundle bundle){
        FragmentLetsLearnPlayQuiz fragment = new FragmentLetsLearnPlayQuiz();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lets_learn_paly_quiz, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        view = getView();
        settings = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);
        getBundleArguments();
        setAnimation();
        setViews(view);
        setFacebookLoginCredential();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i(TAG, ""+ TAG +" Four Option Play Quiz");
    }
    private void setViews(View view)
    {
        txtTrueQuestion = (TextView) view.findViewById(R.id.txtTrue);
        txtScore = (TextView) view.findViewById(R.id.txtScore);
        txtFalseQuestion = (TextView) view.findViewById(R.id.txtFalse);
        txtOutOfQue = (TextView) view.findViewById(R.id.txtOutOfQue);
        txtQuestion = (TextView) view.findViewById(R.id.txtQuestion);
        optionA = (TextView) view.findViewById(R.id.optionA);
        optionB = (TextView) view.findViewById(R.id.optionB);
        optionC = (TextView) view.findViewById(R.id.optionC);
        optionD = (TextView) view.findViewById(R.id.optionD);
        txtOption1 = (TextView) view.findViewById(R.id.txtOption1);
        txtOption2 = (TextView) view.findViewById(R.id.txtOption2);
        txtOption3 = (TextView) view.findViewById(R.id.txtOption3);
        txtOption4 = (TextView) view.findViewById(R.id.txtOption4);
        btnAddToFavourite = (ImageButton) view.findViewById(R.id.btnBookmark);
        relative1 = (RelativeLayout) view.findViewById(R.id.relative1);
        relative2 = (RelativeLayout) view.findViewById(R.id.relative2);
        relative3 = (RelativeLayout) view.findViewById(R.id.relative3);
        relative4 = (RelativeLayout) view.findViewById(R.id.relative4);

        img1 = (ImageView) view.findViewById(R.id.img1);
        img2 = (ImageView) view.findViewById(R.id.img2);
        img3 = (ImageView) view.findViewById(R.id.img3);
        img4 = (ImageView) view.findViewById(R.id.img4);
        imgOption1 = (ImageView) view.findViewById(R.id.imgOption1);
        imgOption2 = (ImageView) view.findViewById(R.id.imgOption2);
        imgOption3 = (ImageView) view.findViewById(R.id.imgOption3);
        imgOption4 = (ImageView) view.findViewById(R.id.imgOption4);

        txtTrueQuestion.setTypeface(tp);
        txtScore.setTypeface(tp);
        txtFalseQuestion.setTypeface(tp);
        txtOutOfQue.setTypeface(tp);
        txtQuestion.setTypeface(tpHindi);
        optionA.setTypeface(tp);
        optionB.setTypeface(tp);
        optionC.setTypeface(tp);
        optionD.setTypeface(tp);
        txtOption1.setTypeface(tpHindi);
        txtOption2.setTypeface(tpHindi);
        txtOption3.setTypeface(tpHindi);
        txtOption4.setTypeface(tpHindi);

        txtOption1.setOnClickListener(this);
        txtOption2.setOnClickListener(this);
        txtOption3.setOnClickListener(this);
        txtOption4.setOnClickListener(this);
        txtScore.setText("Score: 0");

        setAnimation();

        relative1.setOnClickListener(this);
        relative2.setOnClickListener(this);
        relative3.setOnClickListener(this);
        relative4.setOnClickListener(this);
        btnAddToFavourite.setOnClickListener(this);

        txtQestionNo = (TextView) view.findViewById(R.id.txtOutOfQue);



        btnNext = (ImageButton) view.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quextionIndex++;
                nextQuizQuestion();
            }
        });

        quizQuestion =  (TextView) view.findViewById(R.id.txtQuestion);
        txtScore =  (TextView) view.findViewById(R.id.txtScore);

        txtTrueQuestion =  (TextView) view.findViewById(R.id.txtTrue);
        txtFalseQuestion =  (TextView) view.findViewById(R.id.txtFalse);

        btnAddToFavourite = (ImageButton) view.findViewById(R.id.btnBookmark);
        btnAddToFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialoug);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
                TextView textView = (TextView) dialog.findViewById(R.id.txtBookmark);
                textView.setTypeface(tp);
                dialog.show();
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                QuestionsDAO temp = new QuestionsDAO(getActivity());
                temp.updateBookmark(level.getQuestion().get(quextionIndex).getQuestionID(), true);
                Toast.makeText(getActivity().getApplicationContext(), "Add favourite successfully", Toast.LENGTH_SHORT).show();
                if (level.getQuestion().get(quextionIndex).isBookmark()) {
                    level.getQuestion().get(quextionIndex).setIsBookmark(false);
                } else {
                    level.getQuestion().get(quextionIndex).setIsBookmark(true);
                }
                if (level.getQuestion().get(quextionIndex).isBookmark()) {
                    btnAddToFavourite.setImageResource(R.drawable.ic_book_on);
                } else {
                    btnAddToFavourite.setImageResource(R.drawable.ic_book_off);
                }
                        dialog.cancel();
                    }
                });
            }
        });

        resetAllValue();
        nextQuizQuestion();

        Animation.AnimationListener listener = new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
                relative1.setClickable(false);
                relative2.setClickable(false);
                relative3.setClickable(false);
                relative4.setClickable(false);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Do your stuff
                relative1.setClickable(true);
                relative2.setClickable(true);
                relative3.setClickable(true);
                relative4.setClickable(true);
            }
        };

        animation.setAnimationListener(listener);
        animationFromLeft.setAnimationListener(listener);
        animationFromRight.setAnimationListener(listener);
    }

    public void setListener(Listener l) {
        mListener = l;
    }

    private void setFacebookLoginCredential()
    {

        mFacebook = new Facebook(getResources().getString(R.string.facebook_id));
        mAsyncRunner = new AsyncFacebookRunner(mFacebook);

    }


    private void getBundleArguments()
    {

        Bundle bundle = getArguments();
        letsLeanLevelID = bundle.getInt(Constants.LETS_LEARN_LEVEL_ID);
    }

    private void setAnimation(){

        in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(1000);

        out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(1000);

        animationFromRight = new TranslateAnimation(500f, 0f, 0f, 0f);
        animationFromRight.setDuration(600);
        animationFromLeft = new TranslateAnimation(-500f, 0f, 0f, 0f);
        animationFromLeft.setDuration(600);

        animationFromRight = new TranslateAnimation(500f, 0f, 0f, 0f);
        animationFromRight.setDuration(600);
        animationFromLeft = new TranslateAnimation(-500f, 0f, 0f, 0f);
        animationFromLeft.setDuration(600);
    }



    @Override
    public void onClick(View v)
    {
        if(quextionIndex<level.getNoOfQuestion()){
//            relative1.setClickable(false);
//            relative2.setClickable(false);
//            relative3.setClickable(false);
//            relative4.setClickable(false);

            switch(v.getId()){
                case R.id.relative1:
                    level.getQuestion().get(quextionIndex).setAnsIndex(0);
                    if(txtOption1.getText().toString().trim().equalsIgnoreCase(level.getQuestion().get(quextionIndex).getTrueAns().trim())){
                        level.getQuestion().get(quextionIndex).setIsQuestionTrue(true);

                        txtOption1.setTextColor(getResources().getColor(R.color.green));
                        img1.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_round_button));
                        imgOption1.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_green));
                        optionA.setTextColor(getResources().getColor(R.color.green));
                        addScore();
                        txtOption1.startAnimation(animation);
                    }else{
                        level.getQuestion().get(quextionIndex).setIsQuestionTrue(false);
                        wrongeQuestion();
                        txtOption1.setTextColor(getResources().getColor(R.color.red));
                        img1.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_round_button));
                        imgOption1.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_red));
                        optionA.setTextColor(getResources().getColor(R.color.red));
                    }
                    break;
                case R.id.relative2:
                    level.getQuestion().get(quextionIndex).setAnsIndex(1);
                    if(txtOption2.getText().toString().trim().equalsIgnoreCase(level.getQuestion().get(quextionIndex).getTrueAns().trim())){
                        level.getQuestion().get(quextionIndex).setIsQuestionTrue(true);
                        txtOption2.setTextColor(getResources().getColor(R.color.green));
                        addScore();
                        txtOption2.startAnimation(animation);
                        img2.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_round_button));
                        imgOption2.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_green));
                        optionB.setTextColor(getResources().getColor(R.color.green));
                    }else{
                        level.getQuestion().get(quextionIndex).setIsQuestionTrue(false);
                        wrongeQuestion();
                        txtOption2.setTextColor(getResources().getColor(R.color.red));
                        img2.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_round_button));
                        imgOption2.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_red));
                        optionB.setTextColor(getResources().getColor(R.color.red));
                    }
                    break;
                case R.id.relative3:
                    level.getQuestion().get(quextionIndex).setAnsIndex(2);
                    if(txtOption3.getText().toString().trim().equalsIgnoreCase(level.getQuestion().get(quextionIndex).getTrueAns().trim())){
                        level.getQuestion().get(quextionIndex).setIsQuestionTrue(true);
                        txtOption3.setTextColor(getResources().getColor(R.color.green));
                        addScore();
                        txtOption3.startAnimation(animation);
                        img3.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_round_button));
                        imgOption3.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_green));
                        optionC.setTextColor(getResources().getColor(R.color.green));
                    }else{
                        level.getQuestion().get(quextionIndex).setIsQuestionTrue(false);
                        wrongeQuestion();
                        txtOption3.setTextColor(getResources().getColor(R.color.red));
                        img3.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_round_button));
                        imgOption3.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_red));
                        optionC.setTextColor(getResources().getColor(R.color.red));
                    }
                    break;
                case R.id.relative4:
                    level.getQuestion().get(quextionIndex).setAnsIndex(3);
                    if(txtOption4.getText().toString().trim().equalsIgnoreCase(level.getQuestion().get(quextionIndex).getTrueAns().trim())){
                        level.getQuestion().get(quextionIndex).setIsQuestionTrue(true);
                        txtOption4.setTextColor(getResources().getColor(R.color.green));
                        addScore();
                        txtOption4.startAnimation(animation);
                        img4.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_round_button));
                        imgOption4.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_green));
                        optionD.setTextColor(getResources().getColor(R.color.green));
                    }else{
                        level.getQuestion().get(quextionIndex).setIsQuestionTrue(false);
                        wrongeQuestion();
                        txtOption4.setTextColor(getResources().getColor(R.color.red));
                        img4.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_round_button));
                        imgOption4.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_red));
                        optionD.setTextColor(getResources().getColor(R.color.red));
                    }
                    break;
            }
        }
    }



    private void nextQuizQuestion(){

        int tempQuestionIndex = quextionIndex;
        tempQuestionIndex++;
        txtQestionNo.setText("Question: "+tempQuestionIndex+"/"+10);
        txtOption1.clearAnimation();
        txtOption2.clearAnimation();
        txtOption3.clearAnimation();
        txtOption4.clearAnimation();

        txtOption1.startAnimation(animationFromLeft);
        txtOption2.startAnimation(animationFromRight);
        txtOption3.startAnimation(animationFromLeft);
        txtOption4.startAnimation(animationFromRight);

        int count_question_completed = mListener.getGameData().getCountHowManyQuestionCompleted();
        count_question_completed++;
        if(quextionIndex>=NO_OF_QUESTION){

            //showIntersitialAds();


            int howManyTimesPlayQuiz = mListener.getGameData().getCountHowManyTimePlay();
            //System.out.println("How Many Time Play: "+howManyTimesPlayQuiz);
            howManyTimesPlayQuiz++;
           // unlockPlayTime(howManyTimesPlayQuiz);
            mListener.getGameData().setCountHowManyTimePlay(howManyTimesPlayQuiz);

            count_question_completed = mListener.getGameData().getCountHowManyQuestionCompleted();
            count_question_completed--;
            mListener.getGameData().setCountHowManyQuestionCompleted(count_question_completed);

            saveScore();
            //ArrayList<pla extends CurrentAffairQuestion> currentAffairQuestion = (ArrayList)level.getQuestion();
            List<PlayQuizQuestion> playQuizQuestions = level.getQuestion();
            ArrayList<CurrentAffairQuestion> currentAffQu = new ArrayList<CurrentAffairQuestion>();
            for (PlayQuizQuestion quizQuestiontmp : playQuizQuestions)
                if (quizQuestiontmp instanceof CurrentAffairQuestion) {
                    CurrentAffairQuestion caffairQuesiton = quizQuestiontmp;
                    currentAffQu.add(caffairQuesiton);
                   // Log.i("Queston: ",""+caffairQuesiton.getQuestion());
                }
            Log.i("Info: ",currentAffQu.toString());
            mListener.setPlayQuizCAQuestionsScoreBoard(currentAffQu);
            getActivity().getSupportFragmentManager().popBackStack();

            getActivity().getSupportFragmentManager().beginTransaction().replace( R.id.ha_flContentContainer1, mListener.getQuizFragmentScoreBoard() ).addToBackStack( "tag" ).commit();

            blankAllValue();


        }else{
            mListener.getGameData().setCountHowManyQuestionCompleted(count_question_completed);


            relative1.setClickable(true);
            relative2.setClickable(true);
            relative3.setClickable(true);
            relative4.setClickable(true);


            txtOption1.setTextColor(Color.parseColor("#212121"));
            txtOption2.setTextColor(Color.parseColor("#212121"));
            txtOption3.setTextColor(Color.parseColor("#212121"));
            txtOption4.setTextColor(Color.parseColor("#212121"));
            optionA.setTextColor(Color.parseColor("#00bcd4"));
            optionB.setTextColor(Color.parseColor("#00bcd4"));
            optionC.setTextColor(Color.parseColor("#00bcd4"));
            optionD.setTextColor(Color.parseColor("#00bcd4"));
            img1.setBackgroundResource(R.drawable.rounded_button);
            img2.setBackgroundResource(R.drawable.rounded_button);
            img3.setBackgroundResource(R.drawable.rounded_button);
            img4.setBackgroundResource(R.drawable.rounded_button);
            imgOption1.setBackgroundResource(R.drawable.round_sky);
            imgOption2.setBackgroundResource(R.drawable.round_sky);
            imgOption3.setBackgroundResource(R.drawable.round_sky);
            imgOption4.setBackgroundResource(R.drawable.round_sky);

            if(quextionIndex<level.getNoOfQuestion()){
                int temp = quextionIndex;
                txtOutOfQue.setText(""+ ++temp+"/"+NO_OF_QUESTION);
                String imgName = level.getQuestion().get(quextionIndex).getQuestion();
                Pattern p = Pattern.compile(" ");
                Matcher m = p.matcher(imgName);
                imgName = m.replaceAll("_");
                quizQuestion.setText(level.getQuestion().get(quextionIndex).getQuestion());

                ArrayList<String> options = new ArrayList<String>();
                options.addAll(level.getQuestion().get(quextionIndex).getOptions());
                //Collections.shuffle(options);
                txtOption1.setText("" + options.get(0).trim());
                txtOption2.setText("" + options.get(1).trim());
                txtOption3.setText("" + options.get(2).trim());
                txtOption4.setText("" + options.get(3).trim());



                txtOption1.invalidate();
                txtOption2.invalidate();
                txtOption3.invalidate();
                txtOption4.invalidate();


                if(level.getQuestion().get(quextionIndex).isBookmark()){
                    btnAddToFavourite.setImageResource(R.drawable.ic_book_on);
                }else{
                    btnAddToFavourite.setImageResource(R.drawable.ic_book_off);
                }
            }
        }
    }


    private void addScore(){
        rightSound();
        correctQuestion++;
        txtTrueQuestion.setText(" "+correctQuestion +" ");
        totalScore = totalScore + 10;
        score = score + 10;
        txtScore.setText("Score: "+totalScore+" ");
        mListener.getGameData().saveDataLocal(settings);

        int rightAns = mListener.getGameData().getCountHowManyRightAnswareQuestion();
        rightAns++;
        mListener.getGameData().setCountHowManyRightAnswareQuestion(rightAns);
        mListener.getGameData().setTotalScore(totalScore);
    }


    private void wrongeQuestion(){
        playWrongSound();
        //saveScore();
        inCorrectQuestion++;
        totalScore = totalScore - 3;
        score = score - 3;
        if(totalScore<0){
            totalScore=0;
        }
        txtScore.setText("Score: "+totalScore);
        txtFalseQuestion.setText(" " + inCorrectQuestion + " ");
        if(txtOption1.getText().toString().trim().equalsIgnoreCase(level.getQuestion().get(quextionIndex).getTrueAns().trim())){
            txtOption1.setTextColor(getResources().getColor(R.color.green));
            img1.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_round_button));
            imgOption1.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_green));
            optionA.setTextColor(getResources().getColor(R.color.green));
            txtOption1.startAnimation(animation);
        }
        if(txtOption2.getText().toString().trim().equalsIgnoreCase(level.getQuestion().get(quextionIndex).getTrueAns().trim())){
            txtOption2.setTextColor(getResources().getColor(R.color.green));
            img2.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_round_button));
            imgOption2.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_green));
            optionB.setTextColor(getResources().getColor(R.color.green));
            txtOption2.startAnimation(animation);
        }
        if(txtOption3.getText().toString().trim().equalsIgnoreCase(level.getQuestion().get(quextionIndex).getTrueAns().trim())){
            txtOption3.setTextColor(getResources().getColor(R.color.green));
            img3.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_round_button));
            imgOption3.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_green));
            optionC.setTextColor(getResources().getColor(R.color.green));
            txtOption3.startAnimation(animation);
        }
        if(txtOption4.getText().toString().trim().equalsIgnoreCase(level.getQuestion().get(quextionIndex).getTrueAns().trim())){
            txtOption4.setTextColor(getResources().getColor(R.color.green));
            img4.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_round_button));
            imgOption4.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_green));
            optionD.setTextColor(getResources().getColor(R.color.green));
            txtOption4.startAnimation(animation);
        }
    }

    private void saveScore(){
        editor = settings.edit();
        editor.putInt(MainActivity.LAST_LEVEL_SCORE, score);
        editor.putInt(MainActivity.LET_LERAN_LAST_LEVEL_COMPLETED, letsLeanLevelID);
        if(correctQuestion>=5){
            // levelNo++;
            editor.putBoolean(MainActivity.IS_LAST_LEVEL_COMPLETED, true);
        }else{
            editor.putBoolean(MainActivity.IS_LAST_LEVEL_COMPLETED, false);
        }

        mListener.getGameData().saveDataLocal(settings);
        editor.putInt(MainActivity.LAST_LEVE_TRUE_ANS, correctQuestion);
        editor.putInt(MainActivity.LAST_LEVE_FALSE_ANS, inCorrectQuestion);
        editor.putBoolean(MainActivity.IS_LETS_LEARN_LEVEL_PLAY,true);
        editor.commit();
        mListener.getGameData().addLetLearnScore(new LetLeranScore(letsLeanLevelID, score, true));
        mListener.saveDataToCloud();
    }

    public void rightSound(){
        if(IsDirectAnswerShow()){
            AudioManager meng = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
            int volume = meng.getStreamVolume( AudioManager.STREAM_NOTIFICATION);

            if (volume != 0)
            {
                if (rightAnsware == null)
                    rightAnsware = MediaPlayer.create(getActivity(), R.raw.right_ans);
                if (rightAnsware != null)
                    rightAnsware.start();
            }
        }
    }

    private void playWrongSound(){
        if(IsDirectAnswerShow()){
            AudioManager meng = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
            int volume = meng.getStreamVolume( AudioManager.STREAM_NOTIFICATION);

            if (volume != 0)
            {
                if (wrongeAnsware == null)
                    wrongeAnsware = MediaPlayer.create(getActivity(), R.raw.wronge_ans);
                if (wrongeAnsware != null)
                    wrongeAnsware.start();
            }
        }
        if(isVibration){
            Vibrator myVib = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
            myVib.vibrate(200);
        }
    }
    private void resetAllValue(){

        questionDaoNew = new QuestionsDAO(getActivity());
        level = new PlayQuizLevel(letsLeanLevelID,NO_OF_QUESTION);
        level.setQuestion(questionDaoNew.getSingleAnswareQuestion(level));


        txtOption1 = (TextView)view.findViewById(R.id.txtOption1);
        txtOption2 = (TextView)view.findViewById(R.id.txtOption2);
        txtOption3 = (TextView)view.findViewById(R.id.txtOption3);
        txtOption1.setOnClickListener(this);
        txtOption4 = (TextView)view.findViewById(R.id.txtOption4);
        txtOption2.setOnClickListener(this);

        txtOption1.setTextColor(getResources().getColor(R.color.textPrimary));
        txtOption2.setTextColor(getResources().getColor(R.color.textPrimary));
        txtOption3.setTextColor(getResources().getColor(R.color.textPrimary));
        txtOption4.setTextColor(getResources().getColor(R.color.textPrimary));
        optionA.setTextColor(getResources().getColor(R.color.colorAccent));
        optionB.setTextColor(getResources().getColor(R.color.colorAccent));
        optionC.setTextColor(getResources().getColor(R.color.colorAccent));
        optionD.setTextColor(getResources().getColor(R.color.colorAccent));
        img1.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_button));
        img2.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_button));
        img3.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_button));
        img4.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_button));
        imgOption1.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_sky));
        imgOption2.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_sky));
        imgOption3.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_sky));
        imgOption4.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_sky));

        txtTrueQuestion = (TextView)view.findViewById(R.id.txtTrue);
        txtTrueQuestion.setText("0");
        txtFalseQuestion = (TextView)view.findViewById(R.id.txtFalse);
        txtFalseQuestion.setText("0");

        quizQuestion  = (TextView)view.findViewById(R.id.txtQuestion);

        animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in


        //totalScore  = mListener.getGameData().getTotalScore();
        txtScore = (TextView)view.findViewById(R.id.txtScore);

        txtScore.setText("Score: "+totalScore);
    }

    public void blankAllValue(){
        quextionIndex=0;
        // isSoundEffect=false;
        isVibration=false;

        totalScore=0;
        score=0;
        correctQuestion=0;
        inCorrectQuestion=0;
    }

    private boolean IsDirectAnswerShow() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        return sp.getBoolean("play_quiz_right_wrong_sound", true);
    }
}

