package com.qartf.doseforreddit.network;

import android.support.annotation.NonNull;
import android.util.Base64;

import com.qartf.doseforreddit.utility.Constants.Auth;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class PostAuthRedditAPI {


    public static String getAccessToken(String code) throws JSONException, IOException {
        HashMap<String, String> args = new HashMap<>();
        args.put("grant_type", "authorization_code");
        args.put("code", code);
        args.put("redirect_uri", Auth.REDIRECT_URI);

        return postQueryJSONObject(Auth.ACCESS_TOKEN_URL, basicToken(Auth.CLIENT_ID), args);
    }

    public static String refreshToken(String refreshToken) throws JSONException, IOException {
        HashMap<String, String> args = new HashMap<>();
        args.put("grant_type", "refresh_token");
        args.put("refresh_token", refreshToken);

        return postQueryJSONObject(Auth.ACCESS_TOKEN_URL, basicToken(Auth.CLIENT_ID), args);
    }

    public static String revokeTokenAccess(String token) throws JSONException, IOException {
        HashMap<String, String> args = new HashMap<>();
        args.put("token", token);
        args.put("token_type_hint", "access_token");

        return postQueryJSONObject(Auth.REVOKE_TOKEN_URL, basicToken(Auth.CLIENT_ID), args);
    }

    public static String revokeRefreshToken(String token) throws JSONException, IOException {
        HashMap<String, String> args = new HashMap<>();
        args.put("token", token);
        args.put("token_type_hint", "refresh_token");

        return postQueryJSONObject(Auth.REVOKE_TOKEN_URL, basicToken(Auth.CLIENT_ID), args);
    }

    public static String getGuestAccess() throws JSONException, IOException {
        String uuid = UUID.randomUUID().toString();
        HashMap<String, String> args = new HashMap<>();
        args.put("grant_type", Auth.NO_NAME_GRAND_TYPE);
        args.put("device_id", uuid);

        return postQueryJSONObject(Auth.ACCESS_TOKEN_URL, basicToken(Auth.CLIENT_ID), args);
    }

    public static String getSubreddits(String token, String queryString) throws JSONException, IOException {
        HashMap<String, String> args = new HashMap<>();
        args.put("exact", "false");
        args.put("query", queryString);
        String url = Auth.BASE_URL_OAUTH + "/api/search_subreddits";
        String jsonString = postQueryJSONObject(url, bearerToken(token), args);
        return jsonString;
    }

    public static String postSubscribe(String token, String subscribe, String fullname) throws JSONException, IOException {
        HashMap<String, String> args = new HashMap<>();
        args.put("action", subscribe);
        args.put("sr", fullname);
        String url = Auth.BASE_URL_OAUTH + "/api/subscribe";
        String jsonString = postQueryJSONObject(url, bearerToken(token), args);
        return jsonString;
    }

    public static String postVote(String token, String dir, String fullname) throws JSONException, IOException {
        HashMap<String, String> args = new HashMap<>();
        args.put("dir", dir);
        args.put("id", fullname);
        String url = Auth.BASE_URL_OAUTH + "/api/vote";
        String jsonString = postQueryJSONObject(url, bearerToken(token), args);
        return jsonString;
    }

    @NonNull
    private static String postQueryJSONObject(String urlString, String token, HashMap<String, String> args) throws IOException, JSONException {

        String postData = getUri(args);

        HttpURLConnection urlConnection;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);
        urlConnection.setDoOutput(true);

        urlConnection.setRequestProperty("User-Agent", "android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)");
        urlConnection.setRequestProperty("Authorization", token);

        OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
        out.write(postData);
        out.close();

        InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
        BufferedReader br = new BufferedReader(inputStreamReader);

        StringBuilder jsonString = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            jsonString.append(line).append("\n");
        }

        br.close();
        return jsonString.toString();
    }

    private static String basicToken(String token) {
        String authString = token + ":";
        String encodedAuthString = Base64.encodeToString(authString.getBytes(), Base64.NO_WRAP);
        String newToken = "Basic " + encodedAuthString;
        return newToken;
    }

    private static String bearerToken(String token){
        return "bearer " + token;
    }

    @NonNull
    private static String getUri(HashMap<String, String> args) {
        Set<String> keys = args.keySet();
        String postData = "";
        for (String key : keys) {
            if (postData.length() > 0) {
                postData += "&";
            }
            postData += key + "=" + args.get(key);
        }
        return postData;
    }
}
