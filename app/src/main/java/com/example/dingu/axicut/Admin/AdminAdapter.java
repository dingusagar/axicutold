package com.example.dingu.axicut.Admin;

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
import android.widget.Toast;

import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;
import com.example.dingu.axicut.Utils.General.MyDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by dingu on 17/5/17.
 */

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.ViewHolder> implements Filterable {

    private ArrayList<String> filteredSaleOrderList;
    private ArrayList<String> saleOrderList;
    private Context context;
    private DatabaseReference mydbRefOrders;
    SaleOrder saleOrder;

    public AdminAdapter(ArrayList<String> saleOrderList, Context context) {
        this.filteredSaleOrderList = saleOrderList;
        this.saleOrderList = saleOrderList;
        this.context = context;
        mydbRefOrders = MyDatabase.getDatabase().getReference().child("Orders");

    }

    @Override
    public AdminAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inward_main_page_list_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String saleOrderNo = filteredSaleOrderList.get(position);
        holder.saleOrderText.setText(saleOrderNo);



        holder.mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {



                mydbRefOrders.child(saleOrderNo).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists())
                            return;
                        saleOrder = dataSnapshot.getValue(SaleOrder.class);
                        Toast.makeText(v.getContext(),"Fetching data....",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(v.getContext(),AdminWorkOrder.class);
                        intent.putExtra("SaleOrder",saleOrder);
                        mydbRefOrders.child(saleOrderNo).removeEventListener(this);
                        v.getContext().startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });








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
        TextView numOfWorkOrders;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mview = itemView;

            saleOrderText = (TextView)mview.findViewById(R.id.saleOrderNum);
            numOfWorkOrders = (TextView)mview.findViewById(R.id.numOfWO);
            linearLayout = (LinearLayout) mview.findViewById(R.id.linear_layout);


        }

    }
}
