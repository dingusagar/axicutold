package com.example.dingu.axicut.Admin.user;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.dingu.axicut.Admin.AdminAddUser;
import com.example.dingu.axicut.Admin.PlusClickerAdapter;
import com.example.dingu.axicut.Admin.PlusClickerAdapter;
import com.example.dingu.axicut.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by grey-hat on 7/5/17.
 */

public class UserAdapter implements PlusClickerAdapter {
    private DatabaseReference databaseRef= FirebaseDatabase.getInstance().getReference().child("Users");
    private  FirebaseRecyclerAdapter<User,UserViewHolder> userAdapter;
    public FirebaseRecyclerAdapter<User,UserViewHolder> getAdapter(){
        userAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(User.class, R.layout.user_card_view,UserViewHolder.class,databaseRef) {
            @Override
            protected void populateViewHolder(final UserViewHolder viewHolder, User model, int position) {
                Button removeButton = (Button)viewHolder.mView.findViewById(R.id.UserRemoveButton);
                viewHolder.setName(model.getName());
                viewHolder.setMode(model.getUserMode());
                viewHolder.setUserEmail(model.getEmail());
                removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getRef(viewHolder.getAdapterPosition()).removeValue();
                    }
                });
            }

        };

        return userAdapter;
    }

    public View.OnClickListener onPlusClicked(){
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context,AdminAddUser.class);
                context.startActivity(intent);
            }
        };
        return clickListener;
    }

}