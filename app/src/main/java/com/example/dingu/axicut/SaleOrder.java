package com.example.dingu.axicut;

import java.util.ArrayList;

/**
 * Created by dingu on 6/5/17.
 */

public class SaleOrder {

    private String SaleOrderNumber;
    private String Date;
    private String Time;
    private String CustomerName;
    private String CustomerDCNumber;
    private  ArrayList<WorkOrder> workOrders;

    public SaleOrder(String saleOrderNumber, String date, String time, String customerName, String customerDCNumber) {
        SaleOrderNumber = saleOrderNumber;
        Date = date;
        Time = time;
        CustomerName = customerName;
        CustomerDCNumber = customerDCNumber;
        workOrders = new ArrayList<>();
    }
    public SaleOrder()
    {
        workOrders = new ArrayList<>();
    }



    public String getSaleOrderNumber() {
        return SaleOrderNumber;
    }

    public void setSaleOrderNumber(String saleOrderNumber) {
        SaleOrderNumber = saleOrderNumber;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerDCNumber() {
        return CustomerDCNumber;
    }

    public void setCustomerDCNumber(String customerDCNumber) {
        CustomerDCNumber = customerDCNumber;
    }

    public ArrayList<WorkOrder> getWorkOrders() {
        return workOrders;
    }

    public void setWorkOrders(ArrayList<WorkOrder> workOrders) {
        this.workOrders = workOrders;
    }






}
