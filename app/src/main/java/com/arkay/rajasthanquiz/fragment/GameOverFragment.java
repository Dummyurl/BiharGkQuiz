package com.arkay.rajasthanquiz.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.arkay.rajasthanquiz.R;
import com.arkay.rajasthanquiz.activity.MainActivity;
import com.arkay.rajasthanquiz.facebook.AsyncFacebookRunner;
import com.arkay.rajasthanquiz.facebook.DialogError;
import com.arkay.rajasthanquiz.facebook.Facebook;
import com.arkay.rajasthanquiz.facebook.FacebookError;
import com.arkay.rajasthanquiz.util.Constants;
import com.google.android.gms.plus.PlusShare;

/**
 * Created by India on 10-04-2017.
 */

public class GameOverFragment extends Fragment implements View.OnClickListener {

    Listener mListener = null;
    TextView great,Level,txtLevel,success,total_score,txtTotalScore,levelScore,txtLevelScore,share_score;
    Button btnHome,btnPlayAgain;
    Typeface tp;
    private Facebook mFacebook = null;
    ImageButton btnFb,btnGp,btnShare;
    int levelNo,totalScore,lastlevelScore;
    private AsyncFacebookRunner mAsyncRunner = null;
    private SharedPreferences settings;

    public static GameOverFragment newInstance(Bundle bundle) {
        GameOverFragment fragment = new GameOverFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game_over, container, false);
    }

    public void setListener(Listener l) {
        mListener = l;
    }

    public interface Listener {
        public void displyHomeScreen();
        public void playAgainCategory(int categoryID);
        public void playAgain();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();

        tp = Typeface.createFromAsset(getActivity().getAssets(),
                "MarkoOne-Regular.ttf");


        settings = getActivity().getSharedPreferences(MainActivity.PREFS_NAME, 0);
        great = (TextView) view.findViewById(R.id.great);
        Level = (TextView) view.findViewById(R.id.Level);
        txtLevel = (TextView) view.findViewById(R.id.txtLevel);
        //success = (TextView) view.findViewById(R.id.success);
        total_score = (TextView) view.findViewById(R.id.total_score);
        txtTotalScore = (TextView) view.findViewById(R.id.txtTotalScore);
        levelScore = (TextView) view.findViewById(R.id.levelScore);
        txtLevelScore = (TextView) view.findViewById(R.id.txtLevelScore);
        share_score = (TextView) view.findViewById(R.id.share_score);
        btnHome = (Button) view.findViewById(R.id.btnHome);
        btnPlayAgain = (Button) view.findViewById(R.id.btnPlayAgain);
        btnFb = (ImageButton) view.findViewById(R.id.btnFb);
        btnGp = (ImageButton) view.findViewById(R.id.btnGp);
        btnShare = (ImageButton) view.findViewById(R.id.btnShare);

        totalScore = settings.getInt(MainActivity.CURRENT_AFAIR_TOTAL_SCORE, 0);
        txtTotalScore.setText(""+totalScore);

        lastlevelScore = settings.getInt(MainActivity.LAST_LEVEL_SCORE, 0);
        txtLevelScore = (TextView)view.findViewById(R.id.txtLevelScore);
        txtLevelScore.setText(""+lastlevelScore);

        great.setTypeface(tp);
        Level.setTypeface(tp);
        txtLevel.setTypeface(tp);
      //  success.setTypeface(tp);
        total_score.setTypeface(tp);
        txtTotalScore.setTypeface(tp);
        levelScore.setTypeface(tp);
        txtLevelScore.setTypeface(tp);
        share_score.setTypeface(tp);
        btnHome.setOnClickListener(this);
        btnPlayAgain.setOnClickListener(this);
        btnFb.setOnClickListener(this);
        btnGp.setOnClickListener(this);
        btnShare.setOnClickListener(this);

        mFacebook = new Facebook(getResources().getString(R.string.facebook_id));
        mAsyncRunner = new AsyncFacebookRunner(mFacebook);

        boolean islevelcomplted = settings.getBoolean(MainActivity.IS_LAST_LEVEL_COMPLETED, false);
        if(settings.getBoolean(MainActivity.IS_LAST_LEVEL_CATEGORY_PLAY, false)){
            levelNo = settings.getInt(MainActivity.LAST_TIME_CURRENT_AFAIR_PLAY, 1);
            if (islevelcomplted) {
                //levelNo--;
                txtLevel.setText(getActivity().getString(R.string.level_text) + " " + levelNo + " " + getActivity().getResources().getString(R.string.finished));
                btnPlayAgain.setText("Play Next");
            } else {
                txtLevel.setText(getActivity().getString(R.string.level_text) + " " + levelNo + " " + getActivity().getResources().getString(R.string.not_completed));
                btnPlayAgain.setText("Play Again");
            }
        }else {

            levelNo = settings.getInt(MainActivity.LAST_TIME_CURRENT_AFAIR_PLAY, 1);
            if (islevelcomplted) {
                //levelNo--;
                txtLevel.setText(getActivity().getString(R.string.level_text) + " " + levelNo + " " + getActivity().getResources().getString(R.string.finished));
                btnPlayAgain.setText("Play Next");
            } else {
                txtLevel.setText(getActivity().getString(R.string.level_text) + " " + levelNo + " " + getActivity().getResources().getString(R.string.not_completed));
                btnPlayAgain.setText("Play Again");
            }
        }
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){

            case R.id.btnPlayAgain:
                getActivity().getSupportFragmentManager().popBackStack();
                boolean isCategoryPlay = settings.getBoolean(MainActivity.IS_LAST_LEVEL_CATEGORY_PLAY, false);
                if(isCategoryPlay){
                    int lastLevelPlayedID = settings.getInt(Constants.QUESTION_SELECT_INDEX_OR_CATEGORY_ID,0);
                    mListener.playAgainCategory(lastLevelPlayedID);
                }else{
                    mListener.playAgain();
                }
                break;

            case R.id.btnHome:
                mListener.displyHomeScreen();
                break;
            case R.id.btnFb:
                facebookPost();
                break;
            case R.id.btnGp:
                Intent shareIntent = new PlusShare.Builder(getActivity())
                        .setType("text/plain")
                        .setText(""+getResources().getString(R.string.app_name)+"   I'm playing #"+getResources().getString(R.string.app_hash_tag)+" Android App and just completed "+levelNo+" levels with "+totalScore+ " score Can you beat my high score?.")
                        .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id="+getActivity().getPackageName()))
                        .getIntent();
                getActivity().startActivityForResult(shareIntent, 0);

                break;
            case R.id.btnShare:
                Intent s = new Intent(Intent.ACTION_SEND);
                s.setType("text/plain");
                s.putExtra(Intent.EXTRA_SUBJECT, ""+getResources().getString(R.string.app_name));
                s.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name)+"   I'm playing #"+getResources().getString(R.string.app_hash_tag)+" Android App and just completed "+levelNo+" levels with "+totalScore+ " score Can you beat my high score?.");
                s.putExtra("url", ""+getResources().getString(R.string.app_url));
                getActivity().startActivity(Intent.createChooser(s, ""+getResources().getString(R.string.app_name)));
                break;
            default:break;
        }
    }

    public void facebookPost() {
        Bundle params = new Bundle();
        params.putString("name", ""+getResources().getString(R.string.app_name));
        params.putString("caption", "I'm playing #"+getResources().getString(R.string.app_hash_tag)+" Android App.");
        params.putString("description", "I'm just completed "+levelNo+" level on #"+getResources().getString(R.string.app_hash_tag)+" with "+totalScore+" Can you beat my high score?.");
        params.putString("link", "https://play.google.com/store/apps/details?id="+getActivity().getPackageName());
//        params.putString("picture", getActivity().getResources().getString(R.string.icon_url));
        mFacebook.dialog(getActivity(), "stream.publish", params, new Facebook.DialogListener() {

            @Override
            public void onFacebookError(FacebookError e) {
                // TODO handle error in publishing
            }

            @Override
            public void onError(DialogError e) {
                // TODO handle dialog errors
            }

            @Override
            public void onComplete(Bundle values) {
                Toast.makeText(getActivity(), "Post successful",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                // TODO user don't want to share and presses cancel button
            }
        });
    }
}
