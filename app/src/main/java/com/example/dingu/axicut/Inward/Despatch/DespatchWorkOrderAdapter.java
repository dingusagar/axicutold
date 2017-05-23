package com.example.dingu.axicut.Inward.Despatch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.dingu.axicut.R;
import com.example.dingu.axicut.WorkOrder;

import java.util.ArrayList;

/**
 * Created by dingu on 22/5/17.
 */

public class DespatchWorkOrderAdapter extends RecyclerView.Adapter<DespatchWorkOrderAdapter.ViewHolder>  {

    private ArrayList<WorkOrder> workOrdersList;
    Context context;


    public DespatchWorkOrderAdapter(ArrayList<WorkOrder> workOrdersList, Context context) {
        this.workOrdersList = workOrdersList;
        this.context = context;


    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workorder_item_for_despatch_scrap,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        WorkOrder workOrder = workOrdersList.get(position);
        holder.despatchCheckBox.setOnClickListener(holder);
        holder.scrapCheckBox.setOnClickListener(holder);
        holder.setDespatch(workOrder.getDespatchDate(),workOrder.getDespatchDC());
        holder.setScrap(workOrder.getScrapDate(),workOrder.getScrapDC());

        holder.setWorkOrderNoText(workOrder.getWorkOrderNumber());
        holder.setOtherDetails(workOrder.getMaterialType(),workOrder.getLotNumber(),workOrder.getInspectionRemark());

    }

    @Override
    public int getItemCount() {
        return workOrdersList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        View mview;

        CheckBox despatchCheckBox;
        TextView despatchDate;
        TextView despatchDC;
        CheckBox scrapCheckBox;
        TextView scrapDate;
        TextView scrapDC;

        TextView workOrderNoText;
        TextView materialType;
        TextView lotNoText;
        TextView inspectionRemark;


        public ViewHolder(View itemView){
            super(itemView);
            mview = itemView;

            despatchCheckBox = (CheckBox) mview.findViewById(R.id.despatchedCheckbox);
            scrapCheckBox = (CheckBox) mview.findViewById(R.id.scrapCheckBox);

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

            if(date.equals("") && dc.equals(""))
                despatchCheckBox.setChecked(false);
            else
                despatchCheckBox.setChecked(true);
        }

        public void setScrap(String date , String dc )
        {
                scrapDate.setText(date);
                scrapDC.setText(dc);

            if(date.equals("")  && dc.equals(""))
                scrapCheckBox.setChecked(false);
            else
                scrapCheckBox.setChecked(true);

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


        @Override
        public void onClick(View v) {
            if(v == despatchCheckBox)
            {

                ((DespatchScrapActivity)context).openDialogAndWriteBackToDB(despatchCheckBox,getAdapterPosition(),DespatchAction.DESPATCH_WORKORDER);

            }
            else if(v == scrapCheckBox)
            {

                    ((DespatchScrapActivity)context).openDialogAndWriteBackToDB(scrapCheckBox,getAdapterPosition(),DespatchAction.SEND_SCRAP);
            }
        }
    }
}
