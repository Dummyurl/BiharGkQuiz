package com.arkay.rajsthanquiz.fragment;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arkay.rajsthanquiz.R;
import com.arkay.rajsthanquiz.activity.MainActivity;
import com.arkay.rajsthanquiz.application.MainApplication;
import com.arkay.rajsthanquiz.beans.PlayQuizQuestion;
import com.arkay.rajsthanquiz.handler.QuestionsDAO;
import com.arkay.rajsthanquiz.util.Constants;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.Tracker;

import java.util.List;

public class FragmentSingleAnswareQuiz extends Fragment
        implements OnClickListener, View.OnTouchListener {

    private String TAG = FragmentSingleAnswareQuiz.class.getSimpleName();
    private float x1 = 0, y1 = 0, x2 = 0, y2 = 0, x3 = 0, y3 = 0;
    private RelativeLayout rl;

    private TextView txtQuestion;
    private TextView txtCount,txtAnsware;

    private ImageButton btnNext;
    private ImageButton btnPrev;
    private ImageView btnBackList;
    private ImageButton btnBookmark;
    private Button btnshowanswer;
    private float moveCount;

    Typeface tp,tpHindi;


    public static boolean setSound = true;


    //  private GestureDetector mGestureDetector;
    private boolean isBookMarkScreen = false;
    public int selectedQuestion = 0;

    //public boolean isCategorySelect;

    Animation in;
    Animation out;

    InterstitialAd mInterstitialAd;

    List<PlayQuizQuestion> questionsList;

    private Tracker mTracker;
    public static FragmentSingleAnswareQuiz newInstance(Bundle bundle) {
        FragmentSingleAnswareQuiz fragment = new FragmentSingleAnswareQuiz();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.single_ans_quiz, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();


        getBundleArguments();
        setAnimation();
        setSound = getSoundStatus();

        setQuestionList();

        setViews(view);



    }



    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "Gk in Hindi Single Answer");
    }


    private void setViews(View view) {
        txtQuestion = (TextView) view.findViewById(R.id.txtQuestion);
        txtQuestion.setOnTouchListener(this);
        txtCount = (TextView) view.findViewById(R.id.txtConunt);

        tpHindi = Typeface.createFromAsset(getActivity().getAssets(),
                "olivier_demo.ttf");

        btnNext = (ImageButton) view.findViewById(R.id.btn_next);
        btnPrev = (ImageButton) view.findViewById(R.id.btn_prev);
       // btnBackList = (ImageView) view.findViewById(R.id.btn_jump);
        btnBookmark = (ImageButton) view.findViewById(R.id.img_bookmark);

        btnshowanswer = (Button) view.findViewById(R.id.btnshowanswer);
        btnshowanswer.setOnClickListener(this);

        txtAnsware = (TextView) view.findViewById(R.id.txtAnsware);
        setSelectedQuoteText();


        rl = (RelativeLayout) view.findViewById(R.id.linearLayout6);
        rl.setOnTouchListener(this);



        //	NextPrevButtonListener listener = new NextPrevButtonListener();
        btnNext.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
       // btnBackList.setOnClickListener(this);
        btnBookmark.setOnClickListener(this);

        Log.i(TAG,"IS Bookmark Screen: "+isBookMarkScreen);
        if (isBookMarkScreen) {
            btnBookmark.setImageResource(R.drawable.ic_book_on);
        }

        txtAnsware.setTypeface(tpHindi);
        txtQuestion.setTypeface(tpHindi);
    }

    private void setQuestionList() {
        Bundle bundle = getArguments();
        QuestionsDAO questionDao = new QuestionsDAO(getActivity());
        //ArrayList<PlayQuizQuestion> questions = new ArrayList<PlayQuizQuestion>();
        if(isBookMarkScreen){
            //questions = questionDao.getTotalSingleAnswareBookmarkQuestions();
            questionsList = questionDao.getBookmarkQuestions();
        }else {
            // isCategorySelect = bundle.getBoolean(Constants.IS_CATEGORY_SELECTE);
            int cateIndexORid = bundle.getInt(Constants.QUESTION_SELECT_INDEX_OR_CATEGORY_ID);

            //Log.i(HomeActivity.TAG, "IS Category Select: " + isCategorySelect);
            Log.i(MainActivity.TAG, "IS Category ID: " + cateIndexORid);

        }
    }

    private String getSelectedQuote(int currentPosition) {
        if (questionsList == null || questionsList.size() <= 0) {
            if (isBookMarkScreen)
                return "एक भी पसंदगी का सवाल नही है";
            else
                return "एक भी सवाल नही है";
        } else {
            if (questionsList.size() > currentPosition) {
                    return ((PlayQuizQuestion) questionsList.get(currentPosition)).getQuestion();

            } else {
                selectedQuestion = questionsList.size() - 1;
                    return ((PlayQuizQuestion) questionsList.get(selectedQuestion)).getQuestion();
            }
        }
    }


    private int getSelectedQuoteId(int currentPosition) {
        if (questionsList == null || questionsList.size() <= 0) {

            return -1;
        }
        return  questionsList.get(currentPosition).getQuestionID();
    }


    private boolean isQuoteBookMark(int currentPosition) {
        if (questionsList == null || questionsList.size() <= 0) {
            return false;
        }
        else {
            if (questionsList.size() > currentPosition) {
                return ((PlayQuizQuestion) questionsList.get(currentPosition)).isBookmark();
            } else {
                selectedQuestion = questionsList.size()-1;
                return ((PlayQuizQuestion) questionsList.get(selectedQuestion)).isBookmark();
            }
        }
    }


    private void getBundleArguments() {

        Bundle bundle = getArguments();
        isBookMarkScreen = bundle.getBoolean(Constants.IS_BOOKMARK_SCREEN);
    }

    private boolean getSoundStatus() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        System.out.println("Sound: " + sp.getBoolean("next_previous_sound", true));
        return sp.getBoolean("next_previous_sound", true);
    }

    private void setAnimation() {

        in = new AlphaAnimation(0.0f, 1.0f);
        in.setDuration(1000);

        out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(1000);
    }


    @Override
    public void onClick(View v) {
        Log.v("click", "");
        switch (v.getId()) {
            case R.id.img_bookmark:
                QuestionsDAO questionDAO = new QuestionsDAO(getActivity());
                boolean result = questionDAO.updateBookmark(getSelectedQuoteId(selectedQuestion), isQuoteBookMark(selectedQuestion) ? false : true);
                if (result) {
                    if (isQuoteBookMark(selectedQuestion)) {


                        btnBookmark.setImageResource(R.drawable.ic_book_off);


                    } else {

                        btnBookmark.setImageResource(R.drawable.ic_book_on);


                    }
                    if(questionsList.size()>1) {
                        (questionsList.get(selectedQuestion)).setIsBookmark(isQuoteBookMark(selectedQuestion) ? false : true );
                    }
                }

                if(isBookMarkScreen){
                    setQuestionList();
                    setSelectedQuoteText();
                }
                break;

            case R.id.btn_next:
                Log.v("Cick n next", "");
                setNextQuote();


                break;

            case R.id.btn_prev:
                setPrevQuote();


                break;
            case R.id.btnshowanswer:
                if (questionsList != null && questionsList.size() > 0) {
                    txtAnsware.setText(questionsList.get(selectedQuestion).getTrueAns());
                }
                break;

        }

    }

    private void setNextQuote() {
        if (setSound) {
            MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.beep1);
            mp.start();
            mp.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });

        }

        if(selectedQuestion>=1) {
            if (selectedQuestion % 20 == 0) {
                Log.i(TAG, "Ads Going to display");
                if (mInterstitialAd.isLoaded()) {
                    Log.i(TAG, "Ads load success fully");
                    mInterstitialAd.show();
                }
            }
        }
        Log.v(TAG, "===selectedQuestion====" + selectedQuestion);
        Log.v(TAG, "===getQuoteListCount()====" + getQuoteListCount());
        int tempSelectedQuote = selectedQuestion + 1;
        if (tempSelectedQuote < getQuoteListCount()) {

            // setQuoteIsRead();

            selectedQuestion++;
            setSelectedQuoteText();

            btnPrev.setEnabled(true);


            txtQuestion.startAnimation(out);
            txtQuestion.startAnimation(in);
            txtAnsware.startAnimation(out);
            txtAnsware.startAnimation(in);

        }
    }

    private void setBookMarkIcon() {
        if (isQuoteBookMark(selectedQuestion)) {

            btnBookmark.setImageResource(R.drawable.ic_book_on);

        } else {

            btnBookmark.setImageResource(R.drawable.ic_book_off);


        }
    }

    private void setPrevQuote() {
        if (setSound) {
            MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.beep1);
            mp.start();
            mp.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
        }
        Log.v(TAG, "===selectedQuestion====" + selectedQuestion);
        Log.v(TAG, "===getQuoteListCount()====" + getQuoteListCount());
        int tempSelectedQuote = selectedQuestion - 1;
        if (tempSelectedQuote > -1) {
            //setQuoteIsRead();
            selectedQuestion--;
            setSelectedQuoteText();

            btnNext.setEnabled(true);

            txtQuestion.startAnimation(out);
            // mSwitcher.setText("new text");
            txtQuestion.startAnimation(in);
            txtAnsware.startAnimation(out);
            txtAnsware.startAnimation(in);
        }

    }

    private void setSelectedQuoteText() {
        txtQuestion.setText(getSelectedQuote(selectedQuestion));
        if(IsDirectAnswerShow()) {
            if (questionsList != null && questionsList.size() > 0) {
                txtAnsware.setText(questionsList.get(selectedQuestion).getTrueAns());
            }
        }else{
            txtAnsware.setText("");
        }

        if (questionsList != null && questionsList.size() > 0)
            txtCount.setText(selectedQuestion + 1 + " of " + getQuoteListCount());
        else
            txtCount.setVisibility(View.INVISIBLE);

        setBookMarkIcon();

        if (selectedQuestion < 1) {
            btnPrev.setEnabled(false);
        }

        if (selectedQuestion == getQuoteListCount() - 1) {
            btnNext.setEnabled(false);
        }
    }


    private int getQuoteListCount() {
        if (questionsList != null)
            return questionsList.size();
        else
            return 0;
    }

    //============Touch Event=====================
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //System.out.println("custom_dialoug.....");
        switch (v.getId()) {
            case R.id.linearLayout6:
                touchChangeQuote(v, event);
                return true;
            case R.id.txtQuestion:
                touchChangeQuote(v, event);
                return true;
            default:
                break;
        }

        return false;
    }

    public boolean touchChangeQuote(View v, MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                x2 = event.getX();
                y2 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x3 = event.getX();
                y3 = event.getY();
                if (x1 < x3) {
                    moveCount = x3 - x1;
                    if (moveCount > 150) {
                        setPrevQuote();
                    }

                } else if (x1 > x3) {
                    moveCount = x1 - x3;
                    if (moveCount > 150) {
                        setNextQuote();
                    }

                }

                break;

        }
        return false;

    }

    private boolean IsDirectAnswerShow() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        // System.out.println("Sound: " + sp.getBoolean("is_direct_answer_show", false));
        return sp.getBoolean("is_direct_answer_show", false);
    }

}

