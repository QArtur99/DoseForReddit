package com.qartf.doseforreddit.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.database.DatabaseContract;
import com.qartf.doseforreddit.model.AccessToken;
import com.qartf.doseforreddit.model.Post;
import com.qartf.doseforreddit.model.PostParent;
import com.qartf.doseforreddit.network.GetAuthRedditAPI;
import com.qartf.doseforreddit.network.PostAuthRedditAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private List<Post> data;
    private static AccessToken accessToken;

    public GridRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
    }


    @Override
    public void onDataSetChanged() {
        data = new ArrayList<>();

        try {
            actionLoadUsers();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        data.clear();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (data == null || data.size() == 0) {
            return null;
        }

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_row_item);
        remoteViews.setTextViewText(R.id.widgetTitle, data.get(position).title);

        String jsonString = new Gson().toJson(data.get(position));
        String jsonStringAccess = new Gson().toJson(accessToken);
        Bundle extras = new Bundle();
        extras.putString("link", jsonString);
        extras.putString("token", jsonStringAccess);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.row, fillInIntent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void actionLoadUsers() throws IOException, JSONException {
        Cursor cursor = mContext.getContentResolver().query(DatabaseContract.Accounts.CONTENT_URI, DatabaseContract.Accounts.PROJECTION_LIST, null, null, null);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String logged = sharedPreferences.getString(mContext.getResources().getString(R.string.pref_login_signed_in), "Anonymous");
        if (cursor != null && !cursor.isClosed() && cursor.moveToFirst()) {
            do {
                String userName = cursor.getString(cursor.getColumnIndex(DatabaseContract.Accounts.USER_NAME));
                if (userName.equals(logged)) {
                    accessToken = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(DatabaseContract.Accounts.ACCESS_TOKEN)), AccessToken.class);
                }
            } while (cursor.moveToNext());
            getRefreshToken(accessToken.getRefreshToken());
        } else {
            getGuestAccess();
        }
        PostParent postParent = getPosts(accessToken.getAccessToken());
        data = postParent.postList;

        if(cursor != null){
            cursor.close();
        }

    }

    private static String getRefreshToken(String refreshToken) throws IOException, JSONException {
        String jsonString = PostAuthRedditAPI.refreshToken(refreshToken);
        accessToken = new Gson().fromJson(jsonString, AccessToken.class);
        return accessToken.getAccessToken();
    }

    private static String getGuestAccess() throws IOException, JSONException {
        String jsonString = PostAuthRedditAPI.getGuestAccess();
        accessToken = new Gson().fromJson(jsonString, AccessToken.class);
        return accessToken.getAccessToken();
    }

    private PostParent getPosts(String accessTokenString) throws IOException, JSONException {

        String jsonString = GetAuthRedditAPI.getPopular(accessTokenString, "popular", "hot");
        JSONObject jsonObject = new JSONObject(jsonString).getJSONObject("data");
        PostParent postParent = new Gson().fromJson(jsonObject.toString(), PostParent.class);
        return postParent;
    }

}

