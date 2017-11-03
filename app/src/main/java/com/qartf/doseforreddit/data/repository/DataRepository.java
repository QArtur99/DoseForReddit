package com.qartf.doseforreddit.data.repository;

import com.qartf.doseforreddit.data.entity.AccessToken;
import com.qartf.doseforreddit.data.entity.CommentParent;
import com.qartf.doseforreddit.data.entity.PostParent;
import com.qartf.doseforreddit.data.entity.RuleParent;
import com.qartf.doseforreddit.data.entity.SubredditParent;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public interface DataRepository {
    interface Retrofit {
        Observable<Object> tokenInfo();

        Observable<String> resetToken();

        Observable<String> getAboutMe();

        Observable<RuleParent> getSubredditRules();

        Observable<PostParent> getPosts();

        Observable<PostParent> searchPosts();

        Observable<CommentParent> getComments();

        Observable<ResponseBody> postVote(String dir, String fullname);

        Observable<SubredditParent> getSubreddits();

        Observable<SubredditParent> getMineSubreddits();

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
