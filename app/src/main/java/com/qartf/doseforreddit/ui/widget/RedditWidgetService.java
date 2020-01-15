package com.qartf.doseforreddit.ui.widget;

import android.app.IntentService;
import android.app.Notification;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.Nullable;

import com.qartf.doseforreddit.R;
import com.qartf.doseforreddit.data.entity.PostParent;
import com.qartf.doseforreddit.data.repository.DataRepository;
import com.qartf.doseforreddit.App;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class RedditWidgetService extends IntentService {

    public static final String ACTION_UPDATE_PLANT_WIDGETS = "com.qartf.doseforreddit.ui.widget.action.update_widgets";

    @Inject
    DataRepository.Retrofit repository;

    public RedditWidgetService() {
        super("RedditWidgetService");
    }


    public static void startActionUpdatePlantWidgets(Context context) {
        Intent intent = new Intent(context, RedditWidgetService.class);
        intent.setAction(ACTION_UPDATE_PLANT_WIDGETS);
        context.stopService(intent);
        if (Build.VERSION.SDK_INT < 26) {
            context.startService(intent);
        } else {
            context.startForegroundService(intent);
        }
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= 26) {
            startForeground(-1, new Notification());
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_PLANT_WIDGETS.equals(action)) {
                handleActionUpdateWidgets();
            }
        }
    }

    private void handleActionUpdateWidgets() {
        ((App) getApplicationContext()).getComponent().inject(this);
        repository.getPosts("").observeOn(AndroidSchedulers.mainThread()).
                subscribeOn(Schedulers.io()).subscribeWith(new DisposableObserver<PostParent>() {

            @Override
            public void onNext(@NonNull PostParent postParent) {
                upDatePosts(postParent);
            }

            @Override
            public void onError(@NonNull Throwable e) {
//                checkConnection();
            }

            @Override
            public void onComplete() { }

        });
    }

    private void upDatePosts(@NonNull PostParent postParent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(RedditWidgetService.this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(RedditWidgetService.this, RedditWidgetProvider.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
        //Now update all widgets
        RedditWidgetProvider.updateWidgets(RedditWidgetService.this, appWidgetManager, appWidgetIds, postParent);
    }


}
