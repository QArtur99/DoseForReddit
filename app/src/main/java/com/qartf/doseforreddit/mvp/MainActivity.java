package com.qartf.doseforreddit.mvp;

import android.os.Bundle;
import android.widget.Toast;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.dialog.SearchDialog;
import com.qartf.doseforreddit.model.Post;
import com.qartf.doseforreddit.mvp.sharedPreferences.SharedPreferencesMVP;
import com.qartf.doseforreddit.mvp.token.TokenMVP;
import com.qartf.doseforreddit.network.RetrofitControl;
import com.qartf.doseforreddit.utility.Constants;
import com.qartf.doseforreddit.utility.Navigation;
import com.qartf.doseforreddit.utility.Utility;


public class MainActivity extends BaseNavigationMainActivity implements PostsFragment.ListViewFragmentInterface,
SubredditsFragment.ListViewFragmentInterface,
        SearchDialog.SearchDialogInter, SharedPreferencesMVP.View, TokenMVP.View {

    private PostsFragment postsFragment;
    private SubredditsFragment subredditsFragment;

    @Override
    public int getContentLayout() {
        return R.layout.activity_main_new;
    }

    @Override
    public void initComponents() {
        setSupportActionBar(toolbar);
        loadStartFragment(savedInstanceState);
    }

    public void loadStartFragment(Bundle savedInstanceState) {
        postsFragment = new PostsFragment();
        postsFragment.setArguments(savedInstanceState);
        Navigation.setFragmentFrame(this, R.id.mainFrame, postsFragment);
    }

    @Override
    public RetrofitControl getRetrofitControl() {
        return null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        tokenPresenter.setView(this);
        tokenPresenter.tokenInfo();
        presenterPref.setView(this);
    }

    @Override
    public void restoreDetailFragment() {

    }

    @Override
    public void loadDetailFragment() {

    }

    @Override
    public void setPost(Post post) {

    }

    @Override
    public void startDetailActivity(Post post) {

    }

    @Override
    public void loginReddit() {

    }

    @Override
    public void searchDialog(int id) {
        new SearchDialog(this, id, this);

    }

    @Override
    public void searchPosts() {
        postPresenter.loadPosts();
    }

    @Override
    public void searchSubreddits() {

    }

    @Override
    public void loadFragment(int fragmentId) {
        switch (fragmentId) {
            case Constants.Id.SEARCH_POSTS:
                postsFragment = Utility.getFragmentInstance(PostsFragment.class, postsFragment);
                Navigation.setFragmentFrame(this, R.id.mainFrame, postsFragment);
                break;
            case Constants.Id.SEARCH_SUBREDDITS:
                subredditsFragment = Utility.getFragmentInstance(SubredditsFragment.class, subredditsFragment);
                Navigation.setFragmentFrame(this, R.id.mainFrame, subredditsFragment);
                break;
        }

    }

    @Override
    public void setTitle() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        tokenPresenter.onStop();
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

    }

    @Override
    public void loadUser() {

    }

    @Override
    public void tokenInfo(String info) {
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }
}
