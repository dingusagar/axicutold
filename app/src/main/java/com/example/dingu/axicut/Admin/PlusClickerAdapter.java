package com.example.dingu.axicut.Admin;

import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

/**
 * Created by grey-hat on 8/5/17.
 */
public interface PlusClickerAdapter {

    View.OnClickListener onPlusClicked();
    FirebaseRecyclerAdapter getAdapter();
}
