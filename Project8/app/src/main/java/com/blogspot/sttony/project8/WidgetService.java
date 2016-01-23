package com.blogspot.sttony.project8;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
    public WidgetService() {
        Log.d("STTONY", "Widget factory created");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        android.os.Debug.waitForDebugger();
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        Log.d("STTONY", "Widget factory called");
        return new WidgetFactory(this.getApplicationContext(), intent);
    }
}
