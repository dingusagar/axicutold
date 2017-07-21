package com.example.dingu.axicut.Utils;

import com.example.dingu.axicut.WorkOrder;

import java.util.ArrayList;

/**
 * Created by root on 21/7/17.
 */

public interface Validator {
    ArrayList<WorkOrder> isValid(ArrayList<WorkOrder> workOrders);
    boolean isValid(WorkOrder workOrder);
}
