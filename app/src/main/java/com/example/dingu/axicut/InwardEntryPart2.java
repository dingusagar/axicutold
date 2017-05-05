package com.example.dingu.axicut;

import android.content.Intent;
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

import java.util.ArrayList;

public class InwardEntryPart2 extends AppCompatActivity {

    private ViewGroup mContainerView;
    ArrayList<WorkOrder> workOrders;
    Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inward_entry_part2);

        mContainerView = (ViewGroup) findViewById(R.id.container);
        workOrders = new ArrayList<>();

        confirmButton = (Button)findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FillWorkOrderList();
                Log.e("APP","Workorders : \n\n " + workOrders.toString());
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

    public void FillWorkOrderList()
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

            workOrders.add(workOrder);
        }
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
