package com.blogspot.sttony.project8.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;


public class TodoProvider extends ContentProvider {

    private TodoDBHelper mOpenHelper;

    static final int TASK = 100;  // query by ID
    static final int GOAL = 200;
    static final int GOALS = 201;
    static final int TASKS_WITH_GOAL = 101;
    static final int TASKS_WITH_DATE_RANGE = 102;


    //private static final SQLiteQueryBuilder sWeatherByLocationSettingQueryBuilder;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TodoContract.CONTENT_AUTHORITY;

        // content://[Project8]/task/123
        matcher.addURI(authority, TodoContract.PATH_TASK + "/#", TASK);
        // content://[Project8]/goal/123
        matcher.addURI(authority, TodoContract.PATH_GOAL + "/#", GOAL);
        // content://[Project8]/goal
        matcher.addURI(authority, TodoContract.PATH_GOAL, GOALS);

        // content://[Project8]/task/goal/123
        matcher.addURI(authority, TodoContract.PATH_TASK + "/" +
                TodoContract.PATH_GOAL  + "/#", TASKS_WITH_GOAL);
        // content://[Project8]/tasks?start_date=123&end_date=456
        matcher.addURI(authority, TodoContract.PATH_TASK, TASKS_WITH_DATE_RANGE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new TodoDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        Log.d("STTONY", uri.toString());
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case TASK:
                return TodoContract.TaskEntry.CONTENT_ITEM_TYPE;
            case GOAL:
                return TodoContract.GoalEntry.CONTENT_ITEM_TYPE;
            case GOALS:
                return TodoContract.GoalEntry.CONTENT_TYPE;
            case TASKS_WITH_GOAL:
                return TodoContract.TaskEntry.CONTENT_TYPE;
            case TASKS_WITH_DATE_RANGE:
                return TodoContract.TaskEntry.CONTENT_TYPE;
            default:
                return null;
                //throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case TASK: {
                long _id = db.insert(TodoContract.TaskEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = TodoContract.TaskEntry.buildTaskUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case GOAL: {
                long _id = db.insert(TodoContract.GoalEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = TodoContract.GoalEntry.buildGoalUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if ( null == selection ) selection = "1";
        switch (match) {
            case TASK:
                rowsDeleted = db.delete(
                        TodoContract.TaskEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case GOAL:
                rowsDeleted = db.delete(
                        TodoContract.GoalEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
