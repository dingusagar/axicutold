package com.example.dingu.axicut.Inward.Despatch.Dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dingu.axicut.Inward.Despatch.DespatchScrapActivity;
import com.example.dingu.axicut.Inward.InwardAddEditSaleOrder;
import com.example.dingu.axicut.Inward.MyCustomDialog;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.WorkOrder;

import java.util.ArrayList;

/**
 * Created by dingu on 20/7/17.
 */

public class DespatchDialog implements MyCustomDialog {
    private Context context;
    private String title = "Confirm Despatch";
    private int layout = R.layout.despatch_alert_dialog_fragment;

    LayoutInflater inflater;
    AlertDialog.Builder builder;
    View contentView;
    boolean selectedItems[];



    public DespatchDialog(Context context,boolean selectedItems[]) {
        this.context = context;
        this.selectedItems = selectedItems;

        setupDialog();
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
        builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setView(contentView);

        builder.setPositiveButton("Despatch", new DialogInterface.OnClickListener() {
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

    }

    @Override
    public void onPositiveButtonClicked() {





    }

    @Override
    public void onNegativeButtonClicked() {}

    }

