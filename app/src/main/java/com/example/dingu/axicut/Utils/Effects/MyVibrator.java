package com.example.dingu.axicut.Utils.Effects;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by dingu on 14/8/17.
 */

public class MyVibrator {
    Vibrator vibrator;
    int VIBRATE_DURATION = 100;

    public MyVibrator(Context context) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void vibrate()
    {
        vibrator.vibrate(VIBRATE_DURATION);
    }

    public void setDuration(int duration)
    {
        VIBRATE_DURATION = duration;
    }
}
