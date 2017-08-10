package com.example.dingu.axicut.Utils.General.Search;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;
import com.example.dingu.axicut.Utils.General.MyDatabase;
import com.example.dingu.axicut.WorkOrder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SearchFields searchFields;
    private DatabaseReference myDBRefSaleOrderNums;
    DatabaseReference myDBRefOrders;
    ArrayList<String> saleOrderNums;
    ArrayList<SearchItem> searchResults;
    SearchAdapter searchAdapter;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        searchFields = (SearchFields) getIntent().getSerializableExtra("SearchFields");
        myDBRefSaleOrderNums = MyDatabase.getDatabase().getReference().child("SaleOrderNums");
        myDBRefOrders = MyDatabase.getDatabase().getReference().child("Orders");

        searchResults = new ArrayList<>();
        searchAdapter = new SearchAdapter(searchResults,this);
        saleOrderNums = new ArrayList<>();
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();


        fetchSaleOrderNumbersFromDatabase(searchFields.getFromTimeStamp(),searchFields.getToTimeStamp(),searchFields.getLimitNumber());




    }

    public void fetchSaleOrderNumbersFromDatabase(Long startTS, Long endTS, Integer limit) {
        Query query;
        if(endTS !=null)
            query= myDBRefSaleOrderNums.orderByChild("TS").startAt(startTS).endAt(endTS).limitToFirst(limit);
        else
            query= myDBRefSaleOrderNums.orderByChild("TS").startAt(startTS).limitToLast(limit);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()) {

                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren())
                    {
                        saleOrderNums.add(0,childSnapshot.getKey());
                    }
                    doSearching();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void doSearching() {


        for(String saleOrderNum : saleOrderNums)
        {
            myDBRefOrders.child(saleOrderNum).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    SaleOrder saleOrder =  dataSnapshot.getValue(SaleOrder.class);
                    String custID = saleOrder.getCustomerID();
                    ArrayList <WorkOrder> workOrders =saleOrder.getWorkOrders();
                    if(searchFields.getCustID().equals(custID) || searchFields.getCustID().equals(""))
                    {
                        SearchItem item = new SearchItem();
                        item.setSaleOrderNum(saleOrder.getSaleOrderNumber());
                        ArrayList<String> workOrderNums = new ArrayList<>();

                        for(WorkOrder wo : workOrders)
                        {
                            if(searchFields.getMaterialType().equals(wo.getMaterialType())  ||  searchFields.getMaterialType().equals(""))
                            {
                                if(searchFields.getThickness()==null || (getStringToDoublePrecision(searchFields.getThickness()).equals(getStringToDoublePrecision(wo.getThickness()))))
                                workOrderNums.add(wo.getWorkOrderNumber());
                            }
                        }
                        item.setWorkOrderNumbers(workOrderNums);

                        searchResults.add(item);
                        searchAdapter.notifyDataSetChanged();

                    }




                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
    String getStringToDoublePrecision(Float number){
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(number);
    }
}
