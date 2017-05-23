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

import com.example.dingu.axicut.Design.DesignAdapter;
import com.example.dingu.axicut.Design.DesignWorkOrder;
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
 * Created by root on 23/5/17.
 */

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.ViewHolder> implements Filterable {

    private ArrayList<SaleOrder> filteredSaleOrderList;
    private ArrayList<SaleOrder> saleOrderList=new ArrayList<>();
    private DatabaseReference myDBRef;
    private Context context;
    public AdminAdapter(Context context) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_main_card_view,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SaleOrder saleOrder = filteredSaleOrderList.get(position);
        holder.saleOrderText.setText(saleOrder.getSaleOrderNumber());
        holder.numOfWorkOrders.setText("" + saleOrder.getWorkOrders().size());
        holder.companyName.setText(saleOrder.getCustomerName());
        holder.numOfDesign.setText("" + saleOrder.getNumOfLayouts());
        holder.numProd.setText("" +saleOrder.getNumOfProduced());
        holder.numDespatched.setText("" +saleOrder.getNumOfDespatched());
        holder.numScraped.setText("" +saleOrder.getNumScrapped());
        holder.mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),AdminWorkOrder.class);
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
        TextView saleOrderText,numOfWorkOrders,companyName,numOfDesign,numProd,numDespatched,numScraped;

        public ViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
            saleOrderText = (TextView)mview.findViewById(R.id.adminSaleOrder);
            numOfWorkOrders = (TextView)mview.findViewById(R.id.numOfWO);
            companyName=(TextView)mview.findViewById(R.id.companyName);
            numOfDesign=(TextView)mview.findViewById(R.id.numOfDesigns);
            numProd=(TextView)mview.findViewById(R.id.numOfProd);
            numDespatched=(TextView)mview.findViewById(R.id.numOfDespatched);
            numScraped=(TextView)mview.findViewById(R.id.numOfScraps);
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

                SaleOrder saleOrder = dataSnapshot.getValue(SaleOrder.class);
                if(saleOrder != null)
                updateSaleOrderAndNotify(saleOrder);

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

    private void updateSaleOrderAndNotify(SaleOrder saleOrder) {
        for(int i =0 ;i<saleOrderList.size() ; i++)
        {
            SaleOrder so = saleOrderList.get(i);
            if(so.getSaleOrderNumber().equals(saleOrder.getSaleOrderNumber()))
            {
                saleOrderList.set(i,saleOrder);
            }
        }

        for(int i =0 ;i<filteredSaleOrderList.size() ; i++)
        {
            SaleOrder so = filteredSaleOrderList.get(i);
            if(so.getSaleOrderNumber().equals(saleOrder.getSaleOrderNumber()))
            {
                saleOrderList.set(i,saleOrder);
            }
        }

        notifyDataSetChanged();
    }
}
