package com.qartf.doseforreddit.mvp.presenter.post;

import android.support.annotation.Nullable;

import com.qartf.doseforreddit.mvp.data.model.PostParent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;


public class PostPresenter implements PostMVP.Presenter {

    @Nullable
    private PostMVP.View view;
    private PostMVP.Model model;
    private CompositeDisposable disposable = new CompositeDisposable();

    public PostPresenter(PostMVP.Model model) {
        this.model = model;
    }

    @Override
    public void loadPosts() {
        DisposableObserver<PostParent> disposableObserver = model.getPosts().observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).subscribeWith(new DisposableObserver<PostParent>() {

            @Override
            public void onNext(@NonNull PostParent postParent) {
                if (view != null) {
                    view.setPostParent(postParent);
                    view.setLoadIndicatorOff();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                checkConnection();
            }

            @Override
            public void onComplete() { }

        });
        disposable.add(disposableObserver);

    }

    @Override
    public void searchPosts() {
        DisposableObserver<PostParent> disposableObserver = model.searchPosts().observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).subscribeWith(new DisposableObserver<PostParent>() {

            @Override
            public void onNext(@NonNull PostParent postParent) {
                if (view != null) {
                    view.setPostParent(postParent);
                    view.setLoadIndicatorOff();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                checkConnection();
            }

            @Override
            public void onComplete() { }

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
                    view.error("Success");
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                checkConnection();
            }

            @Override
            public void onComplete() { }

        });
        disposable.add(disposableObserver);
    }

    @Override
    public void onStop() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
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

    @Override
    public void setView(PostMVP.View view) {
        this.view = view;
    }
}
