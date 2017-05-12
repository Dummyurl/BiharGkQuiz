package com.arkay.rajasthanquiz.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.arkay.rajasthanquiz.R;
import com.bluejamesbond.text.DocumentView;
import com.bluejamesbond.text.style.TextAlignment;


/**
 * Created by arkayapps on 01/09/15.
 */
public class FragmentAboutApp extends Fragment implements View.OnClickListener {

    private TextView txtAppName, txt,txtDevelopedby,txtAllRightReserve;
    private ImageButton btnFacebook, btnGooglePlus;
    private Typeface tp,tpHindi;
    private DocumentView documentView;

    public static FragmentAboutApp newInstance(Bundle bundle) {
        FragmentAboutApp fragment = new FragmentAboutApp();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_app, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.detail_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        tp = Typeface.createFromAsset(getActivity().getAssets(),
                "MarkoOne-Regular.ttf");

        tpHindi = Typeface.createFromAsset(getActivity().getAssets(),
                "olivier_demo.ttf");

        // Show the Up button in the action bar.
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        toolbar.setTitle("");

        txt = (TextView) view.findViewById(R.id.txtAppTitle);
        txt.setText("About");
        txtAllRightReserve = (TextView) view.findViewById(R.id.txtAllRightReserve);
        txtDevelopedby = (TextView) view.findViewById(R.id.txtDevelopedby);


        txt.setTypeface(tp);
        txtDevelopedby.setTypeface(tp);
        txtAllRightReserve.setTypeface(tp);
        setViews(view);

        documentView = addDocumentView(new StringBuilder().append(getResources().getString(R.string.about_text)).toString(), DocumentView.PLAIN_TEXT);
        documentView.getDocumentLayoutParams().setTextAlignment(TextAlignment.JUSTIFIED);
        documentView.getDocumentLayoutParams().setHyphenated(true);
        RelativeLayout linearLayout = (RelativeLayout)view.findViewById(R.id.linearLayout6);
        linearLayout.removeAllViews();
        linearLayout.addView(documentView);

        txtAppName.setTypeface(tp);
        btnFacebook = (ImageButton) view.findViewById(R.id.btnFacebook);
        btnGooglePlus = (ImageButton) view.findViewById(R.id.btnGooglePlus);

        btnFacebook.setOnClickListener(this);
        btnGooglePlus.setOnClickListener(this);

    }
    private void setViews(View view)  {
        txtAppName = (TextView) view.findViewById(R.id.txtAppName);

        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            txtAppName.setText(getResources().getString(R.string.app_name) + " " + pInfo.versionName +getResources().getString(R.string.copyright));
        }catch(PackageManager.NameNotFoundException e){
            txtAppName.setText(getResources().getString(R.string.app_name) );
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFacebook:
                String url = "http://facebook.com/arkayapps";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case R.id.btnGooglePlus:
                url = "http://plus.google.com/+Arkayapps";
                i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;

            default:
                break;
        }
    }

    public DocumentView addDocumentView(CharSequence article, int type) {
        return addDocumentView(article, type, true);
    }

    public DocumentView addDocumentView(CharSequence article, int type, boolean rtl) {
        final DocumentView documentView = new DocumentView(getActivity(), type);
        documentView.getDocumentLayoutParams().setTextColor(getResources().getColor(R.color.textSecondary));
       // documentView.getDocumentLayoutParams().setTextTypeface(tpHindi);
        documentView.getDocumentLayoutParams().setTextAlignment(TextAlignment.JUSTIFIED);
        documentView.getDocumentLayoutParams().setInsetPaddingLeft(15f);
        documentView.getDocumentLayoutParams().setInsetPaddingRight(15f);
        documentView.getDocumentLayoutParams().setInsetPaddingTop(15f);
        documentView.getDocumentLayoutParams().setInsetPaddingBottom(15f);
        documentView.getDocumentLayoutParams().setLineHeightMultiplier(1f);
        documentView.getDocumentLayoutParams().setReverse(rtl);
        documentView.setText(article);
        return documentView;
    }
}