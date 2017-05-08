package com.example.dingu.axicut.Admin;

import android.view.View;

import com.example.dingu.axicut.Admin.user.UserAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

/**
 * Created by grey-hat on 7/5/17.
 */

public abstract class AdapterFactory {
    public static PlusClickerAdapter getPlusClickerAdapter(NavigationOptions option){
    switch (option){
        case USER:
            return new UserAdapter();
    }
        return null;
    }


}
