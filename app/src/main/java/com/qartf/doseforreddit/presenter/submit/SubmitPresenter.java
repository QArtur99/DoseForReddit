package com.qartf.doseforreddit.presenter.submit;

import android.support.annotation.Nullable;

import com.qartf.doseforreddit.data.entity.RuleParent;
import com.qartf.doseforreddit.data.entity.SubmitParent;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;


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
            public void onNext(@NonNull RuleParent ruleParent) {
                if (view != null) {
                    if (ruleParent.rules != null && ruleParent.rules.size() > 0) {
                        view.setSubredditRules(ruleParent);
                    } else {
                        view.error("No rules for this subreddit!");
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if (view != null) {
                    if (model.checkConnection()) {
                        if (e instanceof HttpException) {
                            HttpException exception = (HttpException) e;
                            switch (exception.code()) {
                                case 403:
                                    view.error("Subreddit is not available!");
                                    break;
                                case 404:
                                    view.error("Subreddit does not exist!");
                                    break;
                                default:
                                    view.error("Something went wrong: " + e.getMessage());
                                    break;
                            }
                        }
                    } else {
                        view.error("Something wrong with internet connection");
                    }
                }
            }

            @Override
            public void onComplete() { }

        });
        disposable.add(disposableObserver);
    }

    @Override
    public void postSubmit(HashMap<String, String> args) {
        DisposableObserver<SubmitParent> disposableObserver = model.postSubmit(args).observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).subscribeWith(new DisposableObserver<SubmitParent>() {

            @Override
            public void onNext(@NonNull SubmitParent ruleParent) {
                if (view != null) {
                    if(ruleParent.success){
                        view.setSubmitted();
                    }else{
                        view.error("You are doing that too much. try again in 5 minute");
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if (view != null) {
                    view.error("Something went wrong: " + e.getMessage());
                }
            }

            @Override
            public void onComplete() { }

        });
        disposable.add(disposableObserver);
    }

}
