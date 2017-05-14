package com.example.dingu.axicut.Admin.Company;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.dingu.axicut.R;

import org.w3c.dom.Text;

/**
 * Created by root on 14/5/17.
 */

public class CompanyViewHolder extends RecyclerView.ViewHolder {
    public View mView;
    private TextView companyName;
    private TextView companyId;
    public CompanyViewHolder(View itemView) {
        super(itemView);
        mView=itemView;
        companyId=(TextView)itemView.findViewById(R.id.ComapanyId);
        companyName=(TextView)itemView.findViewById(R.id.CompanyName);
    }
    public void setCompanyName(String name){
        companyName.setText(name);
    }
    public void setCompanyId(String id){
        companyId.setText(id);
    }

}
