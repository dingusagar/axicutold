package com.example.dingu.axicut.Utils.Navigation;

import com.example.dingu.axicut.Admin.Company.CompanyAdapterHolder;
import com.example.dingu.axicut.Admin.user.UserAdapterHolder;

/**
 * Created by grey-hat on 7/5/17.
 */

public abstract class AdapterFactory {
    public static CustomAdapterHolder getPlusClickerAdapter(NavigationOptions option){
    switch (option){
        case USER:
            return new UserAdapterHolder();
        case COMPANY:
            return new CompanyAdapterHolder();
    }
        return null;
    }


}
