package com.example.dingu.axicut;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/*
* From InwardEntry Activity a SaleOrder Object is passed via intent .
* This object is received here as newSaleOrder.
* All though newSaleOrder contains an ArrayList of WorkOrders,
* a local Arraylist of WorkOrders is created in the onCreate of this Activity which
* is finally copied to the Arraylist inside newSaleOrder
*
* After the UI is filled with details , FillWorkOrderList() is called when the user performs the confirm button
* This populates newSaleOrder completely and will start to write to the database
*
* About the UI
* everytime user presses plus button , a new workOrder View is created in the list.
* There is a cross button for each item in the list view to remove that from the list
*
* When an item is removed from the listview , all the workorder numbers of other items are
* updated using the method correctWorkOrderNumbers()
* */



public class InwardEntryPart2 extends AppCompatActivity {

    private ViewGroup mContainerView;
    ArrayList<WorkOrder> workOrders;
    Button confirmButton;
    SaleOrder newSaleOrder;
    ProgressDialog progress;
    DatabaseReference dbRefOrders; // database reference to all orders

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inward_entry_part2);

        dbRefOrders = FirebaseDatabase.getInstance().getReference().child("Orders");
        progress = new ProgressDialog(this);

        newSaleOrder = (SaleOrder) getIntent().getSerializableExtra("SaleOrder");

        mContainerView = (ViewGroup) findViewById(R.id.container);
        workOrders = new ArrayList<>(); // temporary Arraylist of WorkOrders which will finally be written into newSaleOrder's Arraylist in FillWorkList() method

        confirmButton = (Button)findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FillWorkOrderList();
                //Log.e("APP","Workorders : \n\n " + newSaleOrder.workOrders.toString());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.inward_entry_work_orders, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Navigate "up" the demo structure to the launchpad activity.
                // See http://developer.android.com/design/patterns/navigation.html for more.
                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
                return true;

            case R.id.action_add_item:
                // Hide the "empty" view since there is now at least one item in the list.
                findViewById(android.R.id.empty).setVisibility(View.GONE);
                addItem();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addItem() {
        // Instantiate a new "row" view.
        final ViewGroup newView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.workorder_list_item, mContainerView, false);

        TextView tv = (TextView)newView.findViewById(R.id.workOrderNo);
        tv.setText("W"+(mContainerView.getChildCount()+1));

        // Set a click listener for the "X" button in the row that will remove the row.
        newView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove the row from its parent (the container view).
                // Because mContainerView has android:animateLayoutChanges set to true,
                // this removal is automatically animated.

                int pos = mContainerView.indexOfChild(newView);

                mContainerView.removeView(newView);
                correctWorkOrderNumbers();

                // If there are no rows remaining, show the empty view.
                if (mContainerView.getChildCount() == 0) {
                    findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
                }
            }
        });

        // Because mContainerView has android:animateLayoutChanges set to true,
        // adding this view is automatically animated.
        mContainerView.addView(newView, 0);
    }

    public void FillWorkOrderList() // getting data from UI to a local arraylist called workorders
    {
        for(int i = 0; i<mContainerView.getChildCount();i++)
        {
            WorkOrder workOrder = new WorkOrder();
            View view = mContainerView.getChildAt(i);
            workOrder.setWorkOrderNumber(i+1);

            Spinner sp = (Spinner)view.findViewById(R.id.materialSpinner);
            workOrder.setMaterialType(sp.getSelectedItem().toString());

            sp = (Spinner)view.findViewById(R.id.lotNoSpinner);
            workOrder.setLotNumber(sp.getSelectedItem().toString());

            EditText et = (EditText)view.findViewById(R.id.size1);
            workOrder.setThickness(Float.parseFloat(et.getText().toString()));

            et = (EditText)view.findViewById(R.id.size2);
            workOrder.setLength(Float.parseFloat(et.getText().toString()));

            et = (EditText)view.findViewById(R.id.size3);
            workOrder.setBreadth(Float.parseFloat(et.getText().toString()));

            et = (EditText)view.findViewById(R.id.remark);
            workOrder.setInspectionRemark(et.getText().toString());

            workOrders.add(workOrder);  // adding to local workOrders List
        }

        newSaleOrder.setWorkOrders(workOrders);  // coping the local list of workorders to original list in newSaleOrder

        // everything is ready to be added to the database

        progress.setMessage("Adding new Sale Order...");
        progress.show();
        dbRefOrders.child(newSaleOrder.getSaleOrderNumber()).setValue(newSaleOrder).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(getApplicationContext(),"Successfully added records",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Opps : Error - " + e.toString(),Toast.LENGTH_LONG).show();
            }
        });
        progress.dismiss();

    }

    public void correctWorkOrderNumbers()
    {
        int  totalChildren = mContainerView.getChildCount();
        for(int i = 0; i<totalChildren;i++)
        {
            View view = mContainerView.getChildAt(i);
            TextView tv = (TextView)view.findViewById(R.id.workOrderNo);
            tv.setText("W"+(totalChildren - i));
        }

    }
}
