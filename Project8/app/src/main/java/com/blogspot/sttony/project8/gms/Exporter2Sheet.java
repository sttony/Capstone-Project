package com.blogspot.sttony.project8.gms;


import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import au.com.bytecode.opencsv.CSVWriter;
import android.content.Context;
import android.database.Cursor;

import com.blogspot.sttony.project8.data.TodoContract;

public class Exporter2Sheet {

    private SimpleDateFormat dateFormatter= new SimpleDateFormat("MM-dd-yyyy", Locale.US);

    public void exportTaskToSheet(Context context, Writer wr) throws IOException {
        CSVWriter writer = new CSVWriter(wr);
        String[] header =  new String[]
                {
                        "TASK_ID",
                        "GOAL_ID",
                        "DUE_DATE",
                        "IS_COMPLETE",
                        "IS_REMINDER",
                        "QUANTITY",
                        "TITLE",
                        "COMMENT",
                        "PRIORITY",
                        "COMPLETE_DATE"
                };
        writer.writeNext(header);

        Cursor cr = context.getContentResolver().query(TodoContract.TaskEntry.buildTaskWithRange(Long.MAX_VALUE),
                null, null, null, null);
        cr.moveToPosition(-1);
        while(cr.moveToNext())
        {
            String[] entries = new String[10];
            entries[0] = Long.toString(cr.getLong(0)); // ID
            entries[1] = Long.toString(cr.getLong(1)); // GOAL_ID
            entries[2] = dateFormatter.format(new Date(cr.getLong(2))); // DUE_DATE
            entries[3] = Integer.toString(cr.getInt(3)); // IS_COMPLETE
            entries[4] = Integer.toString(cr.getInt(4)); // IS_REMINDER
            entries[5] = Integer.toString(cr.getInt(5)); // QUANTITY
            entries[6] = cr.getString(6); // TITLE
            entries[7] = cr.getString(7); // COMMENT
            entries[8] = Integer.toString(cr.getInt(8)); // PRIORITY
            entries[9] = dateFormatter.format(new Date(cr.getLong(9))); // COMPLETE_DATE
            writer.writeNext(entries);
        }
    }

    public void exportGoalToSheet(Context context, Writer wr)  throws IOException {
        CSVWriter writer = new CSVWriter(wr);
        String[] header =  new String[]
                {
                        "GOAL_ID",
                        "TITLE",
                        "DURATION",
                        "QUANTITY",
                        "UNIT",
                        "START_DATE"
                };
        writer.writeNext(header);

        Cursor cr = context.getContentResolver().query(TodoContract.GoalEntry.CONTENT_URI,
                null, null, null, null);
        cr.moveToPosition(-1);
        while(cr.moveToNext())
        {
            String[] entries = new String[6];
            entries[0] = Long.toString(cr.getLong(0)); // ID
            entries[1] = cr.getString(1); // TITLE
            entries[2] = Integer.toString(cr.getInt(2)); // DURATION
            entries[3] = Integer.toString(cr.getInt(3)); // QUANTITY
            entries[4] = cr.getString(4); // UNIT
            entries[5] = dateFormatter.format(new Date(cr.getLong(5))); // START_DATE
            writer.writeNext(entries);
        }
    }
}
