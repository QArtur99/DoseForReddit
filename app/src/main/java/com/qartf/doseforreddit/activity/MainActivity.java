package com.qartf.doseforreddit.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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


public class MainActivity extends BaseNavigationMainActivity implements LoaderManager.LoaderCallbacks,
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

    @Override
    public int getContentLayout() {
        return R.layout.activity_main_new;
    }

    @Override
    public void initComponents() {
        setSupportActionBar(toolbar);
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

        getSupportLoaderManager().initLoader(Id.LOAD_USERS, null, this).forceLoad();
        setTitle();
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
        setFragmentFrame(listViewFragment);
    }

    @Override
    public void loadFragment(int fragmentId) {
        switch (fragmentId) {
            case Id.SEARCH_POSTS:
                if (listViewFragment == null) {
                    listViewFragment = new ListViewFragment();
                }
                setFragmentFrame(listViewFragment);
                break;
            case Id.SEARCH_SUBREDDITS:
                if (subredditListViewFragment == null) {
                    subredditListViewFragment = new SubredditListViewFragment();
                }
                setFragmentFrame(subredditListViewFragment);
                break;
        }
    }

    private void setFragmentFrame(Fragment fragment) {
        fragmentManager.beginTransaction()
                .add(R.id.fragmentFrame, fragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.Utility.POST_OBJECT_STRING, postObjectString);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(prefPostSubreddit)) {
            setTitle();
            retrofitControl.getSubredditPosts();
        } else if (key.equals(prefPostSortBy)) {
            setTabLayoutPosition();
            retrofitControl.getSubredditPosts();
        } else if (key.equals(getResources().getString(R.string.pref_login_signed_in))) {
            String logged = sharedPreferences.getString(getResources().getString(R.string.pref_login_signed_in), Constants.Utility.ANONYMOUS);
            headerUsername.setText(logged);
            if (logged.equals(Constants.Utility.ANONYMOUS)) {
                retrofitControl.guestToken();
            } else {
                retrofitControl.getSubredditPosts();
            }
        }
    }

    private void setTitle() {
        String subreddit = sharedPreferences.getString(prefPostSubreddit, "");
        getSupportActionBar().setTitle("/r/" + subreddit);
    }

    @Override
    public void loadUsers() {
        getSupportLoaderManager().initLoader(Id.LOAD_USERS, null, this).forceLoad();
    }

    public void loginReddit() {
        String url = String.format(Auth.AUTH_URL, Auth.CLIENT_ID, Auth.STATE, Auth.REDIRECT_URI, Auth.SCOPE);
        Intent intent = new Intent(this, LinkActivity.class);
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
        checkLogin();
    }

    private void checkLogin() {
        if (isLoginCode) {
            String loginCode = sharedPreferences.getString(getResources().getString(R.string.pref_access_code), "");
            if (loginCode.equals(Constants.ACCESS_DECLINED)) {
                loginFailed();
            } else if (!loginCode.isEmpty()) {
                retrofitControl.accessToken(loginCode);
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
    public Loader onCreateLoader(int id, Bundle args) {
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
                }
            } while (cursor.moveToNext());
            retrofitControl.refreshToken();
        } else {
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
