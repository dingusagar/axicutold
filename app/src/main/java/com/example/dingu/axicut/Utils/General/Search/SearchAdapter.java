package com.example.dingu.axicut.Utils.General.Search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.dingu.axicut.Inward.Despatch.DespatchScrapActivity;
import com.example.dingu.axicut.Inward.InwardAction;
import com.example.dingu.axicut.Inward.InwardAdapter;
import com.example.dingu.axicut.Inward.InwardAddEditSaleOrder;
import com.example.dingu.axicut.Inward.InwardMainActivity;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;
import com.example.dingu.axicut.Utils.General.MyDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements Filterable {

    private ArrayList<String> filteredSaleOrderList;
    private ArrayList<String> saleOrderList;
    private Context context;
    private DatabaseReference mydbRefOrders;
    SaleOrder saleOrder;

    public SearchAdapter(ArrayList<String> saleOrderList, Context context) {
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
        final String saleOrderNo = filteredSaleOrderList.get(position);
        holder.saleOrderText.setText(saleOrderNo);


    }

    @Override
    public int getItemCount() {
        return filteredSaleOrderList.size();
    }

    @Override
    public android.widget.Filter getFilter() {
        return new android.widget.Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String searchKey = constraint.toString();
                if(searchKey.isEmpty())
                    filteredSaleOrderList = saleOrderList;
                else
                {
                    ArrayList<String> filterlist = new ArrayList<>();
                    for(String so : saleOrderList)
                    {
                        if(so.contains(searchKey.toUpperCase()))
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

                filteredSaleOrderList = (ArrayList<String>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mview;
        TextView saleOrderText;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mview = itemView;

            saleOrderText = (TextView)mview.findViewById(R.id.saleOrder);
            linearLayout = (LinearLayout) mview.findViewById(R.id.linear_layout);


        }

    }
}
