package com.qartf.doseforreddit.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.snackbar.Snackbar;
import com.qartf.doseforreddit.BuildConfig;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.data.entity.Post;
import com.qartf.doseforreddit.presenter.sharedPreferences.SharedPreferencesMVP;
import com.qartf.doseforreddit.presenter.token.TokenMVP;
import com.qartf.doseforreddit.presenter.utility.Constants;
import com.qartf.doseforreddit.presenter.utility.Constants.Pref;
import com.qartf.doseforreddit.presenter.utility.Navigation;
import com.qartf.doseforreddit.presenter.utility.Utility;
import com.qartf.doseforreddit.view.dialog.SearchDialog;
import com.qartf.doseforreddit.view.fragment.DetailFragment;
import com.qartf.doseforreddit.view.fragment.PostsFragment;
import com.qartf.doseforreddit.view.fragment.SubmitFragment;
import com.qartf.doseforreddit.view.fragment.SubredditsFragment;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import butterknife.BindView;


public class MainActivity extends BaseNavigationMainActivity implements PostsFragment.PostsFragmentInt
        , SubredditsFragment.SubredditsFragmentInt, DetailFragment.DetailFragmentInt
        , SearchDialog.SearchDialogInter, SharedPreferencesMVP.View, TokenMVP.View
        , SubmitFragment.SubmitFragmentInt {

    private static final String ADMOB_APP_ID = BuildConfig.ADMOB_APP_ID;
    private static final String TEST_DEVICE = "44D4117369EADEFD23949802BA0B7DE6";
    @Nullable
    @BindView(R.id.adView)
    AdView mAdView;
    @BindView(R.id.mainActivityFrame) CoordinatorLayout mainActivityFrame;
    private PostsFragment postsFragment;
    private SubredditsFragment subredditsFragment;
    private SubmitFragment submitFragment;
    private DetailFragment detailFragment;
    private boolean isLoginCode = false;

    @Override
    public int getContentLayout() {
        return R.layout.activity_main_new;
    }

    @Override
    public void initComponents() {
        loadAd();
//        MainActivity.this.deleteDatabase("Doseforreddit.db");
        setSupportActionBar(toolbar);
        loadStartFragment(savedInstanceState);
        loadSecondFragment();

        if (presenterPref != null) {
            presenterPref.loadTitle();
        }

        super.setResult(0);
    }

    private void loadAd() {
        if (mAdView != null) {
            MobileAds.initialize(this, ADMOB_APP_ID);
            AdRequest adRequest = new AdRequest.Builder().addTestDevice(TEST_DEVICE).build();
            mAdView.loadAd(adRequest);
        }
    }

    public void loadStartFragment(Bundle savedInstanceState) {
        postsFragment = new PostsFragment();
        postsFragment.setArguments(savedInstanceState);
        Navigation.setFragmentFrame(this, R.id.mainFrame, postsFragment);
    }

    public void loadSecondFragment() {
        if (commentPresenter.getPost() != null && this.findViewById(R.id.detailsViewFrame) != null) {
            String secondFragmentString = sharedPreferences.getString(Pref.prefSecondFragment, Pref.prefDetailFragment);
            if (secondFragmentString.equals(Pref.prefDetailFragment)) {
                loadDetailFragment();
            } else if (secondFragmentString.equals(Pref.prefSubmitFragment)) {
                loadSubmitFragment();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        tokenPresenter.setView(this);
        tokenPresenter.tokenInfo();

        presenterPref.setView(this);
        setTitle("Home");
        presenterPref.loadUserName();
    }

    @Override
    public void loadDetailFragment() {
        sharedPreferences.edit().putString(Pref.prefSecondFragment, Pref.prefDetailFragment).apply();
        detailFragment = Utility.getFragmentInstance(DetailFragment.class, detailFragment);
        Navigation.setFragmentFrame(this, R.id.detailsViewFrame, detailFragment);
    }

    public void loadSubmitFragment() {
        sharedPreferences.edit().putString(Pref.prefSecondFragment, Pref.prefSubmitFragment).apply();
        submitFragment = Utility.getFragmentInstance(SubmitFragment.class, submitFragment);
        Navigation.setFragmentFrame(this, R.id.detailsViewFrame, submitFragment);
    }

    @Override
    public void setPost(Post post) {
        commentPresenter.setPost(post);
        commentPresenter.loadPostData();
        if (detailFragment != null) {
            detailFragment.loadComments();
        }
    }

    @Override
    public void loginReddit() {
        Navigation.startLoginActivity(this);
        isLoginCode = true;
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
        checkLogin();
    }

    private void checkLogin() {
        if (isLoginCode) {
            String loginCode = sharedPreferences.getString(getResources().getString(R.string.pref_access_code), "");
            if (loginCode.equals(Constants.ACCESS_DECLINED)) {
                loginFailed();
            } else if (!loginCode.isEmpty()) {
                tokenPresenter.getAboutMe();
            }
            sharedPreferences.edit().putString(getResources().getString(R.string.pref_access_code), "").apply();
            isLoginCode = false;
        }
    }

    private void loginFailed() {
        Snackbar snackbar = Snackbar
                .make(mainActivityFrame, "Login permission declined", Snackbar.LENGTH_LONG)
                .setAction("Log in", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utility.clearCookies(MainActivity.this);
                        loginReddit();
                    }
                });
        snackbar.show();
    }


    @Override
    public void searchDialog(int id) {
        new SearchDialog(this, id, this);

    }

    @Override
    public void searchPosts() {
        postsFragment.clearAdapter();
        postsFragment.setPostType(Constants.PostLoaderId.SEARCH_POSTS);
        postPresenter.searchPosts("");
    }

    @Override
    public void searchSubreddits() {
        subredditsFragment.clearAdapter();
        subredditsFragment.setSubredditType(false);
        subredditPresenter.loadSubreddits("");
    }

    @Override
    public void loadFragment(int fragmentId) {
        switch (fragmentId) {
            case Constants.Id.SEARCH_POSTS:
                postsFragment = Utility.getFragmentInstance(PostsFragment.class, postsFragment);
                Navigation.setFragmentFrame(this, R.id.mainFrame, postsFragment);
                tabLayout.setVisibility(View.VISIBLE);
                break;
            case Constants.Id.SEARCH_SUBREDDITS:
                subredditsFragment = Utility.getFragmentInstance(SubredditsFragment.class, subredditsFragment);
                Navigation.setFragmentFrame(this, R.id.mainFrame, subredditsFragment);
                tabLayout.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public void refresh() {
        loadStartFragment(savedInstanceState);
    }

    @Override
    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        tokenPresenter.onStop();
//        postPresenter.onStop();
//        subredditPresenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdView != null) {
            mAdView.destroy();
        }
        presenterPref.onDestroy();

    }

    @Override
    public void getPosts() {
        postsFragment.clearAdapter();
        getSubredditLoadPosts();
    }

    @Override
    public void getSubredditPosts(int postLoaderId) {
        postsFragment.setPostType(postLoaderId);
        postsFragment.clearAdapter();
        postsFragment.setRecyclerView();
        getSubredditLoadPosts();
    }

    private void getSubredditLoadPosts() {
        switch (postsFragment.getPostType()) {
            case Constants.PostLoaderId.POST_HOME:
                postPresenter.loadHome("");
                break;
            case Constants.PostLoaderId.POST_VIEW:
                postPresenter.loadPosts("");
                break;
            case Constants.PostLoaderId.SEARCH_POSTS:
                postPresenter.searchPosts("");
                break;
        }
    }

    @Override
    public void setTabLayoutPos() {
        setTabLayoutPosition();
    }

    @Override
    public void setUserName(String userName) {
        headerUsername.setText(userName);
    }

    @Override
    public void setMySubreddits() {
        String logged = sharedPreferences.getString(getResources().getString(R.string.pref_login_signed_in), Constants.Utility.ANONYMOUS);
        if (logged.equals(Constants.Utility.ANONYMOUS)) {
            loginSnackBar();
        } else {
            setTitle("My Subreddits");
            loadFragment(Constants.Id.SEARCH_SUBREDDITS);
            subredditsFragment.clearAdapter();
            subredditsFragment.setRecyclerView();
            subredditsFragment.setSubredditType(true);
            subredditPresenter.loadMineSubreddits("");
        }
    }

    @Override
    public void loginSnackBar() {
        Snackbar snackbar = Snackbar
                .make(mainActivityFrame, "You must be logged in", Snackbar.LENGTH_LONG)
                .setAction("Log in", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Utility.clearCookies(MainActivity.this);
                        loginReddit();
                    }
                });
        snackbar.show();
    }

    @Override
    public void setPostFragment() {
        if (!postsFragment.isVisible()) {
            loadFragment(Constants.Id.SEARCH_POSTS);
        }
    }

    @Override
    public void setSubmitFragment() {
        String logged = sharedPreferences.getString(getResources().getString(R.string.pref_login_signed_in), Constants.Utility.ANONYMOUS);
        if (logged.equals(Constants.Utility.ANONYMOUS)) {
            loginSnackBar();
        } else {
            if (this.findViewById(R.id.detailsViewFrame) != null) {
                loadSubmitFragment();
            } else {
                Navigation.startSubmitActivity(this);
            }
        }
    }

    @Override
    public void loadUser() {
        tokenPresenter.resetToken();
    }

    @Override
    public void tokenInfo(String info) {
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void switchToPostFragment() {

    }

    @Override
    public void loadPosts() {
        loadStartFragment(null);
    }
}
