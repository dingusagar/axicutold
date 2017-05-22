package com.example.dingu.axicut.Design;

import com.example.dingu.axicut.SaleOrder;
import com.example.dingu.axicut.WorkOrder;

import java.io.Serializable;

/**
 * Created by root on 22/5/17.
 */

public interface DesignLayoutCommunicator extends Serializable {
     SaleOrder getSaleOrder();
     int getWorkOrderPos();
     void adapterNotify(WorkOrder workOrder);

}
