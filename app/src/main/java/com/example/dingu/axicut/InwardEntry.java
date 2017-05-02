package com.example.dingu.axicut;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class InwardEntry extends AppCompatActivity {

    EditText dateText;
    EditText timeText;
    SimpleDateFormat formatter;
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inward_entry);

        dateText = (EditText) findViewById(R.id.date);
        timeText = (EditText) findViewById(R.id.time);


        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment();
                cdp.show(InwardEntry.this.getSupportFragmentManager(), "Material Calendar Example");
                cdp.setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                        try {
                             formatter = new SimpleDateFormat("dd/MM/yyyy");
                            String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            Date date = formatter.parse(dateInString);

                            dateText.setText(formatter.format(date).toString());

                        } catch (Exception ex) {
                            dateText.setText(ex.getMessage().toString());
                        }
                    }
                });
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        // to get the date and time
        calendar = Calendar.getInstance();
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        dateText.setText(formatter.format(new Date()));  // sets the present date
        formatter = new SimpleDateFormat("HH:mm");
        timeText.setText(formatter.format(new Date()));


    }
}
