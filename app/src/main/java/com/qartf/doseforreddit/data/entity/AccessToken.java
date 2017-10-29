package com.qartf.doseforreddit.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ART_F on 2017-08-31.
 */

public class AccessToken {

    @SerializedName("access_token")
    public String accessToken;

    @SerializedName("token_type")
    public String tokenType;

    @SerializedName("expires_in")
    public String expiresIn;

    @SerializedName("refresh_token")
    public String refreshToken;

    @SerializedName("scope")
    public String scope;


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
