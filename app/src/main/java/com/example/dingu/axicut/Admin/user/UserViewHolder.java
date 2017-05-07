package com.example.dingu.axicut.Admin.user;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.dingu.axicut.R;
import com.example.dingu.axicut.UserMode;

/**
 * Created by grey-hat on 7/5/17.
 */

public class UserViewHolder extends RecyclerView.ViewHolder{
    public View mView;
    private TextView userName;
    private TextView userMode;
    private TextView userEmail;
    public UserViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        userName=(TextView)mView.findViewById(R.id.UserName);
        userMode=(TextView)mView.findViewById(R.id.UserMode);
        userEmail=(TextView)mView.findViewById(R.id.UserEmail);

    }
    public void setName(String name){
        userName.setText(name);
    }
    public void setMode(UserMode Mode){
        String modeText;
        switch (Mode){
            case ADMIN:
                modeText = "Administrator";
                break;
            case INWARD:
                modeText = "Inward";
                break;
            case DESPATCH:
                modeText="Despatch";
                break;
            default:
                modeText="Unknown";

        }
        userMode.setText(modeText);
    }

    public void setUserEmail(String email){
        userEmail.setText(email);
    }
}