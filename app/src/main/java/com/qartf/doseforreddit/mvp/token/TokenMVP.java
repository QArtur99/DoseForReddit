package com.qartf.doseforreddit.mvp.token;

import io.reactivex.Observable;

public interface TokenMVP {

    interface View {
        void tokenInfo(String info);
    }

    interface Presenter {

        void tokenInfo();

        void setView(TokenMVP.View view);

        void onStop();
    }

    interface Model {
        Observable<Object> tokenInfo();
    }

}
