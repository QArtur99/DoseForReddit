package com.qartf.doseforreddit.presenter.token;

import android.support.annotation.Nullable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class TokenPresenter implements TokenMVP.Presenter {

    @Nullable
    private TokenMVP.View view;
    private TokenMVP.Model model;
    private CompositeDisposable disposable = new CompositeDisposable();

    public TokenPresenter(TokenMVP.Model model) {
        this.model = model;
    }

    @Override
    public void getAboutMe() {
        DisposableObserver<String> disposableObserver = model.getAboutMe().observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).subscribeWith(new DisposableObserver<String>() {

            @Override
            public void onNext(@NonNull String accessToken) {
                if (view != null) {
                    view.tokenInfo(accessToken);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if (view != null) {
                    view.tokenInfo(e.toString());
                }
            }

            @Override
            public void onComplete() {

            }

        });
        disposable.add(disposableObserver);
    }

    @Override
    public void resetToken() {
        DisposableObserver<String> disposableObserver = model.resetToken().observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).subscribeWith(new DisposableObserver<String>() {

            @Override
            public void onNext(@NonNull String accessToken) {
                if (view != null) {
                    view.tokenInfo(accessToken);
                    view.getPosts();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if (view != null) {
                    view.tokenInfo(e.toString());
                }
            }

            @Override
            public void onComplete() {

            }

        });
        disposable.add(disposableObserver);
    }

    @Override
    public void tokenInfo() {
        DisposableObserver<Object> disposableObserver = model.tokenInfo().observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).subscribeWith(new DisposableObserver<Object>() {

            @Override
            public void onNext(@NonNull Object accessToken) {
                if (view != null) {
                    view.tokenInfo(accessToken.toString());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if (view != null) {
//                    view.error(e.toString());
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
    public void setView(TokenMVP.View view) {
        this.view = view;
    }

}