package com.example.dingu.axicut.Production;


import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.dingu.axicut.Utils.General.QuickDataFetcher;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;
import com.example.dingu.axicut.WorkOrder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProdTimer extends DialogFragment implements View.OnClickListener {

    private ImageButton startPauseButton;
    private ImageButton saveButton;
    private ImageButton closeButton;
    private ImageButton resetButton;
    private EditText timerValue;
    private boolean isRunning;
    private long startTime = 0L;
    private ProductionLayoutCommunicator communicator;
    private SaleOrder saleOrder;
    private int workOrderPos;
    private Handler customHandler = new Handler();

    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;

    public ProdTimer() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        communicator = (ProductionLayoutCommunicator)getArguments().get("Communicator");
        this.saleOrder=communicator.getSaleOrder();
        this.workOrderPos=communicator.getWorkOrderPos();
        return inflater.inflate(R.layout.fragment_prod_timer, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        startPauseButton=(ImageButton)getView().findViewById(R.id.startPauseButton);
        timerValue=(EditText) getView().findViewById(R.id.timertext);
        resetButton=(ImageButton)getView().findViewById(R.id.Reset);
        closeButton=(ImageButton)getView().findViewById(R.id.CloseButton);
        saveButton=(ImageButton)getView().findViewById(R.id.saveButton);
        resetButton.setOnClickListener(this);
        startPauseButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        closeButton.setOnClickListener(this);
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            timerValue.setText("" + mins + ":"
                    + String.format("%02d", secs) + ":"
                    + String.format("%03d", milliseconds));
            customHandler.postDelayed(this, 0);
        }

    };

    public void pauseTimer(){
        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
    }
    public void startTimer(){
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }
    public void resetTimer(){
        updatedTime=0L;
        timeSwapBuff=0L;
        timerValue.setText("00:00:00");
        customHandler.removeCallbacks(updateTimerThread);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startPauseButton:

                if(!isRunning){
                    startTimer();
                    startPauseButton.setImageResource(R.drawable.pause);
                    isRunning=true;
                }
                else {
                    pauseTimer();
                    startPauseButton.setImageResource(R.drawable.start);
                    isRunning=false;
                }
                break;

            case R.id.Reset:
                resetTimer();
                if(isRunning){
                    startPauseButton.setImageResource(R.drawable.start);
                    isRunning=false;
                }
                break;
            case R.id.saveButton:
                saveToDataBase(timerValue.getText().toString());
                dismiss();
                break;
            case R.id.CloseButton:
                dismiss();
                break;
        }
    }

    public void saveToDataBase(String time){
        String email=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String userName = email.substring(0,email.lastIndexOf("@"));
        String date = QuickDataFetcher.getServerDate();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(saleOrder.getSaleOrderNumber()).child("workOrders");
        DatabaseReference workOrderRef= dbRef.child(String.valueOf(workOrderPos));
        DatabaseReference operatorRef = workOrderRef.child("prodName");
        DatabaseReference timeRef = workOrderRef.child("prodTime");
        DatabaseReference dateRef = workOrderRef.child("prodDate");
        timeRef.setValue(time);
        dateRef.setValue(date);
        operatorRef.setValue(userName);
        modifyWorkOrder(time,date,userName);
        communicator.adapterNotify();
    }

    public void modifyWorkOrder(String time,String date,String userName){
        WorkOrder wo = saleOrder.getWorkOrders().get(workOrderPos);
        wo.setProdDate(date);
        wo.setProdName(userName);
        wo.setProdTime(time);
    }
}
