package com.example.dingu.axicut.Utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * Created by dingu on 20/7/17.
 */

public class ErrorMessage {
    Context context;
    View parentView;

    public ErrorMessage(Context context) {
        this.context = context;
    }

    public ErrorMessage(View parentView) {
        this.parentView = parentView;
    }
    public ErrorMessage(Context context,View parentView) {
        this.context = context;
        this.parentView = parentView;
    }

    public void displayToast(String errorMessage)
    {
        Toast.makeText(context,errorMessage,Toast.LENGTH_LONG).show();
    }
    public void displayToast(String errorMessage,int duration)
    {
        Toast.makeText(context,errorMessage,duration).show();
    }

    public void displaySnackbar(String errorMessage , int duration)
    {
        Snackbar.make(parentView, errorMessage,duration).show();
    }
}
