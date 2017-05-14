package com.example.dingu.axicut.Inward;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;
import com.example.dingu.axicut.Utils.General.ButtonAnimator;
import com.example.dingu.axicut.WorkOrder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class InwardAddEditSaleOrder extends AppCompatActivity {

    ViewGroup mContainerView;
    ArrayList<WorkOrder> workOrders;
    Button confirmButton;
    SaleOrder newSaleOrder;
    ProgressDialog progress;
    DatabaseReference dbRefOrders; // database reference to all orders
    DatabaseReference dbRefUtils ;//  reference to utilities in database like lastsaleOrderNumber , Server.TimeStamp


    TextView dateText;
    TextView timeText;
    TextView saleOrderNumberText;
    Spinner customerDCNumber;
    SimpleDateFormat formatter;
    Calendar calendar;
    ImageButton dateButton , timeButton;

    Vibrator vibrator;
    int VIBRATE_DURATION = 100;


    InwardAction inwardAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inward_add_edit_saleorder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        progress = new ProgressDialog(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                addItem(); // adds a workorder view
            }
        });


        // acts as list view
        mContainerView = (ViewGroup) findViewById(R.id.container);
        workOrders = new ArrayList<>(); // temporary Arraylist of WorkOrders which will finally be written into newSaleOrder's Arraylist in FillWorkList() method

        // db references
        dbRefOrders = FirebaseDatabase.getInstance().getReference().child("Orders");
        dbRefUtils = FirebaseDatabase.getInstance().getReference().child("Utils");

        // sale order header view
        dateText = (TextView) findViewById(R.id.dateText);
        timeText = (TextView) findViewById(R.id.timeText);
        dateButton = (ImageButton)findViewById(R.id.dateButton) ;
        timeButton = (ImageButton)findViewById(R.id.timeButton) ;
        customerDCNumber = (Spinner) findViewById(R.id.customerDC);
        saleOrderNumberText = (TextView)findViewById(R.id.saleOrder);

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

                            dateText.setText(formatter.format(date).toString());

                        } catch (Exception ex) {
                            dateText.setText(ex.getMessage().toString());
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
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        confirmButton = (Button)findViewById(R.id.confirmButton);
        ButtonAnimator.setEffect(confirmButton, ButtonAnimator.Effects.REVERSE_BACKGROUND_FOREGROUND);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vibrator.vibrate(VIBRATE_DURATION);
                final Button button = (Button)view;
                new AlertDialog.Builder(InwardAddEditSaleOrder.this)
                        .setTitle("Confirm Entry")
                        .setMessage("Do you want to save the changes ?")
                        .setIcon(R.mipmap.db_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
//
                                if (button.isEnabled()) {
                                    button.setEnabled(false);
                                    button.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.button_disabled_text_color));
                                }
                                UpdateSaleOrderObject();
                                UpdateWorkOrderObjectsFromListView();
                                writeBackOnDatabase();
                            }})
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                confirmButton.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.button_enabled_text_color));
                            }
                        }).show();


            }
        });


        inwardAction = (InwardAction)getIntent().getSerializableExtra("InwardAction");

        if(inwardAction.equals(InwardAction.EDIT_SALE_ORDER))
        {
            newSaleOrder = (SaleOrder) getIntent().getSerializableExtra("SaleOrder");
            confirmButton.setEnabled(true);
            confirmButton.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.button_enabled_text_color));
        }
        else if(inwardAction.equals(InwardAction.CREATE_NEW_SALE_ORDER))
        {
            newSaleOrder = new SaleOrder();

            dbRefUtils.child("ServerTimeStamp").setValue(ServerValue.TIMESTAMP);
            dbRefUtils.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Long serverTimeStamp = (Long) dataSnapshot.child("ServerTimeStamp").getValue();
                    String lastSaleOrderNumber = (String) dataSnapshot.child("LastSaleOrderNumber").getValue();

                    if(serverTimeStamp != null && lastSaleOrderNumber != null)
                    {
                        newSaleOrder.invalidateSaleOrderNumber(serverTimeStamp,lastSaleOrderNumber);
                        saleOrderNumberText.setText(newSaleOrder.getSaleOrderNumber());
                        confirmButton.setEnabled(true);
                        confirmButton.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.button_enabled_text_color));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        InvalidateViews(inwardAction); // put existing stuffs accross different views based on InwardACtion


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.inward_entry_work_orders, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
//                if (mContainerView.getChildCount() == 0) {
//                    findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
//                }
            }
        });

        // Because mContainerView has android:animateLayoutChanges set to true,
        // adding this view is automatically animated.
        mContainerView.addView(newView, 0);
    }



    public void UpdateSaleOrderObject()
    {
        newSaleOrder.setSaleOrderNumber(saleOrderNumberText.getText().toString());
        newSaleOrder.setCustomerDCNumber(customerDCNumber.getSelectedItem().toString());
        newSaleOrder.setCustomerName("");
        newSaleOrder.setDate(dateText.getText().toString());
        newSaleOrder.setTime(timeText.getText().toString());
    }

    public void UpdateWorkOrderObjectsFromListView() // getting data from UI to a local arraylist called workorders
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

            Log.e("App",workOrder.toString());
            workOrders.add(workOrder);  // adding to local workOrders List
        }

        newSaleOrder.setWorkOrders(workOrders);  // coping the local list of workorders to original list in newSaleOrder


    }



    public void writeBackOnDatabase()
    {
        // everything is ready to be added to the database

        progress.setMessage("Adding new Sale Order...");
        progress.show();
        dbRefOrders.child(newSaleOrder.getSaleOrderNumber()).setValue(newSaleOrder).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            if(inwardAction.equals(InwardAction.CREATE_NEW_SALE_ORDER))
                dbRefUtils.child("LastSaleOrderNumber").setValue(newSaleOrder.getSaleOrderNumber()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Opps : Error - " + e.toString(),Toast.LENGTH_LONG).show();
                }
            });
                Toast.makeText(getApplicationContext(),"Successfully added records",Toast.LENGTH_SHORT).show();
                goBackToPreviousActivity.start();


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

    private void InvalidateViews(InwardAction inwardAction) {

        if(inwardAction.equals(InwardAction.EDIT_SALE_ORDER))
        {
            saleOrderNumberText.setText(newSaleOrder.getSaleOrderNumber());
            customerDCNumber.setSelection( ( (ArrayAdapter) customerDCNumber.getAdapter()).getPosition(newSaleOrder.getCustomerDCNumber()) );
            dateText.setText(newSaleOrder.getDate());
            timeText.setText(newSaleOrder.getTime());

            //populating workorders

           ArrayList<WorkOrder> WOrdersList = newSaleOrder.getWorkOrders();

            for(int i = 0 ; i < WOrdersList.size() ; i++)
            {
                WorkOrder w = WOrdersList.get(i);
                addItem();
                View view = mContainerView.getChildAt(0);


                Spinner sp = (Spinner)view.findViewById(R.id.materialSpinner);
                sp.setSelection( ( (ArrayAdapter) sp.getAdapter()).getPosition(w.getMaterialType()) );

                sp = (Spinner)view.findViewById(R.id.lotNoSpinner);
                sp.setSelection( ( (ArrayAdapter) sp.getAdapter()).getPosition(w.getLotNumber()) );

                EditText et = (EditText)view.findViewById(R.id.size1);
                et.setText(""+w.getThickness());

                et = (EditText)view.findViewById(R.id.size2);
                et.setText(""+w.getLength());

                et = (EditText)view.findViewById(R.id.size3);
                et.setText(""+w.getBreadth());

                et = (EditText)view.findViewById(R.id.remark);
                et.setText(w.getInspectionRemark());


            }
        }else if(inwardAction.equals(InwardAction.CREATE_NEW_SALE_ORDER))
        {
            calendar = Calendar.getInstance();
            formatter = new SimpleDateFormat("dd/MM/yyyy");
            dateText.setText(formatter.format(new Date()));  // sets the present date
            formatter = new SimpleDateFormat("HH:mm");
            timeText.setText(formatter.format(new Date()));
        }



    }

    // Thread to wait till the Toast Message to disappear
    Thread goBackToPreviousActivity = new Thread(){
        @Override
        public void run() {
            try {
                Thread.sleep(3000); // As I am using LENGTH_LONG in Toast
                InwardAddEditSaleOrder.this.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


}
