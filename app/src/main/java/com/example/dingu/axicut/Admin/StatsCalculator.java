package com.example.dingu.axicut.Admin;

/**
 * Created by root on 31/7/17.
 */

public interface StatsCalculator {
    String getSaleOrder();
    String getCompany();
    String getNumOfWorkOrders();
    String getlayoutsAssigned();
    String getDesignsProduced();
    String getDesignsDespatched();
    String getScrapsDespatched();
    String getNumOfSheets();
}
