package com.qartf.doseforreddit.mvp.subreddit;

import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.model.SubredditParent;

import io.reactivex.Observable;


public interface SubredditMVP {

    interface View {
        void showToast(AccessToken accessToken);
        void setSubredditParent(SubredditParent postParent);

        void error(String errorString);
        void setLoadIndicatorOff();

//        String getFirstName();
//        String getLastName();
//
//        void showInputError();
//
//        void setFirstName(String firstName);
//
//        void setLastName(String lastName);
//
//        void showUserSavedMessage();
    }

    interface Presenter {

        void setView(SubredditMVP.View view);

        void onStop();

        void loadSubreddits();
//
//        void loginButtonClicked();
//
//        void getCurrentUser();

    }

    interface Model {
        Observable<SubredditParent> getSubreddits();

    }
}
