package com.example.dingu.axicut.Design;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dingu.axicut.R;
import com.example.dingu.axicut.WorkOrder;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditDesignLayout extends DialogFragment {
    private ImageButton saveButton;
    private Button cancelButton;
    private ImageButton decrementButton;
    private ImageButton incrementButton;
    private TextView indicator;
    private DesignLayoutCommunicator communicator;
    private WorkOrder workOrder;
    public final static int cutOffset = 25;

    public EditDesignLayout() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        communicator = (DesignLayoutCommunicator) getArguments().get("Communicator");
        return inflater.inflate(R.layout.edit_design_layout_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        workOrder = communicator.getWorkOrder();
        saveButton = (ImageButton) getView().findViewById(R.id.workOrderSaveButton);
        cancelButton = (Button) getView().findViewById(R.id.CancelButton);
        decrementButton = (ImageButton) getView().findViewById(R.id.decrement);
        incrementButton = (ImageButton) getView().findViewById(R.id.increment);
        indicator = (TextView) getView().findViewById(R.id.CounterIndicator);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText designLayout = (EditText) getView().findViewById(R.id.designLayoutEditText);
                communicator.adapterNotify(designLayout.getText().toString());
                communicator.updateWorkOrderLayoutToDatabase(designLayout.getText().toString());
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
