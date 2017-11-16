package com.qartf.doseforreddit.data.repository;

import com.qartf.doseforreddit.data.entity.AccessToken;
import com.qartf.doseforreddit.data.entity.Comment;
import com.qartf.doseforreddit.data.entity.CommentParent;
import com.qartf.doseforreddit.data.entity.PostParent;
import com.qartf.doseforreddit.data.entity.RuleParent;
import com.qartf.doseforreddit.data.entity.SubmitParent;
import com.qartf.doseforreddit.data.entity.SubredditParent;
import com.qartf.doseforreddit.data.entity.childComment.ChildCommentParent;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public interface DataRepository {
    interface Retrofit {
        Observable<Object> tokenInfo();

        Observable<String> resetToken();

        Observable<String> getAboutMe();

        Observable<RuleParent> getSubredditRules();

        Observable<SubmitParent> postSubmit(HashMap<String, String> args);

        Observable<PostParent> getPosts(String after);

        Observable<PostParent> searchPosts(String after);

        Observable<CommentParent> getComments();

        Observable<SubmitParent> postComment(String fullname, String text);

        Observable<ChildCommentParent> getMorechildren(Comment comment);

        Observable<ResponseBody> postVote(String dir, String fullname);

        Observable<ResponseBody> postSave(String fullname);

        Observable<ResponseBody> postUnsave(String fullname);

        Observable<ResponseBody> postDel(String fullname);

        Observable<SubredditParent> getSubreddits(String after);

        Observable<SubredditParent> getMineSubreddits(String after);

        Observable<ResponseBody> postSubscribe(String subscribe, String fullname);

    }

    interface Token {
        Observable<AccessToken> getAccessTokenX();

        Observable<AccessToken> accessTokenRX();

        void setAccessTokenValue(String accessTokenValue);

        AccessToken getAccessToken();

        void setAccessToken(AccessToken accessToken);
    }

    interface Utility {
        boolean checkConnection();
    }
}
