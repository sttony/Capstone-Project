package com.blogspot.sttony.project8.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.blogspot.sttony.project8.data.TodoContract.TaskEntry;
import com.blogspot.sttony.project8.data.TodoContract.GoalEntry;

/**
 * Created by sttony on 12/28/2015.
 */
public class TodoDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "todo.db";

    public TodoDBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_GOAL_TABLE = "CREATE TABLE " + GoalEntry.TABLE_NAME + " (" +
                GoalEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GoalEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                GoalEntry.COLUMN_DURATION + " INTEGER NOT NULL, " +
                GoalEntry.COLUMN_QUANTITY + " REAL NOT NULL, " +
                GoalEntry.COLUMN_UNIT + " TEXT NOT NULL, " +
                GoalEntry.COLUMN_START_DATE + " INTEGER NOT NULL " +
                " );";

        final String SQL_CREATE_TASK_TABLE = "CREATE TABLE " + TaskEntry.TABLE_NAME + " (" +
                TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TaskEntry.COLUMN_GOAL_ID + " INTEGER NOT NULL, " +
                TaskEntry.COLUMN_DUE_DATE + " INTEGER NOT NULL, " +
                TaskEntry.COLUMN_IS_COMPLETE + " INTEGER NOT NULL, " +
                TaskEntry.COLUMN_IS_REMINDER + " INTEGER NOT NULL, " +
                TaskEntry.COLUMN_QUANTITY + " REAL NOT NULL," +
                TaskEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                TaskEntry.COLUMN_COMMENT + " TEXT, " +
                TaskEntry.COLUMN_PRIORITY + " INTEGER NOT NULL, " +
                TaskEntry.COLUMN_COMPLETE_DATE + " INTEGER , " +

                // Set up the location column as a foreign key to location table.
                " FOREIGN KEY (" + TaskEntry.COLUMN_GOAL_ID + ") REFERENCES " +
                GoalEntry.TABLE_NAME + " (" + GoalEntry._ID + ") ); ";

        db.execSQL(SQL_CREATE_GOAL_TABLE);
        db.execSQL(SQL_CREATE_TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new UnsupportedOperationException();
    }
}
