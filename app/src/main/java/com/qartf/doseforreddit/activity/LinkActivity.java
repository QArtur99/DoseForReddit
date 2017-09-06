package com.qartf.doseforreddit.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.utility.Constants;
import com.qartf.doseforreddit.utility.Constants.Auth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LinkActivity extends AppCompatActivity {


    private static final String TAG = LinkActivity.class.getPackage().getName();
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.webview) WebView webView;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        String a = TAG;
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
                    if (uri.getQueryParameter("error") != null) {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LinkActivity.this);
                        sharedPreferences.edit().putString(getResources().getString(R.string.pref_access_code), Constants.ACCESS_DECLINED).apply();
                        LinkActivity.this.onBackPressed();
                    } else {
                        String state = uri.getQueryParameter("state");
                        if (state.equals(Auth.STATE)) {
                            String code = uri.getQueryParameter("code");
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LinkActivity.this);
                            sharedPreferences.edit().putString(getResources().getString(R.string.pref_access_code), code).apply();
                            LinkActivity.this.onBackPressed();
                        }
                    }
                }

                super.onPageFinished(view, url);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}