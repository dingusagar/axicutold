package com.example.dingu.axicut.Utils.General;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.example.dingu.axicut.Inward.InwardAddEditSaleOrder;
import com.example.dingu.axicut.Inward.MyCustomDialog;
import com.example.dingu.axicut.Inward.SaleOrderNumsFetcher;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.WorkOrder;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dingu on 24/7/17.
 */

public class SaleOrderDisplayLimitter implements MyCustomDialog {
    private Context context;
    private String title = "Limit the Number of SaleOrder to display";
    private int layout = R.layout.saleorder_display_range_selector;

    LayoutInflater inflater;
    AlertDialog.Builder builder;
    View contentView;

    ImageButton fromDateButton,toDateButton;
    TextView fromDateText,toDateText;
    EditText limitNumberText;
    FragmentManager fragmentManager;
    SimpleDateFormat formatter;

    Long fromTS,toTS;

    public int getLimitNumber() {
        return limitNumber;
    }

    int limitNumber;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    final String AXICUT_PREFERENCE = "AXICUT_LOCAL_STORAGE";
    final String LIMIT = "SaleOrderLoadingLimit";
    final String FROM_DATE = "FromDate";
    final String TO_DATE = "ToDate";
    SaleOrderNumsFetcher dbFetcher;





    public SaleOrderDisplayLimitter(Context context, FragmentManager fragmentManager, SaleOrderNumsFetcher dbFetcher) {
        this.context = context;
        this.dbFetcher = dbFetcher;
        this.fragmentManager = fragmentManager;
        sharedPreferences = context.getSharedPreferences(AXICUT_PREFERENCE,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        limitNumber = sharedPreferences.getInt(LIMIT,100);
    }

    @Override
    public void showDialog() {
        AlertDialog dialog =  builder.create();
        dialog.show();
    }

    @Override
    public void setupDialog() {
        inflater = LayoutInflater.from(context);
        contentView = inflater.inflate(layout,null);

        fromDateButton = (ImageButton) contentView.findViewById(R.id.fromDateButton);
        toDateButton = (ImageButton) contentView.findViewById(R.id.toDateButton);
        fromDateText = (TextView)contentView.findViewById(R.id.fromDate);
        toDateText = (TextView) contentView.findViewById(R.id.toDate);
        limitNumberText = (EditText)contentView.findViewById(R.id.limitNumber);

        limitNumberText.setText(""+limitNumber);
        fromDateText.setText(sharedPreferences.getString(FROM_DATE,"14/02/2017"));
        toDateText.setText(sharedPreferences.getString(TO_DATE,"14/07/2017"));

        builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setView(contentView);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onPositiveButtonClicked();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onNegativeButtonClicked();
            }
        });

        fromDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateFromDatePicker(fromDateText);
            }
        });

        toDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateFromDatePicker(toDateText);
            }
        });

    }

    @Override
    public void onPositiveButtonClicked() {
        limitNumber = Integer.parseInt(limitNumberText.getText().toString());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date parsedDate;
        Timestamp timestamp;
        try {
            parsedDate = dateFormat.parse(fromDateText.getText().toString());
            timestamp = new java.sql.Timestamp(parsedDate.getTime());
            fromTS = timestamp.getTime();
            editor.putString(FROM_DATE,fromDateText.getText().toString());

            parsedDate = dateFormat.parse(toDateText.getText().toString());
            timestamp = new java.sql.Timestamp(parsedDate.getTime());
            toTS = timestamp.getTime();
            editor.putString(TO_DATE,toDateText.getText().toString());
            editor.putInt(LIMIT,limitNumber);

            editor.commit();
            dbFetcher.fetchSaleOrderNumbersFromDatabase(fromTS,toTS,limitNumber);
        } catch (ParseException e) {
            Toast.makeText(context,"Date parsing error",Toast.LENGTH_LONG);
        }


    }

    @Override
    public void onNegativeButtonClicked() {

    }


    private void setDateFromDatePicker(final TextView textview) {
        CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment();
        cdp.show(fragmentManager, "Select Date");
        cdp.setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
            @Override
            public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                try {
                    formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                    Date date = formatter.parse(dateInString);
                    textview.setText(formatter.format(date));

                } catch (Exception ex) {
                    Toast.makeText(context,"Error getting date",Toast.LENGTH_LONG);
                }
            }
        });
    }


}
