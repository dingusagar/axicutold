package com.example.dingu.axicut.Design;

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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import static com.example.dingu.axicut.R.id.saleOrder;

/**
 * Created by root on 20/5/17.
 */

public class DesignAdapter extends RecyclerView.Adapter<DesignAdapter.ViewHolder> implements Filterable {

    private ArrayList<SaleOrder> filteredSaleOrderList;
    private ArrayList<SaleOrder> saleOrderList=new ArrayList<>();
    private DatabaseReference myDBRef;
    private Context context;
    public DesignAdapter(Context context) {
        this.filteredSaleOrderList = saleOrderList;
        myDBRef = MyDatabase.getDatabase().getInstance().getReference("Orders");
        myDBRef.keepSynced(true);
        this.context=context;
        addDatabaseListeners();
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
            return new DesignAdapter.ViewHolder(view);

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
    public void addDatabaseListeners() {
        myDBRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {
                        SaleOrder saleOrder = dataSnapshot.getValue(SaleOrder.class);
                        saleOrderList.add(0,saleOrder);
                        notifyDataSetChanged();
                    } catch (Exception e) {
                        Toast.makeText(context, "Error : " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
