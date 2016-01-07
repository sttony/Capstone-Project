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


    private static final SQLiteQueryBuilder sTaskQueryBuilder;
    static{
        sTaskQueryBuilder = new SQLiteQueryBuilder();
        sTaskQueryBuilder.setTables(TodoContract.TaskEntry.TABLE_NAME);
    }

    private static final String sTaskByGoalSelection =
            TodoContract.TaskEntry.TABLE_NAME +
                    "." + TodoContract.TaskEntry.COLUMN_GOAL_ID + " = ? ";

    private static final String sTaskByDateRangeSelection =
            TodoContract.TaskEntry.TABLE_NAME +
                    "." + TodoContract.TaskEntry.COLUMN_DUE_DATE + " <= ?";

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TodoContract.CONTENT_AUTHORITY;

        // content://[Project8]/task/123
        matcher.addURI(authority, TodoContract.PATH_TASK, TASK);
        matcher.addURI(authority, TodoContract.PATH_TASK + "/#", TASK);

        // content://[Project8]/goal/123
        matcher.addURI(authority, TodoContract.PATH_GOAL, GOAL);
        matcher.addURI(authority, TodoContract.PATH_GOAL + "/#", GOAL);
        // content://[Project8]/goal
        matcher.addURI(authority, TodoContract.PATH_GOALS, GOALS);

        // content://[Project8]/task/goal/123
        matcher.addURI(authority, TodoContract.PATH_TASK + "/" +
                TodoContract.PATH_GOAL  + "/#", TASKS_WITH_GOAL);
        // content://[Project8]/tasks/deadline/456
        matcher.addURI(authority, TodoContract.PATH_TASK + "/" +
                TodoContract.TaskEntry.QUERY_DEAD_LINE_DATE + "/#", TASKS_WITH_DATE_RANGE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new TodoDBHelper(getContext());
        return true;
    }

    private Cursor getTaskByGoal(Uri uri, String[] projection, String sortOrder) {
        long _id = TodoContract.TaskEntry.getGoalIdFromUri(uri);

        return sTaskQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sTaskByGoalSelection,
                new String[]{Long.toString(_id)},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getTaskByDateRange(Uri uri, String[] projection, String sortOrder) {
        long _end_date = TodoContract.TaskEntry.getEndDateFromUri(uri);

        return sTaskQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sTaskByDateRangeSelection,
                new String[]{Long.toString(_end_date)},
                null,
                null,
                sortOrder
        );
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor = null;
        switch (sUriMatcher.match(uri)) {
            case TASK:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        TodoContract.TaskEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case GOAL:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        TodoContract.GoalEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

            case GOALS:
                // get all goals
                retCursor = mOpenHelper.getReadableDatabase().query(
                        TodoContract.GoalEntry.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        sortOrder);
                break;
            case TASKS_WITH_GOAL:
                retCursor = getTaskByGoal(uri,projection, sortOrder);
                break;
            case TASKS_WITH_DATE_RANGE:
                retCursor =getTaskByDateRange(uri, projection, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
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
