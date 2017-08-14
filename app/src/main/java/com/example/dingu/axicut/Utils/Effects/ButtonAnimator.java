package com.example.dingu.axicut.Utils.Effects;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by dingu on 29/4/17.
 *
 * This is used to get effects while the user clicks on the button.
 * The first argument of setColorFilter() is the color of the button when it is pressed.
 */

public class ButtonAnimator {

    private static void setGreyButtonEffect(View view){
        view.setOnTouchListener(new View.OnTouchListener() {

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

    private static void setReverseBackgroundForeGroundEffect( View view){

        final Button button = (Button)view ;
        ColorStateList mList = button.getTextColors();
        final int textColor = mList.getDefaultColor();
        final int backgroundColor;

        Drawable background = button.getBackground();
        if (background instanceof ColorDrawable) {
            backgroundColor = ((ColorDrawable)background).getColor();

            button.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            button.getBackground().setColorFilter(textColor, PorterDuff.Mode.SRC_ATOP);
                            button.setTextColor(backgroundColor);
                            button.invalidate();

                            break;
                        }
                        case MotionEvent.ACTION_UP: {
                            button.getBackground().clearColorFilter();
                            button.setTextColor(textColor);
                            button.invalidate();
                            break;
                        }
                    }
                    return false;
                }
            });
        }



    }


    public static void setEffect(View button, Effects effect)
    {
        switch (effect)
        {
            case SIMPLE_ON_TOUCH_GREY:
                setGreyButtonEffect(button);
                break;
            case REVERSE_BACKGROUND_FOREGROUND:
                setReverseBackgroundForeGroundEffect(button);
        }
    }

    public  enum Effects {
        SIMPLE_ON_TOUCH_GREY , REVERSE_BACKGROUND_FOREGROUND
    }

}
