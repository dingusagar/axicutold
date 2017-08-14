package com.example.dingu.axicut.Inward;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;
import com.example.dingu.axicut.Utils.Effects.MyVibrator;
import com.example.dingu.axicut.Utils.Effects.ButtonAnimator;
import com.example.dingu.axicut.Utils.General.MyDatabase;
import com.example.dingu.axicut.Utils.General.NetworkLostDetector;
import com.example.dingu.axicut.Utils.General.QuickDataFetcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InwardAddEditSaleOrder extends AppCompatActivity {

    RecyclerView workorderRecyclerView;
    InwardWorkOrderAdapter workOrderAdapter;

    Button confirmButton;
    SaleOrder saleOrder;
    ProgressDialog progress;
    DatabaseReference dbRootRef;
    DatabaseReference dbRefOrders; // database reference to all orders
    DatabaseReference dbRefUtils ;//  reference to utilities in database like lastsaleOrderNumber , Server.TimeStamp
    DatabaseReference dbRefSaleOrderNums;
    View parentLayout;


    TextView dateText;
    TextView timeText;
    TextView saleOrderNumberText;
    TextView customerDCText;
    Spinner customerID_Spinner;
    SimpleDateFormat formatter;
    Calendar calendar;
    ImageButton dateButton , timeButton;

    MyVibrator myVibrator;

    TextView workOrderListEmptyMessage;
    InwardAction inwardAction;
    NetworkLostDetector networkLostDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inward_add_edit_sale_order);

        networkLostDetector = new NetworkLostDetector(android.R.id.content,this);
        myVibrator = new MyVibrator(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        parentLayout = findViewById(R.id.parent_layout);
        progress = new ProgressDialog(this);

        calendar = Calendar.getInstance();

        workorderRecyclerView = (RecyclerView)findViewById(R.id.workorderRecyclerview);
        workorderRecyclerView.setHasFixedSize(true);
        workorderRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        workOrderListEmptyMessage = (TextView) findViewById(R.id.list_empty_message);


        // db references
        dbRootRef = MyDatabase.getDatabase().getInstance().getReference();
        dbRefOrders = MyDatabase.getDatabase().getInstance().getReference().child("Orders");
        dbRefUtils = MyDatabase.getDatabase().getInstance().getReference().child("Utils");
        dbRefSaleOrderNums = MyDatabase.getDatabase().getReference().child("SaleOrderNums");


        // sale order header view
        dateText = (TextView) findViewById(R.id.dateText);
        timeText = (TextView) findViewById(R.id.timeText);
        dateButton = (ImageButton)findViewById(R.id.dateButton) ;
        timeButton = (ImageButton)findViewById(R.id.timeButton) ;
        customerID_Spinner = (Spinner) findViewById(R.id.customerID);
        customerID_Spinner.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, QuickDataFetcher.getCustomerIDs()));
        customerDCText = (TextView)findViewById(R.id.customerDC);
        saleOrderNumberText = (TextView)findViewById(R.id.saleOrderNum);


        // setting up date picker
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment();
                cdp.show(InwardAddEditSaleOrder.this.getSupportFragmentManager(), "Material Calendar Example");
                cdp.setOnDateSetListener(new CalendarDatePickerDialogFragment.OnDateSetListener() {
                    @Override
                    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            formatter = new SimpleDateFormat("dd/MM/yyyy");
                            String dateInString = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            Date date = formatter.parse(dateInString);
                            dateText.setText(formatter.format(date));

                        } catch (Exception ex) {
                            displayError("date time error ",ex);
                        }
                    }
                });
            }
        });

        // setting up time picker
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(InwardAddEditSaleOrder.this, new TimePickerDialog.OnTimeSetListener() {


                    @Override
                    public void onTimeSet(android.widget.TimePicker timePicker, int i, int i1) {
                        timeText.setText( i + ":" + i1);
                    }
                }, hour, minute, false);// 12 hour time is displayed , but stored as 24 hour clock time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });



        confirmButton = (Button)findViewById(R.id.confirmButton);
        ButtonAnimator.setEffect(confirmButton, ButtonAnimator.Effects.REVERSE_BACKGROUND_FOREGROUND);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmButtonAction(view);
            }
        });


        inwardAction = (InwardAction)getIntent().getSerializableExtra("InwardAction");

        if(inwardAction.equals(InwardAction.EDIT_SALE_ORDER))
        {
            saleOrder = (SaleOrder) getIntent().getSerializableExtra("SaleOrder");
        }
        else if(inwardAction.equals(InwardAction.CREATE_NEW_SALE_ORDER))
        {
            saleOrder = setUpNewSaleOrder();  // this will take care of the valid saleOrder number
        }

        InvalidateViews(); // put existing stuffs accross different views including recycler view


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workOrderListEmptyMessage.setVisibility(View.GONE);
                MyCustomDialog workorderMassEntry = new MassEntryDialog(InwardAddEditSaleOrder.this,saleOrder.getWorkOrders());
                workorderMassEntry.showDialog();
            }
        });


    }



    private void confirmButtonAction(View view) {
        myVibrator.vibrate();
        new AlertDialog.Builder(InwardAddEditSaleOrder.this)
                .setTitle("Confirm Entry")
                .setMessage("Do you want to save the changes ?")
                .setIcon(R.mipmap.db_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        UpdateSaleOrderObject();
                        writeBackOnDatabase();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    private SaleOrder setUpNewSaleOrder() {
        saleOrder = new SaleOrder();

        dbRefUtils.child("ServerTimeStamp").setValue(ServerValue.TIMESTAMP);
        dbRefUtils.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long serverTimeStamp = (Long) dataSnapshot.child("ServerTimeStamp").getValue();
                String lastSaleOrderNumber = (String) dataSnapshot.child("LastSaleOrderNumber").getValue();

                if(serverTimeStamp != null && lastSaleOrderNumber != null)
                {
                    saleOrder.invalidateSaleOrderNumber(serverTimeStamp,lastSaleOrderNumber);
                    saleOrder.setTimestamp(serverTimeStamp);
                    saleOrderNumberText.setText(saleOrder.getSaleOrderNumber());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        saleOrder = updateCurrentDateTime(saleOrder);

        return saleOrder;

    }

    private void displayError(String msg, Exception e) {
        Toast.makeText(getApplicationContext(),msg+" : " + e.toString(),Toast.LENGTH_LONG).show();
    }




    public void UpdateSaleOrderObject()
    {
        saleOrder.setSaleOrderNumber(saleOrderNumberText.getText().toString());
        saleOrder.setCustomerID(customerID_Spinner.getSelectedItem().toString());
        saleOrder.setCustomerDC(customerDCText.getText().toString());
        saleOrder.setDate(dateText.getText().toString());
        saleOrder.setTime(timeText.getText().toString());
    }





    public void writeBackOnDatabase()
    {
        if(!checkMandatoryFields())
            return;

        // everything is ready to be added to the database

        progress.setMessage("Adding new Sale Order...");
        progress.show();
        if(saleOrder.isValidSaleOrderNumber())
        {

            Map<String, Object> update = new HashMap<>();
            if(inwardAction.equals(InwardAction.CREATE_NEW_SALE_ORDER)){

                update.put("Orders/"+saleOrder.getSaleOrderNumber(), saleOrder);
                update.put("Utils/LastSaleOrderNumber",saleOrder.getSaleOrderNumber());
                update.put("SaleOrderNums/"+saleOrder.getSaleOrderNumber()+"/TS",saleOrder.getTimestamp());

            }else if(inwardAction.equals(InwardAction.EDIT_SALE_ORDER)){
                update.put("Orders/"+saleOrder.getSaleOrderNumber(), saleOrder);
            }

            dbRootRef.updateChildren(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        progress.dismiss();
                        Snackbar.make(parentLayout,"Successfully Saved Data ", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progress.dismiss();
                    Snackbar.make(parentLayout,"ERROR : " + e.toString(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });





        }


    }

    private boolean checkMandatoryFields() {
        StringBuffer errorMessage = new StringBuffer("");
        boolean isError = false;
        if(saleOrder.getCustomerDC() == null || saleOrder.getCustomerDC().equals(""))
        {
            errorMessage.append("-> Customer DC Number cannot be empty").append("\n");
            isError = true;
        }

        if(saleOrder.getSaleOrderNumber() == null || saleOrder.getSaleOrderNumber().equals(""))
        {
            errorMessage.append("-> SaleOrder Number Error. Check Internet connection").append("\n");
            isError = true;
        }


        if(isError)
        {
            final Snackbar snackbar = Snackbar.make(parentLayout, errorMessage ,Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Okay", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
            return false;
        }

        return true;
    }

    private void InvalidateViews() {

            saleOrderNumberText.setText(saleOrder.getSaleOrderNumber());
            customerID_Spinner.setSelection( ( (ArrayAdapter) customerID_Spinner.getAdapter()).getPosition(saleOrder.getCustomerID()) );
            dateText.setText(saleOrder.getDate());
            timeText.setText(saleOrder.getTime());
            customerDCText.setText(saleOrder.getCustomerDC());

            workOrderAdapter = new InwardWorkOrderAdapter(saleOrder.getWorkOrders(),this);
            workorderRecyclerView.setAdapter(workOrderAdapter);

            if(saleOrder.getWorkOrders().size() == 0)
              workOrderListEmptyMessage.setVisibility(View.VISIBLE);


    }

    public void refreshRecyclerView()
    {
        workOrderAdapter.notifyDataSetChanged();
    }

    private SaleOrder updateCurrentDateTime(SaleOrder saleOrder)
    {
        // putting on the view
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        dateText.setText(formatter.format(new Date()));
        formatter = new SimpleDateFormat("HH:mm");
        timeText.setText(formatter.format(new Date()));

        // putting on the object
        saleOrder.setDate(dateText.getText().toString());
        saleOrder.setTime(timeText.getText().toString());
        return saleOrder;
    }




}
