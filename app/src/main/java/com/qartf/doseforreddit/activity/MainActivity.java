package com.qartf.doseforreddit.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.database.DatabaseContract;
import com.qartf.doseforreddit.dialog.SearchDialog;
import com.qartf.doseforreddit.fragment.DetailFragment;
import com.qartf.doseforreddit.fragment.ListViewFragment;
import com.qartf.doseforreddit.fragment.SubredditListViewFragment;
import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.model.Post;
import com.qartf.doseforreddit.network.RetrofitControl;
import com.qartf.doseforreddit.utility.Constants;
import com.qartf.doseforreddit.utility.Constants.Auth;
import com.qartf.doseforreddit.utility.Constants.Id;
import com.qartf.doseforreddit.utility.Utility;

import butterknife.BindView;


public class MainActivity extends BaseNavigationActivity implements LoaderManager.LoaderCallbacks,
        SharedPreferences.OnSharedPreferenceChangeListener,
        DetailFragment.DetailFragmentInterface,
        ListViewFragment.ListViewFragmentInterface,
        SubredditListViewFragment.ShubredditListViewFragmentInterface {

    public FragmentManager fragmentManager;
    public String postObjectString;
    public Post post;
    @BindView(R.id.mainActivityFrame) CoordinatorLayout mainActivityFrame;
    @BindView(R.id.adView) AdView adView;
    private boolean isLoginCode = false;
    private boolean isGuest = true;

    @Override
    public int getContentLayout() {
        return R.layout.activity_main_new;
    }

    @Override
    public void initComponents() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        loadAd();

        if (savedInstanceState != null) {
            postObjectString = savedInstanceState.getString(Constants.Utility.POST_OBJECT_STRING);
            if (postObjectString != null && !postObjectString.isEmpty()) {
                post = new Gson().fromJson(postObjectString, new TypeToken<Post>() {}.getType());
            }
        }
        retrofitControl = new RetrofitControl(MainActivity.this, this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);


        accessToken = new AccessToken();

        loadStartFragment(savedInstanceState);

        if (!isLoginCode) {
            getSupportLoaderManager().initLoader(Id.LOAD_USERS, null, this).forceLoad();
        }
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.Utility.POST_OBJECT_STRING, postObjectString);
        outState.putString("bundle", "bundle");
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(prefPostSubreddit)) {
            retrofitControl.getSubredditPosts();
        } else if (key.equals(prefPostSortBy)) {
            setTabLayoutPosition();
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    retrofitControl.getSubredditPosts();
                }
            }, 400);
        } else if (key.equals(getResources().getString(R.string.pref_login_signed_in))) {
            String logged = sharedPreferences.getString(getResources().getString(R.string.pref_login_signed_in), Constants.Utility.ANONYMOUS);
            headerUsername.setText(logged);
            if (logged.equals(Constants.Utility.ANONYMOUS)) {
                isGuest = true;
                retrofitControl.guestToken();
            } else {
                isGuest = false;
                retrofitControl.getSubredditPosts();
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

    public void loginReddit() {
        String url = String.format(Auth.AUTH_URL, Auth.CLIENT_ID, Auth.STATE, Auth.REDIRECT_URI, Auth.SCOPE);
        Intent intent = new Intent(MainActivity.this, LinkActivity.class);
        intent.putExtra("login", Uri.parse(url).toString());
        startActivity(intent);
        isLoginCode = true;
    }

    @Override
    public void searchDialog(int id) {
        new SearchDialog(this, id);
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
                retrofitControl.accessToken(loginCode);
            }
            sharedPreferences.edit().putString(getResources().getString(R.string.pref_access_code), "").apply();
            isLoginCode = false;
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Utility.sleepOneSec();

        switch (id) {
            case Id.LOAD_USERS:
                return new CursorLoader(this, DatabaseContract.Accounts.CONTENT_URI, DatabaseContract.Accounts.PROJECTION_LIST, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()) {
            case Id.LOAD_USERS:
                actionLoadUsers((Cursor) data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {}

    private void actionLoadUsers(Cursor cursor) {
        String logged = sharedPreferences.getString(getResources().getString(R.string.pref_login_signed_in), Constants.Utility.ANONYMOUS);
        headerUsername.setText(logged);
        if (!logged.equals(Constants.Utility.ANONYMOUS) && cursor != null && !cursor.isClosed() && cursor.moveToFirst()) {
            do {
                String userName = cursor.getString(cursor.getColumnIndex(DatabaseContract.Accounts.USER_NAME));
                if (userName.equals(logged)) {
                    accessToken = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DatabaseContract.Accounts.ACCESS_TOKEN)), AccessToken.class);
                    isGuest = false;
                }
            } while (cursor.moveToNext());
            retrofitControl.refreshToken();
        } else {
            isGuest = true;
            retrofitControl.guestToken();
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
    public RetrofitControl getRetrofitControl() {
        return retrofitControl;
    }

    @Override
    public void restoreDetailFragment() {
        if (post != null && findViewById(R.id.detailsViewFrame) != null) {
            loadDetailFragment();
            detailFragment.setPost(post);
        }
    }

    @Override
    public void setPost(Post post) {
        detailFragment.setPost(post);
    }

    @Override
    public void startDetailActivity(Post post) {
        this.post = post;
        postObjectString = new Gson().toJson(post);
        String tokenString = new Gson().toJson(accessToken);
        Intent intent = new Intent(MainActivity.this, CommentsActivity.class);
        intent.putExtra("link", postObjectString);
        intent.putExtra("token", tokenString);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
            startActivity(intent, bundle);
        } else {
            startActivity(intent);
        }
    }

    public void loadDetailFragment() {
        if (findViewById(R.id.detailsViewFrame) != null) {
            if (detailFragment == null) {
                detailFragment = new DetailFragment();
            }
            fragmentManager.beginTransaction()
                    .replace(R.id.detailsViewFrame, detailFragment)
                    .commit();
        }
    }

}
