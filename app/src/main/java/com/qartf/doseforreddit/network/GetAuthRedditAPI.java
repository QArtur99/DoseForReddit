package com.qartf.doseforreddit.network;

import android.support.annotation.NonNull;

import com.qartf.doseforreddit.utility.Constants.Auth;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;


public class GetAuthRedditAPI {


    public static String getPopular(String token, String subreddit, String sortBy) throws JSONException, IOException {
        String url = Auth.BASE_URL_OAUTH + "/r/" + subreddit + "/" + sortBy;
        String jsonString = getQueryJSONObject(url, token, null);
        return jsonString;
    }

    public static String getMe(String token) throws JSONException, IOException {
        String url = Auth.BASE_URL_OAUTH + "/api/v1/me";
        String jsonString = getQueryJSONObject(url, token, null);
        return jsonString;
    }

    public static String getComments(String token, String subreddit, String id, String sortBy) throws JSONException, IOException {
        HashMap<String, String> args = new HashMap<>();
        args.put("depth", "5");
        args.put("limit", "30");
        args.put("showedits", "false");
        args.put("showmore", "false");
        args.put("sort", sortBy);

        String url = Auth.BASE_URL_OAUTH + "/r/" + subreddit + "/comments/" + id;
        String jsonString = getQueryJSONObject(url, token, args);
        return jsonString;
    }

    public static String getSubreddits(String token, String queryString) throws JSONException, IOException {
        HashMap<String, String> args = new HashMap<>();
        args.put("limit", "30");
        args.put("q", queryString);
        String url = Auth.BASE_URL_OAUTH + "/subreddits/search";
        String jsonString = getQueryJSONObject(url, token, args);
        return jsonString;
    }

    public static String getQueryPosts(String token, String subreddit, String queryString) throws JSONException, IOException {
        HashMap<String, String> args = new HashMap<>();
        args.put("limit", "30");
        args.put("q", queryString);
        String url = Auth.BASE_URL_OAUTH + "/r/" + subreddit + "/search";
        String jsonString = getQueryJSONObject(url, token, args);
        return jsonString;
    }


    @NonNull
    private static String getQueryJSONObject(String urlString, String token, HashMap<String, String> args) throws IOException, JSONException {

        if(args != null){
            urlString += "?" + getUri(args);
        }

        HttpURLConnection urlConnection;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);
        urlConnection.setRequestProperty("Authorization", "bearer " + token);
        urlConnection.setRequestProperty("User-Agent", "android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)");
        urlConnection.connect();

        InputStream is;
        if (urlConnection.getResponseCode() != 200) {
            is = urlConnection.getErrorStream();
        } else {
            is = urlConnection.getInputStream();
        }

        InputStreamReader inputStreamReader = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(inputStreamReader);

        StringBuilder jsonString = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            jsonString.append(line).append("\n");
        }

        br.close();

        return jsonString.toString();
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
