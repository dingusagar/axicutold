package com.example.dingu.axicut.Design;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.dingu.axicut.Utils.General.NetworkLostDetector;
import com.example.dingu.axicut.Utils.General.QuickDataFetcher;
import com.example.dingu.axicut.Utils.RangeSelector;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;
import com.example.dingu.axicut.Utils.RecyclerViewRefresher;
import com.example.dingu.axicut.Utils.Validator;
import com.example.dingu.axicut.WorkOrder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class DesignWorkOrder extends AppCompatActivity implements RecyclerViewRefresher{
    private RecyclerView workOrderRecyclerView;
    private WorkOrderAdapter workOrderAdapter;
    private Button assignLayoutButton;
    private HashMap<String,Boolean> selectedItems;
    private RangeSelector rangeSelector;
    private ArrayList<WorkOrder> validSelections;
    private ArrayList<WorkOrder> workOrderArrayList;

    private SaleOrder saleOrder;
    private NetworkLostDetector networkLostDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_work_order);
        networkLostDetector = new NetworkLostDetector(android.R.id.content,this);

        saleOrder=(SaleOrder) getIntent().getSerializableExtra("SaleOrder");
        workOrderArrayList=saleOrder.getWorkOrders();
        Validator validator = new DesignValidator();
        validSelections=validator.isValid(workOrderArrayList);
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
                for(int i = 0 ; i<validSelections.size();i++){
                    WorkOrder w = validSelections.get(i);
                    if(selectedItems.containsKey(w.getWorkOrderNumber())){
                        w.setLayoutName(layout);
                        w.setLayoutDate(QuickDataFetcher.getServerDate());
                    }
                }
                workOrderAdapter.notifyDataSetChanged();
            }

            @Override
            public void updateWorkOrderLayoutToDatabase(String layout) {
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(saleOrder.getSaleOrderNumber());
                syncValidWorkOrdersToArrayList();
                dbRef.setValue(saleOrder);

            }

            @Override
            public WorkOrder getWorkOrder() {
                if(selectedItems.size()==1) {
                    for(WorkOrder workOrder : saleOrder.getWorkOrders())
                        if(selectedItems.containsKey(workOrder.getWorkOrderNumber()))
                            return workOrder;
                    return null;
                }
                else return null;
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
        rangeSelector = new RangeSelector(this,this,validSelections);
        this.selectedItems=rangeSelector.getSelectedItems();
        workOrderAdapter = new WorkOrderAdapter(validSelections,rangeSelector.getSelectedItems(),this);
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

    private void syncValidWorkOrdersToArrayList(){
        for(int validWorkOrderCounter=0;validWorkOrderCounter<validSelections.size();validWorkOrderCounter++){
            WorkOrder validWorkOrder = validSelections.get(validWorkOrderCounter);
         if(!workOrderArrayList.contains(validWorkOrder)){
             int indexOfPrevOrigWorkOrder = workOrderArrayList.indexOf(validSelections.get(validWorkOrderCounter-1));
             workOrderArrayList.add(indexOfPrevOrigWorkOrder+1,validWorkOrder);
         }
        }
    }
}
