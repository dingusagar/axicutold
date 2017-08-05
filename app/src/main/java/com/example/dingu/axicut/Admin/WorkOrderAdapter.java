package com.example.dingu.axicut.Admin;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;
import com.example.dingu.axicut.WorkOrder;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by root on 23/5/17.
 */

public class WorkOrderAdapter extends RecyclerView.Adapter<WorkOrderAdapter.ViewHolder> {
    private Context context;
    private ArrayList<WorkOrder> workOrderList;
    private SaleOrder saleOrder;

    public WorkOrderAdapter(ArrayList<WorkOrder> workOrderList, Context context) {
        this.workOrderList = workOrderList;
        this.context = context;
        this.saleOrder = ((AdminWorkOrder) context).getSaleOrder();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_work_order_card_view, parent, false);
        return new ViewHolder(view);

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
        holder.setProdOperator(workOrder.getProdName());
        holder.setProductionDate(workOrder.getProdDate());
        holder.setTimeTaken(workOrder.getProdTime());
        holder.setDespatchDate(workOrder.getDespatchDate());
        holder.setDespatchDCNum(workOrder.getDespatchDC());
        holder.setScrapDate(workOrder.getScrapDate());
        holder.setScrapDCNum(workOrder.getScrapDC());
    }

    @Override
    public int getItemCount() {
        return workOrderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mview;
        TextView materialText, lotNoText, workOrderText, inspectionRemarkText,
                length,breadth,thickness, dateText, layoutText, productionDate, prodOperator, timeTaken,
        despatchDate,despatchDCNum,scrapDate,scrapDCNum;
        private ImageButton timerButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
            timerButton = (ImageButton) mview.findViewById(R.id.TimerButton);
            materialText = (TextView) mview.findViewById(R.id.materialText);
            lotNoText = (TextView) mview.findViewById(R.id.lotNoText);
            workOrderText = (TextView) mview.findViewById(R.id.workOrderNo);
            inspectionRemarkText = (TextView) mview.findViewById(R.id.remark);
            length = (TextView) mview.findViewById(R.id.length);
            breadth = (TextView) mview.findViewById(R.id.breadth);
            thickness = (TextView) mview.findViewById(R.id.thickness);
            layoutText = (TextView) mview.findViewById(R.id.DesignLayout);
            dateText = (TextView) mview.findViewById(R.id.DateModified);
            productionDate = (TextView) mview.findViewById(R.id.prod_date);
            prodOperator = (TextView) mview.findViewById(R.id.operatorName);
            timeTaken = (TextView) mview.findViewById(R.id.timeText);
            despatchDate = (TextView) mview.findViewById(R.id.despatchDate);
            despatchDCNum =  (TextView) mview.findViewById(R.id.DespatchDC);
            scrapDate =  (TextView) mview.findViewById(R.id.scrapDate);
            scrapDCNum =  (TextView) mview.findViewById(R.id.scrapDCNum);

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
            dateText.setText(date);
        }

        public void setProductionDate(String date) {
            productionDate.setText(date);
            }

        public void setProdOperator(String text) {
            prodOperator.setText(text);
        }

        public void setTimeTaken(String time) {
            timeTaken.setText(time);
        }

        public void setDespatchDate(String date){despatchDate.setText(date);}
        public void setDespatchDCNum(String dcNum){despatchDCNum.setText(dcNum);}
        public void setScrapDate(String date){scrapDate.setText(date);}
        public void setScrapDCNum(String dcNum){scrapDCNum.setText(dcNum);}

    }
}
