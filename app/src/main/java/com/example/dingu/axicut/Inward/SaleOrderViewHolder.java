package com.example.dingu.axicut.Inward;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.dingu.axicut.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by dingu on 10/5/17.
 */

public  class SaleOrderViewHolder extends RecyclerView.ViewHolder{
    View mview;
    TextView saleOrderText;
    TextView numOfWorkOrders;
    DatabaseReference mDatabaseRef;

    public SaleOrderViewHolder(View itemView) {
        super(itemView);
        mview = itemView;
        saleOrderText = (TextView)mview.findViewById(R.id.saleOrder);
        numOfWorkOrders = (TextView)mview.findViewById(R.id.numOfWO);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Orders");
        mDatabaseRef.keepSynced(true);


    }

    public void setSaleOrder(String SO_Number)
    {
        saleOrderText = (TextView)mview.findViewById(R.id.saleOrder);
        saleOrderText.setText(SO_Number);
        Log.e("App","s n : "+saleOrderText.getText());
    }

    public void setNumOfWorkOrders(int num)
    {
        numOfWorkOrders.setText(""+num);
    }
}
