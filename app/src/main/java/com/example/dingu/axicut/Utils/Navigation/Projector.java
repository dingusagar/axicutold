package com.example.dingu.axicut.Utils.Navigation;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Filterable;

import com.example.dingu.axicut.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

public class Projector extends AppCompatActivity {

    protected ProgressDialog progressDialog;
    protected RecyclerView recyclerView;
    protected RecyclerView.Adapter recyclerAdapter;
    private CustomAdapterHolder customAdapterHolder;
    private String actionBarText;
    private Toolbar toolbar;
    NavigationOptions option;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proj);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        option=(NavigationOptions)getIntent().getSerializableExtra("Adapter");
        progressDialog = new ProgressDialog(this);
        recyclerView = (RecyclerView)findViewById(R.id.projRecyclList);
        customAdapterHolder = AdapterFactory.getPlusClickerAdapter(option);
        recyclerAdapter= customAdapterHolder.getAdapter();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(customAdapterHolder.onPlusClicked());
        actionBarText=customAdapterHolder.getActionBarText();
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(this.recyclerAdapter);
        setTitle(actionBarText);
        progressDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.projector_menu, menu);

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

                customAdapterHolder.getFilter().filter(newText);
                return true;
            }
        });
    }


}
