package com.example.dingu.axicut.Design;

import com.example.dingu.axicut.Utils.Validator;
import com.example.dingu.axicut.WorkOrder;

import java.util.ArrayList;

/**
 * Created by root on 23/7/17.
 */

public class DesignValidator implements Validator {
    @Override
    public ArrayList<WorkOrder> isValid(ArrayList<WorkOrder> workOrders) {
        ArrayList<WorkOrder> validSelections = new ArrayList<>();
        for(WorkOrder workOrder : workOrders){
            if(isValid(workOrder))
                validSelections.add(workOrder);
        }
        return validSelections;
    }

    @Override
    public boolean isValid(WorkOrder workOrder) {
        return workOrder.getProdName() == null || workOrder.getProdName().equals("");
    }
}
