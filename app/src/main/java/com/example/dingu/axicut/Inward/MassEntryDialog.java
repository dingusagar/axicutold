package com.example.dingu.axicut.Inward;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dingu.axicut.R;
import com.example.dingu.axicut.Utils.General.QuickDataFetcher;
import com.example.dingu.axicut.WorkOrder;

import java.util.ArrayList;

/**
 * Created by dingu on 11/7/17.
 */

public class MassEntryDialog implements MyCustomDialog {
    private Context context;
    private String title = "Add Work-Orders";
    private int layout = R.layout.inward_mass_entry__work_order__dialog_fragment;

    LayoutInflater inflater;
    AlertDialog.Builder builder;
    View contentView;

    TextView workOrderNoText;
    Spinner materialSpinner;
    Spinner lotNoSpinner;
    EditText length,breadth,thickness,inspectionRemark;
    TextView num_of_wo;

    ArrayList<WorkOrder> workOrders;
    int lastWorkOrderNo;

    public MassEntryDialog(Context context, ArrayList<WorkOrder> workOrders) {
        this.context = context;
        this.workOrders = workOrders;
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

        workOrderNoText = (TextView) contentView.findViewById(R.id.workOrderNo);
        materialSpinner = (Spinner)contentView.findViewById(R.id.materialSpinner);
        lotNoSpinner = (Spinner)contentView.findViewById(R.id.lotNoSpinner);
        materialSpinner.setAdapter(new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item, QuickDataFetcher.getMaterialTypes()));
        lotNoSpinner.setAdapter(new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item, QuickDataFetcher.getLotNos()));
        length = (EditText)contentView.findViewById(R.id.length);
        breadth = (EditText)contentView.findViewById(R.id.breadth);
        thickness = (EditText)contentView.findViewById(R.id.thickness);
        inspectionRemark = (EditText)contentView.findViewById(R.id.remark);
        num_of_wo = (TextView) contentView.findViewById(R.id.num_of_wo);

        lastWorkOrderNo = getLastWorkOrderNo();
        workOrderNoText.setText("W"+(lastWorkOrderNo+1));
        num_of_wo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                try{
                    int numOfWorkOrders = Integer.parseInt(s.toString());
                    if(numOfWorkOrders > 1)
                        workOrderNoText.setText("W"+(lastWorkOrderNo+1) + " - " + "W"+(lastWorkOrderNo+numOfWorkOrders));
                    else
                        workOrderNoText.setText("W"+(lastWorkOrderNo+1));

                }catch (NumberFormatException e){

                }



            }
        });

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

    @Override
    public void onPositiveButtonClicked() {
        WorkOrder workOrder;

        int numOfWorkOrders = Integer.parseInt(num_of_wo.getText().toString());

        int lastNum = getLastWorkOrderNo();



        for(int i =1; i<= numOfWorkOrders;i++)
        {
            workOrder = new WorkOrder();
            workOrder.setMaterialType(materialSpinner.getSelectedItem().toString());
            workOrder.setLotNumber(lotNoSpinner.getSelectedItem().toString());
            workOrder.setLength(Float.parseFloat(length.getText().toString()));
            workOrder.setBreadth(Float.parseFloat(breadth.getText().toString()));
            workOrder.setThickness(Float.parseFloat(thickness.getText().toString()));
            workOrder.setInspectionRemark(inspectionRemark.getText().toString());
            workOrder.setWorkOrderNumber(""+(lastNum + i));
            workOrders.add(workOrder);
        }

        ((InwardAddEditSaleOrder)context).refreshRecyclerView();

    }

    @Override
    public void onNegativeButtonClicked() {

    }

    public int getLastWorkOrderNo()
    {
        if(workOrders.size() == 0)
            return 0;
        else
        {
           String woNum =  workOrders.get(workOrders.size() - 1).getWorkOrderNumber();
            return (int) Math.floor(Float.parseFloat(woNum));
        }
    }
}
