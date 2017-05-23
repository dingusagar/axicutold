package com.example.dingu.axicut.Admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;
import com.example.dingu.axicut.WorkOrder;

import java.util.ArrayList;

public class AdminWorkOrder extends AppCompatActivity {
    private RecyclerView workOrderRecyclerView;
    private WorkOrderAdapter workOrderAdapter;
    private ArrayList<WorkOrder> workOrderArrayList;
    private SaleOrder saleOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_work_order);
        saleOrder=(SaleOrder) getIntent().getSerializableExtra("SaleOrder");
        workOrderArrayList=saleOrder.getWorkOrders();
        workOrderRecyclerView = (RecyclerView)findViewById(R.id.workOrderRecyclist);
        workOrderRecyclerView.setNestedScrollingEnabled(false);
        workOrderRecyclerView.setHasFixedSize(true);
        workOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        workOrderAdapter = new WorkOrderAdapter(this.workOrderArrayList,this);
        workOrderRecyclerView.setAdapter(workOrderAdapter);
    }

    public SaleOrder getSaleOrder(){
        return this.saleOrder;
    }

}
