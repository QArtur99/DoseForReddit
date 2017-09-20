package com.qartf.doseforreddit.network;

import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.model.PostObjectParent;
import com.qartf.doseforreddit.utility.Constants.Auth;

import java.util.Map;

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
    Call<AccessToken> refreshToken(@Header("Authorization") String authorization,
                                   @Field("grant_type") String grant_type,
                                   @Field("refresh_token") String refresh_token);


    @GET("/r/{subbreddit_name}/{sort}")
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Call<PostObjectParent> getSubredditPosts(@Header("Authorization") String authorization,
                                             @Path(value = "subbreddit_name", encoded = true) String subreddit_name,
                                             @Path(value = "sort", encoded = true) String sort,
                                             @QueryMap Map<String, String> options);


}
