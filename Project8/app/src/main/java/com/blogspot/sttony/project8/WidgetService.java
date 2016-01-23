package com.blogspot.sttony.project8;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.IBinder;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.widget.RemoteViewsService;

import com.blogspot.sttony.project8.data.TodoContract;

import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        CursorLoader cl;
        Calendar c = Calendar.getInstance();
        c.set(c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH),
                0,
                0,
                0
        );
        long todayStart = c.getTime().getTime();
        c.set(
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH),
                23,
                59,
                59
        );
        long todayEnd = c.getTime().getTime();
        c = Calendar.getInstance();
        c.set(c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH),
                23,
                59,
                59
        );
        Cursor cr = getContentResolver()
                .query(TodoContract.TaskEntry.buildTaskWithRange(c.getTime().getTime()),
                        null,
                        TodoContract.TaskEntry.COLUMN_DUE_DATE + " <= ? AND (" +
                                TodoContract.TaskEntry.COLUMN_IS_COMPLETE + " =0 OR (" +
                                TodoContract.TaskEntry.COLUMN_COMPLETE_DATE + " >= ? AND " +
                                TodoContract.TaskEntry.COLUMN_COMPLETE_DATE + " <= ? )) " ,
                        new String[] {Long.toString(c.getTime().getTime()), Long.toString(todayStart),Long.toString(todayEnd)},
                        TodoContract.TaskEntry.COLUMN_DUE_DATE + " ASC");
        ArrayList<ContentValues> _Task = new ArrayList<ContentValues>();

        while (cr.moveToNext()) {
            ContentValues map = new ContentValues();
            DatabaseUtils.cursorRowToContentValues(cr, map);
            _Task.add(map);
        }
        cr.close();
        return new WidgetFactory(this.getApplicationContext(), intent,_Task);
    }
}
