package com.qartf.doseforreddit.presenter.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.presenter.utility.Constants;

public class SharedPreferencesPresenter implements SharedPreferences.OnSharedPreferenceChangeListener,
SharedPreferencesMVP.Presenter{

    private SharedPreferencesMVP.View view;
    private String prefPostSubreddit;
    private String prefPostSortBy;
    private String prefMySubreddits;
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
        prefMySubreddits = context.getResources().getString(R.string.pref_my_subreddit);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(prefPostSubreddit)) {
//            String subreddit = sharedPreferences.getString(prefPostSubreddit, "");
//            view.setTitle("/r/" + subreddit);
//            view.getSubredditPosts();
        } else if (key.equals(prefPostSortBy)) {
            view.setTabLayoutPos();
            view.getPosts();
        } else if (key.equals(context.getResources().getString(R.string.pref_login_signed_in))) {
            String userName = sharedPreferences.getString(context.getResources().getString(R.string.pref_login_signed_in), Constants.Utility.ANONYMOUS);
            view.setUserName(userName);
            view.loadUser();
        } else if(key.equals(prefMySubreddits)){
//            view.setMySubreddits();

        }
    }

    @Override
    public void onDestroy() {
        if (sharedPreferences != null) {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        }
    }

    @Override
    public void loadUserName() {
        String logged = sharedPreferences.getString(context.getResources().getString(R.string.pref_login_signed_in), Constants.Utility.ANONYMOUS);
        view.setUserName(logged);
    }

    @Override
    public void loadTitle() {
        if(view != null) {
            String subreddit = sharedPreferences.getString(prefPostSubreddit, "");
            view.setTitle("/r/" + subreddit);
        }
    }

    @Override
    public void setView(SharedPreferencesMVP.View view) {
        this.view = view;
    }

}
