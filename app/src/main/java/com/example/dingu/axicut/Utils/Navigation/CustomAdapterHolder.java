package com.example.dingu.axicut.Utils.Navigation;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

/**
 * Created by grey-hat on 8/5/17.
 */
public interface CustomAdapterHolder {

    View.OnClickListener onPlusClicked();
    RecyclerView.Adapter getAdapter();
    Filter getFilter();
}
