package com.example.dingu.axicut.Admin.Company;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;

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

public class CompanyAdapter extends RecyclerView.Adapter<CompanyViewHolder> implements Filterable{

    ArrayList<Company> filteredCompanyList;
    ArrayList<Company> companyList;

    DatabaseReference dbRef = MyDatabase.getDatabase().getInstance().getReference().child("Company");
    DatabaseReference dbRefQuick = MyDatabase.getDatabase().getInstance().getReference().child("InwardUtilities").child("customerIDs");


    public CompanyAdapter() {
        dbRef.keepSynced(true);
        companyList = new ArrayList<>();
        filteredCompanyList = new ArrayList<>();
        fetchDataFromDatabase();
    }


    @Override
    public CompanyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_card_view,parent,false);

        return new CompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CompanyViewHolder holder, int position) {

        final Company company = filteredCompanyList.get(position);
        holder.setCompanyId(company.getCompanyId());
        holder.setCompanyName(company.getComapanyName());

        Button removeButton = (Button) holder.mView.findViewById(R.id.CompanyRemoveButton);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef.child(company.getCompanyId()).removeValue();
                dbRefQuick.child(company.getCompanyId()).removeValue();
            }
        });



    }

    @Override
    public int getItemCount() {
        return filteredCompanyList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchKey = constraint.toString();
                if (searchKey.isEmpty())
                {
                    filteredCompanyList = new ArrayList<>(companyList);
                }
                else {
                    ArrayList<Company> filterlist = new ArrayList<>();
                    for(Company company : companyList)
                    {
                        String concat = company.getComapanyName() +" " + company.getCompanyId();
                        concat = concat.toLowerCase();
                        if(concat.contains(searchKey.toLowerCase()))
                            filterlist.add(company);
                    }
                    filteredCompanyList = filterlist;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredCompanyList;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                filteredCompanyList = (ArrayList<Company>) results.values;
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
                        Company company = dataSnapshot.getValue(Company.class);
                        if(!companyList.contains(company))
                        {
                            companyList.add(0,company);
                            filteredCompanyList.add(0,company);
                            Log.e("App","filtered : " + filteredCompanyList.toString());
                            Log.e("App","actual : " + companyList.toString());

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
                    Company company = dataSnapshot.getValue(Company.class);

                    removeAndNotify(company);

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

    private void removeAndNotify(Company company) {

        String companyID = company.getCompanyId();

        for(int i=0; i<companyList.size();i++)
        {
            if(companyList.get(i).getCompanyId().equals(companyID))
            {
                companyList.remove(i);


            }
        }

        for(int i=0; i<filteredCompanyList.size();i++)
        {
            if(filteredCompanyList.get(i).getCompanyId().equals(companyID))
            {
                filteredCompanyList.remove(i);


            }
        }
        notifyDataSetChanged();

    }


}




// for editing
//
//    private void replaceAndNotify(Company updatedCompany) {
//
//        String companyID = updatedCompany.getCompanyId();
//
//        for(int i=0; i<materialList.size();i++)
//        {
//            if(materialList.get(i).getCompanyId().equals(companyID))
//            {
//                materialList.set(i,updatedCompany);
//                filteredMaterialList = materialList;
//                notifyDataSetChanged();
//                break;
//            }
//        }
//    }