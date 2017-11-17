package com.qartf.doseforreddit.presenter.subreddit;

import com.qartf.doseforreddit.data.entity.AccessToken;
import com.qartf.doseforreddit.data.entity.SubredditParent;

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

        void setRefreshing();

        void setPostSubscribe();
    }

    interface Presenter {

        void setView(SubredditMVP.View view);

        void onStop();

        void loadSubreddits(String after);

        void loadMineSubreddits(String after);

        void postSubscribe(String subscribe, String subredditName);
    }

    interface Model {
        Observable<SubredditParent> getSubreddits(String after);

        Observable<SubredditParent> getMineSubreddits(String after);

        Observable<ResponseBody> postSubscribe(String subscribe, String fullname);

        boolean checkConnection();

    }
}
