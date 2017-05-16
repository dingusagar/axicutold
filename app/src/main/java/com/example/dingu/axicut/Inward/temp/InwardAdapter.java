package com.example.dingu.axicut.Inward.temp;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.dingu.axicut.Inward.InwardAction;
import com.example.dingu.axicut.Inward.InwardAddEditSaleOrder;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by dingu on 17/5/17.
 */

public class InwardAdapter extends RecyclerView.Adapter<InwardAdapter.ViewHolder> implements Filterable {

    private ArrayList<SaleOrder> filteredSaleOrderList;
    private ArrayList<SaleOrder> saleOrderList;


    public InwardAdapter(ArrayList<SaleOrder> saleOrderList) {
        this.filteredSaleOrderList = saleOrderList;
        this.saleOrderList = saleOrderList;

    }

    @Override
    public InwardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inward_main_page_list_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SaleOrder saleOrder = filteredSaleOrderList.get(position);
        holder.saleOrderText.setText(saleOrder.getSaleOrderNumber());
        holder.numOfWorkOrders.setText("" + saleOrder.getWorkOrders().size());

        holder.mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),InwardAddEditSaleOrder.class);
                intent.putExtra("SaleOrder",saleOrder);
                intent.putExtra("InwardAction",InwardAction.EDIT_SALE_ORDER);
                v.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return filteredSaleOrderList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String searchKey = constraint.toString();
                if(searchKey.isEmpty())
                    filteredSaleOrderList = saleOrderList;
                else
                {
                    ArrayList<SaleOrder> filterlist = new ArrayList<>();
                    for(SaleOrder so : saleOrderList)
                    {
                        if(so.getSaleOrderNumber().contains(searchKey.toUpperCase()))
                            filterlist.add(so);
                    }
                    filteredSaleOrderList = filterlist;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredSaleOrderList;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                filteredSaleOrderList = (ArrayList<SaleOrder>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mview;
        TextView saleOrderText;
        TextView numOfWorkOrders;

        public ViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
            saleOrderText = (TextView)mview.findViewById(R.id.saleOrder);
            numOfWorkOrders = (TextView)mview.findViewById(R.id.numOfWO);


        }

    }
}
