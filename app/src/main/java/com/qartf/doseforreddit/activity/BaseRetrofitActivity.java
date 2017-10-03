package com.qartf.doseforreddit.activity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.database.DatabaseHelper;
import com.qartf.doseforreddit.fragment.DetailFragment;
import com.qartf.doseforreddit.fragment.PostsFragment;
import com.qartf.doseforreddit.fragment.SubredditsFragment;
import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.network.RetrofitControl;

public abstract class BaseRetrofitActivity extends BaseActivity implements RetrofitControl.RetrofitControlInterface{


    protected AccessToken accessToken;
    protected DetailFragment detailFragment;
    protected PostsFragment postsFragment;
    protected SubredditsFragment subredditsFragment;
    protected RetrofitControl retrofitControl;
    protected SharedPreferences sharedPreferences;

    BaseRetrofitActivity(){}

    @Override
    public AccessToken getAccessToken() {
        return accessToken;
    }

    @Override
    public void actionMe(String userName) {
        Cursor cursor = DatabaseHelper.updateUser(this, userName);
        if (cursor != null && cursor.getCount() == 0) {
            DatabaseHelper.insertAccount(this, userName, accessToken);
        } else {
            DatabaseHelper.updateAccount(this, userName, accessToken);
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putString(getResources().getString(R.string.pref_login_signed_in), userName).apply();

        if (cursor != null) {
            cursor.close();
        }
    }

    @Override
    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public void setAccessTokenValue(String accessToken) {
        this.accessToken.setAccessToken(accessToken);
    }

    @Override
    public DetailFragment getDetailFragment() {
        return detailFragment;
    }

    public PostsFragment getPostsFragment() {
        return postsFragment;
    }

    public SubredditsFragment getSubredditsFragment() {
        return subredditsFragment;
    }

}
