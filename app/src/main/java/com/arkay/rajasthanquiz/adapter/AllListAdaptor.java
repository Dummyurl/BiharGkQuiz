package com.arkay.rajasthanquiz.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.arkay.rajasthanquiz.R;
import com.arkay.rajasthanquiz.beans.LearnListData;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.ArrayList;

public class AllListAdaptor extends BaseAdapter {


    private Activity activity;
    private static ArrayList<LearnListData> learnListDatas;
    private LayoutInflater inflater = null;
    private TextView txtName, txtSerial,txtNoOfQue;
    private String[] bgColors;
    private int lastPosition = -1;
    private ImageView imgStatus;
    DonutProgress timer_progress;
    Typeface tp,tpHindi;

    public AllListAdaptor(Activity a, ArrayList<LearnListData> learnListDatas) {
        activity = a;
        this.learnListDatas=learnListDatas;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        bgColors = activity.getApplicationContext().getResources().getStringArray(R.array.app_serial_bg);

    }

    public int getCount() {
        return learnListDatas.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;

        if(convertView==null){
            vi = inflater.inflate(R.layout.item_level, null);
        }

        tp = Typeface.createFromAsset(activity.getAssets(),
                "MarkoOne-Regular.ttf");
        tpHindi = Typeface.createFromAsset(activity.getAssets(),
                "olivier_demo.ttf");
        imgStatus = (ImageView)vi.findViewById(R.id.imgStatus);
        txtSerial = (TextView) vi.findViewById(R.id.txtNumber);

        timer_progress = (DonutProgress) vi.findViewById(R.id.timer_progress);

        timer_progress.setFinishedStrokeWidth(activity.getResources().getDimension(R.dimen.arrowStrokeWidth));
        timer_progress.setUnfinishedStrokeWidth(activity.getResources().getDimension(R.dimen.arrowStrokeWidth));

        txtName = (TextView)vi.findViewById(R.id.txtLevelName);
        txtNoOfQue = (TextView)vi.findViewById(R.id.txtNoOfQue);
        txtName.setTypeface(tpHindi);
        if(learnListDatas.get(position).getLetLernScore().isLevelPlayed()) {
            if(learnListDatas.get(position).getLetLernScore().getLevelScore()>=50){
                imgStatus.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.ic_completed));
            }else{
                imgStatus.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.ic_played));
            }
        }else{
            imgStatus.setBackgroundDrawable(null);
        }

        int tempPosstion = position;
        tempPosstion++;
        txtName.setText("" + learnListDatas.get(position).getStrData()+ " "+"( " + ((tempPosstion * learnListDatas.get(position).getNumberCount()) - 9) +" to "+ tempPosstion * learnListDatas.get(position).getNumberCount() + " )");
        txtSerial.setText(String.valueOf(learnListDatas.get(position).getId()));
        Animation animation = AnimationUtils.loadAnimation(this.activity, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        vi.startAnimation(animation);
        lastPosition = position;
        System.out.println("count : "+learnListDatas.get(position).getLetLernScore().getLevelScore());

        txtNoOfQue.setText("Q. "+learnListDatas.get(position).getNumberCount());
        int per;
        if(learnListDatas.get(position).getLetLernScore().getLevelScore() <= 0)
            per = 0;
        else
            per = learnListDatas.get(position).getLetLernScore().getLevelScore();
        timer_progress.setProgress(per);
        return vi;
    }
}
