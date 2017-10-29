package com.qartf.doseforreddit.mvp.presenter.subreddit;

import com.qartf.doseforreddit.mvp.data.entity.AccessToken;
import com.qartf.doseforreddit.mvp.data.entity.SubredditParent;

import io.reactivex.Observable;
import okhttp3.ResponseBody;


public interface SubredditMVP {

    interface View {
        void setInfoServerIsBroken();

        void setInfoNoConnection();

        void showToast(AccessToken accessToken);

        void setSubredditParent(SubredditParent postParent);

        void error(String errorString);

        void setLoadIndicatorOff();
    }

    interface Presenter {

        void setView(SubredditMVP.View view);

        void onStop();

        void loadSubreddits();

        void postSubscribe(String subscribe, String subredditName);
    }

    interface Model {
        Observable<SubredditParent> getSubreddits();

        Observable<ResponseBody> postSubscribe(String subscribe, String fullname);

        boolean checkConnection();

    }
}
