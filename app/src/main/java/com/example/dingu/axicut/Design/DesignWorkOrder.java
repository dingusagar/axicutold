package com.example.dingu.axicut.Design;
import android.support.annotation.NonNull;
import android.support.constraint.solver.ArrayLinkedVariables;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;
import com.example.dingu.axicut.WorkOrder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DesignWorkOrder extends AppCompatActivity {
    private RecyclerView workOrderRecyclerView;
    private WorkOrderAdapter workOrderAdapter;
   private ArrayList<WorkOrder> workOrderArrayList;
    private SaleOrder saleOrder;
    private static boolean[] layoutUpdate;
private Button saveButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_work_order);
        saleOrder=(SaleOrder) getIntent().getSerializableExtra("SaleOrder");
        saveButton=(Button)findViewById(R.id.workOrderSave);
        workOrderArrayList=saleOrder.getWorkOrders();
        workOrderRecyclerView = (RecyclerView)findViewById(R.id.workOrderRecyclist);
        workOrderRecyclerView.setNestedScrollingEnabled(false);
        workOrderRecyclerView.setHasFixedSize(true);
        workOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        layoutUpdate=new boolean[workOrderArrayList.size()];
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            updateWorkOrderLayoutToDatabase();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        workOrderAdapter = new WorkOrderAdapter(this.workOrderArrayList,this);
        workOrderRecyclerView.setAdapter(workOrderAdapter);
    }

    public void setUpdateValue(int pos,boolean value){
        layoutUpdate[pos]=value;
    }

    public void updateWorkOrderLayoutToDatabase(){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(saleOrder.getSaleOrderNumber()).child("workOrders");
        for(int counter =0;counter<layoutUpdate.length;counter++){
            if(layoutUpdate[counter]){
                dbRef.child(String.valueOf(counter)).child("layoutName").setValue(workOrderArrayList.get(counter).getLayoutName()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        workOrderRecyclerView.getAdapter().notifyDataSetChanged();
                    }
                });

            }
        }
    }
}
