package com.qartf.doseforreddit.activity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.database.DatabaseContract;
import com.qartf.doseforreddit.database.DatabaseHelper;
import com.qartf.doseforreddit.fragment.DetailFragment;
import com.qartf.doseforreddit.fragment.ListViewFragment;
import com.qartf.doseforreddit.fragment.SubredditListViewFragment;
import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.network.RetrofitControl;

public abstract class BaseViewActivity extends BaseActivity implements RetrofitControl.RetrofitControlInterface{


    protected AccessToken accessToken;
    protected DetailFragment detailFragment;
    protected ListViewFragment listViewFragment;
    protected SubredditListViewFragment subredditListViewFragment;
    protected RetrofitControl retrofitControl;
    public SharedPreferences sharedPreferences;

    BaseViewActivity(){}

    @Override
    public AccessToken getAccessToken() {
        return accessToken;
    }

    @Override
    public void actionMe(String userName) {
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

    @Override
    public ListViewFragment getListViewFragment() {
        return listViewFragment;
    }

    @Override
    public SubredditListViewFragment getSubredditListViewFragment() {
        return subredditListViewFragment;
    }

}
