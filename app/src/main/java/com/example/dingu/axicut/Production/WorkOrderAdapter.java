package com.example.dingu.axicut.Production;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;

import com.example.dingu.axicut.WorkOrder;

import java.util.ArrayList;

import java.util.HashMap;


public class WorkOrderAdapter extends RecyclerView.Adapter<WorkOrderAdapter.ViewHolder> {
    private Context context;
    private ArrayList<WorkOrder> workOrderList;
    private SaleOrder saleOrder;
    private HashMap<String,Boolean> selectedItems;
    public WorkOrderAdapter(ArrayList<WorkOrder> workOrderList, Context context,HashMap<String,Boolean>selectedItems) {
        this.workOrderList = workOrderList;
        this.selectedItems=selectedItems;
        this.context = context;
        this.saleOrder = ((ProductionWorkOrder)context).getSaleOrder();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prod_work_order_card_view, parent, false);
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
        holder.setProdOperator(workOrder.getProdName());
        holder.setProductionDate(workOrder.getProdDate());
        holder.setTimeTaken(workOrder.getProdTime());
        if(selectedItems!=null)
            holder.setCheckBoxTicked(selectedItems.containsKey(workOrder.getWorkOrderNumber()));
        holder.timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Do you want to start timing for the production machine ??");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FragmentManager fm =((ProductionWorkOrder)context).getSupportFragmentManager();
                        ProdTimer dialogFrag = new ProdTimer();
                        dialogFrag.setCancelable(false);
                        Bundle bundle = new Bundle();
                        ProductionLayoutCommunicator communicator = new ProductionLayoutCommunicator() {
                            @Override
                            public SaleOrder getSaleOrder() {
                                return saleOrder;
                            }

                            @Override
                            public int getWorkOrderPos() {
                                return holder.getAdapterPosition();
                            }

                            @Override
                            public void adapterNotify() {
                                notifyDataSetChanged();
                            }
                        };
                        bundle.putSerializable("Communicator",communicator);
                        dialogFrag.setArguments(bundle);
                        dialogFrag.show(fm,"Timer");

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
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
                length,breadth,thickness, dateText,layoutText,productionDate,prodOperator,timeTaken;
        private ImageButton timerButton;
        CheckBox checkBox;
        public ViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
            timerButton=(ImageButton) mview.findViewById(R.id.TimerButton);
            materialText = (TextView) mview.findViewById(R.id.materialText);
            lotNoText = (TextView) mview.findViewById(R.id.lotNoText);
            workOrderText = (TextView) mview.findViewById(R.id.workOrderNo);
            inspectionRemarkText = (TextView) mview.findViewById(R.id.remark);
            length  = (TextView) mview.findViewById(R.id.length);
            breadth = (TextView) mview.findViewById(R.id.breadth);
            thickness = (TextView) mview.findViewById(R.id.thickness);
            layoutText = (TextView) mview.findViewById(R.id.DesignLayout);
            dateText = (TextView) mview.findViewById(R.id.DateModified);
            productionDate=(TextView)mview.findViewById(R.id.prod_date);
            prodOperator=(TextView)mview.findViewById(R.id.operatorName);
            timeTaken=(TextView)mview.findViewById(R.id.timeText);
            checkBox = (CheckBox)mview.findViewById(R.id.selected);
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
        public void setProductionDate(String date){
            if (date != null)productionDate.setText(date);
        }
        public void setProdOperator(String text){
            prodOperator.setText(text);
        }
        public void setTimeTaken(String time){
            timeTaken.setText(time);
        }
        public void setCheckBoxTicked(Boolean isTicked){
            if(isTicked==null)
                checkBox.setChecked(false);
            checkBox.setChecked(isTicked);
        }
    }

}
