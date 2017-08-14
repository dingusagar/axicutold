package com.example.dingu.axicut.Production;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dingu.axicut.Utils.General.QuickDataFetcher;
import com.example.dingu.axicut.Inward.MyCustomDialog;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;
import com.example.dingu.axicut.Utils.General.MyDatabase;
import com.example.dingu.axicut.Utils.RecyclerViewRefresher;
import com.example.dingu.axicut.WorkOrder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by root on 20/7/17.
 */

public class TimePickerDialog implements MyCustomDialog {
    private Context context;
    private String title = "Time picker";
    private int layout = R.layout.prod_time_picker;
    private SaleOrder saleOrder;
    TextView fromTime,toTime;
    private static DatabaseReference saleOrderRef;
    LayoutInflater inflater;
    AlertDialog.Builder builder;
    View contentView;
    RecyclerViewRefresher recyclerViewRefresher;
    HashMap<String,Boolean> selectedItems;

    public TimePickerDialog(Context context, HashMap<String,Boolean> selectedItems, SaleOrder saleOrder, RecyclerViewRefresher recyclerViewRefresher) {
        this.context = context;
        this.saleOrder=saleOrder;
        this.recyclerViewRefresher=recyclerViewRefresher;
        saleOrderRef= MyDatabase.getDatabase().getReference().child("Orders").child(saleOrder.getSaleOrderNumber());
        this.selectedItems = selectedItems;
        setupDialog();
    }

    @Override
    public void showDialog() {
        AlertDialog dialog =  builder.create();
        dialog.show();
    }

    @Override
    public void setupDialog() {
        inflater = LayoutInflater.from(context);
        contentView = inflater.inflate(layout,null);
        builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        ImageView fromTimePicker = (ImageView)contentView.findViewById(R.id.from);
        ImageView toTimePicker = (ImageView)contentView.findViewById(R.id.to);
        fromTime=(TextView)contentView.findViewById(R.id.FromText);
        toTime=(TextView)contentView.findViewById(R.id.ToText);
        builder.setView(contentView);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onPositiveButtonClicked();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onNegativeButtonClicked();
            }
        });
        fromTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            getTimePicker(fromTime);
            }
        });
        toTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTimePicker(toTime);
            }
        });
    }

    @Override
    public void onPositiveButtonClicked() {
    String fromText = fromTime.getText().toString();
    String toText = toTime.getText().toString();
    Integer duration = getDuration(fromText,toText);
        if(duration==null){
            Toast.makeText(context,"Invalid start and end time. Make sure start time is less than end time",Toast.LENGTH_LONG).show();
            return;
        }
    float durationPerWO =(float) duration/selectedWorkOrders();
        for(int counter=0;counter<saleOrder.getWorkOrders().size();counter++){
            WorkOrder wo = saleOrder.getWorkOrders().get(counter);
            if(selectedItems.containsKey(wo.getWorkOrderNumber())){
                upDateDB(wo,durationPerWO);
            }
        }
        recyclerViewRefresher.refreshRecyclerView();
    }

    @Override
    public void onNegativeButtonClicked() {}

    private void getTimePicker(final TextView textView){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        android.app.TimePickerDialog mTimePicker;
        mTimePicker = new android.app.TimePickerDialog(context, new android.app.TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(android.widget.TimePicker timePicker, int i, int i1) {
                textView.setText( i + ":" + i1);
            }
        }, hour, minute, false);// 12 hour time is displayed , but stored as 24 hour clock time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private Integer getDuration(String from, String to){
        DateFormat formatter = new SimpleDateFormat("HH:mm");
        try {
            formatter.parse(from);
            formatter.parse(to);
            int fromMins=toMins(from);
            int toMins=toMins(to);
            return fromMins<toMins?toMins-fromMins:null;
        } catch (ParseException e) {
            // This can happen if you are trying to parse an invalid date, e.g., 25:19:12.
            // Here, you should log the error and decide what to do next
            e.printStackTrace();
        }
        return null;
    }
    private static int toMins(String s){
        String[] hourMin = s.split(":");
        int hour = Integer.parseInt(hourMin[0]);
        int mins = Integer.parseInt(hourMin[1]);
        int hoursInMins = hour * 60;
        return hoursInMins + mins;
    }

    private int selectedWorkOrders(){
        return selectedItems.size();
    }

    private void upDateDB(WorkOrder workOrder,float time){
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String userName = email.substring(0,email.lastIndexOf("@"));
        String date = QuickDataFetcher.getServerDate();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(saleOrder.getSaleOrderNumber()).child("workOrders");
        DatabaseReference workOrderRef= dbRef.child(String.valueOf(saleOrder.getWorkOrders().indexOf(workOrder)));
        DatabaseReference operatorRef = workOrderRef.child("prodName");
        DatabaseReference timeRef = workOrderRef.child("prodTime");
        DatabaseReference dateRef = workOrderRef.child("prodDate");
        timeRef.setValue(""+time);
        dateRef.setValue(date);
        operatorRef.setValue(userName);
        modifyWorkOrder(workOrder,"" + time,date,userName);
    }
    public void modifyWorkOrder(WorkOrder wo,String time,String date,String userName){
        wo.setProdDate(date);
        wo.setProdName(userName);
        wo.setProdTime(time);
    }
}
