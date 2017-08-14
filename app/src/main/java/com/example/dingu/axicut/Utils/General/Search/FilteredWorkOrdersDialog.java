package com.example.dingu.axicut.Utils.General.Search;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.dingu.axicut.Inward.MyCustomDialog;
import com.example.dingu.axicut.R;

import java.util.ArrayList;

/**
 * Created by dingu on 20/7/17.
 */

public class FilteredWorkOrdersDialog implements MyCustomDialog {
    private Context context;
    private String title = "Work Orders";
    private int layout = R.layout.filtered_work_orders_list;

    LayoutInflater inflater;
    AlertDialog.Builder builder;
    View contentView;

    ArrayList<String> workOrderNums;
    ListView workorderListView;
//    DatabaseReference dbRef = MyDatabase.getDatabase().getReference().child("Orders");



    public FilteredWorkOrdersDialog(Context context, ArrayList<String> workOrderNums) {
        this.context = context;
        this.workOrderNums = workOrderNums;



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

        workorderListView = (ListView) contentView.findViewById(R.id.workOrderListView);
        workorderListView.setAdapter(new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,workOrderNums.toArray()));

        builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setView(contentView);



    }


    @Override
    public void onPositiveButtonClicked() {

    }

    @Override
    public void onNegativeButtonClicked() {

    }




}

