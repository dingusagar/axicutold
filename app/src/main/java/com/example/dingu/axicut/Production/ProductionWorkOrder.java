package com.example.dingu.axicut.Production;
import android.support.annotation.NonNull;
import android.support.constraint.solver.ArrayLinkedVariables;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.dingu.axicut.Inward.InwardUtilities;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;
import com.example.dingu.axicut.Utils.General.MyDatabase;
import com.example.dingu.axicut.Utils.RangeSelector;
import com.example.dingu.axicut.Utils.RecyclerViewRefresher;
import com.example.dingu.axicut.WorkOrder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.security.Timestamp;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ProductionWorkOrder extends AppCompatActivity implements RecyclerViewRefresher {
    private RecyclerView workOrderRecyclerView;
    private WorkOrderAdapter workOrderAdapter;
    private ArrayList<WorkOrder> workOrderArrayList;
    private SaleOrder saleOrder;
    private Button timeTakenButton;
    private RangeSelector rangeSelector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production_work_order);
        saleOrder=(SaleOrder) getIntent().getSerializableExtra("SaleOrder");
        ProductionValidator validator = new ProductionValidator();
        workOrderArrayList=validator.isValid(saleOrder.getWorkOrders());
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
        rangeSelector = new RangeSelector(this,this,workOrderArrayList);
        workOrderAdapter = new WorkOrderAdapter(this.workOrderArrayList,this,rangeSelector.getSelectedItems());
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
