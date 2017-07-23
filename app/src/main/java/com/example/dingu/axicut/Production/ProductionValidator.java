package com.example.dingu.axicut.Production;

import com.example.dingu.axicut.Utils.Validator;
import com.example.dingu.axicut.WorkOrder;

import java.util.ArrayList;

/**
 * Created by root on 21/7/17.
 */

public class ProductionValidator implements Validator {
    @Override
    public ArrayList<WorkOrder> isValid(ArrayList<WorkOrder> workOrders) {
        ArrayList<WorkOrder> validSelections=new ArrayList<>();
        for(WorkOrder wo : workOrders)
            if(isValid(wo))
                validSelections.add(wo);
        return workOrders;
    }

    @Override
    public boolean isValid(WorkOrder workOrder) {
        if(workOrder.getLayoutName()!=null && !workOrder.getLayoutName().equals(""))
            if(workOrder.getDespatchDate()==null || workOrder.getDespatchDate().equals(""))
                return true;
        return false;
    }
}
