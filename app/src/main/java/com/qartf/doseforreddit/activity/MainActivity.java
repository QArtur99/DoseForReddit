package com.qartf.doseforreddit.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.database.DatabaseContract;
import com.qartf.doseforreddit.database.DatabaseHelper;
import com.qartf.doseforreddit.dialog.LoginDialog;
import com.qartf.doseforreddit.dialog.SearchDialog;
import com.qartf.doseforreddit.fragment.DetailFragment;
import com.qartf.doseforreddit.fragment.ListViewFragment;
import com.qartf.doseforreddit.fragment.SubredditListViewFragment;
import com.qartf.doseforreddit.fragmentControl.DetailFragmentControl;
import com.qartf.doseforreddit.fragmentControl.ListViewFragmentControl;
import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.model.Comment;
import com.qartf.doseforreddit.model.PostObject;
import com.qartf.doseforreddit.model.PostObjectParent;
import com.qartf.doseforreddit.model.SubredditParent;
import com.qartf.doseforreddit.network.DataLoader;
import com.qartf.doseforreddit.network.RetrofitControl;
import com.qartf.doseforreddit.utility.Constants;
import com.qartf.doseforreddit.utility.Constants.Auth;
import com.qartf.doseforreddit.utility.Constants.Id;
import com.qartf.doseforreddit.utility.Constants.Post;
import com.qartf.doseforreddit.utility.Utility;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks,
        SharedPreferences.OnSharedPreferenceChangeListener,
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public DetailFragment detailFragment;
    public AccessToken accessToken;
    public SharedPreferences sharedPreferences;
    public FragmentManager fragmentManager;
    public Bundle argsPost, argsSubredit, argsDetail;
    public String postObjectString;
    public PostObject postObject;
    public ListViewFragmentControl listViewFragmentControl;
    public DetailFragmentControl detailFragmentControl;
    @BindView(R.id.mainActivityFrame) CoordinatorLayout mainActivityFrame;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabLayout) TabLayout tabLayout;
    @BindView(R.id.navigation_view) NavigationView navigationView;
    @BindView(R.id.drawer) DrawerLayout drawerLayout;
    @BindView(R.id.adView) AdView adView;
    @BindString(R.string.pref_post_subreddit) public String prefPostSubreddit;
    @BindString(R.string.pref_post_sort_by) public String prefPostSortBy;
    private ActionBarDrawerToggle drawerToggle;
    public ListViewFragment listViewFragment;
    private SubredditListViewFragment subredditListViewFragment;
    private boolean isLoginCode = false;
    private boolean isGuest = true;
    private ActionBar actionBar;
    private TextView headerUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        loadAd();


        if (savedInstanceState != null) {
            postObjectString = savedInstanceState.getString(Constants.Utility.POST_OBJECT_STRING);
            if (postObjectString != null && !postObjectString.isEmpty()) {
                postObject = new Gson().fromJson(postObjectString, new TypeToken<PostObject>() {}.getType());
            }
        }

        listViewFragmentControl = new ListViewFragmentControl(this);
        detailFragmentControl = new DetailFragmentControl(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);


        accessToken = new AccessToken();

        loadStartFragment(savedInstanceState);
        setTabLayout();
        setNavigationDrawer();

        getSupportLoaderManager().initLoader(Id.LOAD_USERS, null, this).forceLoad();

    }

    private void loadAd() {
        //        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .build();
//        adView.loadAd(adRequest);
        adView.setVisibility(View.GONE);
    }

    public void loadStartFragment(Bundle savedInstanceState) {
        fragmentManager = getSupportFragmentManager();
        listViewFragment = new ListViewFragment();
        listViewFragment.setArguments(savedInstanceState);
        fragmentManager.beginTransaction()
                .add(R.id.fragmentFrame, listViewFragment)
                .commit();
    }

    public void loadFragment(int fragmentId) {
        switch (fragmentId) {
            case Id.SEARCH_POSTS:
                if (listViewFragment == null) {
                    listViewFragment = new ListViewFragment();
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentFrame, listViewFragment)
                        .commit();
                break;
            case Id.SEARCH_SUBREDDITS:
                if (subredditListViewFragment == null) {
                    subredditListViewFragment = new SubredditListViewFragment();
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentFrame, subredditListViewFragment)
                        .commit();
                break;
        }
    }

    private void setNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(this);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
                try {
                } catch (Exception e) {
                    //null
                }

//                if (mPendingRunnable != null) {
//                    mHandler.post(mPendingRunnable);
//                    mPendingRunnable = null;
//                }

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                try {

                } catch (Exception e) {
                    //null
                }
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }


        };
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        View headerView = getLayoutInflater().inflate(R.layout.header, navigationView, false);
        headerView.setOnClickListener(this);
        headerUsername = headerView.findViewById(R.id.username);
        navigationView.addHeaderView(headerView);
    }

    private void setTabLayout() {

        tabLayout.addTab(tabLayout.newTab().setText("HOT"), 0);
        tabLayout.addTab(tabLayout.newTab().setText("NEW"), 1);
        tabLayout.addTab(tabLayout.newTab().setText("RISING"), 2);
        tabLayout.addTab(tabLayout.newTab().setText("CONTROVERSIAL"), 3);
        tabLayout.addTab(tabLayout.newTab().setText("TOP"), 4);

        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        drawable.setSize(4, 1);
//        linearLayout.setDividerPadding(30);
        linearLayout.setDividerDrawable(drawable);
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
    }

    private void setTabLayoutPosition() {
        String[] postSortValue = getResources().getStringArray(R.array.sortValuePost);
        String subredditSortBy = sharedPreferences.getString(prefPostSortBy, getResources().getString(R.string.pref_post_sort_by_default));
        int tabLayoutIndex = Utility.getTabLayoutIndex(postSortValue, subredditSortBy);

        TabLayout.Tab tab = tabLayout.getTabAt(tabLayoutIndex);
        if (tab != null) {
            tab.select();
        }
    }

    @Override
    public void onClick(View view) {
        String logged = sharedPreferences.getString(getResources().getString(R.string.pref_login_signed_in), Constants.Utility.ANONYMOUS);
        if (logged.equals(Constants.Utility.ANONYMOUS)) {
            Utility.clearCookies(MainActivity.this);
            loginReddit();
        } else {
            new LoginDialog(MainActivity.this, sharedPreferences);
        }
        drawerLayout.closeDrawers();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int posioton = 0;

        if (menuItem.isChecked()) menuItem.setChecked(false);
        else menuItem.setChecked(true);
        switch (menuItem.getItemId()) {
            case R.id.frontPage:
                sharedPreferences.edit().putString(prefPostSubreddit, "popular").apply();
                sharedPreferences.edit().putString(prefPostSortBy, "hot").apply();
                posioton = 0;
                break;
            case R.id.searchPosts:
                posioton = 1;
                new SearchDialog(this, Id.SEARCH_POSTS);
                break;
            case R.id.searchSubreddits:
                posioton = 2;
                new SearchDialog(this, Id.SEARCH_SUBREDDITS);
                break;
        }
        navigationView.getMenu().getItem(posioton).setChecked(true);
        drawerLayout.closeDrawers();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.Utility.POST_OBJECT_STRING, postObjectString);
        outState.putString("bundle", "bundle");
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(prefPostSubreddit)) {
            getSupportLoaderManager().restartLoader(Id.POSTS, null, this).forceLoad();
        } else if (key.equals(prefPostSortBy)) {
            setTabLayoutPosition();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    getSupportLoaderManager().restartLoader(Id.POSTS, null, MainActivity.this).forceLoad();
                }
            }, 400);
        } else if (key.equals(getResources().getString(R.string.pref_login_signed_in))) {
            String logged = sharedPreferences.getString(getResources().getString(R.string.pref_login_signed_in), Constants.Utility.ANONYMOUS);
            headerUsername.setText(logged);
            if (logged.equals(Constants.Utility.ANONYMOUS)) {
                isGuest = true;
                getSupportLoaderManager().restartLoader(Id.GUEST_AUTH, null, this).forceLoad();
            } else {
                isGuest = false;
                getSupportLoaderManager().restartLoader(Id.POSTS, null, this).forceLoad();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
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
                new SearchDialog(this, Id.SEARCH_POSTS);
                break;
            case R.id.searchSubreddits:
                new SearchDialog(this, Id.SEARCH_SUBREDDITS);
                break;
            case R.id.refresh:
                loadFragment(Id.SEARCH_POSTS);
                getSupportLoaderManager().initLoader(Id.LOAD_USERS, null, this).forceLoad();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private void loginReddit() {
        String url = String.format(Auth.AUTH_URL, Auth.CLIENT_ID, Auth.STATE, Auth.REDIRECT_URI, Auth.SCOPE);
        Intent intent = new Intent(MainActivity.this, LinkActivity.class);
        intent.putExtra("login", Uri.parse(url).toString());
        startActivity(intent);
        isLoginCode = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLoginCode) {
            String loginCode = sharedPreferences.getString(getResources().getString(R.string.pref_access_code), "");
            if (loginCode.equals(Constants.ACCESS_DECLINED)) {
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
            } else if (!loginCode.isEmpty()) {
                getAccessToken(loginCode);
            }
            sharedPreferences.edit().putString(getResources().getString(R.string.pref_access_code), "").apply();
            isLoginCode = false;
        }


    }

    private void getAccessToken(String code) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.LOGIN_TAG, code);
        getSupportLoaderManager().initLoader(Id.USER_AUTH, bundle, this).forceLoad();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Utility.sleepOneSec();

        switch (id) {
            case Id.LOAD_USERS:
                return new CursorLoader(this, DatabaseContract.Accounts.CONTENT_URI, DatabaseContract.Accounts.PROJECTION_LIST, null, null, null);
            case Id.GUEST_AUTH:
                return new DataLoader(this, Id.GUEST_AUTH);
            case Id.USER_AUTH:
                String code = args.getString(Constants.LOGIN_TAG);
                return new DataLoader(this, code, Id.USER_AUTH);
            case Id.USER_REFRESH:
                return new DataLoader(this, accessToken.getRefreshToken(), Id.USER_REFRESH);
            case Id.ME:
                return new DataLoader(this, accessToken.getAccessToken(), Id.ME);
            case Id.POSTS:
                Bundle argsPostOnly = getPostBundle();
                argsPost = argsPostOnly;
                return new DataLoader(this, accessToken.getAccessToken(), argsPostOnly, Id.POSTS);
            case Id.SEARCH_POSTS:
                argsPost = args;
                return new DataLoader(this, accessToken.getAccessToken(), args, Id.SEARCH_POSTS);
            case Id.SEARCH_SUBREDDITS:
                argsSubredit = args;
                return new DataLoader(this, accessToken.getAccessToken(), args, Id.SEARCH_SUBREDDITS);
            case Id.SUBREDDIT_SUBSCRIBE:
                return new DataLoader(this, accessToken.getAccessToken(), args, Id.SUBREDDIT_SUBSCRIBE);
            case Id.VOTE:
                return new DataLoader(this, accessToken.getAccessToken(), args, Id.VOTE);
            case Id.COMMENTS:
                argsDetail = args;
                return new DataLoader(this, accessToken.getAccessToken(), args, Id.COMMENTS);

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()) {
            case Id.LOAD_USERS:
                actionLoadUsers((Cursor) data);
                break;
            case Id.GUEST_AUTH:
                accessToken.setAccessToken((String) data);
                getSupportLoaderManager().restartLoader(Id.POSTS, null, this).forceLoad();
                break;
            case Id.USER_AUTH:
                accessToken = (AccessToken) data;
                getSupportLoaderManager().restartLoader(Id.ME, null, this).forceLoad();
                break;
            case Id.USER_REFRESH:
                accessToken.setAccessToken((String) data);
                getSupportLoaderManager().restartLoader(Id.POSTS, null, this).forceLoad();
                break;
            case Id.ME:
                actionMe((String) data);
                break;
            case Id.POSTS:
                listViewFragment.onLoadFinished(loader, (PostObjectParent) data);
                break;
            case Id.SEARCH_POSTS:
                listViewFragment.onLoadFinished(loader, (PostObjectParent) data);
                break;
            case Id.SEARCH_SUBREDDITS:
                subredditListViewFragment.onLoadFinished(loader, (SubredditParent) data);
                break;
            case Id.SUBREDDIT_SUBSCRIBE:
                break;
            case Id.VOTE:
                break;
            case Id.COMMENTS:
                detailFragment.onLoadFinished(loader, (List<Comment>) data);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @NonNull
    private Bundle getPostBundle() {
        Bundle argsPostOnly = new Bundle();
        String subreddit = sharedPreferences.getString(prefPostSubreddit, getResources().getString(R.string.pref_post_subreddit_default));
        String subredditSortBy = sharedPreferences.getString(prefPostSortBy, getResources().getString(R.string.pref_post_sort_by_default));
        argsPostOnly.putString(Post.SUBREDDIT, subreddit);
        argsPostOnly.putString(Post.SORT_BY, subredditSortBy);
        actionBar.setTitle("/r/" + subreddit);
        return argsPostOnly;
    }

    private void actionMe(String userName) {
        String selection = DatabaseContract.Accounts.USER_NAME + "=?";
        String[] selectionArgs = new String[]{userName};
        Cursor cursor = getContentResolver().query(DatabaseContract.Accounts.CONTENT_URI,
                DatabaseContract.Accounts.PROJECTION_LIST,
                selection, selectionArgs, null);

        if (cursor != null && cursor.getCount() == 0) {
            DatabaseHelper.insertAccount(this, userName, accessToken);
        } else {
            DatabaseHelper.updateAccount(this, userName, accessToken);
        }

        sharedPreferences.edit().putString(getResources().getString(R.string.pref_login_signed_in), userName).apply();

        if (cursor != null) {
            cursor.close();
        }
    }

    private void actionLoadUsers(Cursor cursor) {
        String logged = sharedPreferences.getString(getResources().getString(R.string.pref_login_signed_in), Constants.Utility.ANONYMOUS);
        headerUsername.setText(logged);
        if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()) {
            do {
                String userName = cursor.getString(cursor.getColumnIndex(DatabaseContract.Accounts.USER_NAME));
                if (userName.equals(logged)) {
                    accessToken = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DatabaseContract.Accounts.ACCESS_TOKEN)), AccessToken.class);
                    isGuest = false;
                }
            } while (cursor.moveToNext());
//            getSupportLoaderManager().restartLoader(Id.USER_REFRESH, null, this).forceLoad();
            RetrofitControl retrofitControl = new RetrofitControl(MainActivity.this);

            retrofitControl.refreshToken();
        } else {
            isGuest = true;
            getSupportLoaderManager().restartLoader(Id.GUEST_AUTH, null, this).forceLoad();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sharedPreferences != null) {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        }
    }

}
