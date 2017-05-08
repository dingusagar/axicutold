package com.example.dingu.axicut.Inward;


import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;



public class InwardEntrySaleOrder extends AppCompatActivity {

    EditText dateText;
    EditText timeText;
    EditText customerNameText , customerDCText,saleOrderNumberText;
    SimpleDateFormat formatter;
    Calendar calendar;
    Button createWorkOrderButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inward_entry);





        dateText = (EditText) findViewById(R.id.date);
        timeText = (EditText) findViewById(R.id.time);

        customerNameText = (EditText)findViewById(R.id.customerName);
        customerDCText = (EditText)findViewById(R.id.customerDC);
        saleOrderNumberText = (EditText)findViewById(R.id.saleOrder);

        createWorkOrderButton = (Button)findViewById(R.id.createWO);
        createWorkOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoWorkOrderEntryActivity();
            }
        });



        // setting up date picker
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment();
                cdp.show(InwardEntrySaleOrder.this.getSupportFragmentManager(), "Material Calendar Example");
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


        // setting up time picker
        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(InwardEntrySaleOrder.this, new TimePickerDialog.OnTimeSetListener() {


                    @Override
                    public void onTimeSet(android.widget.TimePicker timePicker, int i, int i1) {
                        timeText.setText( i + ":" + i1);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
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

    private void gotoWorkOrderEntryActivity() {
        final SaleOrder newOrder = new SaleOrder();

        newOrder.setSaleOrderNumber(saleOrderNumberText.getText().toString());
        newOrder.setCustomerDCNumber(customerDCText.getText().toString());
        newOrder.setCustomerName(customerNameText.getText().toString());
        newOrder.setDate(dateText.getText().toString());
        newOrder.setTime(timeText.getText().toString());

        Intent intent  = new  Intent(getApplicationContext(),InwardEntryWorkOrder.class);
        intent.putExtra("SaleOrder",newOrder);
        startActivity(intent);



    }
}
