package com.qartf.doseforreddit.network;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.fragment.DetailFragment;
import com.qartf.doseforreddit.fragment.ListViewFragment;
import com.qartf.doseforreddit.fragment.SubredditListViewFragment;
import com.qartf.doseforreddit.model.AboutMe;
import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.model.Comment;
import com.qartf.doseforreddit.model.CommentParent;
import com.qartf.doseforreddit.model.PostParent;
import com.qartf.doseforreddit.model.SubredditParent;
import com.qartf.doseforreddit.utility.Constants;
import com.qartf.doseforreddit.utility.Constants.Auth;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import butterknife.BindString;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitControl {
    private SharedPreferences sharedPreferences;
    private RetrofitControlInterface mCallback;
    private Retrofit retrofit;
    private RetrofitRedditAPI feedAPI;
    @BindString(R.string.pref_post_subreddit) public String prefPostSubreddit;
    @BindString(R.string.pref_post_sort_by) public String prefPostSortBy;
    @BindString(R.string.pref_search_post) String prefSearchPost;
    @BindString(R.string.pref_search_subreddit) String prefSearchSubreddit;
    @BindString(R.string.pref_empty_tag) String prefEmptyTag;
    @BindString(R.string.pref_post_sort_by_default) String prefSortByDefault;
    @BindString(R.string.pref_post_subreddit_default) String prefPostSubredditDefault;
    @BindString(R.string.pref_post_detail_sub) String prefPostDetailSub;
    @BindString(R.string.pref_post_detail_id) String prefPostDetailId;
    @BindString(R.string.pref_post_detail_sort_key) String prefPostDetailSortKey;


    public RetrofitControl(AppCompatActivity activity, RetrofitControlInterface mCallback){
        ButterKnife.bind(this, activity);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        this.mCallback = mCallback;
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.Auth.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        feedAPI = retrofit.create(RetrofitRedditAPI.class);
    }


    public void accessToken(String code){
        Call<AccessToken> call = feedAPI.accessToken(basicToken(Auth.CLIENT_ID), "authorization_code", code, Auth.REDIRECT_URI);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                mCallback.setAccessToken(response.body());
                getAboutMe();
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.e(this.getClass().getSimpleName(), "onFailure: Unable to retrieve RSS: " + t.getMessage() );

            }
        });
    }


    public void refreshToken(){
        Call<AccessToken> call = feedAPI.refreshToken(basicToken(Auth.CLIENT_ID), "refresh_token", mCallback.getAccessToken().getRefreshToken());
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                String token = response.body().getAccessToken();
                mCallback.setAccessTokenValue(token);
                getSubredditPosts();
//                mainActivity.getSupportLoaderManager().restartLoader(Constants.Id.POSTS, null, mainActivity).forceLoad();
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.e(this.getClass().getSimpleName(), "onFailure: Unable to retrieve RSS: " + t.getMessage() );

            }
        });
    }

    public void guestToken(){
        String uuid = UUID.randomUUID().toString();

        Call<AccessToken> call = feedAPI.guestToken(basicToken(Auth.CLIENT_ID), Auth.NO_NAME_GRAND_TYPE, uuid);
        call.enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                String token = response.body().getAccessToken();
                mCallback.setAccessTokenValue(token);
                getSubredditPosts();
//                mainActivity.getSupportLoaderManager().restartLoader(Constants.Id.POSTS, null, mainActivity).forceLoad();
            }

            @Override
            public void onFailure(Call<AccessToken> call, Throwable t) {
                Log.e(this.getClass().getSimpleName(), "onFailure: Unable to retrieve RSS: " + t.getMessage() );

            }
        });
    }

    public void getAboutMe(){

        Call<AboutMe> call = feedAPI.getAboutMe(getBearerToken());
        call.enqueue(new Callback<AboutMe>() {
            @Override
            public void onResponse(Call<AboutMe> call, Response<AboutMe> response) {
                String name = response.body().name;
                mCallback.actionMe(name);
            }

            @Override
            public void onFailure(Call<AboutMe> call, Throwable t) {
                Log.e(this.getClass().getSimpleName(), "onFailure: Unable to retrieve RSS: " + t.getMessage() );

            }
        });
    }

    public void getSubredditPosts(){

        String subreddit = sharedPreferences.getString(prefPostSubreddit, prefPostSubredditDefault);
        String subredditSortBy = sharedPreferences.getString(prefPostSortBy, prefSortByDefault);
        HashMap<String, String> args = new HashMap<>();

        Call<PostParent> call = feedAPI.getSubredditPosts(getBearerToken(), subreddit, subredditSortBy, args);
        call.enqueue(new Callback<PostParent>() {
            @Override
            public void onResponse(Call<PostParent> call, Response<PostParent> response) {
                mCallback.getListViewFragment().onLoadFinished(response.body());
            }

            @Override
            public void onFailure(Call<PostParent> call, Throwable t) {
                Log.e(this.getClass().getSimpleName(), "onFailure: Unable to retrieve RSS: " + t.getMessage() );

            }
        });
    }

    public void getComments(){

        String postSub = sharedPreferences.getString(prefPostDetailSub, prefEmptyTag);
        String postId = sharedPreferences.getString(prefPostDetailId, prefEmptyTag);
        String sortBy = sharedPreferences.getString(prefPostDetailSortKey, prefEmptyTag);


        HashMap<String, String> args = new HashMap<>();
        args.put("depth", "5");
        args.put("limit", "30");
        args.put("showedits", "false");
        args.put("showmore", "false");
        args.put("sort", sortBy);

        Call<CommentParent> call = feedAPI.getComments(getBearerToken(), postSub, postId, args);
        call.enqueue(new Callback<CommentParent>() {
            @Override
            public void onResponse(Call<CommentParent> call, Response<CommentParent> response) {
                List<Comment> commentList = response.body().commentList;
                mCallback.getDetailFragment().onLoadFinished(commentList);
            }

            @Override
            public void onFailure(Call<CommentParent> call, Throwable t) {
                Log.e(this.getClass().getSimpleName(), "onFailure: Unable to retrieve RSS: " + t.getMessage() );

            }
        });
    }

    public void searchPosts(){

        String queryString = sharedPreferences.getString(prefSearchPost, prefEmptyTag);
        String subreddit = sharedPreferences.getString(prefPostSubreddit, prefPostSubredditDefault);

        HashMap<String, String> args = new HashMap<>();
        args.put("limit", "30");
        args.put("q", queryString);
        args.put("restrict_sr", "true");

        Call<PostParent> call = feedAPI.searchPosts(getBearerToken(), subreddit, args);
        call.enqueue(new Callback<PostParent>() {
            @Override
            public void onResponse(Call<PostParent> call, Response<PostParent> response) {
                mCallback.getListViewFragment().onLoadFinished(response.body());
            }

            @Override
            public void onFailure(Call<PostParent> call, Throwable t) {
                Log.e(this.getClass().getSimpleName(), "onFailure: Unable to retrieve RSS: " + t.getMessage() );

            }
        });
    }

    public void searchSubreddits(){

        String queryString = sharedPreferences.getString(prefSearchSubreddit, prefEmptyTag);

        HashMap<String, String> args = new HashMap<>();
        args.put("limit", "30");
        args.put("q", queryString);

        Call<SubredditParent> call = feedAPI.searchSubreddits(getBearerToken(), args);
        call.enqueue(new Callback<SubredditParent>() {
            @Override
            public void onResponse(Call<SubredditParent> call, Response<SubredditParent> response) {
                mCallback.getSubredditListViewFragment().onLoadFinished(response.body());
            }

            @Override
            public void onFailure(Call<SubredditParent> call, Throwable t) {
                Log.e(this.getClass().getSimpleName(), "onFailure: Unable to retrieve RSS: " + t.getMessage() );
            }
        });
    }

    public void postSubscribe(String subscribe, String fullname){
        Call<ResponseBody> call = feedAPI.postSubscribe(getBearerToken(), subscribe, fullname);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String vote = "";

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(this.getClass().getSimpleName(), "onFailure: Unable to retrieve RSS: " + t.getMessage() );
            }
        });
    }

    public void postVote(String dir, String fullname){
        Call<ResponseBody> call = feedAPI.postVote(getBearerToken(), dir, fullname);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String vote = "";

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(this.getClass().getSimpleName(), "onFailure: Unable to retrieve RSS: " + t.getMessage() );
            }
        });
    }


    private static String basicToken(String token) {
        String authString = token + ":";
        String encodedAuthString = Base64.encodeToString(authString.getBytes(), Base64.NO_WRAP);
        String newToken = "Basic " + encodedAuthString;
        return newToken;
    }

    private String getBearerToken(){
        String token = "bearer " + mCallback.getAccessToken().getAccessToken();
        return token;
    }

    public interface RetrofitControlInterface {
        AccessToken getAccessToken();
        void actionMe(String name);
        void setAccessToken(AccessToken accessToken);
        void setAccessTokenValue(String accessToken);
        DetailFragment getDetailFragment();
        ListViewFragment getListViewFragment();
        SubredditListViewFragment getSubredditListViewFragment();

    }

}
