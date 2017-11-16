package com.qartf.doseforreddit.presenter.post;

import com.qartf.doseforreddit.data.entity.AccessToken;
import com.qartf.doseforreddit.data.entity.PostParent;

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

        void setSaveStarActivated();

        void setSaveStarUnActivated();

    }

    interface Presenter {

        void setView(PostMVP.View view);

        void onStop();

        void loadPosts(String after);

        void searchPosts(String after);

        void postVote(String dir, String fullname);

        void postSave(String fullname);

        void postUnsave(String fullname);

    }

    interface Model {
        Observable<PostParent> getPosts(String after);

        Observable<PostParent> searchPosts(String after);

        Observable<ResponseBody> postVote(String dir, String fullname);

        Observable<ResponseBody> postSave(String fullname);

        Observable<ResponseBody> postUnsave(String fullname);

        boolean checkConnection();
//        void createUser(String name, String lastName);
//
//        User getUser();


    }
}
