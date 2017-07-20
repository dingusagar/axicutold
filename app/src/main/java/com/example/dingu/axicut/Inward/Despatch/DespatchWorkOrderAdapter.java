package com.example.dingu.axicut.Inward.Despatch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.dingu.axicut.R;
import com.example.dingu.axicut.WorkOrder;

import java.util.ArrayList;

import javax.security.auth.callback.CallbackHandler;

/**
 * Created by dingu on 22/5/17.
 */

public class DespatchWorkOrderAdapter extends RecyclerView.Adapter<DespatchWorkOrderAdapter.ViewHolder>  {

    private ArrayList<WorkOrder> workOrdersList;
    Context context;
    boolean selectedItems[];


    public DespatchWorkOrderAdapter(ArrayList<WorkOrder> workOrdersList, boolean[] selectedItems,Context context) {
        this.workOrdersList = workOrdersList;
        this.context = context;
        this.selectedItems = selectedItems;


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workorder_item_for_despatch_scrap,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        WorkOrder workOrder = workOrdersList.get(position);
        holder.setDespatch(workOrder.getDespatchDate(),workOrder.getDespatchDC());
        holder.setScrap(workOrder.getScrapDate(),workOrder.getScrapDC());

        holder.setWorkOrderNoText(workOrder.getWorkOrderNumber());
        holder.setOtherDetails(workOrder.getMaterialType(),workOrder.getLotNumber(),workOrder.getInspectionRemark());
        if(selectedItems != null) {
            holder.setCheckBox(selectedItems[workOrder.getWorkOrderNumber()]);
            holder.addListnerForCheckBox(workOrder.getWorkOrderNumber());
        }
    }

    @Override
    public int getItemCount() {
        return workOrdersList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        View mview;
        CheckBox workOrdercheckBox;
        TextView despatchDate;
        TextView despatchDC;
        TextView scrapDate;
        TextView scrapDC;

        TextView workOrderNoText;
        TextView materialType;
        TextView lotNoText;
        TextView inspectionRemark;


        public ViewHolder(View itemView){
            super(itemView);
            mview = itemView;


            workOrdercheckBox = (CheckBox)mview.findViewById(R.id.checkBox);
            despatchDate = (TextView)mview.findViewById(R.id.despatchDate);
            scrapDate = (TextView)mview.findViewById(R.id.scrapDate);

            despatchDC = (TextView)mview.findViewById(R.id.despatchDC);
            scrapDC = (TextView)mview.findViewById(R.id.ScrapDC);

            workOrderNoText = (TextView)mview.findViewById(R.id.workOrderNo);
            materialType = (TextView)mview.findViewById(R.id.materialText);
            lotNoText = (TextView)mview.findViewById(R.id.lotNoText);
            inspectionRemark = (TextView)mview.findViewById(R.id.remark);


        }

        public void setDespatch(String date , String dc) {
            despatchDate.setText(date);
            despatchDC.setText(dc);

        }

        public void setScrap(String date , String dc )
        {
                scrapDate.setText(date);
                scrapDC.setText(dc);

        }

        public void setWorkOrderNoText(int num)
        {
            workOrderNoText.setText("W"+num);
        }


        public void setOtherDetails(String material,String lotNo,String remark)
        {
            materialType.setText(material);
            lotNoText.setText(lotNo);
            inspectionRemark.setText(remark);

        }

        public void setCheckBox(boolean value)
        {
            workOrdercheckBox.setChecked(value);
        }


        public void addListnerForCheckBox(final int workOrderNumber) {
           workOrdercheckBox.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                  selectedItems[workOrderNumber] = ((CheckBox)v).isChecked();


               }

           });
        }
    }
}
