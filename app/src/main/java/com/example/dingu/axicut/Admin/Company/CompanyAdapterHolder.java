package com.example.dingu.axicut.Admin.Company;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;

import android.widget.Filter;
import android.widget.Filterable;


import com.example.dingu.axicut.Utils.Navigation.CustomAdapterHolder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by root on 14/5/17.
 */

public class CompanyAdapterHolder implements CustomAdapterHolder {
    private DatabaseReference databaseRef= FirebaseDatabase.getInstance().getReference().child("Company");
    private static RecyclerView.Adapter companyAdapter;
    public CompanyAdapterHolder(){
        if(companyAdapter==null)
            companyAdapter=new CompanyAdapter();
    }
    @Override
    public View.OnClickListener onPlusClicked() {
        View.OnClickListener fabClicked = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context,AdminAddCompany.class);
                context.startActivity(intent);
            }
        };
        return fabClicked;
    }

    @Override

    public RecyclerView.Adapter getAdapter() {

        return companyAdapter;
    }

    @Override
    public Filter getFilter() {
        return ((Filterable)companyAdapter).getFilter();
    }
}
