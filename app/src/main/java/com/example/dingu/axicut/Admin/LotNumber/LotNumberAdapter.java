package com.example.dingu.axicut.Admin.LotNumber;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;

import com.example.dingu.axicut.Admin.Materials.Material;
import com.example.dingu.axicut.Admin.Materials.MaterialViewHolder;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.Utils.General.MyDatabase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by dingu on 23/5/17.
 */

public class LotNumberAdapter extends RecyclerView.Adapter<LotNumberViewHolder> implements Filterable{

    ArrayList<LotNumber> filteredLotNumberList;
    ArrayList<LotNumber> lotNumberList;

    DatabaseReference dbRef = MyDatabase.getDatabase().getInstance().getReference().child("Lot Numbers");
    DatabaseReference dbRefQuick = MyDatabase.getDatabase().getInstance().getReference().child("InwardUtilities").child("lotNumberTypes");


    public LotNumberAdapter() {
        dbRef.keepSynced(true);
        lotNumberList = new ArrayList<>();
        filteredLotNumberList = new ArrayList<>();
        fetchDataFromDatabase();
    }


    @Override
    public LotNumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lot_num_card_view,parent,false);

        return new LotNumberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LotNumberViewHolder holder, int position) {

        final LotNumber lotNumber = filteredLotNumberList.get(position);
        holder.setLotNumberText(lotNumber.getLotNum());

        ImageButton removeButton = (ImageButton) holder.mView.findViewById(R.id.MaterialRemoveButton);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef.child(lotNumber.getLotNum()).removeValue();
                dbRefQuick.child(lotNumber.getLotNum()).removeValue();
            }
        });



    }

    @Override
    public int getItemCount() {
        return filteredLotNumberList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchKey = constraint.toString();
                if (searchKey.isEmpty())
                {
                    filteredLotNumberList = new ArrayList<>(lotNumberList);
                }
                else {
                    ArrayList<LotNumber> filterlist = new ArrayList<>();
                    for(LotNumber lotNumber : lotNumberList)
                    {
                        String concat = lotNumber.getLotNum();
                        concat = concat.toLowerCase();
                        if(concat.contains(searchKey.toLowerCase()))
                            filterlist.add(lotNumber);
                    }
                    filteredLotNumberList = filterlist;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredLotNumberList;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                filteredLotNumberList = (ArrayList<LotNumber>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void fetchDataFromDatabase()
    {
        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot != null && dataSnapshot.getValue() != null)
                {
                    try{
                        LotNumber lotNumber = dataSnapshot.getValue(LotNumber.class);
                        if(!lotNumberList.contains(lotNumber))
                        {
                            lotNumberList.add(0,lotNumber);
                            filteredLotNumberList.add(0,lotNumber);


                            notifyDataSetChanged();
                        }

                    }catch (Exception e)
                    {
//                        Toast.makeText(getApplicationContext(), "Error : " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null)
                {
                    LotNumber lotNumber = dataSnapshot.getValue(LotNumber.class);

                    removeAndNotify(lotNumber);

                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void removeAndNotify(LotNumber lotNumber) {

        String lotNumId = lotNumber.getLotNum();

        for(int i=0; i<lotNumberList.size();i++)
        {
            if(lotNumberList.get(i).getLotNum().equals(lotNumId))
            {
                lotNumberList.remove(i);


            }
        }

        for(int i=0; i<filteredLotNumberList.size();i++)
        {
            if(filteredLotNumberList.get(i).getLotNum().equals(lotNumId))
            {
                filteredLotNumberList.remove(i);


            }
        }
        notifyDataSetChanged();

    }


}


