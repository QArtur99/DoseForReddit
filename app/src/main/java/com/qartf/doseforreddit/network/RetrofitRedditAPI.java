package com.qartf.doseforreddit.network;

import com.qartf.doseforreddit.mvp.data.model.AboutMe;
import com.qartf.doseforreddit.mvp.data.model.AccessToken;
import com.qartf.doseforreddit.mvp.data.model.CommentParent;
import com.qartf.doseforreddit.mvp.data.model.PostParent;
import com.qartf.doseforreddit.mvp.data.model.SubredditParent;
import com.qartf.doseforreddit.mvp.presenter.utility.Constants.Auth;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by ART_F on 2017-09-19.
 */

public interface RetrofitRedditAPI {


    @FormUrlEncoded
    @POST(Auth.ACCESS_TOKEN_URL)
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Call<AccessToken> accessToken(@Header("Authorization") String authorization,
                                  @Field("grant_type") String grant_type,
                                  @Field("code") String code,
                                  @Field("redirect_uri") String redirectUti);

    @FormUrlEncoded
    @POST(Auth.ACCESS_TOKEN_URL)
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Call<AccessToken> refreshToken(@Header("Authorization") String authorization,
                                   @Field("grant_type") String grant_type,
                                   @Field("refresh_token") String refresh_token);

    @FormUrlEncoded
    @POST(Auth.ACCESS_TOKEN_URL)
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Call<AccessToken> guestToken(@Header("Authorization") String authorization,
                                 @Field("grant_type") String grant_type,
                                 @Field("device_id") String deviceId);

    @GET(Auth.BASE_URL_OAUTH + "/api/v1/me")
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Call<AboutMe> getAboutMe(@Header("Authorization") String authorization);


    @GET(Auth.BASE_URL_OAUTH + "/r/{subbreddit_name}/{sort}")
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Call<PostParent> getSubredditPosts(@Header("Authorization") String authorization,
                                       @Path(value = "subbreddit_name", encoded = true) String subreddit_name,
                                       @Path(value = "sort", encoded = true) String sort,
                                       @QueryMap Map<String, String> options);

    @GET(Auth.BASE_URL_OAUTH + "/r/{subbreddit_name}/comments/{id}")
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Call<CommentParent> getComments(@Header("Authorization") String authorization,
                                    @Path(value = "subbreddit_name", encoded = true) String subreddit_name,
                                    @Path(value = "id", encoded = true) String sort,
                                    @QueryMap Map<String, String> options);

    @GET(Auth.BASE_URL_OAUTH + "/r/{subbreddit_name}/search")
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Call<PostParent> searchPosts(@Header("Authorization") String authorization,
                                 @Path(value = "subbreddit_name", encoded = true) String subreddit_name,
                                 @QueryMap Map<String, String> options);

    @GET(Auth.BASE_URL_OAUTH + "/subreddits/search")
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Call<SubredditParent> searchSubreddits(@Header("Authorization") String authorization,
                                           @QueryMap Map<String, String> options);

    @FormUrlEncoded
    @POST(Auth.BASE_URL_OAUTH + "/api/subscribe")
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Call<ResponseBody> postSubscribe(@Header("Authorization") String authorization,
                               @Field("action") String subscribe,
                               @Field("sr") String fullname);

    @FormUrlEncoded
    @POST(Auth.BASE_URL_OAUTH + "/api/vote")
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Call<ResponseBody> postVote(@Header("Authorization") String authorization,
                                @Field("dir") String dir,
                                @Field("id") String fullname);

}
