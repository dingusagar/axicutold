package com.example.dingu.axicut.Production;


import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dingu.axicut.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProdTimer extends DialogFragment {

    private ImageButton startPauseButton;
    private ImageButton saveButton;
    private EditText timerValue;
    private boolean isRunning;
    private long startTime = 0L;

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
        return inflater.inflate(R.layout.fragment_prod_timer, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        startPauseButton=(ImageButton)getView().findViewById(R.id.startPauseButton);
        timerValue=(EditText) getView().findViewById(R.id.timertext);
        startPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
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
}
