package com.example.dingu.axicut.Admin.Company;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.dingu.axicut.Admin.user.AdminAddUser;
import com.example.dingu.axicut.Admin.user.User;
import com.example.dingu.axicut.Admin.user.UserViewHolder;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.Utils.Navigation.CustomAdapterHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by root on 14/5/17.
 */

public class CompanyAdapterHolder implements CustomAdapterHolder {
    private DatabaseReference databaseRef= FirebaseDatabase.getInstance().getReference().child("Company");
    private  FirebaseRecyclerAdapter<Company,CompanyViewHolder> companyAdapter;
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
    public FirebaseRecyclerAdapter getAdapter() {
        companyAdapter=new FirebaseRecyclerAdapter<Company, CompanyViewHolder>(Company.class, R.layout.company_card_view,CompanyViewHolder.class,databaseRef) {
            @Override
            protected void populateViewHolder(final CompanyViewHolder viewHolder, Company model, int position) {
                ImageButton removeButton = (ImageButton)viewHolder.mView.findViewById(R.id.CompanyRemoveButton);
                viewHolder.setCompanyName(model.getComapanyName());
                viewHolder.setCompanyId(model.getCompanyId());
                removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getRef(viewHolder.getAdapterPosition()).removeValue();
                        notifyDataSetChanged();
                    }
                });
            }
        };
        return companyAdapter;
    }
}
