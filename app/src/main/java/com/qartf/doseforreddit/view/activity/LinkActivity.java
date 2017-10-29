package com.qartf.doseforreddit.view.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.presenter.utility.Constants;
import com.qartf.doseforreddit.presenter.utility.Constants.Auth;

import butterknife.BindView;

public class LinkActivity extends BaseActivityChild {

    @BindView(R.id.webview) WebView webView;

    @Override
    public int getContentLayout() {
        return R.layout.activity_link;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initComponents() {
        if (getIntent().getStringExtra("link") != null) {
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(getIntent().getStringExtra("link"));
        } else {
            login();
            webView.loadUrl(getIntent().getStringExtra("login"));
        }
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    private void login() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (url.contains(Auth.REDIRECT_URI)) {
                    Uri uri = Uri.parse(url);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LinkActivity.this);
                    if (uri.getQueryParameter("error") != null) {
                        sharedPreferences.edit().putString(getResources().getString(R.string.pref_access_code), Constants.ACCESS_DECLINED).apply();
                    } else {
                        String state = uri.getQueryParameter("state");
                        if (state.equals(Auth.STATE)) {
                            String code = uri.getQueryParameter("code");
                            sharedPreferences.edit().putString(getResources().getString(R.string.pref_access_code), code).apply();
                        }
                    }
                    LinkActivity.this.onBackPressed();
                }

                super.onPageFinished(view, url);
            }
        });
    }
}