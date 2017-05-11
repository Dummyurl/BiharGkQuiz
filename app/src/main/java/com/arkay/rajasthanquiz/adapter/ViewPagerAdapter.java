package com.arkay.rajasthanquiz.adapter;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.arkay.rajasthanquiz.R;
import com.arkay.rajasthanquiz.beans.GkInfoDetail;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ViewPagerAdapter extends PagerAdapter
{
    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<GkInfoDetail> gkInfoDetails;
    ImageView imgflag;
    TextView lblStoryDetail;
    public TextView txtStoryTitle;
    private TextView lblPublish, lblcredit;
    long startDate;
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    int type;

    public ViewPagerAdapter(Context context, ArrayList<GkInfoDetail> gkInfoDetails,int type)
    {
        this.context = context;
        this.gkInfoDetails = gkInfoDetails;
        this.type = type;
    }

    @Override
    public int getCount()
    {
        return gkInfoDetails.size();

    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view == ((LinearLayout) object);
    }

    public float getPageWidth(int position)
    {
        return 1f;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position)
    {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.viewpager_item, container,false);
        lblStoryDetail = (TextView) itemView.findViewById(R.id.lblStoryDetail);
        txtStoryTitle = (TextView) itemView.findViewById(R.id.txtStoryTitle);
        lblPublish = (TextView) itemView.findViewById(R.id.lblPublish);
        lblcredit = (TextView) itemView.findViewById(R.id.lblcredit);

        lblStoryDetail.setText(gkInfoDetails.get(position).getDesc());

        txtStoryTitle.setText(gkInfoDetails.get(position).getTitle());
        lblPublish.setText(gkInfoDetails.get(position).getDate());
        lblcredit.setText("Credit: "+gkInfoDetails.get(position).getImgcredit());
        imgflag = (ImageView) itemView.findViewById(R.id.flag);

        // Capture position and set to the ImageView
        String url;
        if(type == 1)
            url = context.getResources().getString(R.string.get_gk_news_image_url) +  gkInfoDetails.get(position).getImagepath();
        else
            url =  gkInfoDetails.get(position).getImagepath();

        System.out.println("url : "+url);
        //url.replace("\\", "");
		Picasso.with(context)
				.load(url)
				.placeholder(R.drawable.loading1)
				.error(R.drawable.loading1)
				.resize((int) context.getResources().getDimension(R.dimen.home_banner_w_size), (int) context.getResources().getDimension(R.dimen.home_banner_size))
				.onlyScaleDown()
				.centerInside()
				.into(imgflag);

        ((ViewPager) container).addView(itemView);


        return itemView;
    }


    public String getTimeString(String time){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = sdf.parse(time);
            startDate = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String timing = getTimeAgo(startDate,context);
        return timing;
    }

    public static String getTimeAgo(long time, Context ctx) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((LinearLayout) object);
    }
}
