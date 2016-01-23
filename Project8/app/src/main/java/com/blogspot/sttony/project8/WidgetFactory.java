package com.blogspot.sttony.project8;

import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.blogspot.sttony.project8.data.TodoContract;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private int mWidgetId;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
    private List<ContentValues> mTasks;

    public WidgetFactory(Context _context, Intent _intent, List<ContentValues> _array)
    {
        mContext = _context;
        mWidgetId = _intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        mTasks = _array;
    }
    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {


    }

    @Override
    public int getCount() {
        return mTasks.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        ContentValues item = mTasks.get(position);
        RemoteViews rview= new RemoteViews(mContext.getPackageName(), R.layout.list_item_task_widget);

        rview.setTextViewText(R.id.view_task_item_title, item.getAsString(TodoContract.TaskEntry.COLUMN_TITLE));
        long duedate = item.getAsLong(TodoContract.TaskEntry.COLUMN_DUE_DATE);
        String duedateStr = simpleDateFormat.format(new Date(duedate));
        rview.setTextViewText(R.id.view_task_item_due_date, duedateStr);

        return rview;
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
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
