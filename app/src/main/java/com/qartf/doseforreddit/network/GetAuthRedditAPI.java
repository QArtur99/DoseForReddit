package com.qartf.doseforreddit.network;

import android.support.annotation.NonNull;

import com.qartf.doseforreddit.utility.Constants;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by ART_F on 2017-09-01.
 */

public class GetAuthRedditAPI {


    public static String getPopular(String token) throws JSONException, IOException {
        String url = Constants.BASE_URL_OAUTH + "/r/popular/hot";
        String jsonString = postQueryJSONObject(url, token, null);
        return jsonString;
    }

    public static String getMe(String token) throws JSONException, IOException {
        String url = Constants.BASE_URL_OAUTH + "/api/v1/me";
        String jsonString = postQueryJSONObject(url, token, null);
        return jsonString;
    }

    @NonNull
    private static String postQueryJSONObject(String urlString, String token, HashMap<String, String> args) throws IOException, JSONException {

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

    private String toHex(byte[] b) {
        return String.format("%040x", new BigInteger(1, b));
    }
}
