package com.blogspot.sttony.project8.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by sttony on 12/28/2015.
 */
public class TodoContract {
    public static final String CONTENT_AUTHORITY = "com.blogspot.sttony.project8.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_TASK = "task";
    public static final String PATH_GOAL = "goal";
    public static final String PATH_GOALS = "goals";


    public static final class TaskEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASK).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TASK;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TASK;

        // Table name
        public static final String TABLE_NAME = "tasks";

        public static final String COLUMN_IS_COMPLETE = "is_complete";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DUE_DATE = "due_date";
        public static final String COLUMN_QUANTITY = "quatity";
        public static final String COLUMN_GOAL_ID = "goal_id";
        public static final String COLUMN_COMMENT = "comment";
        public static final String COLUMN_IS_REMINDER = "is_reminder";
        public static final String COLUMN_PRIORITY = "priority";


        public static final String QUERY_DEAD_LINE_DATE = "deadline_date";


        public static Uri buildTaskWithRange(long endDate)
        {
            return ContentUris.withAppendedId(CONTENT_URI.buildUpon().
                    appendPath(QUERY_DEAD_LINE_DATE).build(), endDate);
        }

        public static Uri buildTaskWithGoal(long goal_id)
        {
            return ContentUris.withAppendedId(CONTENT_URI.buildUpon().
                    appendPath(PATH_GOAL).build(), goal_id);
        }

        public static Uri buildTaskUri(long task_id)
        {
            return ContentUris.withAppendedId(CONTENT_URI, task_id);
        }

        public static long getGoalIdFromUri(Uri uri)
        {
            return Long.parseLong(uri.getPathSegments().get(2));
        }

        public static long getIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }

        public static long getEndDateFromUri(Uri uri)
        {
            return Long.parseLong(uri.getPathSegments().get(2));
        }
    }

    public static final class GoalEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_GOAL).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GOAL;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GOAL;


        public static Uri buildGoalUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildGoalsUri() {
            return CONTENT_URI;
        }

        public static final String TABLE_NAME = "goals";

        public static final String COLUMN_START_DATE = "start_date";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DURATION= "duration";
        public static final String COLUMN_QUANTITY = "quatity";
        public static final String COLUMN_UNIT= "unit";
    }

}
