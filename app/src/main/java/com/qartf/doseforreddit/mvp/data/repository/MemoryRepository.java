package com.qartf.doseforreddit.mvp.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.model.PostParent;
import com.qartf.doseforreddit.model.SubredditParent;
import com.qartf.doseforreddit.mvp.data.network.RetrofitRedditAPI;

import java.util.ArrayDeque;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


public class MemoryRepository implements DataRepository.Retrofit {

    private static final long TIME_LIMIT_IN_SECONDS = 60;
    private static final int CALL_PER_MINUTE = 4;

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
    Subject<String> mObservable = PublishSubject.create();
    private Context context;
    private SharedPreferences sharedPreferences;
    private RetrofitRedditAPI retrofitRedditAPI;
    private DataRepository.Token token;
    private ArrayDeque<Long> callCounterLine = new ArrayDeque<>();



    public MemoryRepository(Context context, SharedPreferences sharedPreferences, RetrofitRedditAPI retrofitRedditAPI, DataRepository.Token token) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
        this.retrofitRedditAPI = retrofitRedditAPI;
        this.token = token;
        loadStrıngPref();
        this.callCounterLine.addFirst(System.currentTimeMillis() / 1000);
    }

    public boolean setCallCounter() {
        if (callCounterLine.size() > CALL_PER_MINUTE) {
            callCounterLine.removeLast();
        }

        long now = System.currentTimeMillis() / 1000;
        long timeGap = now - callCounterLine.getLast();
        if (callCounterLine.size() >= CALL_PER_MINUTE && TIME_LIMIT_IN_SECONDS > timeGap) {
            String waitString = String.valueOf(TIME_LIMIT_IN_SECONDS - timeGap);
            String x = "You have to wait:" + waitString + "seconds";
            mObservable.onNext(x);
            return true;
        }
        callCounterLine.addFirst(System.currentTimeMillis() / 1000);
        return false;
    }


    public Observable<Object> tokenInfo() {
        return mObservable.map(new Function<String, Object>() {
            @Override
            public Object apply(@NonNull String value) throws Exception {
                return String.valueOf(value);
            }
        });
    }

    public String getBearerToken(AccessToken accessToken) {
        String token = "bearer " + accessToken.getAccessToken();
        return token;
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

//    public Observable<AccessToken> getAccess() {
//        return Observable.create(new ObservableOnSubscribe<AccessToken>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<AccessToken> subscribe) throws Exception {
//                try {
//                    Cursor cursor = getUsers();
//                    String logged = sharedPreferences.getString(context.getResources().getString(R.string.pref_login_signed_in), Constants.Utility.ANONYMOUS);
//                    if (!logged.equals(Constants.Utility.ANONYMOUS) && cursor != null && !cursor.isClosed() && cursor.moveToFirst()) {
//                        do {
//                            String userName = cursor.getString(cursor.getColumnIndex(DatabaseContract.Accounts.USER_NAME));
//                            if (userName.equals(logged)) {
//                                AccessToken accessToken = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DatabaseContract.Accounts.ACCESS_TOKEN)), AccessToken.class);
//                                subscribe.onNext(accessToken);
//                            }
//                            subscribe.onComplete();
//                        } while (cursor.moveToNext());
//                    }
//                } catch (SQLException exception) {
//                    exception.printStackTrace();
//                }
//            }
//        });
//
//    }




    @Override
    public Observable<PostParent> getPosts() {

        if (setCallCounter()) {
            return Observable.empty();
        }

        return token.getAccessTokenX().flatMap(new Function<AccessToken, ObservableSource<PostParent>>() {
            @Override
            public ObservableSource<PostParent> apply(AccessToken accessToken) throws Exception {
                token.setAccessToken(accessToken);

                String subreddit = sharedPreferences.getString(prefPostSubreddit, prefPostSubredditDefault);
                String subredditSortBy = sharedPreferences.getString(prefPostSortBy, prefSortByDefault);
                HashMap<String, String> args = new HashMap<>();
                return retrofitRedditAPI.getPosts(getBearerToken(accessToken), subreddit, subredditSortBy, args);
            }
        });
    }


//    @Override
//    public Observable<PostParent> getPosts() {
//
//        if (setCallCounter()) {
//            return Observable.empty();
//        }
//
//        String subreddit = sharedPreferences.getString(prefPostSubreddit, prefPostSubredditDefault);
//        String subredditSortBy = sharedPreferences.getString(prefPostSortBy, prefSortByDefault);
//        HashMap<String, String> args = new HashMap<>();
//        return retrofitRedditAPI.getPosts(token.getBearerToken(), subreddit, subredditSortBy, args);
//    }

    @Override
    public Observable<SubredditParent> getSubreddits() {

        if (setCallCounter()) {
            return Observable.empty();
        }

        return token.getAccessTokenX().flatMap(new Function<AccessToken, ObservableSource<SubredditParent>>() {
            @Override
            public ObservableSource<SubredditParent> apply(AccessToken accessToken) throws Exception {
                token.setAccessToken(accessToken);

                String queryString = sharedPreferences.getString(prefSearchSubreddit, prefEmptyTag);
                HashMap<String, String> args = new HashMap<>();
                args.put("limit", "30");
                args.put("q", queryString);
                return retrofitRedditAPI.getSubreddits(getBearerToken(accessToken), args);
            }
        });
    }

}
