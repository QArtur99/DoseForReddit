package com.qartf.doseforreddit.data.network;

import com.qartf.doseforreddit.data.entity.AboutMe;
import com.qartf.doseforreddit.data.entity.AccessToken;
import com.qartf.doseforreddit.data.entity.CommentParent;
import com.qartf.doseforreddit.data.entity.PostParent;
import com.qartf.doseforreddit.data.entity.RuleParent;
import com.qartf.doseforreddit.data.entity.SubmitParent;
import com.qartf.doseforreddit.data.entity.SubredditParent;
import com.qartf.doseforreddit.data.entity.childComment.ChildCommentParent;
import com.qartf.doseforreddit.presenter.utility.Constants;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface RetrofitRedditAPI {


    @FormUrlEncoded
    @POST(Constants.Auth.ACCESS_TOKEN_URL)
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Observable<AccessToken> accessToken(@Header("Authorization") String authorization,
                                        @Field("grant_type") String grant_type,
                                        @Field("code") String code,
                                        @Field("redirect_uri") String redirectUti);

    @FormUrlEncoded
    @POST(Constants.Auth.ACCESS_TOKEN_URL)
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Observable<AccessToken> refreshToken(@Header("Authorization") String authorization,
                                         @Field("grant_type") String grant_type,
                                         @Field("refresh_token") String refresh_token);

    @FormUrlEncoded
    @POST(Constants.Auth.ACCESS_TOKEN_URL)
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Observable<AccessToken> guestToken(@Header("Authorization") String authorization,
                                       @Field("grant_type") String grant_type,
                                       @Field("device_id") String deviceId);

    @GET(Constants.Auth.BASE_URL_OAUTH + "/api/v1/me")
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Observable<AboutMe> getAboutMe(@Header("Authorization") String authorization);


    @GET(Constants.Auth.BASE_URL_OAUTH + "/r/{subbreddit_name}/about/rules")
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Observable<RuleParent> getSubredditRules(@Header("Authorization") String authorization,
                                             @Path(value = "subbreddit_name", encoded = true) String subreddit_name,
                                             @QueryMap Map<String, String> options);

    @GET(Constants.Auth.BASE_URL_OAUTH + "/r/{subbreddit_name}/{sort}")
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Observable<PostParent> getPosts(@Header("Authorization") String authorization,
                                       @Path(value = "subbreddit_name", encoded = true) String subreddit_name,
                                       @Path(value = "sort", encoded = true) String sort,
                                       @QueryMap Map<String, String> options);

    @GET(Constants.Auth.BASE_URL_OAUTH + "/r/{subbreddit_name}/search")
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Observable<PostParent> searchPosts(@Header("Authorization") String authorization,
                                 @Path(value = "subbreddit_name", encoded = true) String subreddit_name,
                                 @QueryMap Map<String, String> options);

    @GET(Constants.Auth.BASE_URL_OAUTH + "/r/{subbreddit_name}/comments/{id}")
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Observable<CommentParent> getComments(@Header("Authorization") String authorization,
                                    @Path(value = "subbreddit_name", encoded = true) String subreddit_name,
                                    @Path(value = "id", encoded = true) String sort,
                                    @QueryMap Map<String, String> options);

    @GET(Constants.Auth.BASE_URL_OAUTH + "/api/morechildren")
    Observable<ChildCommentParent> getMorechildren(@Header("Authorization") String authorization,
                                                   @QueryMap Map<String, String> options);

    @GET(Constants.Auth.BASE_URL_OAUTH + "/subreddits/search")
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Observable<SubredditParent> getSubreddits(@Header("Authorization") String authorization,
                                           @QueryMap Map<String, String> options);

    @GET(Constants.Auth.BASE_URL_OAUTH + "/subreddits/mine/{where}")
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Observable<SubredditParent> getMineSubreddits(@Header("Authorization") String authorization,
                                              @Path(value = "where", encoded = true) String where,
                                              @QueryMap Map<String, String> options);


    @FormUrlEncoded
    @POST(Constants.Auth.BASE_URL_OAUTH + "/api/subscribe")
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Observable<ResponseBody> postSubscribe(@Header("Authorization") String authorization,
                                     @Field("action") String subscribe,
                                     @Field("sr") String fullname);

    @FormUrlEncoded
    @POST(Constants.Auth.BASE_URL_OAUTH + "/api/vote")
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Observable<ResponseBody> postVote(@Header("Authorization") String authorization,
                                @Field("dir") String dir,
                                @Field("id") String fullname);

    @FormUrlEncoded
    @POST(Constants.Auth.BASE_URL_OAUTH + "/api/submit")
    @Headers("User-Agent: android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)")
    Observable<SubmitParent> postSubmit(@Header("Authorization") String authorization,
                                        @FieldMap Map<String, String> options);

}
