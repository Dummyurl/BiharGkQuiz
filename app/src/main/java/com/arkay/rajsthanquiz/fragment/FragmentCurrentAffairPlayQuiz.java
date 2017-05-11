package com.arkay.rajsthanquiz.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.arkay.rajsthanquiz.R;
import com.arkay.rajsthanquiz.activity.MainActivity;
import com.arkay.rajsthanquiz.application.MainApplication;
import com.arkay.rajsthanquiz.beans.CurrentAffairLevel;
import com.arkay.rajsthanquiz.beans.CurrentAffairQuestion;
import com.arkay.rajsthanquiz.beans.GameData;
import com.arkay.rajsthanquiz.facebook.AsyncFacebookRunner;
import com.arkay.rajsthanquiz.facebook.Facebook;
import com.arkay.rajsthanquiz.handler.CurrentAffairQuestionsDAO;
import com.arkay.rajsthanquiz.handler.QuestionsDAO;
import com.arkay.rajsthanquiz.util.ConnectionDetector;
import com.arkay.rajsthanquiz.util.Constants;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FragmentCurrentAffairPlayQuiz extends Fragment
        implements View.OnClickListener{

    private String TAG = FragmentSingleAnswareQuiz.class.getSimpleName();

    private CurrentAffairLevel level;
    private CurrentAffairQuestionsDAO questionDao;
    //private static int levelNo=1;
    private int quextionIndex=0;
    private boolean isSoundEffect;
    private boolean isVibration;

    private int NO_OF_QUESTION = 10;
    private int totalScore=0;
    private int score=0;
    private int correctQuestion=0;
    private int inCorrectQuestion=0;
    ImageView img1,img2,img3,img4,imgOption1,imgOption2,imgOption3,imgOption4;
    private SharedPreferences settings;
    private Animation animation;
    Animation animationFromRight,animationFromLeft;
    private MediaPlayer rightAnsware, wrongeAnsware;
    private final Handler mHandler = new Handler();
    private SharedPreferences.Editor editor;
    TextView txtTrue,scoretxt,txtScore,txtFalse,txtOutOfQue,txtQuestion,optionA,optionB,optionC,optionD,txtOption1,txtOption2,txtOption3,txtOption4;


    Animation in;
    Animation out;

    ImageButton btnBookmark;
    RelativeLayout relative1,relative2,relative3,relative4;
    Typeface tp,tpHindi;


    private ProgressDialog progress;

    View view;



    private ProgressDialog dialog;


    //public static String              APP_ID       = "262317043858138";
    private Facebook mFacebook    = null;
    private AsyncFacebookRunner mAsyncRunner = null;

    public interface Listener {
        public GameData getGameData();
        public void setPlayQuizCAQuestionsScoreBoard(ArrayList<CurrentAffairQuestion> playQuizQuestions);
        public FragmentScoreBoard getQuizFragmentScoreBoard();
    }
    Listener mListener = null;
    public static FragmentCurrentAffairPlayQuiz newInstance(Bundle bundle)
    {
        FragmentCurrentAffairPlayQuiz fragment = new FragmentCurrentAffairPlayQuiz();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play_quiz, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        view = getView();
        dialog = new ProgressDialog(getActivity());

        settings = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);


        tp = Typeface.createFromAsset(getActivity().getAssets(),
                "MarkoOne-Regular.ttf");
        tpHindi = Typeface.createFromAsset(getActivity().getAssets(),
                "olivier_demo.ttf");
        setAnimation();

        Bundle bundle = getArguments();

        Log.i("IS From PUSH: ", "" + bundle.getBoolean("isfrompush"));
        boolean isFromPush = bundle.getBoolean("isfrompush");

        level = new CurrentAffairLevel();

        if(isFromPush){
            level.setCurrentAffairLevelID(bundle.getInt(Constants.CURRENT_AFFAIR_LEVEL_ID));
        }else{
            level.setCurrentAffairLevelID(bundle.getInt(Constants.CURRENT_AFFAIR_LEVEL_ID));
        }

        Log.i(TAG, "Level Info: " + level.getCurrentAffairLevelID());

        setViews(view);

        setFacebookLoginCredential();


        // Obtain the shared Tracker instance.
        MainApplication application = (MainApplication) getActivity().getApplication();

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


    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, ""+ TAG +" Current Affiar Play Quiz");
    }
    private void setViews(View view)
    {
        optionA = (TextView) view.findViewById(R.id.optionA);
        optionB = (TextView) view.findViewById(R.id.optionB);
        optionC = (TextView) view.findViewById(R.id.optionC);
        optionD = (TextView) view.findViewById(R.id.optionD);
        txtOption1 = (TextView) view.findViewById(R.id.txtOption1);
        txtOption2 = (TextView) view.findViewById(R.id.txtOption2);
        txtOption3 = (TextView) view.findViewById(R.id.txtOption3);
        txtOption4 = (TextView) view.findViewById(R.id.txtOption4);
        btnBookmark = (ImageButton) view.findViewById(R.id.btnBookmark);
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
        txtTrue =  (TextView) view.findViewById(R.id.txtTrue);
        txtFalse =  (TextView) view.findViewById(R.id.txtFalse);
        txtQuestion =  (TextView) view.findViewById(R.id.txtQuestion);
        txtOutOfQue =  (TextView) view.findViewById(R.id.txtOutOfQue);

        txtScore =  (TextView) view.findViewById(R.id.txtScore);

        txtTrue.setTypeface(tp);
        txtScore.setTypeface(tp);
        txtFalse.setTypeface(tp);
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

        relative1.setOnClickListener(this);
        relative2.setOnClickListener(this);
        relative3.setOnClickListener(this);
        relative4.setOnClickListener(this);
        btnBookmark.setOnClickListener(this);

        resetAllValue();
    }

    public void setListener(Listener l) {
        mListener = l;
    }

    private void setFacebookLoginCredential()
    {


        mFacebook = new Facebook(getResources().getString(R.string.facebook_id));
        mAsyncRunner = new AsyncFacebookRunner(mFacebook);

    }

    private void setAnimation()
    {

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
        if(quextionIndex<level.getQuestion().size()){
//            relative1.setClickable(false);
//            relative2.setClickable(false);
//            relative3.setClickable(false);
//            relative4.setClickable(false);
            switch(v.getId()){
                case R.id.relative1:
                    level.getQuestion().get(quextionIndex).setAnsIndex(0);
                    if(txtOption1.getText().toString().trim().equalsIgnoreCase(level.getQuestion().get(quextionIndex).getTrueAns().trim())){
                        level.getQuestion().get(quextionIndex).setIsQuestionTrue(true);
                        quextionIndex++;

                        txtOption1.setTextColor(getResources().getColor(R.color.green));
                        img1.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_round_button));
                        imgOption1.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_green));
                        optionA.setTextColor(getResources().getColor(R.color.green));
                        addScore();
                        txtOption1.startAnimation(animation);
                    }else{
                        level.getQuestion().get(quextionIndex).setIsQuestionTrue(false);
                        wrongeQuestion();
                        quextionIndex++;
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
                        quextionIndex++;
                        txtOption2.setTextColor(getResources().getColor(R.color.green));
                        addScore();
                        txtOption2.startAnimation(animation);
                        img2.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_round_button));
                        imgOption2.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_green));
                        optionB.setTextColor(getResources().getColor(R.color.green));

                    }else{
                        level.getQuestion().get(quextionIndex).setIsQuestionTrue(false);
                        wrongeQuestion();
                        quextionIndex++;
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
                        quextionIndex++;
                        txtOption3.setTextColor(getResources().getColor(R.color.green));
                        addScore();
                        txtOption3.startAnimation(animation);
                        img3.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_round_button));
                        imgOption3.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_green));
                        optionC.setTextColor(getResources().getColor(R.color.green));
                    }else{
                        level.getQuestion().get(quextionIndex).setIsQuestionTrue(false);
                        wrongeQuestion();
                        quextionIndex++;
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
                        quextionIndex++;
                        txtOption4.setTextColor(getResources().getColor(R.color.green));
                        addScore();
                        txtOption4.startAnimation(animation);
                        img4.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_round_button));
                        imgOption4.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_green));
                        optionD.setTextColor(getResources().getColor(R.color.green));
                    }else{
                        level.getQuestion().get(quextionIndex).setIsQuestionTrue(false);
                        wrongeQuestion();
                        quextionIndex++;
                        txtOption4.setTextColor(getResources().getColor(R.color.red));
                        img4.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_round_button));
                        imgOption4.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_red));
                        optionD.setTextColor(getResources().getColor(R.color.red));
                    }
                    break;
                case R.id.btnBookmark:

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
                            dialog.cancel();
                        }
                    });

                    break;
            }
        }else{
            mHandler.postDelayed(mUpdateUITimerTask, 2 * 10);
        }
        mHandler.postDelayed(mUpdateUITimerTask, 2 * 1000);
        txtScore.setText("Score: " + totalScore);
    }

    private final Runnable mUpdateUITimerTask = new Runnable() {
        public void run() {
            txtOption1.clearAnimation();
            txtOption2.clearAnimation();
            txtOption3.clearAnimation();
            txtOption4.clearAnimation();
            nextQuizQuestion();

        }
    };


    private void nextQuizQuestion(){
        txtOption1.startAnimation(animationFromLeft);
        txtOption2.startAnimation(animationFromRight);
        txtOption3.startAnimation(animationFromLeft);
        txtOption4.startAnimation(animationFromRight);
        if(quextionIndex>=NO_OF_QUESTION){

            saveScore();

            CurrentAffairQuestionsDAO updateLavelPlay = new CurrentAffairQuestionsDAO(getActivity());
            updateLavelPlay.updateUserPlayQuizStatusAndScore(level.getCurrentAffairLevelID(), score);

           // ArrayList<>
            mListener.setPlayQuizCAQuestionsScoreBoard(level.getQuestion());
            getActivity().getSupportFragmentManager().popBackStack();
            getActivity().getSupportFragmentManager().beginTransaction().replace( R.id.ha_flContentContainer1, mListener.getQuizFragmentScoreBoard() ).addToBackStack( "tag" ).commit();
            blankAllValue();
        }else{

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
            if(quextionIndex<level.getQuestion().size()){
                int temp = quextionIndex;
                txtOutOfQue.setText(""+ ++temp+"/"+NO_OF_QUESTION);
                String imgName = level.getQuestion().get(quextionIndex).getQuestion();

                Pattern p = Pattern.compile(" ");
                Matcher m = p.matcher(imgName);
                imgName = m.replaceAll("_");
                txtQuestion.setText(level.getQuestion().get(quextionIndex).getQuestion());

                ArrayList<String> options = new ArrayList<String>();
                options.addAll(level.getQuestion().get(quextionIndex).getOptions());
                txtOption1.setText("" + options.get(0).trim());
                txtOption2.setText("" + options.get(1).trim());
                txtOption3.setText("" + options.get(2).trim());
                txtOption4.setText("" + options.get(3).trim());

                txtOption1.invalidate();
                txtOption2.invalidate();
                txtOption3.invalidate();
                txtOption4.invalidate();
                relative1.setClickable(true);
                relative2.setClickable(true);
                relative3.setClickable(true);
                relative4.setClickable(true);
            }
        }
    }


    private void addScore(){
        rightSound();
        correctQuestion++;
        txtTrue.setText(" "+correctQuestion +" ");
        totalScore = totalScore + 10;
        score = score + 10;
        txtScore.setText(" "+totalScore+" ");
        mListener.getGameData().setCurrentAffairTotalScore(totalScore);
        mListener.getGameData().saveDataLocal(settings);
    }


    private void wrongeQuestion(){
        playWrongSound();
        inCorrectQuestion++;
        totalScore = totalScore - 3;
        score = score - 3;
        txtFalse.setText(" "+ inCorrectQuestion +" ");

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

        if(totalScore<0){
            totalScore=0;
        }
    }

    private void saveScore(){
        editor = settings.edit();
        mListener.getGameData().setCurrentAffairTotalScore(totalScore);
        editor.putInt(MainActivity.LAST_LEVEL_SCORE, score);
        editor.putInt(MainActivity.CURRENT_AFAIR_TOTAL_SCORE,totalScore);
        editor.putInt(MainActivity.LAST_TIME_CURRENT_AFAIR_PLAY, level.getCurrentAffairLevelID());

        if(correctQuestion>=7){
            editor.putBoolean(MainActivity.IS_LAST_LEVEL_COMPLETED, true);

            mListener.getGameData().setCurrentAffairLevelCompleted(level.getCurrentAffairLevelID());
        }else{
            editor.putBoolean(MainActivity.IS_LAST_LEVEL_COMPLETED, false);
        }
        mListener.getGameData().saveDataLocal(settings);
        editor.putInt(MainActivity.LAST_LEVE_TRUE_ANS, correctQuestion);
        editor.putInt(MainActivity.LAST_LEVE_FALSE_ANS, inCorrectQuestion);
        editor.putBoolean(MainActivity.IS_LETS_LEARN_LEVEL_PLAY, false);
        editor.commit();
    }

    public void rightSound()
    {
        if(isSoundEffect){
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
        if(isSoundEffect){
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

        boolean isLoadFromLocal = false;
        questionDao = new CurrentAffairQuestionsDAO(getActivity());
        if(questionDao.getCurrentAffairQuestionsByLevelID(level.getCurrentAffairLevelID()).size()<=0){
            ConnectionDetector checkConnection = new ConnectionDetector(getActivity());
            if(checkConnection.isConnectingToInternet()){

                isLoadFromLocal=false;

                progress = new ProgressDialog(getActivity());
                progress.setTitle("Please Wait!!");
                progress.setMessage("Data Loading..");
                progress.setCancelable(true);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.show();
                String url = getResources().getString(R.string.get_current_affiar_question_from_web);
                url = url+""+level.getCurrentAffairLevelID();
                getCurrentAffairQuestion(url);
            }else{

                showDialogMsg(getResources().getString(R.string.connection_not_found),getResources().getString(R.string.first_time_requ_inte_conn));

            }
        }else{
            level.setQuestion(questionDao.getCurrentAffairQuestionsByLevelID(level.getCurrentAffairLevelID()));

            isLoadFromLocal = true;
             //load form local
        }


        txtOption1 = (TextView)view.findViewById(R.id.txtOption1);
        txtOption2 = (TextView)view.findViewById(R.id.txtOption2);
        txtOption3 = (TextView)view.findViewById(R.id.txtOption3);
        txtOption1.setOnClickListener(this);
        txtOption4 = (TextView)view.findViewById(R.id.txtOption4);
        txtOption4.setOnClickListener(this);

        txtTrue = (TextView)view.findViewById(R.id.txtTrue);
        txtTrue.setText("0");
        txtFalse = (TextView)view.findViewById(R.id.txtFalse);
        txtFalse.setText("0");

        txtQuestion  = (TextView)view.findViewById(R.id.txtQuestion);

        animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in


        totalScore  = mListener.getGameData().getCurrentAffairTotalScore();
        txtScore = (TextView)view.findViewById(R.id.txtScore);

        txtScore.setText("Score: "+totalScore);
        if(isLoadFromLocal){
            nextQuizQuestion();
        }
    }
    public void blankAllValue(){
        quextionIndex=0;
        isSoundEffect=false;
        isVibration=false;

        totalScore=0;
        score=0;
        correctQuestion=0;
        inCorrectQuestion=0;
    }


    public void getCurrentAffairQuestion(String url){

        // appending offset to url
       // String url = getResources().getString(R.string.get_current_affiar_question_from_web) + levelNo;

        Log.i(TAG, "New URL: " + url);

        // Volley's json array request object
        final JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        if (response.length() > 0) {

                            try {
                                // looping through json and adding to movies list
                                JSONArray jsonArray = new JSONArray();
                                for (int i = 0; i < response.length(); i++) {

                                    JSONObject c = response.getJSONObject(i);
                                    CurrentAffairQuestion tempQuestion = new CurrentAffairQuestion(c.getInt("queid"),c.getString("question"),c.getString("rightans"));
                                    String rightAns = c.getString("rightans");
                                    if(rightAns.equalsIgnoreCase("A")){
                                        tempQuestion.setTrueAns(c.getString("optiona"));
                                    }else if(rightAns.equalsIgnoreCase("B")){
                                        tempQuestion.setTrueAns(c.getString("optionb"));
                                    }else if(rightAns.equalsIgnoreCase("C")){
                                        tempQuestion.setTrueAns(c.getString("optionc"));
                                    }else{
                                        tempQuestion.setTrueAns(c.getString("optiond"));
                                    }

                                    ArrayList<String> option = new ArrayList<String>();
                                    option.add(c.getString("optiona"));
                                    option.add(c.getString("optionb"));
                                    option.add(c.getString("optionc"));
                                    option.add(c.getString("optiond"));

                                    tempQuestion.setOptions(option);
                                    level.addQuestion(tempQuestion);
                                    for(int j=0;j<level.getQuestion().size();j++){
                                        Collections.shuffle(level.getQuestion().get(j).getOptions());
                                    }
                                    if(progress.isShowing()) {
                                        progress.cancel();
                                    }
                                    nextQuizQuestion();
                                }

                            } catch (JSONException e) {
                                Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                            }

                        }else {
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Server Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        MainApplication.getInstance().addToRequestQueue(req);
    }

    public void showDialogMsg(String title, String msgDetail){
        new AlertDialog.Builder(getActivity(),  AlertDialog.THEME_HOLO_LIGHT)
                .setTitle(title)
                .setMessage(msgDetail)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).show();
    }
}

