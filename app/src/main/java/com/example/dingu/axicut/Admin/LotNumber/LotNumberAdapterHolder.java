package com.example.dingu.axicut.Admin.LotNumber;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.dingu.axicut.Admin.Materials.AdminAddMaterials;
import com.example.dingu.axicut.Admin.Materials.MaterialAdapter;
import com.example.dingu.axicut.Utils.Navigation.CustomAdapterHolder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by root on 29/7/17.
 */


public class LotNumberAdapterHolder implements CustomAdapterHolder {
    private DatabaseReference databaseRef= FirebaseDatabase.getInstance().getReference().child("LotNumbers");
    private static LotNumberAdapter lotNumberAdapter;
    public LotNumberAdapterHolder(){
        if(lotNumberAdapter==null)
            lotNumberAdapter=new LotNumberAdapter();
    }
    @Override
    public View.OnClickListener onPlusClicked() {
        View.OnClickListener fabClicked = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context,AddLotNum.class);
                context.startActivity(intent);
            }
        };
        return fabClicked;
    }

    @Override

    public RecyclerView.Adapter getAdapter() {

        return lotNumberAdapter;
    }

    @Override
    public Filter getFilter() {
        return ((Filterable)lotNumberAdapter).getFilter();
    }

    @Override
    public String getActionBarText() {
        return "Lot Numbers";
    }
}
