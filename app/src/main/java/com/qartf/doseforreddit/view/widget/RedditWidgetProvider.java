package com.qartf.doseforreddit.view.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.data.entity.PostParent;
import com.qartf.doseforreddit.view.activity.CommentsActivity;


public class RedditWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, PostParent postParent) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_grid_view);

        String postParentString = new Gson().toJson(postParent.postList);
        Intent intent = new Intent(context, GridWidgetService.class);
        intent.putExtra("postList", postParentString);
        views.setRemoteAdapter(R.id.widget_grid_view, intent);

        Intent appIntent = new Intent(context, CommentsActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_grid_view, appPendingIntent);

        views.setEmptyView(R.id.widget_grid_view, R.id.empty_view);
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RedditWidgetService.startActionUpdatePlantWidgets(context);
    }

    public static void updateWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, PostParent postParent) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, postParent);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

