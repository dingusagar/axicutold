package com.example.dingu.axicut.Production;

/**
 * Created by root on 22/5/17.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.dingu.axicut.Design.DesignLayoutCommunicator;
import com.example.dingu.axicut.Design.DesignWorkOrder;
import com.example.dingu.axicut.Design.EditDesignLayout;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;
import com.example.dingu.axicut.WorkOrder;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;
import com.example.dingu.axicut.Utils.General.ButtonAnimator;
import com.example.dingu.axicut.WorkOrder;
import com.google.firebase.database.DatabaseReference;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import static com.example.dingu.axicut.R.id.saleOrder;

/**
 * Created by root on 20/5/17.
 */

public class WorkOrderAdapter extends RecyclerView.Adapter<WorkOrderAdapter.ViewHolder> {
    private Context context;
    private ArrayList<WorkOrder> workOrderList;
    private SaleOrder saleOrder;

    public WorkOrderAdapter(ArrayList<WorkOrder> workOrderList, Context context) {
        this.workOrderList = workOrderList;
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
        holder.setSize1(String.valueOf(workOrder.getThickness()));
        holder.setSize2(String.valueOf(workOrder.getLength()));
        holder.setSize3(String.valueOf(workOrder.getBreadth()));
        holder.setLayoutText(workOrder.getLayoutName());
        holder.setDateText(workOrder.getLayoutDate());
        holder.setProdOperator(workOrder.getProdName());
        holder.setProductionDate(workOrder.getProdDate());
        holder.setTimeTaken(workOrder.getProdTime());
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
    }

    @Override
    public int getItemCount() {
        return workOrderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mview;
        TextView materialText, lotNoText, workOrderText, inspectionRemarkText,
                size1, size2, size3, dateText,layoutText,productionDate,prodOperator,timeTaken;
        private ImageButton timerButton;
        public ViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
            timerButton=(ImageButton) mview.findViewById(R.id.TimerButton);
            materialText = (TextView) mview.findViewById(R.id.materialText);
            lotNoText = (TextView) mview.findViewById(R.id.lotNoText);
            workOrderText = (TextView) mview.findViewById(R.id.workOrderNo);
            inspectionRemarkText = (TextView) mview.findViewById(R.id.remark);
            size1 = (TextView) mview.findViewById(R.id.size1);
            size2 = (TextView) mview.findViewById(R.id.size2);
            size3 = (TextView) mview.findViewById(R.id.size3);
            layoutText = (TextView) mview.findViewById(R.id.DesignLayout);
            dateText = (TextView) mview.findViewById(R.id.DateModified);
            productionDate=(TextView)mview.findViewById(R.id.prod_date);
            prodOperator=(TextView)mview.findViewById(R.id.operatorName);
            timeTaken=(TextView)mview.findViewById(R.id.timeText);

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

        public void setSize1(String text) {
            size1.setText(text);
        }

        public void setSize2(String text) {
            size1.setText(text);
        }

        public void setSize3(String text) {
            size1.setText(text);
        }

        public void setLayoutText(String text) {
            layoutText.setText("Layout: " + text);
        }

        public void setDateText(Date date) {
            if (date != null) dateText.setText(date.getDate()+"/"+date.getMonth()+"/"+String.valueOf(date.getYear()+1900));
        }
        public void setProductionDate(Date date){
            if (date != null)productionDate.setText(date.getDate()+"/"+date.getMonth()+"/"+String.valueOf(date.getYear()+1900));
        }
        public void setProdOperator(String text){
            prodOperator.setText(text);
        }
        public void setTimeTaken(String time){
            timeTaken.setText(time);
        }
    }

}
