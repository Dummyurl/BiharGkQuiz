package com.arkay.rajsthanquiz.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.arkay.rajsthanquiz.R;
import com.arkay.rajsthanquiz.beans.CurrentAffairLevel;
import java.util.List;

/**
 * Created by India on 13-04-2017.
 */

public class CurrentAffairAdapter extends BaseAdapter {
    private Activity activity;
    private List<CurrentAffairLevel> currentAffairs;
    Typeface tp,tpHindi;
    private ImageView imgStatus;
    private TextView txtNumber,txtLevelName,txtNoOfQue;


    public CurrentAffairAdapter(Activity activity, List<CurrentAffairLevel> currentAffairs) {
        this.activity = activity;
        this.currentAffairs = currentAffairs;
    }

    @Override
    public int getCount() {
        return currentAffairs.size();
    }

    @Override
    public CurrentAffairLevel getItem(int location) {
        return currentAffairs.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View v;
        if(convertView==null) {
            LayoutInflater li = activity.getLayoutInflater();
            v = li.inflate(R.layout.item_current_level, null);
        }else{
            v = convertView;
        }

        tp = Typeface.createFromAsset(activity.getAssets(),
                "MarkoOne-Regular.ttf");
        tpHindi = Typeface.createFromAsset(activity.getAssets(),
                "olivier_demo.ttf");



        imgStatus = (ImageView)v.findViewById(R.id.imgStatus);
        txtNumber = (TextView) v.findViewById(R.id.txtNumber);
        txtLevelName = (TextView) v.findViewById(R.id.txtLevelName);
        txtNoOfQue = (TextView) v.findViewById(R.id.txtNoOfQue);

        txtNoOfQue.setText("Score: "+currentAffairs.get(position).getCurrentAffairLevelScore());
        txtLevelName.setText(currentAffairs.get(position).getCurrentAfairStr());

        for (int i = 1;i<currentAffairs.size();i++){
            int j = position + 1;
            txtNumber.setText(""+ j);
        }
        if(currentAffairs.get(position).is_ca_level_play()){

            if(currentAffairs.get(position).getCurrentAffairLevelScore()>=40){
                imgStatus.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.ic_completed));
            }else{
                imgStatus.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.ic_played));
            }
        }else{
            imgStatus.setBackgroundDrawable(null);
        }

        txtNumber.setTypeface(tp);
        txtLevelName.setTypeface(tpHindi);
        txtNoOfQue.setTypeface(tp);

        return v;
    }
}
