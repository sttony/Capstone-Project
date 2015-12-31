package com.blogspot.sttony.project8;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    private DatePickerDialog mDueDataPicker;
    private EditText mDueDate;
    private SimpleDateFormat dateFormatter= new SimpleDateFormat("MM-dd-yyyy", Locale.US);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mDueDate = (EditText)findViewById(R.id.view_due_date);
        mDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDueDataPicker!=null) {
                    mDueDataPicker.show();
                }
            }
        });

        Calendar _calendar = Calendar.getInstance();
        mDueDataPicker = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        mDueDate.setText(dateFormatter.format(newDate.getTime()));
                    }
                },
                _calendar.get(Calendar.YEAR),
                _calendar.get(Calendar.MONTH),
                _calendar.get(Calendar.DAY_OF_MONTH)
        );
    }

    private void PopupDateDialog(View v)
    {
        mDueDataPicker.show();
    }


}
