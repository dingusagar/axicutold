package com.example.dingu.axicut.Design;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dingu.axicut.Inward.InwardAction;
import com.example.dingu.axicut.Inward.InwardAdapter;
import com.example.dingu.axicut.Inward.InwardAddEditSaleOrder;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;
import com.example.dingu.axicut.WorkOrder;

import java.util.ArrayList;

import static com.example.dingu.axicut.R.id.saleOrder;

/**
 * Created by root on 20/5/17.
 */

public class WorkOrderAdapter extends RecyclerView.Adapter<WorkOrderAdapter.ViewHolder>  {

    private ArrayList<WorkOrder> workOrderList;

    public WorkOrderAdapter(ArrayList<WorkOrder> workOrderList) {
        this.workOrderList=workOrderList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_work_order_card_view,parent,false);
        return new WorkOrderAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WorkOrder workOrder = workOrderList.get(position);
        holder.setMaterialText(workOrder.getMaterialType());
        holder.setLotNoText(workOrder.getLotNumber());
        holder.setWorkOrderText("W" + String.valueOf(workOrder.getWorkOrderNumber()));
        holder.setInspectionRemarkText(workOrder.getInspectionRemark());
        holder.setSize1(String.valueOf(workOrder.getThickness()));
        holder.setSize2(String.valueOf(workOrder.getLength()));
        holder.setSize3(String.valueOf(workOrder.getBreadth()));
    }

    @Override
    public int getItemCount() {
        return workOrderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View mview;
        TextView materialText,lotNoText,workOrderText,inspectionRemarkText,size1,size2,size3;
        public ViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
            materialText=(TextView)mview.findViewById(R.id.materialText);
            lotNoText=(TextView)mview.findViewById(R.id.lotNoText);
            workOrderText=(TextView)mview.findViewById(R.id.workOrderNo);
            inspectionRemarkText=(TextView)mview.findViewById(R.id.remark);
            size1=(TextView)mview.findViewById(R.id.size1);
            size2=(TextView)mview.findViewById(R.id.size2);
            size3=(TextView)mview.findViewById(R.id.size3);
        }

        public  void setMaterialText(String text){
            materialText.setText(text);
        }
        public void setLotNoText(String text){lotNoText.setText(text);}
        public void setWorkOrderText(String text){workOrderText.setText(text);}
        public void setInspectionRemarkText(String text){inspectionRemarkText.setText(text);}
        public void setSize1(String text){size1.setText(text);}
        public void setSize2(String text){size1.setText(text);}
        public void setSize3(String text){size1.setText(text);}

    }
}
