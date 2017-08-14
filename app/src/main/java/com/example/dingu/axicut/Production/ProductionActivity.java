package com.example.dingu.axicut.Production;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.dingu.axicut.Utils.General.QuickDataFetcher;
import com.example.dingu.axicut.Inward.SaleOrderNumsFetcher;
import com.example.dingu.axicut.LoginActivity;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.Utils.General.MyDatabase;
import com.example.dingu.axicut.Utils.General.SaleOrderDisplayLimitter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductionActivity extends AppCompatActivity implements SaleOrderNumsFetcher{
    RecyclerView saleOrderRecyclerView;
    FirebaseAuth mAuth;
    ProductionAdapter productionAdapter;
    ArrayList<String> saleOrderNums;
    SaleOrderDisplayLimitter saleOrderDisplayLimiter;
    private DatabaseReference myDBRefSaleOrderNums;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_main);
        QuickDataFetcher.fetchServerTimeStamp();
        setTitle("Production");
        mAuth = FirebaseAuth.getInstance();
        saleOrderRecyclerView = (RecyclerView)findViewById(R.id.DesignRecyclerList);
        saleOrderRecyclerView.setHasFixedSize(true);
        saleOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(ProductionActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
        myDBRefSaleOrderNums = MyDatabase.getDatabase().getInstance().getReference("SaleOrderNums");
        myDBRefSaleOrderNums.keepSynced(true);

        saleOrderDisplayLimiter = new SaleOrderDisplayLimitter(this,getSupportFragmentManager(),this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        saleOrderNums = new ArrayList<>();
        productionAdapter = new ProductionAdapter(saleOrderNums,this);
        saleOrderRecyclerView.setAdapter(productionAdapter);
        int limit = saleOrderDisplayLimiter.getLimitNumber();
        fetchSaleOrderNumbersFromDatabase(0L,null,limit);
    }


    public void fetchSaleOrderNumbersFromDatabase(Long startTS,Long endTS,Integer limit) {
        saleOrderNums.clear();
        Query query;
        if(endTS !=null)
            query= myDBRefSaleOrderNums.orderByChild("TS").startAt(startTS).endAt(endTS).limitToFirst(limit);
        else
            query= myDBRefSaleOrderNums.orderByChild("TS").startAt(startTS).limitToLast(limit);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()) {

                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren())
                    {
                        saleOrderNums.add(0,childSnapshot.getKey());
                    }
                    productionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.design_main_menu, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return super.onCreateOptionsMenu(menu);
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                productionAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
            case R.id.limitSaleOrders:
                saleOrderDisplayLimiter.setupDialog();
                saleOrderDisplayLimiter.showDialog();


        }

        return super.onOptionsItemSelected(item);
    }


    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {

            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }

}
