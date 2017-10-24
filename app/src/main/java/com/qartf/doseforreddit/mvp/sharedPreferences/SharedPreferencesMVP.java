package com.qartf.doseforreddit.mvp.sharedPreferences;

/**
 * Created by ART_F on 2017-10-24.
 */

public interface SharedPreferencesMVP {
    interface View {
        void setTitle();

        void getPosts();

        void setTabLayoutPos();

        void setUserName(String userName);

        void loadUser();
    }

    interface Presenter {
        void onDestroy();
        void setView(SharedPreferencesMVP.View view);
    }
}
