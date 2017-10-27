package com.qartf.doseforreddit.mvp.data.network;

import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.model.PostParent;
import com.qartf.doseforreddit.model.SubredditParent;
import com.qartf.doseforreddit.utility.Constants;

import java.util.Map;

import io.reactivex.Observable;
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
 * Created by ART_F on 2017-10-20.
 */

public interface RetrofitRedditAPI {


    @FormUrlEncoded
    @POST(Constants.Auth.ACCESS_TOKEN_URL)
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Call<AccessToken> accessToken(@Header("Authorization") String authorization,
                                  @Field("grant_type") String grant_type,
                                  @Field("code") String code,
                                  @Field("redirect_uri") String redirectUti);

    @FormUrlEncoded
    @POST(Constants.Auth.ACCESS_TOKEN_URL)
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Call<AccessToken> refreshToken(@Header("Authorization") String authorization,
                                   @Field("grant_type") String grant_type,
                                   @Field("refresh_token") String refresh_token);

    @FormUrlEncoded
    @POST(Constants.Auth.ACCESS_TOKEN_URL)
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Call<AccessToken> guestToken(@Header("Authorization") String authorization,
                                 @Field("grant_type") String grant_type,
                                 @Field("device_id") String deviceId);


    @FormUrlEncoded
    @POST(Constants.Auth.ACCESS_TOKEN_URL)
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Observable<AccessToken> accessTokenRX(@Header("Authorization") String authorization,
                                        @Field("grant_type") String grant_type,
                                        @Field("code") String code,
                                        @Field("redirect_uri") String redirectUti);

    @FormUrlEncoded
    @POST(Constants.Auth.ACCESS_TOKEN_URL)
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Observable<AccessToken> refreshTokenRX(@Header("Authorization") String authorization,
                                   @Field("grant_type") String grant_type,
                                   @Field("refresh_token") String refresh_token);

    @FormUrlEncoded
    @POST(Constants.Auth.ACCESS_TOKEN_URL)
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Observable<AccessToken> guestTokenRX(@Header("Authorization") String authorization,
                                 @Field("grant_type") String grant_type,
                                 @Field("device_id") String deviceId);

    @GET(Constants.Auth.BASE_URL_OAUTH + "/r/{subbreddit_name}/{sort}")
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Observable<PostParent> getPosts(@Header("Authorization") String authorization,
                                       @Path(value = "subbreddit_name", encoded = true) String subreddit_name,
                                       @Path(value = "sort", encoded = true) String sort,
                                       @QueryMap Map<String, String> options);

    @GET(Constants.Auth.BASE_URL_OAUTH + "/subreddits/search")
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Observable<SubredditParent> getSubreddits(@Header("Authorization") String authorization,
                                           @QueryMap Map<String, String> options);

}
