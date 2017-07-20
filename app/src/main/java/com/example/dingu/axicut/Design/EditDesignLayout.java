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
import android.widget.ImageButton;

import com.example.dingu.axicut.Admin.Company.Company;
import com.example.dingu.axicut.Inward.InwardUtilities;
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
    private ImageButton saveButton;
    private Button cancelButton;

    private DesignLayoutCommunicator communicator;
    public EditDesignLayout() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        communicator = (DesignLayoutCommunicator)getArguments().get("Communicator");
        return inflater.inflate(R.layout.edit_design_layout_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        saveButton = (ImageButton)getView().findViewById(R.id.workOrderSaveButton);
        cancelButton = (Button)getView().findViewById(R.id.CancelButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText designLayout = (EditText)getView().findViewById(R.id.designLayoutEditText);
                communicator.updateWorkOrderLayoutToDatabase(designLayout.getText().toString());
                communicator.adapterNotify(designLayout.getText().toString());
                dismiss();
            }
        });
       cancelButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               dismiss();
           }
       });
    }

}
