package com.example.dingu.axicut.Utils.General;

import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by dingu on 29/4/17.
 *
 * This is used to get effects while the user clicks on the button.
 * The first argument of setColorFilter() is the color of the button when it is pressed.
 */

public class ButtonAnimator {
    public static void buttonEffect(View button){
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xff888888, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
    }
}
