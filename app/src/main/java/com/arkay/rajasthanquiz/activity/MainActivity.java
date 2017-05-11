package com.arkay.rajasthanquiz.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.arkay.rajasthanquiz.R;
import com.arkay.rajasthanquiz.application.MainApplication;
import com.arkay.rajasthanquiz.beans.CurrentAffairQuestion;
import com.arkay.rajasthanquiz.beans.GameData;
import com.arkay.rajasthanquiz.fragment.CategoryScreenFragment;
import com.arkay.rajasthanquiz.fragment.CurrentAffairFragment;
import com.arkay.rajasthanquiz.fragment.FragmentAboutApp;
import com.arkay.rajasthanquiz.fragment.FragmentCurrentAffairCompleted;
import com.arkay.rajasthanquiz.fragment.FragmentCurrentAffairPlayQuiz;
import com.arkay.rajasthanquiz.fragment.FragmentFourOptionPlayQuiz;
import com.arkay.rajasthanquiz.fragment.FragmentGKListRefresh;
import com.arkay.rajasthanquiz.fragment.FragmentGkInDetail;
import com.arkay.rajasthanquiz.fragment.FragmentLetsLearnPlayQuiz;
import com.arkay.rajasthanquiz.fragment.FragmentQuizCompleted;
import com.arkay.rajasthanquiz.fragment.FragmentScoreBoard;
import com.arkay.rajasthanquiz.fragment.FragmentSingleAnswareQuiz;
import com.arkay.rajasthanquiz.fragment.FragmentWebsiteView;
import com.arkay.rajasthanquiz.fragment.GameOverFragment;
import com.arkay.rajasthanquiz.fragment.HomeFragment;
import com.arkay.rajasthanquiz.fragment.LetsLearnLevelFragment;
import com.arkay.rajasthanquiz.fragment.PlaceMapFragment;
import com.arkay.rajasthanquiz.handler.BookMarkDatabaseHelper;
import com.arkay.rajasthanquiz.handler.DatabaseHelper;
import com.arkay.rajasthanquiz.handler.QuestionsDAO;
import com.arkay.rajasthanquiz.util.ConnectionDetector;
import com.arkay.rajasthanquiz.util.Constants;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.inmobi.sdk.InMobiSdk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,HomeFragment.Listener,
        CategoryScreenFragment.Listener,
        LetsLearnLevelFragment.Listener,FragmentFourOptionPlayQuiz.Listener,
        FragmentCurrentAffairPlayQuiz.Listener,FragmentLetsLearnPlayQuiz.Listener,PlaceMapFragment.Listener,
        FragmentGKListRefresh.Listener,GameOverFragment.Listener,CurrentAffairFragment.Listener,
        FragmentQuizCompleted.Listener,FragmentCurrentAffairCompleted.Listener,FragmentScoreBoard.Listener,
        FragmentGkInDetail.Listener{

    ArrayList<CurrentAffairQuestion> playQuizCAQuestionsScoreBoard;
    GameOverFragment quizCompletedFragment;
    LetsLearnLevelFragment letsLearnActivity;
    CategoryScreenFragment categoryScreenActivity;
    HomeFragment homeFragment;
    CurrentAffairFragment currentAffairActivity;
    PlaceMapFragment projectMapFragment;
    private FragmentCurrentAffairPlayQuiz mFragmentCurrentAffairPlayQuiz;
    FragmentGKListRefresh gkActivity;
    private FragmentLetsLearnPlayQuiz mFragmentLetsLearnPlayQuiz;
    FragmentSingleAnswareQuiz fragmentSingleAnswareQuiz;
    HomeFragment mFragementHomeScreen;
    private FragmentSingleAnswareQuiz mFragmentSingleAnswareQuiz;
    private FragmentWebsiteView mFragmentWebsiteView;
    public static String isWhichType = "isWhichType";
    public static String LEVEL_NO = "level_no";
    private GameData gameData;
    FragmentScoreBoard mQuizFragmentScoreBoard;
    private FragmentQuizCompleted fragmentQuizCompleted;
    private FragmentAboutApp mFragmentTermsOfUse;

    public static final String TOTAL_SCORE = "total_score";
    public static final String LEVEL ="level";
    public static final String TAG = MainApplication.class.getSimpleName();

    //Achivement
    public static final String LEVEL_COMPLETED = "level_completed";
    public static final String IS_LAST_LEVEL_COMPLETED = "is_last_level_completed";
    public static final String LAST_LEVEL_SCORE = "last_level_score";
    public static final String HOW_MANY_TIMES_PLAY_QUIZ = "how_many_time_play_quiz";
    public static final String COUNT_QUESTION_COMPLETED = "count_question_completed";
    public static final String COUNT_RIGHT_ANSWARE_QUESTIONS = "count_right_answare_questions";

    private int userLevel = 1;
    //Achevement data for Current Affair
    public static final String CURRENT_AFAIR_TOTAL_SCORE = "current_affair_totalScore";
    public static final String CURRENT_AFAIR_LEVEL_COMPLETED="current_affair_levelCompleted";
    public static final String IS_LAST_PLAY_GAME_CURRENT_AFFAIR = "last_play_game";
    public static final String LET_LERAN_LAST_LEVEL_COMPLETED = "let_learn_last_level_complete";


    //LearnScoreJSON
    public static final String LETS_LEARN_SCORE="let_lrn_scr";
    public static final String LEVEL_ID = "lvlID";
    public static final String LEVEL_SCORE = "lvlScr";
    public static final String IS_LEVEL_PLAY = "isLvlP";
    public static final String LET_LEARN_SCORE = "letlrnscr";
    public static final String ALL_LET_LEARN_SCORE = "allLetLrnScr";


    public static final String LAST_TIME_CURRENT_AFAIR_PLAY = "last_time_curr_afair_play";
    public static final String IS_LAST_LEVEL_CATEGORY_PLAY = "is_last_level_category_play";


    public static final String LAST_LEVE_TRUE_ANS="last_level_true_question";
    public static final String LAST_LEVE_FALSE_ANS="last_level_false_question";
    public static final String IS_LETS_LEARN_LEVEL_PLAY = "is_lets_learn_level_play";
    public static final String PREFS_NAME = "preferences";
    public static final String TYPE = "type";


    GameOverFragment gameOverFragment;
    private FragmentCurrentAffairCompleted mfragmentCurrentAffairCompleted;
    SharedPreferences settings;
    private FragmentFourOptionPlayQuiz fragmentFourOptionPlayQuiz;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //InMobiSdk.init(this, "7459daf7a94f4731830f0a8af80e7112"); //'this' is used specify context
        InMobiSdk.init(this, "80bce36c226f4e9fbec83d381ed4c8bd"); //'this' is used specify context

        checkDB();

        mfragmentCurrentAffairCompleted = new FragmentCurrentAffairCompleted();
        mfragmentCurrentAffairCompleted.setListener(this);
        settings = getSharedPreferences(MainApplication.PREFS_NAME, 0);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        fragmentQuizCompleted = new FragmentQuizCompleted();
        fragmentQuizCompleted.setListener(this);

        quizCompletedFragment = new GameOverFragment();
        quizCompletedFragment.setListener(this);

        gameOverFragment = new GameOverFragment();
        gameOverFragment.setListener(this);

        mQuizFragmentScoreBoard = new FragmentScoreBoard();
        mQuizFragmentScoreBoard.setListener(this);


        Bundle bundle = new Bundle();
        homeFragment = homeFragment.newInstance(bundle);
        homeFragment.setListener(this);
        homeFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.ha_flContentContainer1, homeFragment).commit();
        gameData = new GameData(settings);

        if (getGameData().getLevelCompleted() >= 0 && getGameData().getLevelCompleted() <= Constants.MODULE1_LEVEL) {
            //txtTitle.setText(Constants.MODULE1);
            userLevel  = Constants.TOO_EASY_1;
        }else if (getGameData().getLevelCompleted() >= Constants.MODULE1_LEVEL && getGameData().getLevelCompleted() <= Constants.MODULE2_LEVEL) {
           // txtTitle.setText(Constants.MODULE2);
            userLevel  = Constants.EASY_2;
        }else if (getGameData().getLevelCompleted() >= Constants.MODULE2_LEVEL && getGameData().getLevelCompleted() <= Constants.MODULE3_LEVEL) {
           // txtTitle.setText(Constants.MODULE3);
            userLevel  = Constants.MIDEAM_3;
        }else if (getGameData().getLevelCompleted() >= Constants.MODULE3_LEVEL && getGameData().getLevelCompleted() <= Constants.MODULE4_LEVEL) {
            //txtTitle.setText(Constants.MODULE4);
            userLevel  = Constants.HARD_4;
        }else if (getGameData().getLevelCompleted() >= Constants.MODULE4_LEVEL && getGameData().getLevelCompleted() <= Constants.MODULE5_LEVEL) {
            //txtTitle.setText(Constants.MODULE5);
            userLevel  = Constants.TOO_HARD_5;
        }else{
            userLevel = Constants.ALL_ROUNDER;
        }

        // Obtain the shared Tracker instance.
        MainApplication application = (MainApplication) getApplication();
        mTracker = application.getDefaultTracker();


    }
    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Rajasthan Quiz Home Screen");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void checkDB() {
        try {
            String packageName = this.getPackageName();
            String destPath = "/data/data/" + packageName + "/databases";
            String fullPath = "/data/data/" + packageName + "/databases/"+ DatabaseHelper.DATABASE_NAME;
            //String fullPath = "/data/data/" + packageName + "/databases/database.db";//+ DatabaseHelper.DATABASE_NAME;
            // this database folder location
            File f = new File(destPath);
            // this database file location
            File obj = new File(fullPath);
            // check if databases folder exists or not. if not create it
            if (!f.exists()) {
                f.mkdirs();
                f.createNewFile();
                Log.i(TAG, "create new file");
            }else{
                Log.i(TAG, "File Alredy Exits");
                //boolean isDelete = f.delete();
                // Log.i("Delete", "Delete"+isDelete);

            }
            // check database file exists or not, if not copy database from assets
            if (!obj.exists()) {
                this.CopyDB(fullPath);
                Log.i(TAG, "Database not exits");
                Log.i(TAG, "write new file");

            }else{
                DatabaseHelper databaseHalper = new DatabaseHelper(getBaseContext());
                SQLiteDatabase db = databaseHalper.getWritableDatabase();
                Log.i(TAG, "Database exits");
                String sql = "ALTER TABLE " + DatabaseHelper.CURRENT_AFFAIR_LEVEL_TAB + " ADD COLUMN " +
                        DatabaseHelper.CURRENT_AFFAIR_IS_LEVEL_PLAY + " BOOLEAN";
                Log.i("Database Upgrade Call: ", "" + sql);
                //db.execSQL(sql);
                try {
                    db.execSQL(sql);
                } catch (SQLException e) {
                    Log.i("ADD COLUMN ", "ca_isLevel_play already exists");
                }
                // QuestionsDAO qest  = new QuestionsDAO(this);
                // Log.i("Gk in Gujarat", " Al ready Exits Bookmark: " + qest.getBookmarkedQuestion().size());

            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        CopyOldBookmartoNew();

    }

    public void CopyDB(String path) throws IOException {

        InputStream databaseInput = null;
        String outFileName = path;
        OutputStream databaseOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;

        // open database file from asset folder
        databaseInput = this.getAssets().open(DatabaseHelper.DATABASE_NAME);
        while ((length = databaseInput.read(buffer)) > 0) {
            databaseOutput.write(buffer, 0, length);
            databaseOutput.flush();
        }
        databaseInput.close();

        databaseOutput.flush();
        databaseOutput.close();
    }

    public void CopyOldBookmartoNew(){
        String packageName = this.getPackageName();
        String fullPath = "/data/data/" + packageName + "/databases/"+ BookMarkDatabaseHelper.DATABASE_NAME;
        File obj = new File(fullPath);

        // check database file exists or not, if not copy database from assets
        if (obj.exists()) {
            //this.CopyDB(fullPath);
            Log.i("Gk in Gujarat", "OLD BOOKMARK FILE EXITS");
            QuestionsDAO qest  = new QuestionsDAO(this);
            ArrayList<Integer> oldBookmarks = qest.getOLDBookmarkedQuestionIDs();

            for(Integer t : oldBookmarks){
                Log.i("Gk in Gujarat", "Bookmark ID: "+t);
                qest.updateBookmark(t, true);
                qest.updateOLDBookmark(t,"0");
            }
        }else {
            Log.i("Gk in Gujarat", "OLD BOOKMARK FILE NOT EXITS");
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            displyHomeScreen();
            // Handle the camera action
        }  else if (id == R.id.nav_more_app) {
            Intent moreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Arkay+Apps"));
            moreIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            startActivity(moreIntent);
        } else if (id == R.id.nav_like_us) {
            Intent moreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/arkayapps"));
            moreIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            startActivity(moreIntent);
        } else if (id == R.id.nav_privacy_policy) {
            Bundle bundle = new Bundle();
            setWebViewragment(bundle,true);

        } else if (id == R.id.nav_about_us) {
            Bundle bundle = new Bundle();
            setTermsOfUseFragment(bundle);

        }else if (id == R.id.nav_bookmark) {

            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.IS_BOOKMARK_SCREEN, true);
            setSingleAnswareQuizFragment(bundle);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setSingleAnswareQuizFragment(Bundle bundle){
        mFragmentSingleAnswareQuiz = FragmentSingleAnswareQuiz.newInstance(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.ha_flContentContainer1, mFragmentSingleAnswareQuiz).addToBackStack("tag").commit();
    }


    public GameOverFragment GameOver(){
        return gameOverFragment;
    }


    public void LetsLearnLevel(){
        Bundle bundle = new Bundle();
        letsLearnActivity = LetsLearnLevelFragment.newInstance(bundle);
        letsLearnActivity.setListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.ha_flContentContainer1, letsLearnActivity).addToBackStack("tag").commit();
    }

    public void CategoryScreen(){
        Bundle bundle = new Bundle();
        categoryScreenActivity = CategoryScreenFragment.newInstance(bundle);
        categoryScreenActivity.setListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.ha_flContentContainer1, categoryScreenActivity).addToBackStack("tag").commit();
    }
    public void CurrentAffair(){
        Bundle bundle = new Bundle();
        currentAffairActivity = CurrentAffairFragment.newInstance(bundle);
        currentAffairActivity.setListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.ha_flContentContainer1, currentAffairActivity).addToBackStack("tag").commit();
    }

    public void GkLevel(){
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE,1);//1 for gk
        gkActivity = FragmentGKListRefresh.newInstance(bundle);
        gkActivity.setListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.ha_flContentContainer1, gkActivity).addToBackStack("tag").commit();
    }

    public FragmentScoreBoard getQuizFragmentScoreBoard(){
        return mQuizFragmentScoreBoard;
    }

    public void displyHomeScreen() {
        Bundle bundle = new Bundle();
        setHomeScreen(bundle);
    }
    private void setHomeScreen(Bundle bundle) {

        mFragementHomeScreen = HomeFragment.newInstance(bundle);
        mFragementHomeScreen.setListener(this);

        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStack(getSupportFragmentManager().getBackStackEntryAt(0).getId(), 0);
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().beginTransaction().replace(R.id.ha_flContentContainer1, mFragementHomeScreen, mFragementHomeScreen.getClass().getSimpleName()).commit();
    }

    private void setWebViewragment(Bundle bundle, boolean isprivacy_policy)
    {
        ConnectionDetector connectionDetector = new ConnectionDetector(getApplicationContext());
        if (connectionDetector.isConnectingToInternet()) {
            mFragmentWebsiteView = FragmentWebsiteView.newInstance(bundle);
            bundle.putBoolean("isprivacy_policy",isprivacy_policy);
            mFragmentWebsiteView.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.ha_flContentContainer1, mFragmentWebsiteView).addToBackStack("tag").commit();
        } else {
            showDialogMsg(getResources().getString(R.string.connection_not_found), getResources().getString(R.string.visit_web_site_inte_conne_require));
        }
    }

    public void showDialogMsg(String title, String msgDetail){
        new AlertDialog.Builder(this,  AlertDialog.THEME_HOLO_LIGHT)
                .setTitle(title)
                .setMessage(msgDetail)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).show();
    }

    @Override
    public GameData getGameData() {
        return this.gameData;
    }


    public void playQuizLeatsLearnMode(int selectedIndexOrCategoryID){
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.LETS_LEARN_LEVEL_ID,selectedIndexOrCategoryID);
        setLetsLearnPlaQuiz(bundle, selectedIndexOrCategoryID);
    }

    public void setLetsLearnPlaQuiz(Bundle bundle,int levelID){
        bundle.putInt(Constants.LETS_LEARN_LEVEL_ID,levelID);
        mFragmentLetsLearnPlayQuiz = FragmentLetsLearnPlayQuiz.newInstance(bundle);
        mFragmentLetsLearnPlayQuiz.setListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.ha_flContentContainer1, mFragmentLetsLearnPlayQuiz).addToBackStack("tag").commit();
    }

    public void playCategoryQuiz(int selectedIndexOrCategoryID){
        Bundle bundle = new Bundle();
        setPlayQuizFourOptionFragment(bundle, true, selectedIndexOrCategoryID);
    }

    private void setPlayQuizFourOptionFragment(Bundle bundle, boolean isCategorySelected, int categoryID){
        bundle.putBoolean(Constants.IS_CATEGORY_SELECTE, isCategorySelected);
        bundle.putInt(Constants.QUESTION_SELECT_INDEX_OR_CATEGORY_ID, categoryID);
        fragmentFourOptionPlayQuiz = FragmentFourOptionPlayQuiz.newInstance(bundle);
        fragmentFourOptionPlayQuiz.setListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.ha_flContentContainer1, fragmentFourOptionPlayQuiz).addToBackStack(null).commit();
    }

    public FragmentQuizCompleted getFragmentQuizCompleted(){
        return fragmentQuizCompleted;
    }

    public int getUserLevel() {
        return this.userLevel;
    }



    public FragmentCurrentAffairCompleted getCurrentAffairCompletedFragment(){
        return mfragmentCurrentAffairCompleted;
    }

    @Override
    public void playAgainCategory(int categoryID) {
        Bundle bundle = new Bundle();
        setPlayQuizFourOptionFragment(bundle,true,categoryID);
    }

    @Override
    public void playAgain() {
        Bundle bundle = new Bundle();
        setPlayQuizFourOptionFragment(bundle,false,0);
    }

    public ArrayList<CurrentAffairQuestion> getCAScoreBoardQuestion(){
        return this.playQuizCAQuestionsScoreBoard;
    }

    public void setPlayQuizCAQuestionsScoreBoard(ArrayList<CurrentAffairQuestion> caPlayQuizQuestions){
        this.playQuizCAQuestionsScoreBoard = caPlayQuizQuestions;
    }

    public void playCurrentAffairQuiz(int currentAffairLevelID){
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.CURRENT_AFFAIR_LEVEL_ID, currentAffairLevelID);
        setPlayCurrentAffairQuiz(bundle);
    }

    private void setPlayCurrentAffairQuiz(Bundle bundle){
        mFragmentCurrentAffairPlayQuiz = FragmentCurrentAffairPlayQuiz.newInstance(bundle);
        mFragmentCurrentAffairPlayQuiz.setListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.ha_flContentContainer1, mFragmentCurrentAffairPlayQuiz).addToBackStack("tag").commit();
    }

    public void clickOnPlayQuiz(){
        Bundle bundle = new Bundle();
        setPlayQuizFourOptionFragment(bundle,false,0);

    }


    public void setTermsOfUseFragment(Bundle bundle) {
        mFragmentTermsOfUse = FragmentAboutApp.newInstance(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.ha_flContentContainer1, mFragmentTermsOfUse).addToBackStack("tag").commit();
    }

    public void clickOnFamousPlace(){
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE,2);//2 for famous place
        gkActivity = FragmentGKListRefresh.newInstance(bundle);
        gkActivity.setListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.ha_flContentContainer1, gkActivity).addToBackStack("tag").commit();
    }

    public void clickOnFamousPerson(){
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE,3);//3 for famous person
        gkActivity = FragmentGKListRefresh.newInstance(bundle);
        gkActivity.setListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.ha_flContentContainer1, gkActivity).addToBackStack("tag").commit();
    }

    public void setGKDetailScreen(Bundle bundle,int newsID,int type){
        FragmentGkInDetail mFragmentGKNewsDetail = FragmentGkInDetail.newInstance(bundle);
       //bundle.putBoolean("isStoryFromPush",false);
        bundle.putInt(FragmentGKListRefresh.NEWS_ID, newsID);
        bundle.putInt(FragmentGKListRefresh.TYPE, type);
        mFragmentGKNewsDetail.setArguments(bundle);
        mFragmentGKNewsDetail.setListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.ha_flContentContainer1, mFragmentGKNewsDetail).addToBackStack("tag").commit();
    }

    public void setPlaceMap(Bundle bundle,String  lat,String  log,String name){
        PlaceMapFragment mFragmentGKNewsDetail = PlaceMapFragment.newInstance(bundle);
        //bundle.putBoolean("isStoryFromPush",false);
        bundle.putString(FragmentGkInDetail.LATITUDE, lat);
        bundle.putString(FragmentGkInDetail.LOGITUDE, log);
        bundle.putString(FragmentGkInDetail.TITLE, name);
        mFragmentGKNewsDetail.setArguments(bundle);
        mFragmentGKNewsDetail.setListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.ha_flContentContainer1, mFragmentGKNewsDetail).addToBackStack("tag").commit();
    }

    public void saveDataToLocal(){
        gameData.saveDataLocal(settings);
    }

    @Override
    public void saveDataToCloud() {
        saveToCloud();
    }

    public   void saveToCloud() {
            saveDataToLocal();
    }


}
