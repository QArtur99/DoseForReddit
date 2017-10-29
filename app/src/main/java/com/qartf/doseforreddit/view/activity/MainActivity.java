package com.qartf.doseforreddit.view.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.data.entity.Post;
import com.qartf.doseforreddit.presenter.sharedPreferences.SharedPreferencesMVP;
import com.qartf.doseforreddit.presenter.token.TokenMVP;
import com.qartf.doseforreddit.presenter.utility.Constants;
import com.qartf.doseforreddit.presenter.utility.Navigation;
import com.qartf.doseforreddit.presenter.utility.Utility;
import com.qartf.doseforreddit.view.dialog.SearchDialog;
import com.qartf.doseforreddit.view.fragment.DetailFragment;
import com.qartf.doseforreddit.view.fragment.PostsFragment;
import com.qartf.doseforreddit.view.fragment.SubredditsFragment;

import butterknife.BindView;


public class MainActivity extends BaseNavigationMainActivity implements PostsFragment.PostsFragmentInt,
        SubredditsFragment.SubredditsFragmentInt, DetailFragment.DetailFragmentInt,
        SearchDialog.SearchDialogInter, SharedPreferencesMVP.View, TokenMVP.View {

    private PostsFragment postsFragment;
    private SubredditsFragment subredditsFragment;
    private DetailFragment detailFragment;
    private boolean isLoginCode = false;

    @BindView(R.id.mainActivityFrame) CoordinatorLayout mainActivityFrame;

    @Override
    public int getContentLayout() {
        return R.layout.activity_main_new;
    }

    @Override
    public void initComponents() {
//        MainActivity.this.deleteDatabase("Doseforreddit.db");
        setSupportActionBar(toolbar);
        loadStartFragment(savedInstanceState);
    }

    public void loadStartFragment(Bundle savedInstanceState) {
        postsFragment = new PostsFragment();
        postsFragment.setArguments(savedInstanceState);
        Navigation.setFragmentFrame(this, R.id.mainFrame, postsFragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
        tokenPresenter.setView(this);
        tokenPresenter.tokenInfo();

        presenterPref.setView(this);
        presenterPref.loadTitle();
        presenterPref.loadUserName();
    }

    @Override
    public void loadDetailFragment() {
        detailFragment = Utility.getFragmentInstance(DetailFragment.class, detailFragment);
        Navigation.setFragmentFrame(this, R.id.detailsViewFrame, detailFragment);
    }

    @Override
    public void setPost(Post post) {
        commentPresenter.setPost(post);
        commentPresenter.loadPostData();
        if(detailFragment != null) {
            detailFragment.loadComments();
        }
    }

    @Override
    public void loginReddit() {
        Navigation.startLoginActivity(this);
        isLoginCode = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                .setAction("Login", new View.OnClickListener() {
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
        postPresenter.searchPosts();
    }

    @Override
    public void searchSubreddits() {
        subredditPresenter.loadSubreddits();
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
        presenterPref.onDestroy();
    }

    @Override
    public void getPosts() {
        postPresenter.loadPosts();
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
    public void loadUser() {
        tokenPresenter.resetToken();
    }

    @Override
    public void tokenInfo(String info) {
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }
}
