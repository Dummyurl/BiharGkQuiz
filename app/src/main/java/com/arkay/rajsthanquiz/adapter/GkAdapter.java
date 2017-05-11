package com.arkay.rajsthanquiz.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arkay.rajsthanquiz.R;
import com.arkay.rajsthanquiz.beans.GKInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by India on 12-04-2017.
 */

public class GkAdapter extends BaseAdapter {
    private Activity activity;
    private List<GKInfo> gkInfos;
    private String[] bgColors;
    Typeface tp,tpHindi;
    private ImageView imgGk,imgLine;
    private TextView txtTitle,txtDesc,txtTimePublish,txtReadMore;
    int type;


    public GkAdapter(Activity activity, List<GKInfo> gkInfos,int type) {
        this.activity = activity;
        this.gkInfos = gkInfos;
        this.type = type;
        bgColors = activity.getApplicationContext().getResources().getStringArray(R.array.serial_level);
    }

    @Override
    public int getCount() {
        return gkInfos.size();
    }

    @Override
    public GKInfo getItem(int location) {
        return gkInfos.get(location);
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
            v = li.inflate(R.layout.item_gk, null);
        }else{
            v = convertView;
        }

        tp = Typeface.createFromAsset(activity.getAssets(),
                "Roboto-Regular.ttf");
        tpHindi = Typeface.createFromAsset(activity.getAssets(),
                "olivier_demo.ttf");


        imgGk = (ImageView)v.findViewById(R.id.imgGk);
        imgLine = (ImageView)v.findViewById(R.id.imgLine);

        txtTitle = (TextView) v.findViewById(R.id.txtTitle);
        txtDesc = (TextView) v.findViewById(R.id.txtDesc);
        txtTimePublish = (TextView) v.findViewById(R.id.txtTimePublish);
        txtReadMore = (TextView) v.findViewById(R.id.txtReadMore);

        txtTitle.setText(gkInfos.get(position).getTitle());
        txtDesc.setText(gkInfos.get(position).getDesc());
        txtTitle.setTypeface(tpHindi);
        txtDesc.setTypeface(tpHindi);
        txtTimePublish.setTypeface(tp);
        txtReadMore.setTypeface(tp);

        txtTimePublish.setText(""+gkInfos.get(position).getDate());

        String url;
        if(type == 1)
            url = activity.getResources().getString(R.string.get_gk_news_image_url) +  gkInfos.get(position).getImagepath();
        else
            url =  gkInfos.get(position).getImagepath();
        //url.replace("\\", "");
        Picasso.with(activity).load(url).placeholder(R.drawable.loading1)
                .error(R.drawable.loading1).into(imgGk);
        String imgline = bgColors[position % bgColors.length];
        System.out.println("line : "+imgline);

        int drawableResourceId = activity.getResources().getIdentifier(imgline, "drawable", activity.getPackageName());
        imgLine.setBackgroundResource(drawableResourceId);

        return v;
    }
}
