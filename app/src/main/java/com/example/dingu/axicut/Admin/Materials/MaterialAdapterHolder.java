package com.example.dingu.axicut.Admin.Materials;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.dingu.axicut.Admin.Company.AdminAddCompany;
import com.example.dingu.axicut.Admin.Company.CompanyAdapter;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.Utils.Navigation.CustomAdapterHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
/**
 * Created by root on 15/5/17.
 */

public class MaterialAdapterHolder implements CustomAdapterHolder  {
    private DatabaseReference databaseRef= FirebaseDatabase.getInstance().getReference().child("Material");
    private static MaterialAdapter materialAdapter;
    public MaterialAdapterHolder(){
        if(materialAdapter==null)
            materialAdapter=new MaterialAdapter();
    }
    @Override
    public View.OnClickListener onPlusClicked() {
        View.OnClickListener fabClicked = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context,AdminAddMaterials.class);
                context.startActivity(intent);
            }
        };
        return fabClicked;
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        return materialAdapter;
    }

    @Override
    public Filter getFilter() {
        return ((Filterable)materialAdapter).getFilter();
    }
}
