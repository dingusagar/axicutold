package com.example.dingu.axicut.Inward;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dingu.axicut.Inward.Despatch.DespatchScrapActivity;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;

import java.util.ArrayList;

import static com.example.dingu.axicut.R.id.saleOrder;

/**
 * Created by dingu on 17/5/17.
 */

public class InwardAdapter extends RecyclerView.Adapter<InwardAdapter.ViewHolder> implements Filterable {

    private ArrayList<SaleOrder> filteredSaleOrderList;
    private ArrayList<SaleOrder> saleOrderList;
    private Context context;

    public InwardAdapter(ArrayList<SaleOrder> saleOrderList, Context context) {
        this.filteredSaleOrderList = saleOrderList;
        this.saleOrderList = saleOrderList;
        this.context = context;

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

                if(((InwardMainActivity)context).MenuItemId == R.id.inward_entry)
                {
                    Intent intent = new Intent(v.getContext(),InwardAddEditSaleOrder.class);
                    intent.putExtra("SaleOrder",saleOrder);
                    intent.putExtra("InwardAction",InwardAction.EDIT_SALE_ORDER);
                    v.getContext().startActivity(intent);
                }else if(((InwardMainActivity)context).MenuItemId == R.id.despatch_entry)
                {
                    Intent intent = new Intent(v.getContext(),DespatchScrapActivity.class);
                    intent.putExtra("SaleOrder",saleOrder);
                    v.getContext().startActivity(intent);
                }

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
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mview = itemView;

            saleOrderText = (TextView)mview.findViewById(R.id.saleOrder);
            numOfWorkOrders = (TextView)mview.findViewById(R.id.numOfWO);
            linearLayout = (LinearLayout) mview.findViewById(R.id.linear_layout);


        }

    }
}
