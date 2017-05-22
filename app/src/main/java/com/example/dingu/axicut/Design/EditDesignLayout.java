package com.example.dingu.axicut.Design;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.dingu.axicut.Admin.Company.Company;
import com.example.dingu.axicut.R;
import com.example.dingu.axicut.SaleOrder;
import com.example.dingu.axicut.Utils.General.MyDatabase;
import com.example.dingu.axicut.WorkOrder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditDesignLayout extends DialogFragment {
    private Button saveButton;
    private SaleOrder saleOrder;
    private int workOrderPos;
    private DesignLayoutCommunicator communicator;
    public EditDesignLayout() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        communicator = (DesignLayoutCommunicator)getArguments().get("Communicator");
        this.saleOrder=communicator.getSaleOrder();
        this.workOrderPos=communicator.getWorkOrderPos();
        return inflater.inflate(R.layout.edit_design_layout_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        saveButton = (Button)getView().findViewById(R.id.workOrderSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText designLayout = (EditText)getView().findViewById(R.id.designLayoutEditText);
                updateWorkOrderLayoutToDatabase(designLayout.getText().toString());
                WorkOrder wo = saleOrder.getWorkOrders().get(workOrderPos);
                wo.setLayoutName(designLayout.getText().toString());
                communicator.adapterNotify(wo);
                dismiss();
            }
        });
    }
    public Date getDateFromServer(){
        final Date[] currentDate = {new Date()};
        DatabaseReference dbRefUtils;
        dbRefUtils = MyDatabase.getDatabase().getInstance().getReference().child("Utils");
        dbRefUtils.child("ServerTimeStamp").setValue(ServerValue.TIMESTAMP);
        dbRefUtils.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long timestamp = (Long) dataSnapshot.child("ServerTimeStamp").getValue();
                currentDate[0] = new Date(timestamp);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return currentDate[0];
    }

    public void updateWorkOrderLayoutToDatabase(String layout){
      DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(saleOrder.getSaleOrderNumber()).child("workOrders");
      DatabaseReference workOrderRef= dbRef.child(String.valueOf(workOrderPos)).child("layoutName");
        workOrderRef.setValue(layout);
    }
}
