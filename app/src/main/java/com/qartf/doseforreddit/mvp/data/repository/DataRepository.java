package com.qartf.doseforreddit.mvp.data.repository;

import com.qartf.doseforreddit.mvp.data.entity.AccessToken;
import com.qartf.doseforreddit.mvp.data.entity.CommentParent;
import com.qartf.doseforreddit.mvp.data.entity.PostParent;
import com.qartf.doseforreddit.mvp.data.entity.SubredditParent;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public interface DataRepository {
    interface Retrofit {
        Observable<Object> tokenInfo();

        Observable<String> resetToken();

        Observable<String> getAboutMe();

        Observable<PostParent> getPosts();

        Observable<PostParent> searchPosts();

        Observable<CommentParent> getComments();

        Observable<ResponseBody> postVote(String dir, String fullname);

        Observable<SubredditParent> getSubreddits();

        Observable<ResponseBody> postSubscribe(String subscribe, String fullname);

    }

    interface Token {
        Observable<AccessToken> getAccessTokenX();

        Observable<AccessToken> accessTokenRX();

        void setAccessTokenValue(String accessTokenValue);

        AccessToken getAccessToken();

        void setAccessToken(AccessToken accessToken);
    }

    interface Utility{
        boolean checkConnection();
    }
}
