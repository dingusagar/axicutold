package com.example.dingu.axicut.Production;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dingu.axicut.Design.DesignAdapter;
import com.example.dingu.axicut.Design.DesignWorkOrder;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;

import java.util.ArrayList;

import static com.example.dingu.axicut.R.id.saleOrder;

/**
 * Created by root on 22/5/17.
 */
public class ProductionAdapter extends RecyclerView.Adapter<ProductionAdapter.ViewHolder> implements Filterable {

    private ArrayList<SaleOrder> filteredSaleOrderList;
    private ArrayList<SaleOrder> saleOrderList;

    public ProductionAdapter(ArrayList<SaleOrder> saleOrderList) {
        this.filteredSaleOrderList = saleOrderList;
        this.saleOrderList = saleOrderList;

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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inward_main_page_list_item,parent,false);
        return new ProductionAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SaleOrder saleOrder = filteredSaleOrderList.get(position);
        holder.saleOrderText.setText(saleOrder.getSaleOrderNumber());
        holder.numOfWorkOrders.setText("" + saleOrder.getWorkOrders().size());
        holder.mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),DesignWorkOrder.class);
                intent.putExtra("SaleOrder",saleOrder);
                v.getContext().startActivity(intent);
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
        TextView numOfWorkOrders;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
            saleOrderText = (TextView)mview.findViewById(saleOrder);
            numOfWorkOrders = (TextView)mview.findViewById(R.id.numOfWO);
            linearLayout = (LinearLayout) mview.findViewById(R.id.linear_layout);
        }
    }
}
