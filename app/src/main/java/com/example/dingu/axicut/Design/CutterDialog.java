package com.example.dingu.axicut.Design;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dingu.axicut.Inward.MyCustomDialog;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;
import com.example.dingu.axicut.Utils.RecyclerViewRefresher;
import com.example.dingu.axicut.WorkOrder;
import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * Created by root on 21/7/17.
 */


public class CutterDialog implements MyCustomDialog {
    private Context context;
    private String title = "Split WorkOrder";
    private int layout = R.layout.cutter_layout;

    LayoutInflater inflater;
    AlertDialog.Builder builder;
    View contentView;
    SaleOrder saleOrder;
    RecyclerViewRefresher refresher;
    ArrayList<WorkOrder> workOrders;
    WorkOrder workOrderToCut;
    TextView indicator;
    private ImageButton decrementButton,incrementButton;

    public CutterDialog(Context context, RecyclerViewRefresher refresher, ArrayList<WorkOrder> workOrders, WorkOrder workOrderToCut) {
        this.context = context;
        this.refresher = refresher;
        this.workOrderToCut = workOrderToCut;
        this.workOrders = workOrders;
        setupDialog();
    }

    @Override
    public void showDialog() {
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void setupDialog() {
        inflater = LayoutInflater.from(context);
        contentView = inflater.inflate(layout, null);
        indicator = (TextView) contentView.findViewById(R.id.CounterIndicator);

        builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setView(contentView);
        decrementButton = (ImageButton) contentView.findViewById(R.id.decrement);
        incrementButton=(ImageButton) contentView.findViewById(R.id.increment);
        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementPercentCut();
            }
        });
        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementPercentCut();
            }
        });

        builder.setPositiveButton("Split", new DialogInterface.OnClickListener() {
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
        int percentToCut = Integer.parseInt(indicator.getText().toString());
        try {
            WorkOrder newWorkOrder = (WorkOrder) workOrderToCut.clone();
            int total = workOrderToCut.getPercentCut();
            if (total - percentToCut <= 0) return; // invalid division
            workOrderToCut.setPercentCut(percentToCut);
            newWorkOrder.setPercentCut(total - percentToCut);
            addWorkOrderToList(newWorkOrder);
            refresher.refreshRecyclerView();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onNegativeButtonClicked() {

    }

    private void incrementPercentCut(){
        int percentCut = Integer.parseInt(indicator.getText().toString());
        if(percentCut<100 && percentCut>=0)
        indicator.setText(String.valueOf(percentCut+EditDesignLayout.cutOffset));

    }

    private void decrementPercentCut(){
        int percentCut = Integer.parseInt(indicator.getText().toString());
        if(percentCut>0 && percentCut<=100)
        indicator.setText(String.valueOf(percentCut-EditDesignLayout.cutOffset));
    }
    private void addWorkOrderToList(WorkOrder newWorkOrder){
        WorkOrder workOrder = workOrderToCut;
        int index=0;
        while (workOrder!=null && (int)Math.floor(Float.parseFloat(workOrder.getWorkOrderNumber()))==(int)Math.floor(Float.parseFloat(workOrderToCut.getWorkOrderNumber()))){
             index=workOrders.indexOf(workOrder);
            try {
                workOrder = workOrders.get(index + 1);
            }catch (IndexOutOfBoundsException e){break;}
        }
        float wo = Float.parseFloat(workOrders.get(index).getWorkOrderNumber());
        DecimalFormat df = new DecimalFormat("#.##");
        newWorkOrder.setWorkOrderNumber(df.format(wo+0.1));
        workOrders.add(index+1, newWorkOrder);
    }
}