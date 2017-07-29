package com.example.dingu.axicut.Admin.LotNumber;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.dingu.axicut.R;

/**
 * Created by root on 29/7/17.
 */

public class LotNumberViewHolder extends RecyclerView.ViewHolder {
    public View mView;
    private TextView lotNumber;

    public LotNumberViewHolder(View itemView) {
        super(itemView);
        mView=itemView;
        lotNumber = (TextView)mView.findViewById(R.id.lotNumName);
    }

    public void setLotNumberText(String id){
        lotNumber.setText(id);
    }
}