package com.example.dingu.axicut.Admin.user;

import com.example.dingu.axicut.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by grey-hat on 7/5/17.
 */

public abstract class UserAdapter  {
    private static DatabaseReference databaseRef= FirebaseDatabase.getInstance().getReference().child("Users");
    private static FirebaseRecyclerAdapter<User,UserViewHolder> userAdapter;

    public static FirebaseRecyclerAdapter<User,UserViewHolder> getAdapter(){
        userAdapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(User.class, R.layout.user_card_view,UserViewHolder.class,databaseRef) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, User model, int position) {
                viewHolder.setName(model.getName());
                viewHolder.setMode(model.getUserMode());
                viewHolder.setUserEmail(model.getEmail());
            }
        };
        return userAdapter;
    }
}
