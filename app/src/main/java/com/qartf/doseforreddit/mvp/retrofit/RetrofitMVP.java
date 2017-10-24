package com.qartf.doseforreddit.mvp.retrofit;

import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.model.PostParent;

import io.reactivex.Observable;


public interface RetrofitMVP {

    interface View {
        void setAccessToken(AccessToken accessToken);
        AccessToken getAccessToken();
        void showToast(AccessToken accessToken);
        void setPostParent(PostParent postParent);

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

        void setView(RetrofitMVP.View view);

        void onStop();

        void loadAccessToken();

        void loadPosts(AccessToken accessToken);
//
//        void loginButtonClicked();
//
//        void getCurrentUser();

    }

    interface Model {
        Observable<AccessToken> getAccessToken();
        Observable<PostParent> getPosts(AccessToken accessToken);
        void onStop();
//        void createUser(String name, String lastName);
//
//        User getUser();


    }
}
