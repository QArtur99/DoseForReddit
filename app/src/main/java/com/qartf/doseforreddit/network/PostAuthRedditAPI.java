package com.qartf.doseforreddit.network;

import android.support.annotation.NonNull;
import android.util.Base64;

import com.qartf.doseforreddit.utility.Constants;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

/**
 * Created by ART_F on 2017-08-30.
 */

public class PostAuthRedditAPI {


    public static String getAccessToken(String code) throws JSONException, IOException {
        HashMap<String, String> args = new HashMap<>();
        args.put("grant_type", "authorization_code");
        args.put("code", code);
        args.put("redirect_uri", Constants.REDIRECT_URI);

        String jsonString = postQueryJSONObject(Constants.ACCESS_TOKEN_URL, Constants.CLIENT_ID, args);
        return jsonString;
    }


    public static String refreshToken(String refreshToken) throws JSONException, IOException {
        HashMap<String, String> args = new HashMap<>();
        args.put("grant_type", "refresh_token");
        args.put("refresh_token", refreshToken);

        String jsonString = postQueryJSONObject(Constants.ACCESS_TOKEN_URL, Constants.CLIENT_ID, args);
        return jsonString;
    }

    public static String revokeTokenAccess(String token) throws JSONException, IOException {
        HashMap<String, String> args = new HashMap<>();
        args.put("token", token);
        args.put("token_type_hint", "access_token");

        String jsonString = postQueryJSONObject(Constants.REVOKE_TOKEN_URL, Constants.CLIENT_ID, args);
        return jsonString;
    }

    public static String revokeRefreshToken(String token) throws JSONException, IOException {
        HashMap<String, String> args = new HashMap<>();
        args.put("token", token);
        args.put("token_type_hint", "refresh_token");

        String jsonString = postQueryJSONObject(Constants.REVOKE_TOKEN_URL, Constants.CLIENT_ID, args);
        return jsonString;
    }

    public static String appOnly() throws JSONException, IOException {
        String uuid = UUID.randomUUID().toString();
        HashMap<String, String> args = new HashMap<>();
        args.put("grant_type", Constants.NO_NAME_GRAND_TYPE);
        args.put("device_id", uuid);

        String jsonString = postQueryJSONObject(Constants.ACCESS_TOKEN_URL, Constants.CLIENT_ID, args);
        return jsonString;
    }

    public static String getPopular(String token) throws JSONException, IOException {
        HashMap<String, String> args = new HashMap<>();
        String url = Constants.BASE_URL_OAUTH + "/r/popular/hot";
        String jsonString = postQueryJSONObject(url, token, args);
        return jsonString;
    }

    @NonNull
    private static String postQueryJSONObject(String urlString, String token, HashMap<String, String> args) throws IOException, JSONException {

        String postData = getUri(args);

        String authString = token + ":";
        String encodedAuthString = Base64.encodeToString(authString.getBytes(), Base64.NO_WRAP);

        HttpURLConnection urlConnection;
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setReadTimeout(35000);
        urlConnection.setConnectTimeout(40000);
        urlConnection.setDoOutput(true);

        urlConnection.setRequestProperty("User-Agent", "android:com.qartf.doseforreddit:v1.0 (by /u/Qart_f)");
        urlConnection.setRequestProperty("Authorization", "Basic " + encodedAuthString);

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

    private String toHex(byte[] b) {
        return String.format("%040x", new BigInteger(1, b));
    }
}
