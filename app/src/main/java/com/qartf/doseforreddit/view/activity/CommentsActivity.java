package com.qartf.doseforreddit.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qartf.doseforreddit.BuildConfig;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.data.entity.Post;
import com.qartf.doseforreddit.presenter.comment.CommentMVP;
import com.qartf.doseforreddit.presenter.root.App;
import com.qartf.doseforreddit.presenter.utility.Constants.Pref;
import com.qartf.doseforreddit.presenter.utility.Utility;
import com.qartf.doseforreddit.view.fragment.DetailFragment;

import javax.inject.Inject;

import butterknife.BindView;

public class CommentsActivity extends BaseActivityChild implements DetailFragment.DetailFragmentInt {

    @BindView(R.id.adView) AdView mAdView;
    @Inject
    CommentMVP.Presenter commentPresenter;
    @Inject
    SharedPreferences sharedPreferences;
    private Post post;
    private DetailFragment detailFragment;
    private static final String ADMOB_APP_ID = BuildConfig.ADMOB_APP_ID;

    CommentsActivity() {}

    @Override
    public int getContentLayout() {
        return R.layout.activity_comments;
    }

    @Override
    public void initComponents() {
        loadAd();
        ((App) getApplication()).getComponent().inject(this);
        getSupportActionBar().setTitle("Comments");
        sharedPreferences.edit().putString(Pref.prefSecondFragment, Pref.prefDetailFragment).apply();
        if (Utility.isTablet(this)) {
            this.finish();
            return;
        }

        Intent intent = getIntent();
        post = new Gson().fromJson(intent.getStringExtra("link"), new TypeToken<Post>() {}.getType());
        commentPresenter.setPost(post);

        loadFragment();
    }

    private void loadAd() {
        if(mAdView != null) {
            MobileAds.initialize(this, ADMOB_APP_ID);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
            mAdView.loadAd(adRequest);
        }
    }


    private void loadFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        detailFragment = new DetailFragment();
        fragmentManager.beginTransaction()
                .add(R.id.detailsViewFrame, detailFragment)
                .commit();
    }

    @Override
    public void loadPosts() {
        onBackPressed();
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

}
