package com.qartf.doseforreddit.mvp.post;

import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.model.PostParent;

import io.reactivex.Observable;


public interface PostMVP {

    interface View {
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

        void setView(PostMVP.View view);

        void onStop();

        void loadPosts();
//
//        void loginButtonClicked();
//
//        void getCurrentUser();

    }

    interface Model {
        Observable<PostParent> getPosts();
//        void createUser(String name, String lastName);
//
//        User getUser();


    }
}
