package com.example.dingu.axicut.Admin.Materials;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.dingu.axicut.R;

/**
 * Created by root on 15/5/17.
 */

public class MaterialViewHolder extends RecyclerView.ViewHolder {
    public View mView;
    private TextView materialdesc;
    private TextView materialId;
    public MaterialViewHolder(View itemView) {
        super(itemView);
        mView=itemView;
        materialdesc=(TextView)itemView.findViewById(R.id.MaterialName);
        materialId=(TextView)itemView.findViewById(R.id.MaterialId);
    }

    public void setName(String name){
        materialdesc.setText(name);
    }
    public void setId(String id){
        materialId.setText(id);
    }
}
