package com.example.dingu.axicut.Admin;

import com.example.dingu.axicut.Admin.user.UserAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

/**
 * Created by grey-hat on 7/5/17.
 */

public abstract class AdapterFactory {
    public static FirebaseRecyclerAdapter getAdapter(NavigationOptions option){
    switch (option){
        case USER:
            return UserAdapter.getAdapter();
    }

        return null;
    }
}
