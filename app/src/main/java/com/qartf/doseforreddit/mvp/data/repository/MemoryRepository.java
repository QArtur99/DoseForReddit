package com.qartf.doseforreddit.mvp.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Base64;

import com.google.gson.Gson;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.database.DatabaseContract;
import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.model.PostParent;
import com.qartf.doseforreddit.mvp.data.network.RetrofitRedditAPI;
import com.qartf.doseforreddit.utility.Constants;

import java.util.HashMap;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;


public class MemoryRepository implements RetrofitRepository {

    private static final long STALE_MS = 20 * 1000;
    String prefPostSubreddit;
    String prefPostSortBy;
    String prefSearchPost;
    String prefSearchSubreddit;
    String prefEmptyTag;
    String prefSortByDefault;
    String prefPostSubredditDefault;
    String prefPostDetailSub;
    String prefPostDetailId;
    String prefPostDetailSortKey;
    private Context context;
    private SharedPreferences sharedPreferences;
    private RetrofitRedditAPI retrofitRedditAPI;
    private long timestamp;


    public MemoryRepository(Context context, SharedPreferences sharedPreferences, RetrofitRedditAPI retrofitRedditAPI) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
        this.retrofitRedditAPI = retrofitRedditAPI;
        this.timestamp = System.currentTimeMillis();
        loadStrıngPref();
    }

    private static String basicToken(String token) {
        String authString = token + ":";
        String encodedAuthString = Base64.encodeToString(authString.getBytes(), Base64.NO_WRAP);
        String newToken = "Basic " + encodedAuthString;
        return newToken;
    }

    private void loadStrıngPref() {
        prefPostSubreddit = context.getResources().getString(R.string.pref_post_subreddit);
        prefPostSortBy = context.getResources().getString(R.string.pref_post_sort_by);
        prefSearchPost = context.getResources().getString(R.string.pref_search_post);
        prefSearchSubreddit = context.getResources().getString(R.string.pref_search_subreddit);
        prefEmptyTag = context.getResources().getString(R.string.pref_empty_tag);
        prefSortByDefault = context.getResources().getString(R.string.pref_post_sort_by_default);
        prefPostSubredditDefault = context.getResources().getString(R.string.pref_post_subreddit_default);
        prefPostDetailSub = context.getResources().getString(R.string.pref_post_detail_sub);
        prefPostDetailId = context.getResources().getString(R.string.pref_post_detail_id);
        prefPostDetailSortKey = context.getResources().getString(R.string.pref_post_detail_sort_key);

    }

    public Cursor getUsers() {
        Cursor cursor = context.getContentResolver().query(DatabaseContract.Accounts.CONTENT_URI,
                DatabaseContract.Accounts.PROJECTION_LIST, null, null, null);
        return cursor;
    }

    public Observable<AccessToken> getAccessTokenUser() {
        AccessToken accessToken = null;
        Cursor cursor = getUsers();
        String logged = sharedPreferences.getString(context.getResources().getString(R.string.pref_login_signed_in), Constants.Utility.ANONYMOUS);
        if (!logged.equals(Constants.Utility.ANONYMOUS) && cursor != null && !cursor.isClosed() && cursor.moveToFirst()) {
            do {
                String userName = cursor.getString(cursor.getColumnIndex(DatabaseContract.Accounts.USER_NAME));
                if (userName.equals(logged)) {
                    accessToken = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DatabaseContract.Accounts.ACCESS_TOKEN)), AccessToken.class);
                }
            } while (cursor.moveToNext());
        }
        if (accessToken != null) {
            return refreshToken(accessToken);
        }
        return Observable.empty();
    }

    public Observable<AccessToken> getAccessTokenGuest() {
        String uuid = UUID.randomUUID().toString();
        return retrofitRedditAPI.guestToken(basicToken(Constants.Auth.CLIENT_ID), Constants.Auth.NO_NAME_GRAND_TYPE, uuid);
    }

    public Observable<AccessToken> getAccess() {
        return Observable.create(new ObservableOnSubscribe<AccessToken>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<AccessToken> subscribe) throws Exception {
                try {
                    Cursor cursor = getUsers();
                    String logged = sharedPreferences.getString(context.getResources().getString(R.string.pref_login_signed_in), Constants.Utility.ANONYMOUS);
                    if (!logged.equals(Constants.Utility.ANONYMOUS) && cursor != null && !cursor.isClosed() && cursor.moveToFirst()) {
                        do {
                            String userName = cursor.getString(cursor.getColumnIndex(DatabaseContract.Accounts.USER_NAME));
                            if (userName.equals(logged)) {
                                AccessToken accessToken = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DatabaseContract.Accounts.ACCESS_TOKEN)), AccessToken.class);
                                subscribe.onNext(accessToken);
                            }
                            subscribe.onComplete();
                        } while (cursor.moveToNext());
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        });

    }

//    @Override
//    public Observable<AccessToken> getAccessTokenFromMemory() {
//
//        if (isUpToDate() && accessToken != null) {
//            return Observable.just(accessToken);
//        } else {
//            timestamp = System.currentTimeMillis();
//            accessToken = null;
//            return Observable.empty();
//        }
//    }
//
//    public boolean isUpToDate() {
//        return System.currentTimeMillis() - timestamp < STALE_MS;
//    }

    @Override
    public Observable<AccessToken> refreshToken(AccessToken accessToken) {
        return retrofitRedditAPI.refreshToken(basicToken(Constants.Auth.CLIENT_ID), "refresh_token", accessToken.getRefreshToken());
    }

    private String getBearerToken(AccessToken accessToken) {
        String token = "bearer " + accessToken.getAccessToken();
        return token;
    }

    @Override
    public Observable<AccessToken> getAccessToken() {
        return getAccessTokenUser().switchIfEmpty(getAccessTokenGuest());
    }

    @Override
    public Observable<PostParent> getPosts(AccessToken accessToken) {
        String subreddit = sharedPreferences.getString(prefPostSubreddit, prefPostSubredditDefault);
        String subredditSortBy = sharedPreferences.getString(prefPostSortBy, prefSortByDefault);
        HashMap<String, String> args = new HashMap<>();
        return retrofitRedditAPI.getPosts(getBearerToken(accessToken), subreddit, subredditSortBy, args);
    }

}
