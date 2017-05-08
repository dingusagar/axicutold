package com.example.dingu.axicut.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.dingu.axicut.Admin.user.UserAdapter;
import com.example.dingu.axicut.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

public class Projector extends AppCompatActivity {

    protected ProgressDialog progressDialog;
    protected RecyclerView recyclerView;
    protected FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proj);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        progressDialog = new ProgressDialog(this);
        recyclerView = (RecyclerView)findViewById(R.id.projRecyclList);
        firebaseRecyclerAdapter=AdapterFactory.getAdapter(NavigationOptions.USER);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(UserAdapter.onPlusClicked());
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
