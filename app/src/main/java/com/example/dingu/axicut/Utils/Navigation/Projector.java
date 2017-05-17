package com.example.dingu.axicut.Utils.Navigation;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.dingu.axicut.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

public class Projector extends AppCompatActivity {

    protected ProgressDialog progressDialog;
    protected RecyclerView recyclerView;
    protected FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private CustomAdapterHolder customAdapterHolder;
    NavigationOptions option;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proj);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        option=(NavigationOptions)getIntent().getSerializableExtra("Adapter");
        progressDialog = new ProgressDialog(this);
        recyclerView = (RecyclerView)findViewById(R.id.projRecyclList);
        customAdapterHolder = AdapterFactory.getPlusClickerAdapter(option);
        firebaseRecyclerAdapter= customAdapterHolder.getAdapter();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(customAdapterHolder.onPlusClicked());
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(this.firebaseRecyclerAdapter);
        progressDialog.dismiss();
    }

}
