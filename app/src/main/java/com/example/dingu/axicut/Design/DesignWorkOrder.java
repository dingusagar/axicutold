package com.example.dingu.axicut.Design;
import android.support.constraint.solver.ArrayLinkedVariables;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;
import com.example.dingu.axicut.WorkOrder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DesignWorkOrder extends AppCompatActivity {
    private RecyclerView workOrderRecyclerView;
    private WorkOrderAdapter workOrderAdapter;
   private ArrayList<WorkOrder> workOrderArrayList;
    private SaleOrder saleOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_work_order);
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
        workOrderAdapter = new WorkOrderAdapter(this.workOrderArrayList);
        workOrderRecyclerView.setAdapter(workOrderAdapter);
    }
}
