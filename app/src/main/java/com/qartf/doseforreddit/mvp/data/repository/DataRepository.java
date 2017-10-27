package com.qartf.doseforreddit.mvp.data.repository;

import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.model.PostParent;
import com.qartf.doseforreddit.model.SubredditParent;

import io.reactivex.Observable;

public interface DataRepository {
    interface Retrofit {
        Observable<PostParent> getPosts();

        Observable<SubredditParent> getSubreddits();

        Observable<Object> tokenInfo();
    }

    interface Token{
        Observable<AccessToken> getAccessTokenX();
        void setAccessToken(AccessToken accessToken);
    }
}
