package com.blogspot.sttony.project8.data;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
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

        // content://[Project8]/task?start_date=123&end_date=456
        type = mContext.getContentResolver().getType(TodoContract.TaskEntry.buildTaskWithRange(123, 456));
        assertEquals("Error:  // content://[Project8]/tasks?start_date=123&end_date=456 should return TaskEntry.CONTENT_TYPE",
                TodoContract.TaskEntry.CONTENT_TYPE, type);

    }
}
