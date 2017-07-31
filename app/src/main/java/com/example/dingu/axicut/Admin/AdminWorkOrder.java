package com.example.dingu.axicut.Admin;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

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
        setContentView(R.layout.activity_admin_work_order2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        saleOrder=(SaleOrder) getIntent().getSerializableExtra("SaleOrder");
        workOrderArrayList=saleOrder.getWorkOrders();
        workOrderRecyclerView = (RecyclerView)findViewById(R.id.workOrderRecyclist);
        workOrderRecyclerView.setHasFixedSize(true);
        workOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();
        workOrderAdapter = new WorkOrderAdapter(this.workOrderArrayList,this);
        workOrderRecyclerView.setAdapter(workOrderAdapter);
        StatsCalculator calculator = new StatsHelper(saleOrder);
        statsDisplayer(calculator);
    }

    public SaleOrder getSaleOrder(){
        return this.saleOrder;
    }

    private void statsDisplayer(StatsCalculator calculator){
        TextView saleOrder,company,workOrders,sheets,layoutsAssigned,designsProduced,designsDespatched,scrapsDesptached;
        saleOrder = (TextView)findViewById(R.id.adminSaleOrder);
        company= (TextView)findViewById(R.id.companyName);
        workOrders= (TextView)findViewById(R.id.numOfWO);
        sheets= (TextView)findViewById(R.id.numOfSheets);
        layoutsAssigned= (TextView)findViewById(R.id.numOfDesigns);
        designsProduced= (TextView)findViewById(R.id.numOfProd);
        designsDespatched= (TextView)findViewById(R.id.numOfDespatched);
        scrapsDesptached=(TextView)findViewById(R.id.numOfScraps);
        saleOrder.setText(calculator.getSaleOrder());
        company.setText(calculator.getCompany());
        workOrders.setText(calculator.getNumOfWorkOrders());
        sheets.setText(calculator.getNumOfSheets());
        layoutsAssigned.setText(calculator.getlayoutsAssigned());
        designsProduced.setText(calculator.getDesignsProduced());
        designsDespatched.setText(calculator.getDesignsDespatched());
        scrapsDesptached.setText(calculator.getScrapsDespatched());

    }

}
