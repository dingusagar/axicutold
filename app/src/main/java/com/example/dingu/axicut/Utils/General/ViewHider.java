package com.example.dingu.axicut.Utils.General;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.dingu.axicut.Design.DesignAdapter;

/**
 * Created by root on 22/7/17.
 */

public class ViewHider {
    public static void hideView(View view){
        RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)view.getLayoutParams();
        view.setVisibility(View.GONE);
        param.height = 0;
        param.width = 0;
        view.setLayoutParams(param);
    }
}
