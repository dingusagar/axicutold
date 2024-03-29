package com.example.dingu.axicut.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.BoringLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dingu.axicut.Design.DesignWorkOrder;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.WorkOrder;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dingu on 11/7/17.
 */

public class RangeSelector {
    private RecyclerViewRefresher refresher;
    private Context context;
    private String title;
    private int layout = R.layout.range_selector;
    private int from,to;
    private ArrayList<WorkOrder>workOrders;
    private HashMap<String,Boolean> selectedItems=new HashMap<>();
    LayoutInflater inflater;
    AlertDialog.Builder builder;
    View contentView;
    public RangeSelector(Context context , RecyclerViewRefresher refresher, ArrayList<WorkOrder> workOrders) {
        this.context = context;
        this.refresher = refresher;
        this.workOrders = workOrders;
        setupDialog();
    }


    public void showDialog() {
        AlertDialog dialog =  builder.create();
        dialog.show();
    }


    public void setupDialog() {
        inflater = LayoutInflater.from(context);
        contentView = inflater.inflate(layout,null);
        builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setView(contentView);

        builder.setPositiveButton("Generate", new DialogInterface.OnClickListener() {
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


    public void onPositiveButtonClicked() {
        from = Integer.parseInt(((EditText)contentView.findViewById(R.id.from)).getText().toString());
        to = Integer.parseInt(((EditText)contentView.findViewById(R.id.to)).getText().toString());
        if(from<=to){
            for(WorkOrder workOrder:workOrders)
               if(isbetweenRange(workOrder))
                   selectedItems.put(workOrder.getWorkOrderNumber(),true);
            refresher.refreshRecyclerView();
        }
    }


    public void onNegativeButtonClicked() {

    }

    public HashMap<String,Boolean> getSelectedItems(){
        return selectedItems;
    }

    private boolean isbetweenRange(WorkOrder workOrder){
        int num = (int)Math.floor(Float.parseFloat(workOrder.getWorkOrderNumber()));
        return num >= from && num <= to;
    }
}
