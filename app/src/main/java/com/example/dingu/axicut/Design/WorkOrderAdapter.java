package com.example.dingu.axicut.Design;

import android.content.Context;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;

import com.example.dingu.axicut.Utils.RecyclerViewRefresher;
import com.example.dingu.axicut.WorkOrder;

import java.util.ArrayList;

import java.util.HashMap;


/**
 * Created by root on 20/5/17.
 */

public class WorkOrderAdapter extends RecyclerView.Adapter<WorkOrderAdapter.ViewHolder>  {
    private Context context;
    private ArrayList<WorkOrder> workOrderList;
    private HashMap<String,Boolean> selectedItems;
    private SaleOrder saleOrder;

    public WorkOrderAdapter(ArrayList<WorkOrder> workOrderList, HashMap<String,Boolean> selectedItems, Context context) {
        this.workOrderList = workOrderList;
        this.context = context;
        this.selectedItems = selectedItems;
        this.saleOrder = ((DesignWorkOrder)context).getSaleOrder();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_work_order_card_view, parent, false);
        return new WorkOrderAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final WorkOrder workOrder = workOrderList.get(position);
        holder.setMaterialText(workOrder.getMaterialType());
        holder.setLotNoText(workOrder.getLotNumber());
        holder.setWorkOrderText("W" + String.valueOf(workOrder.getWorkOrderNumber()));
        holder.setInspectionRemarkText(workOrder.getInspectionRemark());
        holder.setThickness(String.valueOf(workOrder.getThickness()));
        holder.setLength(String.valueOf(workOrder.getLength()));
        holder.setBreadth(String.valueOf(workOrder.getBreadth()));
        holder.setLayoutText(workOrder.getLayoutName());
        holder.setDateText(workOrder.getLayoutDate());
        holder.setPercentageText(""+workOrder.getPercentCut());
        if(selectedItems!=null)
        holder.setCheckBoxTicked(selectedItems.containsKey(workOrder.getWorkOrderNumber()));
        ImageButton designLayout = (ImageButton)holder.mview.findViewById(R.id.designLayoutEdit);
        designLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CutterDialog cutterDialog = new CutterDialog(context,(RecyclerViewRefresher) context,workOrderList,workOrder);
                cutterDialog.showDialog();
            }
        });
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked())
                selectedItems.put(workOrder.getWorkOrderNumber(),true);
                else selectedItems.remove(workOrder.getWorkOrderNumber());
            }
        });
    }

    @Override
    public int getItemCount() {
        return workOrderList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        View mview;
        TextView materialText, lotNoText, workOrderText, inspectionRemarkText,
                length,breadth,thickness, dateText,layoutText,percentageText;
        CheckBox checkBox;
        public ViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
            materialText = (TextView) mview.findViewById(R.id.materialText);
            lotNoText = (TextView) mview.findViewById(R.id.lotNoText);
            workOrderText = (TextView) mview.findViewById(R.id.workOrderNo);
            inspectionRemarkText = (TextView) mview.findViewById(R.id.remark);
            length = (TextView) mview.findViewById(R.id.length);
            breadth = (TextView) mview.findViewById(R.id.breadth);
            thickness = (TextView) mview.findViewById(R.id.thickness);
            layoutText = (TextView) mview.findViewById(R.id.DesignLayout);
            dateText = (TextView) mview.findViewById(R.id.DateModified);
            checkBox = (CheckBox)mview.findViewById(R.id.selected);
            percentageText=(TextView)mview.findViewById(R.id.percentCutText);
        }

        public void setMaterialText(String text) {
            materialText.setText(text);
        }

        public void setLotNoText(String text) {
            lotNoText.setText(text);
        }

        public void setWorkOrderText(String text) {
            workOrderText.setText(text);
        }

        public void setInspectionRemarkText(String text) {
            inspectionRemarkText.setText(text);
        }

        public void setLength(String text) {
            length.setText(text);
        }

        public void setBreadth(String text) {
            breadth.setText(text);
        }

        public void setThickness(String text) {
            thickness.setText(text);
        }

        public void setLayoutText(String text) {
            layoutText.setText("Layout: " + text);
        }

        public void setDateText(String date) {
            if (date != null) dateText.setText(date);
        }
        public void setCheckBoxTicked(Boolean isTicked){
            if(isTicked==null)
                checkBox.setChecked(false);
            else
            checkBox.setChecked(isTicked);
        }
        public void setPercentageText(String text){
            percentageText.setText(text);
        }

    }

}
