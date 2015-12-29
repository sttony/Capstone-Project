package com.blogspot.sttony.project8.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by sttony on 12/28/2015.
 */
public class TestDB extends AndroidTestCase {

    public void setUp() {
        mContext.deleteDatabase(TodoDBHelper.DATABASE_NAME);
    }

    public void testCreateDb() throws Throwable {
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(TodoContract.GoalEntry.TABLE_NAME);
        tableNameHashSet.add(TodoContract.TaskEntry.TABLE_NAME);

        mContext.deleteDatabase(TodoDBHelper.DATABASE_NAME);
        SQLiteDatabase db = new TodoDBHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());
    }
}
