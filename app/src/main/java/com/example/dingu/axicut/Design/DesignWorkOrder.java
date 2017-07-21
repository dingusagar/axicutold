package com.example.dingu.axicut.Design;
import android.support.annotation.NonNull;
import android.support.constraint.solver.ArrayLinkedVariables;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
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
import com.example.dingu.axicut.Utils.RangeSelector;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;
import com.example.dingu.axicut.Utils.General.MyDatabase;
import com.example.dingu.axicut.Utils.RecyclerViewRefresher;
import com.example.dingu.axicut.WorkOrder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.Timestamp;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DesignWorkOrder extends AppCompatActivity implements RecyclerViewRefresher{
    private RecyclerView workOrderRecyclerView;
    private WorkOrderAdapter workOrderAdapter;
    private Button assignLayoutButton;
    private HashMap<String,Boolean> selectedItems;
    private RangeSelector rangeSelector;
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
        assignLayoutButton=(Button)findViewById(R.id.assignButton);
        assignLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignLayout();
            }
        });
    }

    private void assignLayout() {
        DesignLayoutCommunicator communicator= new DesignLayoutCommunicator() {
            @Override
            public void adapterNotify(String layout) {
                for(int i = 0 ; i<workOrderArrayList.size();i++){
                    WorkOrder w = workOrderArrayList.get(i);
                    if(selectedItems.get(w.getWorkOrderNumber()) == true){
                        w.setLayoutName(layout);
                        w.setLayoutDate(InwardUtilities.getServerDate());
                    }
                }
                workOrderAdapter.notifyDataSetChanged();
            }

            @Override
            public void updateWorkOrderLayoutToDatabase(String layout) {
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(saleOrder.getSaleOrderNumber()).child("workOrders");
                for(int i = 0 ; i<workOrderArrayList.size();i++){
                    WorkOrder w = workOrderArrayList.get(i);
                    if( selectedItems.get(w.getWorkOrderNumber()) == true){
                        DatabaseReference workOrderRef= dbRef.child(String.valueOf(workOrderArrayList.indexOf(w)));
                        DatabaseReference layoutRef = workOrderRef.child("layoutName");
                        DatabaseReference dateRef = workOrderRef.child("layoutDate");
                        layoutRef.setValue(layout);
                        dateRef.setValue(InwardUtilities.getServerDate());
                    }
                }
            }
        };
        FragmentManager fm = getSupportFragmentManager();
        EditDesignLayout editDesignFragment = new EditDesignLayout();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Communicator",communicator);
        editDesignFragment.setArguments(bundle);
        editDesignFragment.show(fm,"Design layout");
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

    @Override
    protected void onStart() {
        super.onStart();
        rangeSelector = new RangeSelector(this,this,workOrderArrayList);
        workOrderAdapter = new WorkOrderAdapter(this.workOrderArrayList,rangeSelector.getSelectedItems(),this);
        workOrderRecyclerView.setAdapter(workOrderAdapter);
        setTitle(saleOrder.getSaleOrderNumber());
    }

    public SaleOrder getSaleOrder(){
        return this.saleOrder;
    }



    @Override
    public void refreshRecyclerView() {
        workOrderAdapter.notifyDataSetChanged();
    }
}
