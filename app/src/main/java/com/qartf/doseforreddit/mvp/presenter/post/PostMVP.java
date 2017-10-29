package com.qartf.doseforreddit.mvp.presenter.post;

import com.qartf.doseforreddit.mvp.data.entity.AccessToken;
import com.qartf.doseforreddit.mvp.data.entity.PostParent;

import io.reactivex.Observable;
import okhttp3.ResponseBody;


public interface PostMVP {

    interface View {
        void showToast(AccessToken accessToken);
        void setPostParent(PostParent postParent);
        void setInfoServerIsBroken();
        void setInfoNoConnection();

        void error(String errorString);
        void setLoadIndicatorOff();
    }

    interface Presenter {

        void setView(PostMVP.View view);

        void onStop();

        void loadPosts();

        void searchPosts();

        void postVote(String dir, String fullname);

    }

    interface Model {
        Observable<PostParent> getPosts();
        Observable<PostParent> searchPosts();
        Observable<ResponseBody> postVote(String dir, String fullname);
        boolean checkConnection();
//        void createUser(String name, String lastName);
//
//        User getUser();


    }
}
