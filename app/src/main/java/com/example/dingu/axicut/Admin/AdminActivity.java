package com.example.dingu.axicut.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.dingu.axicut.Design.DesignAdapter;
import com.example.dingu.axicut.Design.DesignMainActivity;
import com.example.dingu.axicut.Inward.SaleOrderNumsFetcher;
import com.example.dingu.axicut.LoginActivity;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.Utils.General.MyDatabase;
import com.example.dingu.axicut.Utils.General.SaleOrderDisplayLimitter;
import com.example.dingu.axicut.Utils.Navigation.NavigationOptions;
import com.example.dingu.axicut.Utils.Navigation.Projector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity implements SaleOrderNumsFetcher,NavigationView.OnNavigationItemSelectedListener {
    private TextView headerText;
    private TextView headerId;
    AdminAdapter adminAdapter;
    RecyclerView saleOrderRecyclerView;
    FirebaseAuth mAuth;
    ArrayList<String> saleOrderNums;
    SaleOrderDisplayLimitter saleOrderDisplayLimiter;
    private DatabaseReference myDBRefSaleOrderNums;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navView = navigationView.getHeaderView(0);
        headerText = (TextView)navView.findViewById(R.id.HeaderText);
        headerText.setText(getIntent().getStringExtra("name"));
        headerId=(TextView)navView.findViewById(R.id.headerEmailId);
        headerId.setText(getIntent().getStringExtra("id"));
        mAuth = FirebaseAuth.getInstance();
        saleOrderRecyclerView = (RecyclerView)findViewById(R.id.AdminRecyclerList);
        saleOrderRecyclerView.setHasFixedSize(true);
        saleOrderRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        saleOrderNums = new ArrayList<>();
        adminAdapter = new AdminAdapter(saleOrderNums,this);
        saleOrderRecyclerView.setAdapter(adminAdapter);
        int limit = saleOrderDisplayLimiter.getLimitNumber();
        fetchSaleOrderNumbersFromDatabase(0L,null,limit);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin, menu);

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
                adminAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent;
        intent = new Intent(this,Projector.class);
        switch (id){
            case R.id.nav_users:
                intent.putExtra("Adapter", NavigationOptions.USER);
                startActivity(intent);
                break;
            case R.id.nav_company:
                intent.putExtra("Adapter", NavigationOptions.COMPANY);
                startActivity(intent);
                break;
            case R.id.nav_materials:
                intent.putExtra("Adapter", NavigationOptions.MATERIALS);
                startActivity(intent);
                break;
            case R.id.nav_LotNumbers:
                intent.putExtra("Adapter", NavigationOptions.LOTNUM);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void fetchSaleOrderNumbersFromDatabase(Long startTS, Long endTS, Integer limit) {
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
                    adminAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
