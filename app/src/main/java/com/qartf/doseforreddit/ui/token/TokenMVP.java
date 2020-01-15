package com.qartf.doseforreddit.ui.token;

import io.reactivex.Observable;

public interface TokenMVP {

    interface View {
        void tokenInfo(String info);
        void getPosts();
    }

    interface Presenter {

        void getAboutMe();

        void resetToken();

        void tokenInfo();

        void setView(TokenMVP.View view);

        void onStop();
    }

    interface Model {
        Observable<Object> tokenInfo();

        Observable<String> resetToken();

        Observable<String> getAboutMe();
    }

}
