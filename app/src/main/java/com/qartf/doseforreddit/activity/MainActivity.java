package com.qartf.doseforreddit.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.database.DatabaseContract;
import com.qartf.doseforreddit.database.DatabaseHelper;
import com.qartf.doseforreddit.dialog.SearchDialog;
import com.qartf.doseforreddit.fragment.ListViewFragment;
import com.qartf.doseforreddit.fragment.SubredditListViewFragment;
import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.model.PostObject;
import com.qartf.doseforreddit.model.PostObjectParent;
import com.qartf.doseforreddit.model.Subreddit;
import com.qartf.doseforreddit.model.SubredditParent;
import com.qartf.doseforreddit.network.DataLoader;
import com.qartf.doseforreddit.utility.Constants;
import com.qartf.doseforreddit.utility.Constants.Auth;
import com.qartf.doseforreddit.utility.Constants.Id;
import com.qartf.doseforreddit.utility.Constants.Post;
import com.qartf.doseforreddit.utility.Constants.Subscribe;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks,
        ListViewFragment.OnImageClickListener, SubredditListViewFragment.OnImageClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener,
        NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.mainActivityFrame) CoordinatorLayout mainActivityFrame;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabLayout) TabLayout tabLayout;
    @BindView(R.id.navigation_view) NavigationView navigationView;
    @BindView(R.id.drawer) DrawerLayout drawerLayout;

    @BindString(R.string.pref_post_subreddit) String prefPostSubreddit;
    @BindString(R.string.pref_post_sort_by) String prefPostSortBy;

    private ActionBarDrawerToggle drawerToggle;
    private ListViewFragment listViewFragment;
    private SubredditListViewFragment subredditListViewFragment;
    private boolean isLoginCode = false;
    private boolean isGuest = true;
    private AccessToken accessToken;
    private SharedPreferences sharedPreferences;
    private FragmentManager fragmentManager;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new);
        ButterKnife.bind(this);
//        MainActivity.this.deleteDatabase("Doseforreddit.db");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        fragmentManager = getSupportFragmentManager();
        accessToken = new AccessToken();

        getSupportLoaderManager().initLoader(Id.LOAD_USERS, null, this).forceLoad();
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
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

        tabLayout.addTab(tabLayout.newTab().setText("HOT"), 0);
        tabLayout.addTab(tabLayout.newTab().setText("NEW"), 1);
        tabLayout.addTab(tabLayout.newTab().setText("RISING"), 2);
        tabLayout.addTab(tabLayout.newTab().setText("CONTROVERSIAL"), 3);
        tabLayout.addTab(tabLayout.newTab().setText("TOP"), 4);

        listViewFragment = new ListViewFragment();
        fragmentManager.beginTransaction()
                .add(R.id.fragmentFrame, listViewFragment)
                .commit();

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

//        TabLayout.Tab tab = tabLayout.getTabAt(2);
//        tab.select();

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Constants.BASE_URL_OAUTH)
//                .build();
//
//        RedditApiInterface service = retrofit.create(RedditApiInterface.class);
//        Call<String> call = service.getList();
//        call.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call, Response<String> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<String> call, Throwable t) {
//
//            }
//        });


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
                clearCookies();
                loginReddit();
                break;
            case R.id.searchPosts:
                new SearchDialog(this, Id.SEARCH_POSTS);
                break;
            case R.id.searchSubreddits:
                new SearchDialog(this, Id.SEARCH_SUBREDDITS);
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

    private void clearCookies() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(this);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
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
                                clearCookies();
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
        sleepOneSec();

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
                return new DataLoader(this, accessToken.getAccessToken(), argsPostOnly, Id.POSTS);
            case Id.SEARCH_POSTS:
                return new DataLoader(this, accessToken.getAccessToken(), args, Id.SEARCH_POSTS);
            case Id.SEARCH_SUBREDDITS:
                return new DataLoader(this, accessToken.getAccessToken(), args, Id.SEARCH_SUBREDDITS);
            case Id.SUBREDDIT_SUBSCRIBE:
                return new DataLoader(this, accessToken.getAccessToken(), args, Id.SUBREDDIT_SUBSCRIBE);

        }
        return null;
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

    private void sleepOneSec() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if (data != null) {
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
            }
        } else {
            if (loader.getId() == Id.POSTS) {
                if (isGuest) {
                    getSupportLoaderManager().restartLoader(Id.GUEST_AUTH, null, this).forceLoad();
                } else {
                    getSupportLoaderManager().restartLoader(Id.USER_REFRESH, null, this).forceLoad();
                }
            }

        }
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
        getSupportLoaderManager().restartLoader(Id.POSTS, null, this).forceLoad();
    }

    private void actionLoadUsers(Cursor cursor) {
        String logged = sharedPreferences.getString(getResources().getString(R.string.pref_login_signed_in), "Qart_f");
        if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()) {
            do {
                String userName = cursor.getString(cursor.getColumnIndex(DatabaseContract.Accounts.USER_NAME));
                if (userName.equals(logged)) {
                    accessToken = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DatabaseContract.Accounts.ACCESS_TOKEN)), AccessToken.class);
                    isGuest = false;
                }
            } while (cursor.moveToNext());
            getSupportLoaderManager().restartLoader(Id.USER_REFRESH, null, this).forceLoad();
        } else {
            isGuest = true;
            getSupportLoaderManager().restartLoader(Id.GUEST_AUTH, null, this).forceLoad();
        }
    }


    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onImageSelected(Object object, View view) {
        Subreddit subreddit;
        switch (view.getId()) {
            case R.id.commentsAction:
                PostObject postObject = (PostObject) object;
                String jsonString = new Gson().toJson(postObject);
                String tokenString = new Gson().toJson(accessToken);
                Intent intent = new Intent(MainActivity.this, CommentsActivity.class);
                intent.putExtra("link", jsonString);
                intent.putExtra("token", tokenString);
                startActivity(intent);
                break;
            case R.id.isSubscribe:
                subreddit = (Subreddit) object;
                String subscribe = subreddit.subscribers.equals("true") ? Subscribe.UN_SUB : Subscribe.SUB;
                Bundle args = new Bundle();
                args.putString(Subscribe.SUBSCRIBE, subscribe);
                args.putString(Subscribe.FULLNAME, subreddit.name);
                getSupportLoaderManager().restartLoader(Id.SUBREDDIT_SUBSCRIBE, args, this).forceLoad();
                break;
            case R.id.subredditTitleFrame:
                loadFragment(Id.SEARCH_POSTS);
                subreddit = (Subreddit) object;
                sharedPreferences.edit().putString(getResources().getString(R.string.pref_post_subreddit), subreddit.display_name).apply();
                break;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sharedPreferences != null) {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(prefPostSubreddit) || key.equals(prefPostSortBy)) {
            getSupportLoaderManager().restartLoader(Id.POSTS, null, this).forceLoad();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
