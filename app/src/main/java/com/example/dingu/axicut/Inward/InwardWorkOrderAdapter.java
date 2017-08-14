package com.example.dingu.axicut.Inward;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dingu.axicut.R;
import com.example.dingu.axicut.Utils.General.QuickDataFetcher;
import com.example.dingu.axicut.WorkOrder;

import java.util.ArrayList;

/**
 * Created by dingu on 22/5/17.
 */

public class InwardWorkOrderAdapter extends RecyclerView.Adapter<InwardWorkOrderAdapter.ViewHolder>  {
    private ArrayList<WorkOrder> workOrdersList;
    Context context;
    public InwardWorkOrderAdapter(ArrayList<WorkOrder> workOrdersList, Context context) {
        this.workOrdersList = workOrdersList;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workorder_list_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        WorkOrder workOrder = workOrdersList.get(position);
        holder.setWorkOrderNoText(workOrder.getWorkOrderNumber());
        holder.setOtherDetails(workOrder.getMaterialType(),workOrder.getLotNumber(),workOrder.getLength(),workOrder.getBreadth(),workOrder.getThickness(),workOrder.getInspectionRemark());
        holder.setDeleteButton();
        holder.addListenersForFields();
    }

    @Override
    public int getItemCount() {
        return workOrdersList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView workOrderNoText;
        Spinner materialSpinner;
        Spinner lotNoSpinner;
        EditText length;
        EditText breadth;
        EditText thickness;
        EditText inspectionRemark;
        ImageButton deleteButton;


        public ViewHolder(View itemView){
            super(itemView);
            view = itemView;

            workOrderNoText = (TextView)view.findViewById(R.id.workOrderNo);
            materialSpinner= (Spinner)view.findViewById(R.id.materialSpinner);
            materialSpinner.setAdapter(new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item, QuickDataFetcher.getMaterialTypes()));
            lotNoSpinner = (Spinner)view.findViewById(R.id.lotNoSpinner);
            lotNoSpinner.setAdapter(new ArrayAdapter<>(context,android.R.layout.simple_spinner_dropdown_item, QuickDataFetcher.getLotNos()));



            length = (EditText)view.findViewById(R.id.size1);
            breadth= (EditText)view.findViewById(R.id.size2);
            thickness = (EditText)view.findViewById(R.id.size3);
            inspectionRemark = (EditText)view.findViewById(R.id.remark);
            deleteButton = (ImageButton)view.findViewById(R.id.delete_button);

        }



        public void setWorkOrderNoText(String num)
        {
            workOrderNoText.setText("W"+num);
        }


        public void setOtherDetails(String material,String lotNo,float l,float b,float t,String remark)
        {
            materialSpinner.setSelection( ( (ArrayAdapter) materialSpinner.getAdapter()).getPosition(material) );
            lotNoSpinner.setSelection( ( (ArrayAdapter) lotNoSpinner.getAdapter()).getPosition(lotNo) );
            length.setText(""+l);
            breadth.setText(""+b);
            thickness.setText(""+t);
            inspectionRemark.setText(remark);

        }

        public void setDeleteButton()
        {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WorkOrder workOrder = workOrdersList.get(getAdapterPosition());
                    if(workOrder.getLayoutName() == null || workOrder.getLayoutName().equals(""))
                    {
                        workOrdersList.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(),workOrdersList.size());
                    }else
                    {
                        Toast.makeText(context,"Cannot Delete this \n Layout already assigned ",Toast.LENGTH_LONG).show();
                    }

                }
            });
        }

        public void addListenersForFields()
        {
            materialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int pos = getAdapterPosition();
                    workOrdersList.get(pos).setMaterialType(parent.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            lotNoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int pos = getAdapterPosition();
                    workOrdersList.get(pos).setLotNumber(parent.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            length.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int pos = getAdapterPosition();
                    workOrdersList.get(pos).setLength(Float.parseFloat(s.toString()));
                }
            });
            breadth.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int pos = getAdapterPosition();
                    workOrdersList.get(pos).setBreadth(Float.parseFloat(s.toString()));
                }
            });
            thickness.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int pos = getAdapterPosition();
                    workOrdersList.get(pos).setThickness(Float.parseFloat(s.toString()));
                }
            });
            inspectionRemark.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int pos = getAdapterPosition();
                    workOrdersList.get(pos).setInspectionRemark(s.toString());
                }
            });
        }


    }
}
