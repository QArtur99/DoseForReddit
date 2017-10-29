package com.qartf.doseforreddit.view.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.data.entity.Post;
import com.qartf.doseforreddit.data.repository.DataRepository;
import com.qartf.doseforreddit.presenter.root.App;

import java.util.List;

import javax.inject.Inject;


public class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    @Inject
    DataRepository.Retrofit repository;
    private Context mContext;
    private List<Post> data;

    public GridRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
        ((App) mContext.getApplicationContext()).getComponent().inject(this);
    }


    @Override
    public void onDataSetChanged() {
        data = repository.getPosts().blockingSingle().postList;
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

