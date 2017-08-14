package com.example.dingu.axicut.Admin.Materials;

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

public class MaterialAdapter extends RecyclerView.Adapter<MaterialViewHolder> implements Filterable{

    ArrayList<Material> filteredMaterialList;
    ArrayList<Material> materialList;

    DatabaseReference dbRef = MyDatabase.getDatabase().getInstance().getReference().child("Material");
    DatabaseReference dbRefQuick = MyDatabase.getDatabase().getInstance().getReference().child("InwardUtilities").child("materialTypes");


    public MaterialAdapter() {
        dbRef.keepSynced(true);
        materialList = new ArrayList<>();
        filteredMaterialList = new ArrayList<>();
        fetchDataFromDatabase();
    }


    @Override
    public MaterialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_card_view,parent,false);

        return new MaterialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MaterialViewHolder holder, int position) {

        final Material material = filteredMaterialList.get(position);
        holder.setId(material.getId());
        holder.setName(material.getDesc());

        ImageButton removeButton = (ImageButton) holder.mView.findViewById(R.id.MaterialRemoveButton);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef.child(material.getId()).removeValue();
                dbRefQuick.child(material.getId()).removeValue();
            }
        });



    }

    @Override
    public int getItemCount() {
        return filteredMaterialList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchKey = constraint.toString();
                if (searchKey.isEmpty())
                {
                    filteredMaterialList = new ArrayList<>(materialList);
                }
                else {
                    ArrayList<Material> filterlist = new ArrayList<>();
                    for(Material material : materialList)
                    {
                        String concat = material.getId() +" " + material.getDesc();
                        concat = concat.toLowerCase();
                        if(concat.contains(searchKey.toLowerCase()))
                            filterlist.add(material);
                    }
                    filteredMaterialList = filterlist;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredMaterialList;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                filteredMaterialList = (ArrayList<Material>) results.values;
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
                        Material material = dataSnapshot.getValue(Material.class);
                        if(!materialList.contains(material))
                        {
                            materialList.add(0,material);
                            filteredMaterialList.add(0,material);


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
                    Material material = dataSnapshot.getValue(Material.class);

                    removeAndNotify(material);

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

    private void removeAndNotify(Material material) {

        String materialID = material.getId();

        for(int i=0; i<materialList.size();i++)
        {
            if(materialList.get(i).getId().equals(materialID))
            {
                materialList.remove(i);


            }
        }

        for(int i=0; i<filteredMaterialList.size();i++)
        {
            if(filteredMaterialList.get(i).getId().equals(materialID))
            {
                filteredMaterialList.remove(i);


            }
        }
        notifyDataSetChanged();

    }


}




