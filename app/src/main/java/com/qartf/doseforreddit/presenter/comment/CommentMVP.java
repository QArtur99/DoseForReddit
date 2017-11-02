package com.qartf.doseforreddit.presenter.comment;

import com.qartf.doseforreddit.data.entity.AccessToken;
import com.qartf.doseforreddit.data.entity.CommentParent;
import com.qartf.doseforreddit.data.entity.Post;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by ART_F on 2017-10-28.
 */

public interface CommentMVP {
    interface View {

        void setInfoServerIsBroken();

        void setInfoNoConnection();


        void setUps(String upsString);

        void setLikes(String postLikes);

        void setTitle(String titleString);

        void setLinkFlairText(String linkFlairTextString);

        void setDomain(String domainString);

        void setSubreddit(String subredditString);

        void setComments(String commentsString);

        void setTime(String timeString);

        void setThumbnail(Post post);

        void setSelftext(String selftextString);

        void setCommentsNo(String commentsNoString);


        void showToast(AccessToken accessToken);

        void setCommentParent(CommentParent postParent);

        void error(String errorString);

        void setLoadIndicatorOff();
    }

    interface Presenter {

        void setView(CommentMVP.View view);

        void setPost(Post post);

        Post getPost();

        void loadPostData();

        void onStop();

        void loadComments();

        void postVote(String dir, String fullname);
    }

    interface Model {
        Observable<CommentParent> getComments();

        Observable<ResponseBody> postVote(String dir, String fullname);

        boolean checkConnection();
    }
}
