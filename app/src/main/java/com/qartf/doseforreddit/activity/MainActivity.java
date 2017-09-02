package com.qartf.doseforreddit.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.LinearLayout;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.fragment.ListViewFragment;
import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.model.PostObjectParent;
import com.qartf.doseforreddit.network.DataLoader;
import com.qartf.doseforreddit.utility.Constants;
import com.qartf.doseforreddit.utility.Constants.Id;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    @BindView(R.id.mainActivityFrame) CoordinatorLayout mainActivityFrame;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabLayout) TabLayout tabLayout;

    private ListViewFragment listViewFragment;
    private boolean isLoginCode = false;
    private boolean isGuess = true;
    private AccessToken accessToken;
    private String accessTokenGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getSupportLoaderManager().initLoader(Id.POSTS, null, this).forceLoad();
        setSupportActionBar(toolbar);

        tabLayout.addTab(tabLayout.newTab().setText("HOT"), 0);
        tabLayout.addTab(tabLayout.newTab().setText("NEW"), 1);
        tabLayout.addTab(tabLayout.newTab().setText("RISING"), 2);
        tabLayout.addTab(tabLayout.newTab().setText("CONTROVERSIAL"), 3);
        tabLayout.addTab(tabLayout.newTab().setText("TOP"), 4);

        FragmentManager fragmentManager = getSupportFragmentManager();
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
        int id = item.getItemId();
        switch (id) {
            case R.id.menuLogin:
                clearCookies();
                loginReddit();
                break;
            case R.id.menuLogOut:
                clearCookies();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void loginReddit() {
        String url = String.format(Constants.AUTH_URL, Constants.CLIENT_ID, Constants.STATE, Constants.REDIRECT_URI);
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
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String loginCode = sharedPreferences.getString(Constants.LOGIN_TAG, "");
            if (loginCode.equals("accessDeclined")) {
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
            sharedPreferences.edit().putString(Constants.LOGIN_TAG, "").apply();
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
            case Id.GUEST_AUTH:
                return new DataLoader(this, Id.GUEST_AUTH);
            case Id.USER_AUTH:
                String code = args.getString(Constants.LOGIN_TAG);
                return new DataLoader(this, code, Id.USER_AUTH);
            case Id.ME:
                return new DataLoader(this, accessToken.getAccessToken(), Id.ME);
            case Id.POSTS:
                return new DataLoader(this, accessTokenGuest, Id.POSTS);

        }
        return null;
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
                case Id.GUEST_AUTH:
                    accessTokenGuest = (String) data;
                    getSupportLoaderManager().restartLoader(Id.POSTS, null, this).forceLoad();
                    isGuess = true;
                    break;
                case Id.USER_AUTH:
                    accessToken = (AccessToken) data;
                    getSupportLoaderManager().restartLoader(Id.ME, null, this).forceLoad();
                    break;
                case Id.ME:
                    accessToken = (AccessToken) data;
                    getSupportLoaderManager().restartLoader(Id.POSTS, null, this).forceLoad();
                    break;
                case Id.POSTS:
                    listViewFragment.onLoadFinished(loader, (PostObjectParent) data);
                    break;
            }
        } else {
            if (loader.getId() == Id.POSTS) {
                isGuess = false;
                getSupportLoaderManager().restartLoader(Id.GUEST_AUTH, null, this).forceLoad();
            }

        }
    }



    @Override
    public void onLoaderReset(Loader loader) {

    }
}
