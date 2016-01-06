package com.blogspot.sttony.project8.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by sttony on 12/29/2015.
 */
public class TestProvider extends AndroidTestCase {


    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                TodoProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: TodoProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + TodoContract.CONTENT_AUTHORITY,
                    providerInfo.authority, TodoContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: WeatherProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    public void testGetType() {
        // content://[Project8]/task/123
        String type = mContext.getContentResolver().getType(TodoContract.TaskEntry.buildTaskUri(123));
        assertEquals("Error: the content://[Project8]/task/123 should return TaskEntry.CONTENT_ITEM_TYPE",
                TodoContract.TaskEntry.CONTENT_ITEM_TYPE, type);

        // content://[Project8]/goal/123
        type = mContext.getContentResolver().getType(TodoContract.GoalEntry.buildGoalUri(123));
        assertEquals("Error: the content://[Project8]/goal/123 should return GoalEntry.CONTENT_ITEM_TYPE",
                TodoContract.GoalEntry.CONTENT_ITEM_TYPE, type);

        // content://[Project8]/goal
        type = mContext.getContentResolver().getType(TodoContract.GoalEntry.buildGoalsUri());
        assertEquals("Error: the // content://[Project8]/goal should return GoalEntry.CONTENT_TYPE",
                TodoContract.GoalEntry.CONTENT_TYPE, type);

        // content://[Project8]/task?goal=123
        type = mContext.getContentResolver().getType(TodoContract.TaskEntry.buildTaskWithGoal(123));
        assertEquals("Error: the // content://[Project8]/task/goal/123 should return TaskEntry.CONTENT_TYPE",
                TodoContract.TaskEntry.CONTENT_TYPE, type);

        // content://[Project8]/task?end_date=456
        type = mContext.getContentResolver().getType(TodoContract.TaskEntry.buildTaskWithRange(123));
        assertEquals("Error:  // content://[Project8]/tasks?start_date=123&end_date=456 should return TaskEntry.CONTENT_TYPE",
                TodoContract.TaskEntry.CONTENT_TYPE, type);

    }

    public void testInsertReadProvider() {
//        ContentValues testValues = TestUtilities.createNorthPoleLocationValues();
//
//        // Register a content observer for our insert.  This time, directly with the content resolver
//        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
//        mContext.getContentResolver().registerContentObserver(LocationEntry.CONTENT_URI, true, tco);
//        Uri locationUri = mContext.getContentResolver().insert(LocationEntry.CONTENT_URI, testValues);
//
//        // Did our content observer get called?  Students:  If this fails, your insert location
//        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
//        tco.waitForNotificationOrFail();
//        mContext.getContentResolver().unregisterContentObserver(tco);
//
//        long locationRowId = ContentUris.parseId(locationUri);
//
//        // Verify we got a row back.
//        assertTrue(locationRowId != -1);
//
//        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
//        // the round trip.
//
//        // A cursor is your primary interface to the query results.
//        Cursor cursor = mContext.getContentResolver().query(
//                LocationEntry.CONTENT_URI,
//                null, // leaving "columns" null just returns all the columns.
//                null, // cols for "where" clause
//                null, // values for "where" clause
//                null  // sort order
//        );
//
//        TestUtilities.validateCursor("testInsertReadProvider. Error validating LocationEntry.",
//                cursor, testValues);
//
//        // Fantastic.  Now that we have a location, add some weather!
//        ContentValues weatherValues = TestUtilities.createWeatherValues(locationRowId);
//        // The TestContentObserver is a one-shot class
//        tco = TestUtilities.getTestContentObserver();
//
//        mContext.getContentResolver().registerContentObserver(WeatherEntry.CONTENT_URI, true, tco);
//
//        Uri weatherInsertUri = mContext.getContentResolver()
//                .insert(WeatherEntry.CONTENT_URI, weatherValues);
//        assertTrue(weatherInsertUri != null);
//
//        // Did our content observer get called?  Students:  If this fails, your insert weather
//        // in your ContentProvider isn't calling
//        // getContext().getContentResolver().notifyChange(uri, null);
//        tco.waitForNotificationOrFail();
//        mContext.getContentResolver().unregisterContentObserver(tco);
//
//        // A cursor is your primary interface to the query results.
//        Cursor weatherCursor = mContext.getContentResolver().query(
//                WeatherEntry.CONTENT_URI,  // Table to Query
//                null, // leaving "columns" null just returns all the columns.
//                null, // cols for "where" clause
//                null, // values for "where" clause
//                null // columns to group by
//        );
//
//        TestUtilities.validateCursor("testInsertReadProvider. Error validating WeatherEntry insert.",
//                weatherCursor, weatherValues);
//
//        // Add the location values in with the weather data so that we can make
//        // sure that the join worked and we actually get all the values back
//        weatherValues.putAll(testValues);
//
//        // Get the joined Weather and Location data
//        weatherCursor = mContext.getContentResolver().query(
//                WeatherEntry.buildWeatherLocation(TestUtilities.TEST_LOCATION),
//                null, // leaving "columns" null just returns all the columns.
//                null, // cols for "where" clause
//                null, // values for "where" clause
//                null  // sort order
//        );
//        TestUtilities.validateCursor("testInsertReadProvider.  Error validating joined Weather and Location Data.",
//                weatherCursor, weatherValues);
//
//        // Get the joined Weather and Location data with a start date
//        weatherCursor = mContext.getContentResolver().query(
//                WeatherEntry.buildWeatherLocationWithStartDate(
//                        TestUtilities.TEST_LOCATION, TestUtilities.TEST_DATE),
//                null, // leaving "columns" null just returns all the columns.
//                null, // cols for "where" clause
//                null, // values for "where" clause
//                null  // sort order
//        );
//        TestUtilities.validateCursor("testInsertReadProvider.  Error validating joined Weather and Location Data with start date.",
//                weatherCursor, weatherValues);
//
//        // Get the joined Weather data for a specific date
//        weatherCursor = mContext.getContentResolver().query(
//                WeatherEntry.buildWeatherLocationWithDate(TestUtilities.TEST_LOCATION, TestUtilities.TEST_DATE),
//                null,
//                null,
//                null,
//                null
//        );
//        TestUtilities.validateCursor("testInsertReadProvider.  Error validating joined Weather and Location data for a specific date.",
//                weatherCursor, weatherValues);
    }
}
