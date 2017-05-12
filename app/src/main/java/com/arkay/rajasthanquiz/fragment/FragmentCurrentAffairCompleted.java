package com.arkay.rajasthanquiz.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import static com.arkay.rajasthanquiz.R.id.btnHome;

public class FragmentCurrentAffairCompleted extends Fragment implements
        View.OnClickListener {

    public Dialog d;
    private Button btnPlayAgain;
    private ImageButton btnFacebook, btnGooglePlus, btnShare;
    private TextView txtLevelHeading, txtLevelScore, txtLevelTotalScore;
    private SharedPreferences settings;

    private int levelNo = 1;
    private int lastLevelScore = 0;

    //public static String APP_ID = "598077716971222";
    private Facebook mFacebook = null;
    private AsyncFacebookRunner mAsyncRunner = null;
    private int totalScore = 0;
    private View v;

    public interface Listener {
        public void displyHomeScreen();
    }

    Listener mListener = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_game_over, container, false);
        final int[] CLICKABLES = new int[]{
                R.id.btnPlayAgain, R.id.btnFb,
                R.id.btnGp, R.id.btnShare, btnHome
        };
        for (int i : CLICKABLES) {
            v.findViewById(i).setOnClickListener(this);
        }
        settings = getActivity().getSharedPreferences(Constants.PREFS_NAME, 0);


        mFacebook = new Facebook(getResources().getString(R.string.facebook_id));
        mAsyncRunner = new AsyncFacebookRunner(mFacebook);

        txtLevelHeading = (TextView) v.findViewById(R.id.Level);

        txtLevelScore = (TextView) v.findViewById(R.id.txtLevelScore);
        totalScore = settings.getInt(MainActivity.CURRENT_AFAIR_TOTAL_SCORE, 0);
        txtLevelScore.setText("" + totalScore);

        lastLevelScore = settings.getInt(MainActivity.LAST_LEVEL_SCORE, 0);
        txtLevelTotalScore = (TextView) v.findViewById(R.id.txtTotalScore);
        txtLevelTotalScore.setText("" + lastLevelScore);

        btnPlayAgain = (Button) v.findViewById(R.id.btnPlayAgain);
        btnFacebook = (ImageButton) v.findViewById(R.id.btnFb);
        btnGooglePlus = (ImageButton) v.findViewById(R.id.btnGp);
        btnShare = (ImageButton) v.findViewById(R.id.btnShare);

        btnPlayAgain.setOnClickListener(this);
        btnFacebook.setOnClickListener(this);
        btnGooglePlus.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnPlayAgain.setText("Cancel");

        boolean islevelcomplted = settings.getBoolean(MainActivity.IS_LAST_LEVEL_COMPLETED, false);
        if (settings.getBoolean(MainActivity.IS_LAST_LEVEL_CATEGORY_PLAY, false)) {
            levelNo = settings.getInt(MainActivity.LAST_TIME_CURRENT_AFAIR_PLAY, 1);
            if (islevelcomplted) {
                txtLevelHeading.setText(getActivity().getString(R.string.level) + " " + levelNo + " " + getActivity().getResources().getString(R.string.finished));
                btnPlayAgain.setText("Play Next");
            } else {
                txtLevelHeading.setText(getActivity().getString(R.string.level) + " " + levelNo + " " + getActivity().getResources().getString(R.string.not_completed));
                btnPlayAgain.setText("Play Again");
            }
        } else {


            levelNo = settings.getInt(MainActivity.LAST_TIME_CURRENT_AFAIR_PLAY, 1);
            if (islevelcomplted) {
                txtLevelHeading.setText(getActivity().getString(R.string.level) + " " + levelNo + " " + getActivity().getResources().getString(R.string.finished));
                btnPlayAgain.setText("Play Next");
            } else {
                txtLevelHeading.setText(getActivity().getString(R.string.level) + " " + levelNo + " " + getActivity().getResources().getString(R.string.not_completed));
                btnPlayAgain.setText("Play Again");
            }
        }

        return v;
    }

    public void setListener(Listener l) {
        mListener = l;
    }

    public void facebookPost() {
        Bundle params = new Bundle();
        params.putString("name", "" + getResources().getString(R.string.app_name));
        params.putString("caption", "I'm playing #" + getResources().getString(R.string.app_hash_tag) + " Android App.");
        params.putString("description", "I'm just completed " + levelNo + " level on #" + getResources().getString(R.string.app_hash_tag) + " Current Affair with " + totalScore + " Can you beat my high score?.");
        params.putString("link", "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
//		 params.putString("picture", getActivity().getResources().getString(R.string.icon_url));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFb:
                facebookPost();
                break;
            case R.id.btnGp:
                Intent shareIntent = new PlusShare.Builder(getActivity())
                        .setType("text/plain")
                        .setText("" + getResources().getString(R.string.app_name) + "   I'm playing #" + getResources().getString(R.string.app_hash_tag) + " Android App and just completed " + levelNo + " levels with " + totalScore + " score Can you beat my high score?.")
                        .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName()))
                        .getIntent();
                getActivity().startActivityForResult(shareIntent, 0);

                break;
            case R.id.btnShare:
                Intent s = new Intent(Intent.ACTION_SEND);
                s.setType("text/plain");
                s.putExtra(Intent.EXTRA_SUBJECT, "" + getResources().getString(R.string.app_name));
                s.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_name) + "   I'm playing #" + getResources().getString(R.string.app_hash_tag) + " Android App and just completed " + levelNo + " levels with " + totalScore + " score Can you beat my high score?.");
                s.putExtra("url", "" + getResources().getString(R.string.app_url));
                getActivity().startActivity(Intent.createChooser(s, "" + getResources().getString(R.string.app_name)));
                break;
            case R.id.btnPlayAgain:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case btnHome:
                mListener.displyHomeScreen();
                break;
            default:
                break;
        }
    }
}