package com.qartf.doseforreddit.view.activity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.presenter.comment.CommentMVP;
import com.qartf.doseforreddit.presenter.post.PostMVP;
import com.qartf.doseforreddit.presenter.root.App;
import com.qartf.doseforreddit.presenter.sharedPreferences.SharedPreferencesMVP;
import com.qartf.doseforreddit.presenter.subreddit.SubredditMVP;
import com.qartf.doseforreddit.presenter.token.TokenMVP;
import com.qartf.doseforreddit.presenter.utility.Constants;
import com.qartf.doseforreddit.presenter.utility.Utility;
import com.qartf.doseforreddit.view.dialog.LoginDialog;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;

public abstract class BaseNavigationMainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindString(R.string.pref_post_subreddit) public String prefPostSubreddit;
    @BindString(R.string.pref_post_sort_by) public String prefPostSortBy;
    @BindString(R.string.pref_my_subreddit) public String prefMySubreddits;
    protected ActionBarDrawerToggle drawerToggle;
    protected TextView headerUsername;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabLayout) TabLayout tabLayout;
    @BindView(R.id.navigation_view) NavigationView navigationView;
    @BindView(R.id.drawer) DrawerLayout drawerLayout;

    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    SharedPreferencesMVP.Presenter presenterPref;
    @Inject
    PostMVP.Presenter postPresenter;
    @Inject
    SubredditMVP.Presenter subredditPresenter;
    @Inject
    TokenMVP.Presenter tokenPresenter;
    @Inject
    CommentMVP.Presenter commentPresenter;

    BaseNavigationMainActivity() {}

    @Override
    public void initNavigation() {
        ((App) getApplication()).getComponent().inject(this);
        Utility.setTabLayoutDivider(this, tabLayout);
        addTabLayoutTabs();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selected = tabLayout.getSelectedTabPosition();
                String[] postSortValue = getResources().getStringArray(R.array.sortValuePost);
                sharedPreferences.edit().putString(prefPostSortBy, postSortValue[selected]).apply();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setTabLayoutPosition();

        setNavigationDrawer();
        initHeaderView();
    }


    private void addTabLayoutTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("HOT"), 0);
        tabLayout.addTab(tabLayout.newTab().setText("NEW"), 1);
        tabLayout.addTab(tabLayout.newTab().setText("RISING"), 2);
        tabLayout.addTab(tabLayout.newTab().setText("CONTROVERSIAL"), 3);
        tabLayout.addTab(tabLayout.newTab().setText("TOP"), 4);
    }

    protected void setTabLayoutPosition() {
        String[] postSortValue = getResources().getStringArray(R.array.sortValuePost);
        String subredditSortBy = sharedPreferences.getString(prefPostSortBy, getResources().getString(R.string.pref_post_sort_by_default));
        int tabLayoutIndex = Utility.getTabLayoutIndex(postSortValue, subredditSortBy);

        TabLayout.Tab tab = tabLayout.getTabAt(tabLayoutIndex);
        if (tab != null) {
            tab.select();
        }
    }


    private void setNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(this);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

        };
        navigationView.getMenu().getItem(0).setChecked(true);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    private void initHeaderView() {
        View headerView = getLayoutInflater().inflate(R.layout.header, navigationView, false);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String logged = sharedPreferences.getString(getResources().getString(R.string.pref_login_signed_in), Constants.Utility.ANONYMOUS);
                if (logged.equals(Constants.Utility.ANONYMOUS)) {
                    Utility.clearCookies(BaseNavigationMainActivity.this);
                    loginReddit();
                } else {
                    new LoginDialog(BaseNavigationMainActivity.this, sharedPreferences);
                }
                drawerLayout.closeDrawers();
            }
        });
        headerUsername = headerView.findViewById(R.id.username);
        navigationView.addHeaderView(headerView);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int posioton = 0;

        if (menuItem.isChecked()) menuItem.setChecked(false);
        else menuItem.setChecked(true);
        switch (menuItem.getItemId()) {

            case R.id.homePage:
                setTitle("Home");
                sharedPreferences.edit().putInt(Constants.Pref.prefPostLoaderId, Constants.PostLoaderId.POST_HOME).apply();
                sharedPreferences.edit().putString(prefPostSortBy, "hot").apply();
                posioton = 0;
                setPostFragment();
                getSubredditPosts(Constants.PostLoaderId.POST_HOME);
                break;
            case R.id.frontPage:
                setTitle("/r/" + "popular");
                sharedPreferences.edit().putInt(Constants.Pref.prefPostLoaderId, Constants.PostLoaderId.POST_VIEW).apply();
                sharedPreferences.edit().putString(prefPostSubreddit, "popular").apply();
                sharedPreferences.edit().putString(prefPostSortBy, "hot").apply();
                posioton = 1;
                setPostFragment();
                getSubredditPosts(Constants.PostLoaderId.POST_VIEW);
                break;
            case R.id.allPage:
                setTitle("/r/" + "all");
                sharedPreferences.edit().putInt(Constants.Pref.prefPostLoaderId, Constants.PostLoaderId.POST_VIEW).apply();
                sharedPreferences.edit().putString(prefPostSubreddit, "all").apply();
                sharedPreferences.edit().putString(prefPostSortBy, "hot").apply();
                posioton = 2;
                setPostFragment();
                getSubredditPosts(Constants.PostLoaderId.POST_VIEW);
                break;
            case R.id.submit:
                setSubmitFragment();
                posioton = 3;
                break;
            case R.id.mySubreddits:
                setMySubreddits();
                posioton = 4;
                break;
            case R.id.searchPosts:
                posioton = 5;
                searchDialog(Constants.Id.SEARCH_POSTS);
                break;
            case R.id.searchSubreddits:
                posioton = 6;
                searchDialog(Constants.Id.SEARCH_SUBREDDITS);
                break;
            case R.id.helpAndFeedback:
                posioton = 7;
                setTitle("/r/" + "DoseForReddit");
                sharedPreferences.edit().putInt(Constants.Pref.prefPostLoaderId, Constants.PostLoaderId.POST_VIEW).apply();
                sharedPreferences.edit().putString(prefPostSubreddit, "DoseForReddit").apply();
                sharedPreferences.edit().putString(prefPostSortBy, "hot").apply();
                setPostFragment();
                getSubredditPosts(Constants.PostLoaderId.POST_VIEW);
                break;
        }
        navigationView.getMenu().getItem(posioton).setChecked(true);
        drawerLayout.closeDrawers();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.menuLogin:
                Utility.clearCookies(this);
                loginReddit();
                break;
            case R.id.searchPosts:
                searchDialog(Constants.Id.SEARCH_POSTS);
                break;
            case R.id.searchSubreddits:
                searchDialog(Constants.Id.SEARCH_SUBREDDITS);
                break;
            case R.id.refresh:
                refresh();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public abstract void setTitle(String title);

    public abstract void getSubredditPosts(int postLoaderId);

    public abstract void loginReddit();

    public abstract void setMySubreddits();

    public abstract void setPostFragment();

    public abstract void setSubmitFragment();

    public abstract void searchDialog(int id);

    public abstract void refresh();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

}
