package com.blogspot.sttony.project8;

import android.appwidget.AppWidgetManager;
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
import java.util.Locale;


public class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    private Cursor mCursor;
    private Context mContext;
    private int mWidgetId;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);

    public WidgetFactory(Context _context, Intent _intent)
    {
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

        mContext = _context;
        mCursor = mContext.getContentResolver()
                .query(TodoContract.TaskEntry.buildTaskWithRange(c.getTime().getTime()),
                        null,
                        TodoContract.TaskEntry.COLUMN_DUE_DATE + " <= ? AND (" +
                                TodoContract.TaskEntry.COLUMN_IS_COMPLETE + " =0 OR (" +
                                TodoContract.TaskEntry.COLUMN_COMPLETE_DATE + " >= ? AND " +
                                TodoContract.TaskEntry.COLUMN_COMPLETE_DATE + " <= ? )) " ,
                        new String[] {Long.toString(c.getTime().getTime()), Long.toString(todayStart),Long.toString(todayEnd)},
                        TodoContract.TaskEntry.COLUMN_DUE_DATE + " ASC");
        mWidgetId = _intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID);
    }
    @Override
    public void onCreate() {
        android.os.Debug.waitForDebugger();
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
        mCursor.close();
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        mCursor.moveToPosition(position);
        RemoteViews rview= new RemoteViews(mContext.getPackageName(), R.layout.list_item_task);

        rview.setTextViewText(R.id.view_task_item_title, mCursor.getString(TasksFragment.COL_TASK_TITLE));
        rview.setTextColor(R.id.view_task_item_title,0x000000);

        long duedate = mCursor.getLong(TasksFragment.COL_TASK_DUE_DATE);
        String duedateStr = simpleDateFormat.format(new Date(duedate));
        rview.setTextViewText(R.id.view_task_item_due_date, duedateStr);
        rview.setTextColor(R.id.view_task_item_due_date, 0x000000);

        return rview;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
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
