package com.example.dingu.axicut.Inward;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.dingu.axicut.LoginActivity;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InwardMainActivity extends AppCompatActivity {

    private DatabaseReference myDBRef;
    RecyclerView saleOrderList;
    FirebaseAuth mAuth;
    SaleOrder saleOrderObj;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inward_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InwardMainActivity.this,InwardAddEditSaleOrder.class);
                intent.putExtra("InwardAction",InwardAction.CREATE_NEW_SALE_ORDER);
                startActivity(intent);
            }
        });

        myDBRef = FirebaseDatabase.getInstance().getReference("Orders");

        saleOrderList = (RecyclerView)findViewById(R.id.InwardRecyclerList);
        saleOrderList.setHasFixedSize(true);
        saleOrderList.setLayoutManager(new LinearLayoutManager(this));

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null)
                {
                    Intent intent = new Intent(InwardMainActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<SaleOrder,SaleOrderViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<SaleOrder, SaleOrderViewHolder>(
                SaleOrder.class,
                R.layout.inward_main_page_list_item,
                SaleOrderViewHolder.class,
                myDBRef
        ) {
            @Override
            protected void populateViewHolder(SaleOrderViewHolder viewHolder, final SaleOrder model, int position) {
                viewHolder.setSaleOrder(model.getSaleOrderNumber());
                viewHolder.setNumOfWorkOrders(model.getWorkOrders().size());

                viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(),InwardAddEditSaleOrder.class);
                        intent.putExtra("SaleOrder",model);
                        intent.putExtra("InwardAction",InwardAction.EDIT_SALE_ORDER);
                        startActivity(intent);
                    }
                });
            }
        };

        saleOrderList.setAdapter(firebaseRecyclerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.inward_entry_work_orders, menu);

        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.logout:
                mAuth.getInstance().signOut();


        }

        return super.onOptionsItemSelected(item);
    }
}
