package com.qartf.doseforreddit.mvp.subreddit;

import android.support.annotation.Nullable;

import com.qartf.doseforreddit.model.SubredditParent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

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
    public void loadSubreddits() {

        DisposableObserver<SubredditParent> disposableObserver = model.getSubreddits().observeOn(AndroidSchedulers.mainThread()).
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
                if (view != null) {
                    view.error(e.toString());
                }
            }

            @Override
            public void onComplete() {

            }

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
}