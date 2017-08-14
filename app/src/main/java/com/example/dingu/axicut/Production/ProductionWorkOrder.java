package com.example.dingu.axicut.Production;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;
import com.example.dingu.axicut.Utils.General.NetworkLostDetector;
import com.example.dingu.axicut.Utils.RangeSelector;
import com.example.dingu.axicut.Utils.RecyclerViewRefresher;
import com.example.dingu.axicut.Utils.Validator;
import com.example.dingu.axicut.WorkOrder;

import java.util.ArrayList;

public class ProductionWorkOrder extends AppCompatActivity implements RecyclerViewRefresher {
    private RecyclerView workOrderRecyclerView;
    private WorkOrderAdapter workOrderAdapter;
    private ArrayList<WorkOrder> workOrderArrayList;
    private ArrayList<WorkOrder> validSelections;
    private SaleOrder saleOrder;
    private Button timeTakenButton;
    private RangeSelector rangeSelector;
    NetworkLostDetector networkLostDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production_work_order);
        networkLostDetector = new NetworkLostDetector(android.R.id.content,this);
        saleOrder=(SaleOrder) getIntent().getSerializableExtra("SaleOrder");
        workOrderArrayList=saleOrder.getWorkOrders();
        Validator validator = new ProductionValidator();
        validSelections = validator.isValid(workOrderArrayList);
        workOrderRecyclerView = (RecyclerView)findViewById(R.id.workOrderRecyclist);
        workOrderRecyclerView.setNestedScrollingEnabled(false);
        workOrderRecyclerView.setHasFixedSize(true);
        workOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        timeTakenButton=(Button)findViewById(R.id.assignButton);
        timeTakenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(ProductionWorkOrder.this,rangeSelector.getSelectedItems(),saleOrder,ProductionWorkOrder.this);
                dialog.showDialog();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        rangeSelector = new RangeSelector(this,this,validSelections);
        workOrderAdapter = new WorkOrderAdapter(validSelections,this,rangeSelector.getSelectedItems());
        workOrderRecyclerView.setAdapter(workOrderAdapter);
        setTitle(saleOrder.getSaleOrderNumber());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.work_order_options,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.RangeSelection:
                rangeSelector.setupDialog();
                rangeSelector.showDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public SaleOrder getSaleOrder(){
        return this.saleOrder;
    }
    @Override
    public void refreshRecyclerView() {
        workOrderAdapter.notifyDataSetChanged();
    }
}
