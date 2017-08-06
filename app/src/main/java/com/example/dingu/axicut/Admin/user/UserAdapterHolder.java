package com.example.dingu.axicut.Admin.user;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.dingu.axicut.Admin.Materials.MaterialAdapter;
import com.example.dingu.axicut.Utils.Navigation.CustomAdapterHolder;
import com.example.dingu.axicut.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by grey-hat on 7/5/17.
 */

public class UserAdapterHolder implements CustomAdapterHolder{
    private DatabaseReference databaseRef= FirebaseDatabase.getInstance().getReference().child("Users");
    private static UserAdapter userAdapter;
    public UserAdapterHolder(){
        if(userAdapter==null)
            userAdapter=new UserAdapter();
    }
    @Override
    public View.OnClickListener onPlusClicked() {
        View.OnClickListener fabClicked = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context,AdminAddUser.class);
                context.startActivity(intent);
            }
        };
        return fabClicked;
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        return userAdapter;
    }

    @Override
    public Filter getFilter() {
        return userAdapter.getFilter();
    }

    @Override
    public String getActionBarText() {
        return "Users";
    }
}
