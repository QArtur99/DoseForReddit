package com.qartf.doseforreddit.mvp.retrofit;

import android.support.annotation.Nullable;

import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.model.PostParent;

import java.util.ArrayDeque;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class RetrofitPresenter implements RetrofitMVP.Presenter {

    private static final long TIME_LIMIT_IN_SECONDS = 60;
    private static final int CALL_PER_MINUTE = 4;

    @Nullable
    private RetrofitMVP.View view;
    private RetrofitMVP.Model model;
    private CompositeDisposable disposable = new CompositeDisposable();
    private ArrayDeque<Long> callCounterLine = new ArrayDeque<>();

    public RetrofitPresenter(RetrofitMVP.Model model) {
        this.model = model;
        callCounterLine.addFirst(System.currentTimeMillis() / 1000);
    }

    public boolean setCallCounter() {
        if (callCounterLine.size() > CALL_PER_MINUTE) {
            callCounterLine.removeLast();
        }

        long now = System.currentTimeMillis() / 1000;
        long timeGap = now - callCounterLine.getLast();
        if (callCounterLine.size() >= CALL_PER_MINUTE && TIME_LIMIT_IN_SECONDS > timeGap) {
            if (view != null) {
                String waitString = String.valueOf(TIME_LIMIT_IN_SECONDS - timeGap);
                view.error("You have to wait:" + waitString + "seconds");
            }
            return true;
        }
        callCounterLine.addFirst(System.currentTimeMillis() / 1000);
        return false;
    }

    @Override
    public void loadAccessToken() {
//        Disposable subscribe = model.result().observeOn(AndroidSchedulers.mainThread()).
//                subscribeOn(Schedulers.io()).subscribe(new Consumer<AccessToken>() {
//            @Override
//            public void accept(AccessToken accessToken) throws Exception {
//                if (view != null) {
//                    view.showToast(accessToken);
//                }
//            }
//        });
//        disposable.add(subscribe);
        if (setCallCounter()) {
            return;
        }

        DisposableObserver<AccessToken> disposableObserver = model.getAccessToken().observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).subscribeWith(new DisposableObserver<AccessToken>() {

            @Override
            public void onNext(@NonNull AccessToken accessToken) {
                if (view != null) {
                    view.setAccessToken(accessToken);
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
    public void loadPosts(AccessToken accessToken) {
        if (setCallCounter()) {
            return;
        }

        DisposableObserver<PostParent> disposableObserver = model.getPosts(accessToken).observeOn(AndroidSchedulers.mainThread()).
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
    public void rxUnsubscribe() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @Override
    public void setView(RetrofitMVP.View view) {
        this.view = view;
    }
}
