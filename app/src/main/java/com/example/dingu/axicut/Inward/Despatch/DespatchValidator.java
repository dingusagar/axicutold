package com.example.dingu.axicut.Inward.Despatch;

import com.example.dingu.axicut.Utils.Validator;
import com.example.dingu.axicut.WorkOrder;

import java.util.ArrayList;

/**
 * Created by dingu on 23/7/17.
 */

public class DespatchValidator implements Validator {

    @Override
    public ArrayList<WorkOrder> isValid(ArrayList<WorkOrder> workOrders) {
        ArrayList<WorkOrder> validSelections=new ArrayList<>();
        for(WorkOrder wo : workOrders)
            if(isValid(wo))
                validSelections.add(wo);
        return validSelections;
    }

    @Override
    public boolean isValid(WorkOrder workOrder) {
        return workOrder.getProdName() != null && !workOrder.getProdName().equals("");
    }
}
