package com.qartf.doseforreddit;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by ART_F on 2017-09-01.
 */

public interface RedditApiInterface {

    @GET("/r/popular/hot")
    Call<String> getList();


//    @GET("/r/popular/hot")
//    @Headers("User-Agent: android:com.android.rahul_lohra.redditstar:v1.0 (by /u/rahul_lohra)")
//    Call<AboutMe> getAboutMe(@Header(Constants.AUTHORIZATION) String authorization);
}
