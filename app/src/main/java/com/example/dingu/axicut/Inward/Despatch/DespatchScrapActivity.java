package com.example.dingu.axicut.Inward.Despatch;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dingu.axicut.Inward.Despatch.DialogFunctions.DoDespatch;
import com.example.dingu.axicut.Inward.Despatch.DialogFunctions.DoScrap;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;
import com.example.dingu.axicut.Utils.General.MyDatabase;
import com.example.dingu.axicut.Utils.General.NetworkLostDetector;
import com.example.dingu.axicut.Utils.RangeSelector;
import com.example.dingu.axicut.Utils.RecyclerViewRefresher;
import com.example.dingu.axicut.WorkOrder;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;

public class DespatchScrapActivity extends AppCompatActivity implements RecyclerViewRefresher {

    RecyclerView recyclerView;
    AlertDialog.Builder despatchDialog;
    SaleOrder saleOrder;
    ArrayList<WorkOrder> workOrderList;
    DespatchWorkOrderAdapter despatchWorkOrderAdapter;

    DatabaseReference dbRefOrders, dbRefUtils;
    NetworkLostDetector networkLostDetector;

    TextView dateText;
    TextView timeText;
    TextView saleOrderNumberText;
    TextView customerIDText;
    TextView customerDCText;

    Button despatchButton, scrapButton;

    RangeSelector rangeSelector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despatch_scrap);
        networkLostDetector = new NetworkLostDetector(android.R.id.content,this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        dateText = (TextView) findViewById(R.id.dateText);
        timeText = (TextView) findViewById(R.id.timeText);
        customerIDText = (TextView) findViewById(R.id.customerIDText);
        saleOrderNumberText = (TextView)findViewById(R.id.saleOrder);
        customerDCText = (TextView) findViewById(R.id.customerDC);
        despatchButton = (Button)findViewById(R.id.despatchButton);
        scrapButton = (Button)findViewById(R.id.scrapButton);



        dbRefOrders = MyDatabase.getDatabase().getInstance().getReference().child("Orders");
        dbRefUtils = MyDatabase.getDatabase().getInstance().getReference().child("Utils");
        dbRefUtils.keepSynced(true);


        recyclerView = (RecyclerView) findViewById(R.id.workorderRecyclerViewForScrapDespatch);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        despatchDialog = new AlertDialog.Builder(DespatchScrapActivity.this);
        saleOrder = (SaleOrder) getIntent().getSerializableExtra("SaleOrder");
        workOrderList = saleOrder.getWorkOrders();

        saleOrderNumberText.setText(saleOrder.getSaleOrderNumber());
        customerIDText.setText(saleOrder.getCustomerID());
        customerIDText.setText(saleOrder.getCustomerID());
        dateText.setText(saleOrder.getDate());
        timeText.setText(saleOrder.getTime());
        rangeSelector = new RangeSelector(this,this,workOrderList);
        despatchWorkOrderAdapter = new DespatchWorkOrderAdapter(workOrderList , rangeSelector.getSelectedItems(),this);

        final DoDespatch doDespatch = new DoDespatch(this,this,rangeSelector.getSelectedItems(),saleOrder);
        final DoScrap doScrap = new DoScrap(this,this,rangeSelector.getSelectedItems(),saleOrder);
        despatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDespatch.setupDialog();
                doDespatch.showDialog();

            }
        });

        scrapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doScrap.setupDialog();
                doScrap.showDialog();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerView.setAdapter(despatchWorkOrderAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.work_order_options, menu);


        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.RangeSelection:
                rangeSelector.setupDialog();
                rangeSelector.showDialog();
                break;


        }

        return super.onOptionsItemSelected(item);
    }





    @Override
    public void refreshRecyclerView() {
        despatchWorkOrderAdapter.notifyDataSetChanged();
    }
}


