package com.example.dingu.axicut.Admin;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.dingu.axicut.Admin.user.UserAdapter;
import com.example.dingu.axicut.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

public class Projector extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private NavigationOptions option;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projector);
        progressDialog = new ProgressDialog(this);
        recyclerView = (RecyclerView)findViewById(R.id.projRecyclList);
        firebaseRecyclerAdapter= UserAdapter.getAdapter();
        option=(NavigationOptions) getIntent().getSerializableExtra("Adapter");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(AdapterFactory.getAdapter(option));
        progressDialog.dismiss();
    }
}
