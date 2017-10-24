package com.qartf.doseforreddit.mvp;

import android.os.Bundle;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.dialog.SearchDialog;
import com.qartf.doseforreddit.model.Post;
import com.qartf.doseforreddit.mvp.sharedPreferences.SharedPreferencesMVP;
import com.qartf.doseforreddit.network.RetrofitControl;
import com.qartf.doseforreddit.utility.Constants;
import com.qartf.doseforreddit.utility.Navigation;
import com.qartf.doseforreddit.utility.Utility;


public class MainActivity extends BaseNavigationMainActivity implements MainFragment.ListViewFragmentInterface,
        SearchDialog.SearchDialogInter.View, SharedPreferencesMVP.View {

    private MainFragment postsFragment;

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
        postsFragment = new MainFragment();
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
        presenter.setView(this);
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
        new SearchDialog(this, id, postsFragment, this);

    }

    @Override
    public void loadFragment(int fragmentId) {
        switch (fragmentId) {
            case Constants.Id.SEARCH_POSTS:
                postsFragment = Utility.getFragmentInstance(MainFragment.class, postsFragment);
                Navigation.setFragmentFrame(this, R.id.mainFrame, postsFragment);
                break;
            case Constants.Id.SEARCH_SUBREDDITS:
//                subredditsFragment = Utility.getFragmentInstance(SubredditsFragment.class, subredditsFragment);
//                Navigation.setFragmentFrame(this, R.id.mainFrame, subredditsFragment);
                break;
        }

    }

    @Override
    public void loadUsers() {
        postsFragment.loadUser();
    }

    @Override
    public void setTitle() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void getPosts() {
        presenterx.loadPosts(postsFragment.getAccessToken());
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
}
