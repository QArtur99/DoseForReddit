package com.qartf.doseforreddit.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.data.entity.Post;

import java.util.List;


public class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<Post> data;
    private Intent intent;

    public GridRemoteViewsFactory(Context applicationContext, Intent intent) {
        this.intent = intent;
        mContext = applicationContext;
    }

    @Override
    public void onCreate() { }


    @Override
    public void onDataSetChanged() {
        data = new Gson().fromJson(intent.getStringExtra("postList"), new TypeToken<List<Post>>() {}.getType());
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
        Bundle extras = new Bundle();
        extras.putString("link", jsonString);
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

}

