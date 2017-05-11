package com.arkay.rajsthanquiz.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.arkay.rajsthanquiz.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class FragmentWebsiteView extends Fragment {


    private Context context;
    private View v;
    private WebView webView;

    public static FragmentWebsiteView newInstance(Bundle bundle)
    {
        FragmentWebsiteView fragment = new FragmentWebsiteView();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_website_view, container, false);
        this.context = getActivity();
        this.v = v;
        webView = (WebView) v.findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        boolean isprivacy_policy = getArguments ().getBoolean("isprivacy_policy");
        if(isprivacy_policy){
            startWebView(getResources().getString(R.string.privacy_policy_url));
        }else {
        }
        return v;
    }

    private void startWebView(String url) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource (WebView view, String url) {
                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                }
            }
            public void onPageFinished(WebView view, String url) {
                try{
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }

        });

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);

        //Load url in webview
        webView.loadUrl(url);


    }


    @Override
    public void onResume() {
        super.onResume();

    }


}

