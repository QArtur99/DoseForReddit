package com.qartf.doseforreddit.presenter.subreddit;

import android.support.annotation.Nullable;

import com.qartf.doseforreddit.data.entity.SubredditParent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by ART_F on 2017-10-26.
 */

public class SubredditPresenter implements SubredditMVP.Presenter {


    @Nullable
    private SubredditMVP.View view;
    private SubredditMVP.Model model;
    private CompositeDisposable disposable = new CompositeDisposable();

    public SubredditPresenter(SubredditMVP.Model model) {
        this.model = model;

    }

    @Override
    public void loadSubreddits(String after) {
        DisposableObserver<SubredditParent> disposableObserver = model.getSubreddits(after).observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).subscribeWith(new DisposableObserver<SubredditParent>() {

            @Override
            public void onNext(@NonNull SubredditParent postParent) {
                if (view != null) {
                    view.setSubredditParent(postParent);
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
    public void loadMineSubreddits(String after) {
        DisposableObserver<SubredditParent> disposableObserver = model.getMineSubreddits(after).observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).subscribeWith(new DisposableObserver<SubredditParent>() {

            @Override
            public void onNext(@NonNull SubredditParent postParent) {
                if (view != null) {
                    view.setSubredditParent(postParent);
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
    public void postSubscribe(String subscribe, String subredditName) {
        DisposableObserver<ResponseBody> disposableObserver = model.postSubscribe(subscribe, subredditName).observeOn(AndroidSchedulers.mainThread()).
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

    @Override
    public void setView(SubredditMVP.View view) {
        this.view = view;
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