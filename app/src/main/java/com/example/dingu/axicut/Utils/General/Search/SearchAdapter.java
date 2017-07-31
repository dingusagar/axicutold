package com.example.dingu.axicut.Utils.General.Search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;
import com.example.dingu.axicut.Utils.General.MyDatabase;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{

    private ArrayList<SearchItem> filteredSaleOrderList;
    private ArrayList<SearchItem> saleOrderList;
    private Context context;
    private DatabaseReference mydbRefOrders;
    SaleOrder saleOrder;

    public SearchAdapter(ArrayList<SearchItem> saleOrderList, Context context) {
        this.filteredSaleOrderList = saleOrderList;
        this.saleOrderList = saleOrderList;
        this.context = context;
        mydbRefOrders = MyDatabase.getDatabase().getReference().child("Orders");

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inward_main_page_list_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SearchItem item  = filteredSaleOrderList.get(position);
        holder.saleOrderText.setText(item.getSaleOrderNum());
        holder.mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilteredWorkOrdersDialog dialog = new FilteredWorkOrdersDialog(context,item.getWorkOrderNumbers());
                dialog.setupDialog();
                dialog.showDialog();
            }
        });


    }

    @Override
    public int getItemCount() {
        return filteredSaleOrderList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        View mview;
        TextView saleOrderText;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mview = itemView;

            saleOrderText = (TextView)mview.findViewById(R.id.saleOrderNum);
            linearLayout = (LinearLayout) mview.findViewById(R.id.linear_layout);


        }

    }
}
