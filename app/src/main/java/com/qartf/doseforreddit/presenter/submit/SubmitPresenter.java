package com.qartf.doseforreddit.presenter.submit;

import android.support.annotation.Nullable;

import com.qartf.doseforreddit.data.entity.RuleParent;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class SubmitPresenter implements SubmitMVP.Presenter {


    @Nullable
    private SubmitMVP.View view;
    private SubmitMVP.Model model;
    private CompositeDisposable disposable = new CompositeDisposable();

    public SubmitPresenter(SubmitMVP.Model model) {
        this.model = model;
    }

    @Override
    public void setView(SubmitMVP.View view) {
        this.view = view;
    }

    @Override
    public void getSubredditRules() {
        DisposableObserver<RuleParent> disposableObserver = model.getSubredditRules().observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).subscribeWith(new DisposableObserver<RuleParent>() {

            @Override
            public void onNext(@NonNull RuleParent postParent) {
                if (view != null) {
                    view.setSubredditRules(postParent);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() { }

        });
        disposable.add(disposableObserver);
    }
}
