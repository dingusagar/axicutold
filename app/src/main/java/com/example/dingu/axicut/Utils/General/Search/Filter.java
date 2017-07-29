package com.example.dingu.axicut.Utils.General.Search;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.example.dingu.axicut.Inward.InwardUtilities;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.Utils.General.ButtonAnimator;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Filter extends AppCompatActivity {

    TextView fromDateText;
    TextView toDateText;
    EditText thickness;
    Spinner customerID_Spinner,materialSpinner;
    SimpleDateFormat formatter;
    Calendar calendar;
    ImageButton fromDateButton, toDateButton;
    SearchFields searchFields;
    Button okButton,cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_saleorder_workorders);

        searchFields = new SearchFields();
        fromDateText = (TextView) findViewById(R.id.fromDate);
        toDateText = (TextView) findViewById(R.id.toDate);
        fromDateButton = (ImageButton)findViewById(R.id.fromDateButton) ;
        toDateButton = (ImageButton)findViewById(R.id.toDateButton) ;
        thickness = (EditText)findViewById(R.id.thickness);
        customerID_Spinner = (Spinner) findViewById(R.id.customerID);
        customerID_Spinner.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, InwardUtilities.getCustomerIDs()));

        materialSpinner = (Spinner) findViewById(R.id.materialSpinner);
        materialSpinner.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, InwardUtilities.getMaterialTypes()));

        okButton = (Button)findViewById(R.id.okButton);
        cancelButton = (Button)findViewById(R.id.cancel_button);

        // setting up date picker
        fromDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment();
                cdp.show(Filter.this.getSupportFragmentManager(), "Pick a date");
                cdp.setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            formatter = new SimpleDateFormat("dd/MM/yyyy");
                            String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            Date date = formatter.parse(dateInString);
                            fromDateText.setText(formatter.format(date).toString());

                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(),"Error parsing date",Toast.LENGTH_LONG);
                        }
                    }
                });
            }
        });

        toDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment();
                cdp.show(Filter.this.getSupportFragmentManager(), "Pick a date");
                cdp.setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            formatter = new SimpleDateFormat("dd/MM/yyyy");
                            String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            Date date = formatter.parse(dateInString);
                            toDateText.setText(formatter.format(date).toString());

                        } catch (Exception ex) {
                            Toast.makeText(getApplicationContext(),"Error parsing date",Toast.LENGTH_LONG);
                        }
                    }
                });
            }
        });


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSerachActivity();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void goToSerachActivity() {
        searchFields.setCustID(customerID_Spinner.getSelectedItem().toString());
        searchFields.setMaterialType(materialSpinner.getSelectedItem().toString());
        searchFields.setThickness(Float.parseFloat(thickness.getText().toString()));


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date parsedDate;
        Timestamp timestamp;
        try {
            parsedDate = dateFormat.parse(fromDateText.getText().toString());
            timestamp = new java.sql.Timestamp(parsedDate.getTime());
            searchFields.setFromTimeStamp(timestamp.getTime());
            parsedDate = dateFormat.parse(toDateText.getText().toString());
            timestamp = new java.sql.Timestamp(parsedDate.getTime());
            searchFields.setToTimeStamp(timestamp.getTime());
        } catch (ParseException e){
            Toast.makeText(getApplicationContext(),"Date parsing error",Toast.LENGTH_LONG);
        }

     

    }


}
