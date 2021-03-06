package com.qartf.doseforreddit.ui.comment;

import com.qartf.doseforreddit.data.entity.AccessToken;
import com.qartf.doseforreddit.data.entity.Comment;
import com.qartf.doseforreddit.data.entity.CommentParent;
import com.qartf.doseforreddit.data.entity.Post;
import com.qartf.doseforreddit.data.entity.SubmitParent;
import com.qartf.doseforreddit.data.entity.childComment.ChildCommentParent;

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

        void setSaveStarActivated();

        void setSaveStarUnActivated();

        void loadComments();

        void loadPosts();


        void showToast(AccessToken accessToken);

        void setCommentParent(CommentParent postParent);

        void setChildCommentParent(ChildCommentParent postParent);

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

        void loadChildComments(Comment comment);

        void postComment(String fullname, String text);

        void postVote(String dir, String fullname);

        void postSave(String fullname);

        void postUnsave(String fullname);

        void postDel(String fullname);

        void postDelPost(String fullname);
    }

    interface Model {
        Observable<CommentParent> getComments();

        Observable<ChildCommentParent> getMorechildren(Comment comment);

        Observable<ResponseBody> postVote(String dir, String fullname);

        Observable<SubmitParent> postComment(String fullname, String text);

        Observable<ResponseBody> postSave(String fullname);

        Observable<ResponseBody> postUnsave(String fullname);

        Observable<ResponseBody> postDel(String fullname);

        boolean checkConnection();
    }
}
