package com.example.dingu.axicut.Inward.Despatch;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dingu.axicut.Inward.InwardUtilities;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;
import com.example.dingu.axicut.Utils.General.MyDatabase;
import com.example.dingu.axicut.Utils.RangeSelector2;
import com.example.dingu.axicut.Utils.RecyclerViewRefresher;
import com.example.dingu.axicut.WorkOrder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class DespatchScrapActivity extends AppCompatActivity implements RecyclerViewRefresher {

    RecyclerView recyclerView;
    AlertDialog.Builder despatchDialog;
    SaleOrder saleOrder;
    ArrayList<WorkOrder> workOrderList;
    DespatchWorkOrderAdapter despatchWorkOrderAdapter;

    DatabaseReference dbRefOrders, dbRefUtils;


    TextView dateText;
    TextView timeText;
    TextView saleOrderNumberText;
    TextView customerIDText;
    TextView customerDCText;

    String currentDate = "";

    RangeSelector2 rangeSelector2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despatch_scrap);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        dateText = (TextView) findViewById(R.id.dateText);
        timeText = (TextView) findViewById(R.id.timeText);
        customerIDText = (TextView) findViewById(R.id.customerIDText);
        saleOrderNumberText = (TextView)findViewById(R.id.saleOrder);
        customerDCText = (TextView) findViewById(R.id.customerDC);



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

        despatchWorkOrderAdapter = new DespatchWorkOrderAdapter(workOrderList , null,this);

        if(InwardUtilities.getServerDate() != null)
            currentDate = InwardUtilities.getServerDate();

       rangeSelector2 = new RangeSelector2(this,this,getLastWorkOrderNo());


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
                rangeSelector2.setupDialog();
                rangeSelector2.showDialog();
                break;


        }

        return super.onOptionsItemSelected(item);
    }

    public void openDialogAndWriteBackToDB(CheckBox v , final int workOrderPosition , final DespatchAction action){

        if(v.isChecked())
        {
            alertDialogForCheckedBox_True( v ,  workOrderPosition ,  action);
        }else
        {
            alertDialogForCheckedBox_false( v ,  workOrderPosition ,  action);
        }


    }

    private void alertDialogForCheckedBox_false(final CheckBox checkBox, final int workOrderPosition, final DespatchAction action) {
        final boolean checkBoxStatus = checkBox.isChecked();



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Clear Entry");
        builder.setMessage("All you sure you want to clear this");
        builder.setCancelable(false);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(DespatchScrapActivity.this, "confirmed", Toast.LENGTH_LONG).show();


                writeToDatabase(action,"","",workOrderPosition);

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkBox.setChecked(!checkBoxStatus);
            }
        });

        builder.show();
    }

    private void alertDialogForCheckedBox_True(final CheckBox checkBox, final int workOrderPosition, final DespatchAction action) {

        final boolean checkBoxStatus = checkBox.isChecked();

        LayoutInflater inflater = LayoutInflater.from(DespatchScrapActivity.this);
        View subView = inflater.inflate(R.layout.despatch_alert_dialog_fragment,null);
        final EditText dateText = (EditText)subView.findViewById(R.id.Date);
        final EditText dcText = (EditText)subView.findViewById(R.id.DCNumber);
        dateText.setText(currentDate);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Entry");
        builder.setView(subView);
        builder.setCancelable(false);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(DespatchScrapActivity.this, "confirmed", Toast.LENGTH_LONG).show();

                String date = dateText.getText().toString().trim();
                String dc = dcText.getText().toString().trim();

                writeToDatabase(action,date,dc,workOrderPosition);

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkBox.setChecked(!checkBoxStatus);
            }
        });

        builder.show();

    }

    public void writeToDatabase(DespatchAction action, String date, String dc, int workOrderPosition) {

        if(action.equals(DespatchAction.DESPATCH_WORKORDER))
            despatchToDatabase(date,dc,workOrderPosition);
        else
            scrapToDatabase(date,dc,workOrderPosition);
    }

    public void scrapToDatabase(final String date, final String dc, final int workOrderPosition) {
        dbRefOrders.child(saleOrder.getSaleOrderNumber()).child("workOrders").child(""+workOrderPosition).child("scrapDC").setValue(""+dc);
        dbRefOrders.child(saleOrder.getSaleOrderNumber()).child("workOrders").child(""+workOrderPosition).child("scrapDate").setValue(""+date).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                workOrderList.get(workOrderPosition).setScrapDate(date);
                workOrderList.get(workOrderPosition).setScrapDC(dc);
                despatchWorkOrderAdapter.notifyDataSetChanged();

            }
        });
    }

    public void despatchToDatabase(final String date, final String dc, final int workOrderPosition) {

        dbRefOrders.child(saleOrder.getSaleOrderNumber()).child("workOrders").child(""+workOrderPosition).child("despatchDC").setValue(""+dc);
        dbRefOrders.child(saleOrder.getSaleOrderNumber()).child("workOrders").child(""+workOrderPosition).child("despatchDate").setValue(""+date).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                workOrderList.get(workOrderPosition).setDespatchDate(date);
                workOrderList.get(workOrderPosition).setDespatchDC(dc);
                despatchWorkOrderAdapter.notifyDataSetChanged();
            }
        });

    }






    public int getLastWorkOrderNo()
    {
        if(workOrderList.size() == 0)
            return 0;
        else
            return workOrderList.get(workOrderList.size() - 1).getWorkOrderNumber();
    }


    @Override
    public void refreshRecyclerView() {
        despatchWorkOrderAdapter.notifyDataSetChanged();
    }
}


