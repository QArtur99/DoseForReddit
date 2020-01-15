package com.qartf.doseforreddit.ui.sharedPreferences;

/**
 * Created by ART_F on 2017-10-24.
 */

public interface SharedPreferencesMVP {
    interface View {
        void setTitle(String title);

        void getPosts();

        void getSubredditPosts(int postLoaderId);

        void setTabLayoutPos();

        void setUserName(String userName);

        void setMySubreddits();

        void loadUser();
    }

    interface Presenter {
        void onDestroy();
        void loadUserName();
        void loadTitle();
        void setView(SharedPreferencesMVP.View view);
    }
}
