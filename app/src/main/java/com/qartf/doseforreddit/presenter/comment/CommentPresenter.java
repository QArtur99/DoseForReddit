package com.qartf.doseforreddit.presenter.comment;

import android.support.annotation.Nullable;

import com.qartf.doseforreddit.data.entity.CommentParent;
import com.qartf.doseforreddit.data.entity.Post;
import com.qartf.doseforreddit.presenter.utility.Utility;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


public class CommentPresenter implements CommentMVP.Presenter {

    public static final String DOT = "\u2022";

    @Nullable
    private CommentMVP.View view;
    private CommentMVP.Model model;
    private CompositeDisposable disposable = new CompositeDisposable();
    private Post post;

    public CommentPresenter(CommentMVP.Model model) {
        this.model = model;
    }

    @Override
    public void loadComments() {
        DisposableObserver<CommentParent> disposableObserver = model.getComments().observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).subscribeWith(new DisposableObserver<CommentParent>() {

            @Override
            public void onNext(@NonNull CommentParent postParent) {
                if (view != null) {
                    view.setCommentParent(postParent);
                    view.setLoadIndicatorOff();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                checkConnection();
            }

            @Override
            public void onComplete() {    }

        });
        disposable.add(disposableObserver);

    }

    @Override
    public void postVote(String dir, String fullname) {
        DisposableObserver<ResponseBody> disposableObserver = model.postVote(dir, fullname).observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).subscribeWith(new DisposableObserver<ResponseBody>() {

            @Override
            public void onNext(@NonNull ResponseBody postParent) {
                if (view != null) {
//                    view.error("Success");
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                checkConnection();
            }

            @Override
            public void onComplete() {    }

        });
        disposable.add(disposableObserver);
    }

    @Override
    public void onStop() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void setView(CommentMVP.View view) {
        this.view = view;
    }

    @Override
    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public Post getPost() {
        return post;
    }

    @Override
    public void loadPostData() {
        if (view != null) {
            view.setUps(Utility.upsFormatS(Integer.valueOf(post.ups)));
            view.setLikes(post.likes);
            view.setTitle(post.title);
            view.setLinkFlairText(post.linkFlairText);
            view.setDomain("(" + post.domain + ")");
            view.setSubreddit(post.subreddit);
            view.setComments(DOT + post.numComents + " comments");
            view.setTime(Utility.timeFormat(post.createdUTC));
            view.setThumbnail(post);
            view.setSelftext(post.selftext);
            view.setCommentsNo(post.numComents + " comments");
        }
    }

    public void checkConnection() {
        if (view != null) {
            if (model.checkConnection()) {
                view.setInfoServerIsBroken();
            } else {
                view.setInfoNoConnection();
            }
        }
    }

}


