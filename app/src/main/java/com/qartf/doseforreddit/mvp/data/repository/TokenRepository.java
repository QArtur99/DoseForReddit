package com.qartf.doseforreddit.mvp.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Base64;

import com.google.gson.Gson;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.mvp.data.database.DatabaseContract;
import com.qartf.doseforreddit.mvp.data.database.DatabaseHelper;
import com.qartf.doseforreddit.mvp.data.entity.AccessToken;
import com.qartf.doseforreddit.mvp.data.network.RetrofitRedditAPI;
import com.qartf.doseforreddit.mvp.presenter.utility.Constants;

import java.util.UUID;

import io.reactivex.Observable;


public class TokenRepository implements DataRepository.Token {

    private static final long TIME_LIMIT_HOUR = 60 * 60;


    private Context context;
    private AccessToken accessToken;
    private SharedPreferences sharedPreferences;
    private RetrofitRedditAPI retrofitRedditAPI;
    private long timestamp;
    ;


    public TokenRepository(Context context, SharedPreferences sharedPreferences, RetrofitRedditAPI retrofitRedditAPI) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
        this.retrofitRedditAPI = retrofitRedditAPI;
        this.timestamp = System.currentTimeMillis() / 1000;
    }

    private static String basicToken(String token) {
        String authString = token + ":";
        String encodedAuthString = Base64.encodeToString(authString.getBytes(), Base64.NO_WRAP);
        String newToken = "Basic " + encodedAuthString;
        return newToken;
    }

    public Observable<AccessToken> getAccessTokenX() {
        long currentGap = (System.currentTimeMillis() / 1000) - timestamp;

        if (accessToken == null) {
            Cursor cursor = DatabaseHelper.getUsers(context);
            String logged = sharedPreferences.getString(context.getResources().getString(R.string.pref_login_signed_in), Constants.Utility.ANONYMOUS);
            if (!logged.equals(Constants.Utility.ANONYMOUS) && cursor != null && !cursor.isClosed() && cursor.moveToFirst()) {
                do {
                    String userName = cursor.getString(cursor.getColumnIndex(DatabaseContract.Accounts.USER_NAME));
                    if (userName.equals(logged)) {
                        accessToken = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DatabaseContract.Accounts.ACCESS_TOKEN)), AccessToken.class);
                    }
                } while (cursor.moveToNext());
            }
            this.timestamp = System.currentTimeMillis() / 1000;

            if (accessToken != null) {
                return refreshTokenRX(accessToken);
            } else {
                accessToken = new AccessToken();
                return getAccessTokenGuest();
            }
        } else if (currentGap >= TIME_LIMIT_HOUR) {
            this.timestamp = System.currentTimeMillis() / 1000;
            String logged = sharedPreferences.getString(context.getResources().getString(R.string.pref_login_signed_in), Constants.Utility.ANONYMOUS);

            if (!logged.equals(Constants.Utility.ANONYMOUS)) {
                return refreshTokenRX(accessToken);
            } else {
                return getAccessTokenGuest();
            }
        }

        return Observable.just(accessToken);
    }

    @Override
    public AccessToken getAccessToken() {
        return accessToken;
    }

    @Override
    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public void setAccessTokenValue(String accessTokenValue) {
        this.accessToken.setAccessToken(accessTokenValue);
    }

    public Observable<AccessToken> accessTokenRX() {
        String loginCode = sharedPreferences.getString(context.getResources().getString(R.string.pref_access_code), "");
        return retrofitRedditAPI.accessTokenRX(basicToken(Constants.Auth.CLIENT_ID), "authorization_code", loginCode, Constants.Auth.REDIRECT_URI);
    }

    public Observable<AccessToken> refreshTokenRX(AccessToken accessToken) {
        return retrofitRedditAPI.refreshTokenRX(basicToken(Constants.Auth.CLIENT_ID), "refresh_token", accessToken.getRefreshToken());
    }

    public Observable<AccessToken> getAccessTokenGuest() {
        String uuid = UUID.randomUUID().toString();
        return retrofitRedditAPI.guestTokenRX(basicToken(Constants.Auth.CLIENT_ID), Constants.Auth.NO_NAME_GRAND_TYPE, uuid);
    }
}
