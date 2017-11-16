package com.qartf.doseforreddit.data.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.data.database.DatabaseHelper;
import com.qartf.doseforreddit.data.entity.AboutMe;
import com.qartf.doseforreddit.data.entity.AccessToken;
import com.qartf.doseforreddit.data.entity.Comment;
import com.qartf.doseforreddit.data.entity.CommentParent;
import com.qartf.doseforreddit.data.entity.PostParent;
import com.qartf.doseforreddit.data.entity.RuleParent;
import com.qartf.doseforreddit.data.entity.SubmitParent;
import com.qartf.doseforreddit.data.entity.SubredditParent;
import com.qartf.doseforreddit.data.entity.childComment.ChildCommentParent;
import com.qartf.doseforreddit.data.exception.ResetTokenException;
import com.qartf.doseforreddit.data.network.RetrofitRedditAPI;

import java.util.ArrayDeque;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import okhttp3.ResponseBody;


public class RetrofitRepository implements DataRepository.Retrofit {

    private static final long TIME_LIMIT_IN_SECONDS = 60;
    private static final int CALL_PER_MINUTE = 60;

    private String prefPostSubreddit;
    private String prefPostSubredditRule;
    private String prefPostSortBy;
    private String prefSearchPost;
    private String prefSearchSubreddit;
    private String prefEmptyTag;
    private String prefSortByDefault;
    private String prefPostSubredditDefault;
    private String prefPostDetailSub;
    private String prefPostDetailId;
    private String prefPostDetailSortKey;
    private Subject<String> mObservable = PublishSubject.create();
    private Context context;
    private SharedPreferences sharedPreferences;
    private RetrofitRedditAPI retrofitRedditAPI;
    private DataRepository.Token token;
    private ArrayDeque<Long> callCounterLine = new ArrayDeque<>();


    public RetrofitRepository(Context context, SharedPreferences sharedPreferences, RetrofitRedditAPI retrofitRedditAPI, DataRepository.Token token) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
        this.retrofitRedditAPI = retrofitRedditAPI;
        this.token = token;
        loadStrıngPref();
        this.callCounterLine.addFirst(System.currentTimeMillis() / 1000);
    }

    private void loadStrıngPref() {
        prefPostSubreddit = context.getResources().getString(R.string.pref_post_subreddit);
        prefPostSubredditRule = context.getResources().getString(R.string.pref_post_subreddit_rule);
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

    private boolean setCallCounter() {
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

    @Override
    public Observable<String> resetToken() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> subscribe) throws Exception {

                token.setAccessToken(null);
                AccessToken accessToken = token.getAccessToken();
                if (accessToken == null) {
                    subscribe.onNext("User siggned out");
                } else {
                    subscribe.onError(new ResetTokenException("Something wrong :("));
                }

            }
        });
    }

    @Override
    public Observable<String> getAboutMe() {

        if (setCallCounter()) {
            return Observable.empty();
        }

        return token.accessTokenRX().flatMap(new Function<AccessToken, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(AccessToken accessToken) throws Exception {
                token.setAccessToken(accessToken);

                return Observable.zip(Observable.just(accessToken), retrofitRedditAPI.getAboutMe(getBearerToken(accessToken))
                        , new BiFunction<AccessToken, AboutMe, String>() {

                            @Override
                            public String apply(AccessToken accessToken, AboutMe aboutMe) throws Exception {
                                String userName = aboutMe.name;

                                sharedPreferences.edit().putString(context.getResources().getString(R.string.pref_login_signed_in), userName).apply();
                                Cursor cursor = DatabaseHelper.updateUser(context, userName);

                                if (cursor != null && cursor.getCount() == 0) {
                                    Uri newUri = DatabaseHelper.insertAccount(context, userName, accessToken);
                                    if (newUri == null) {
                                        return context.getResources().getString(R.string.editor_insert_user_failed);
                                    } else {
                                        return context.getResources().getString(R.string.editor_insert_user_successful);
                                    }
                                } else {
                                    int rowsUpdated = DatabaseHelper.updateAccount(context, userName, accessToken);
                                    if (rowsUpdated == 0) {
                                        return context.getResources().getString(R.string.editor_insert_user_failed);
                                    } else {
                                        return context.getResources().getString(R.string.editor_insert_user_successful);
                                    }

                                }
                            }
                        });
            }
        });
    }

    @Override
    public Observable<RuleParent> getSubredditRules() {
        if (setCallCounter()) {
            return Observable.empty();
        }

        return token.getAccessTokenX().flatMap(new Function<AccessToken, ObservableSource<RuleParent>>() {
            @Override
            public ObservableSource<RuleParent> apply(AccessToken accessToken) throws Exception {
                token.setAccessTokenValue(accessToken.getAccessToken());

                String subreddit = sharedPreferences.getString(prefPostSubredditRule, prefEmptyTag);

                HashMap<String, String> args = new HashMap<>();
                return retrofitRedditAPI.getSubredditRules(getBearerToken(accessToken), subreddit, args);
            }
        });
    }

    @Override
    public Observable<SubmitParent> postSubmit(final HashMap<String, String> args) {

        if (setCallCounter()) {
            return Observable.empty();
        }

        return token.getAccessTokenX().flatMap(new Function<AccessToken, ObservableSource<SubmitParent>>() {
            @Override
            public ObservableSource<SubmitParent> apply(AccessToken accessToken) throws Exception {
                token.setAccessTokenValue(accessToken.getAccessToken());

                return retrofitRedditAPI.postSubmit(getBearerToken(accessToken), args);
            }
        });
    }

    @Override
    public Observable<PostParent> getPosts(final String after) {

        if (setCallCounter()) {
            return Observable.empty();
        }

        return token.getAccessTokenX().flatMap(new Function<AccessToken, ObservableSource<PostParent>>() {
            @Override
            public ObservableSource<PostParent> apply(AccessToken accessToken) throws Exception {
                token.setAccessTokenValue(accessToken.getAccessToken());

                String subreddit = sharedPreferences.getString(prefPostSubreddit, prefPostSubredditDefault);
                String subredditSortBy = sharedPreferences.getString(prefPostSortBy, prefSortByDefault);
                HashMap<String, String> args = new HashMap<>();
                args.put("after", after);
                return retrofitRedditAPI.getPosts(getBearerToken(accessToken), subreddit, subredditSortBy, args);
            }
        });
    }


    @Override
    public Observable<PostParent> searchPosts(final String after) {

        if (setCallCounter()) {
            return Observable.empty();
        }

        return token.getAccessTokenX().flatMap(new Function<AccessToken, ObservableSource<PostParent>>() {
            @Override
            public ObservableSource<PostParent> apply(AccessToken accessToken) throws Exception {
                token.setAccessTokenValue(accessToken.getAccessToken());

                String queryString = sharedPreferences.getString(prefSearchPost, prefEmptyTag);
                String subreddit = sharedPreferences.getString(prefPostSubreddit, prefPostSubredditDefault);
                String subredditSortBy = sharedPreferences.getString(prefPostSortBy, prefSortByDefault);

                HashMap<String, String> args = new HashMap<>();
                args.put("limit", "25");
                args.put("q", queryString);
                args.put("restrict_sr", "true");
                args.put("sort", subredditSortBy);
                args.put("after", after);
                return retrofitRedditAPI.searchPosts(getBearerToken(accessToken), subreddit, args);
            }
        });
    }


    @Override
    public Observable<CommentParent> getComments() {

        if (setCallCounter()) {
            return Observable.empty();
        }

        return token.getAccessTokenX().flatMap(new Function<AccessToken, ObservableSource<CommentParent>>() {
            @Override
            public ObservableSource<CommentParent> apply(AccessToken accessToken) throws Exception {
                token.setAccessTokenValue(accessToken.getAccessToken());

                String postSub = sharedPreferences.getString(prefPostDetailSub, prefEmptyTag);
                String postId = sharedPreferences.getString(prefPostDetailId, prefEmptyTag);
                String sortBy = sharedPreferences.getString(prefPostDetailSortKey, prefEmptyTag);


                HashMap<String, String> args = new HashMap<>();
                args.put("depth", "8");
                args.put("limit", "25");
                args.put("showedits", "false");
                args.put("showmore", "true");
                args.put("sort", sortBy);

                return retrofitRedditAPI.getComments(getBearerToken(accessToken), postSub, postId, args);
            }
        });
    }

    @Override
    public Observable<SubmitParent> postComment(final String fullname, final String text) {
        if (setCallCounter()) {
            return Observable.empty();
        }

        return token.getAccessTokenX().flatMap(new Function<AccessToken, ObservableSource<SubmitParent>>() {
            @Override
            public ObservableSource<SubmitParent> apply(AccessToken accessToken) throws Exception {
                token.setAccessTokenValue(accessToken.getAccessToken());

                String queryString = sharedPreferences.getString(prefSearchSubreddit, prefEmptyTag);
                HashMap<String, String> args = new HashMap<>();
                args.put("text", text);
                args.put("thing_id", fullname);
                return retrofitRedditAPI.postComment(getBearerToken(accessToken), args);
            }
        });
    }

    @Override
    public Observable<ChildCommentParent> getMorechildren(final Comment comment) {

        if (setCallCounter()) {
            return Observable.empty();
        }

        return token.getAccessTokenX().flatMap(new Function<AccessToken, ObservableSource<ChildCommentParent>>() {
            @Override
            public ObservableSource<ChildCommentParent> apply(AccessToken accessToken) throws Exception {
                token.setAccessTokenValue(accessToken.getAccessToken());

                String postSub = sharedPreferences.getString(prefPostDetailSub, prefEmptyTag);
                String postId = sharedPreferences.getString(prefPostDetailId, prefEmptyTag);
                String sortBy = sharedPreferences.getString(prefPostDetailSortKey, prefEmptyTag);


                HashMap<String, String> args = new HashMap<>();
                args.put("api_type", "json");
                args.put("children", comment.childs.toString().replace("[", "").replace("]", ""));
                args.put("limit_children", "false");
                args.put("link_id", comment.parentId);
                args.put("sort", sortBy);

                return retrofitRedditAPI.getMorechildren(getBearerToken(accessToken), args);
            }
        });
    }


    @Override
    public Observable<ResponseBody> postVote(final String dir, final String fullname) {

        if (setCallCounter()) {
            return Observable.empty();
        }

        return token.getAccessTokenX().flatMap(new Function<AccessToken, ObservableSource<ResponseBody>>() {
            @Override
            public ObservableSource<ResponseBody> apply(AccessToken accessToken) throws Exception {
                token.setAccessTokenValue(accessToken.getAccessToken());

                String queryString = sharedPreferences.getString(prefSearchSubreddit, prefEmptyTag);
                HashMap<String, String> args = new HashMap<>();
                args.put("limit", "25");
                args.put("q", queryString);
                return retrofitRedditAPI.postVote(getBearerToken(accessToken), dir, fullname);
            }
        });
    }

    @Override
    public Observable<ResponseBody> postSave(final String fullname) {

        if (setCallCounter()) {
            return Observable.empty();
        }

        return token.getAccessTokenX().flatMap(new Function<AccessToken, ObservableSource<ResponseBody>>() {
            @Override
            public ObservableSource<ResponseBody> apply(AccessToken accessToken) throws Exception {
                token.setAccessTokenValue(accessToken.getAccessToken());

                HashMap<String, String> args = new HashMap<>();
                args.put("id", fullname);
                return retrofitRedditAPI.postSave(getBearerToken(accessToken), args);
            }
        });
    }

    @Override
    public Observable<ResponseBody> postUnsave(final String fullname) {

        if (setCallCounter()) {
            return Observable.empty();
        }

        return token.getAccessTokenX().flatMap(new Function<AccessToken, ObservableSource<ResponseBody>>() {
            @Override
            public ObservableSource<ResponseBody> apply(AccessToken accessToken) throws Exception {
                token.setAccessTokenValue(accessToken.getAccessToken());

                HashMap<String, String> args = new HashMap<>();
                args.put("id", fullname);
                return retrofitRedditAPI.postUnsave(getBearerToken(accessToken), args);
            }
        });
    }

    @Override
    public Observable<ResponseBody> postDel(final String fullname) {

        if (setCallCounter()) {
            return Observable.empty();
        }

        return token.getAccessTokenX().flatMap(new Function<AccessToken, ObservableSource<ResponseBody>>() {
            @Override
            public ObservableSource<ResponseBody> apply(AccessToken accessToken) throws Exception {
                token.setAccessTokenValue(accessToken.getAccessToken());

                HashMap<String, String> args = new HashMap<>();
                args.put("id", fullname);
                return retrofitRedditAPI.postDel(getBearerToken(accessToken), args);
            }
        });
    }

    @Override
    public Observable<SubredditParent> getSubreddits(final String after) {

        if (setCallCounter()) {
            return Observable.empty();
        }

        return token.getAccessTokenX().flatMap(new Function<AccessToken, ObservableSource<SubredditParent>>() {
            @Override
            public ObservableSource<SubredditParent> apply(AccessToken accessToken) throws Exception {
                token.setAccessTokenValue(accessToken.getAccessToken());

                String queryString = sharedPreferences.getString(prefSearchSubreddit, prefEmptyTag);
                HashMap<String, String> args = new HashMap<>();
                args.put("limit", "25");
                args.put("q", queryString);
                args.put("after", after);
                return retrofitRedditAPI.getSubreddits(getBearerToken(accessToken), args);
            }
        });
    }


    @Override
    public Observable<SubredditParent> getMineSubreddits(final String after) {

        if (setCallCounter()) {
            return Observable.empty();
        }

        return token.getAccessTokenX().flatMap(new Function<AccessToken, ObservableSource<SubredditParent>>() {
            @Override
            public ObservableSource<SubredditParent> apply(AccessToken accessToken) throws Exception {
                token.setAccessTokenValue(accessToken.getAccessToken());

                String queryString = sharedPreferences.getString(prefSearchSubreddit, prefEmptyTag);
                HashMap<String, String> args = new HashMap<>();
                args.put("limit", "25");
                args.put("after", after);
                return retrofitRedditAPI.getMineSubreddits(getBearerToken(accessToken), "subscriber", args);
            }
        });
    }

    @Override
    public Observable<ResponseBody> postSubscribe(final String subscribe, final String fullname) {

        if (setCallCounter()) {
            return Observable.empty();
        }

        return token.getAccessTokenX().flatMap(new Function<AccessToken, ObservableSource<ResponseBody>>() {
            @Override
            public ObservableSource<ResponseBody> apply(AccessToken accessToken) throws Exception {
                token.setAccessTokenValue(accessToken.getAccessToken());

                return retrofitRedditAPI.postSubscribe(getBearerToken(accessToken), subscribe, fullname);
            }
        });
    }


    private String getBearerToken(AccessToken accessToken) {
        return "bearer " + accessToken.getAccessToken();
    }
}
