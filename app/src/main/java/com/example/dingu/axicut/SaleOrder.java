package com.example.dingu.axicut;

import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dingu on 6/5/17.
 */

public class SaleOrder implements Serializable{

    private String saleOrderNumber;
    private String date;
    private String time;
    private String customerName;
    private String customerDCNumber;
    private  ArrayList<WorkOrder> workOrders;

    private final static int SALE_ORDER_NUM_LENGTH = 9;

    public SaleOrder(String saleOrderNumber, String date, String time, String customerName, String customerDCNumber) {
        this.saleOrderNumber = saleOrderNumber;
        this.date = date;
        this.time = time;
        this.customerName = customerName;
        this.customerDCNumber = customerDCNumber;
        workOrders = new ArrayList<>();
    }
    public SaleOrder()
    {
        workOrders = new ArrayList<>();

    }



    public String getSaleOrderNumber() {
        return saleOrderNumber;
    }

    public void setSaleOrderNumber(String saleOrderNumber) {
        this.saleOrderNumber = saleOrderNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerDCNumber() {
        return customerDCNumber;
    }

    public void setCustomerDCNumber(String customerDCNumber) {
        this.customerDCNumber = customerDCNumber;
    }

    public ArrayList<WorkOrder> getWorkOrders() {
        return workOrders;
    }

    public void setWorkOrders(ArrayList<WorkOrder> workOrders) {
        this.workOrders = workOrders;
    }



    public void invalidateSaleOrderNumber(Long serverTimeStamp , String lastSalerOrderNumber)
    {
        String newSONumber;
        Log.e("App"," Fun timeStamp : " + serverTimeStamp);
        SimpleDateFormat yearFormater = new SimpleDateFormat("yy");
        SimpleDateFormat monthFormater = new SimpleDateFormat("MM");

        String serverYear = yearFormater.format(new Date(serverTimeStamp));
        String serverMonth = monthFormater.format(new Date(serverTimeStamp));


        // date of last sale order
        String last_SO_Month = lastSalerOrderNumber.substring(4,6);
        String last_SO_Year = lastSalerOrderNumber.substring(2,4);

        if(serverMonth.equals(last_SO_Month) && serverYear.equals(last_SO_Year)) // last sale order was made today
        {
            String SO_lastSegment = lastSalerOrderNumber.substring(6,9);
            int newNumber = Integer.parseInt(SO_lastSegment)+1;
            newSONumber = "SO"+last_SO_Year+last_SO_Month + String.format("%03d",newNumber); // formatting so that 1 is expressed as 001
        }
        else
        {
            newSONumber = "SO"+serverYear+serverMonth + "000";
        }

        this.setSaleOrderNumber(newSONumber);


    }

    public boolean isValidSaleOrderNumber()
    {
        String saleorderNumber = getSaleOrderNumber();

        if(saleorderNumber.length() == SALE_ORDER_NUM_LENGTH && saleorderNumber.startsWith("SO"))
            return true;
        else
            return false;

    }






}
