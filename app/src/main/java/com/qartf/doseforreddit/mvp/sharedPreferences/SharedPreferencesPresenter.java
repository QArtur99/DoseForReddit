package com.qartf.doseforreddit.mvp.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.utility.Constants;

public class SharedPreferencesPresenter implements SharedPreferences.OnSharedPreferenceChangeListener,
SharedPreferencesMVP.Presenter{

    private SharedPreferencesMVP.View view;
    private String prefPostSubreddit;
    private String prefPostSortBy;
    private Context context;
    private SharedPreferences sharedPreferences;

    public SharedPreferencesPresenter(Context context, SharedPreferences sharedPreferences) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
        this.sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        loadString();
    }

    private void loadString() {
        prefPostSubreddit = context.getResources().getString(R.string.pref_post_subreddit);
        prefPostSortBy = context.getResources().getString(R.string.pref_post_sort_by);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(prefPostSubreddit)) {
            view.setTitle();
            view.getPosts();
        } else if (key.equals(prefPostSortBy)) {
            view.setTabLayoutPos();
            view.getPosts();
        } else if (key.equals(context.getResources().getString(R.string.pref_login_signed_in))) {
            String userName = sharedPreferences.getString(context.getResources().getString(R.string.pref_login_signed_in), Constants.Utility.ANONYMOUS);
            view.setUserName(userName);
            view.loadUser();
        }
    }

    @Override
    public void onDestroy() {
        if (sharedPreferences != null) {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        }
    }

    @Override
    public void setView(SharedPreferencesMVP.View view) {
        this.view = view;
    }

}
