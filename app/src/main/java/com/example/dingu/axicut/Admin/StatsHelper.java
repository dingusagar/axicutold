package com.example.dingu.axicut.Admin;

import com.example.dingu.axicut.SaleOrder;
import com.example.dingu.axicut.WorkOrder;

import java.util.ArrayList;

/**
 * Created by root on 31/7/17.
 */

public class StatsHelper implements StatsCalculator {
    private SaleOrder saleOrder;
    private int numOfSheets;
    private int numOfWorkOrders;
    private int layoutsAssigned,designsProduced,designsDespatched,scrapsDespatched;
    public StatsHelper(SaleOrder saleOrder){
        this.saleOrder=saleOrder;
        this.numOfWorkOrders=saleOrder.getWorkOrders().size();
        getStats();
    }

    @Override
    public String getSaleOrder() {
        return saleOrder.getSaleOrderNumber();
    }

    @Override
    public String getCompany() {
        return saleOrder.getCustomerID();
    }

    @Override
    public String getNumOfWorkOrders() {
        return String.valueOf(numOfWorkOrders);
    }

    @Override
    public String getlayoutsAssigned() {
        return String.valueOf(layoutsAssigned) + "/" + String.valueOf(numOfWorkOrders);
    }

    @Override
    public String getDesignsProduced() {
        return String.valueOf(designsProduced) + "/" + String.valueOf(numOfWorkOrders);
    }

    @Override
    public String getDesignsDespatched() {
        return String.valueOf(designsDespatched)+ "/" + String.valueOf(numOfWorkOrders);
    }

    @Override
    public String getScrapsDespatched() {
        return String.valueOf(scrapsDespatched)+ "/" + String.valueOf(numOfWorkOrders);
    }

    @Override
    public String getNumOfSheets() {
        return String.valueOf(numOfSheets);
    }

    private void getStats(){
        ArrayList<WorkOrder> workOrders = saleOrder.getWorkOrders();
        int sheetsCount=0,layoutsCount=0,designsProdCounts=0,despatchCount=0,scrapCount=0;
        for(WorkOrder workOrder : workOrders){
            if(!workOrder.getWorkOrderNumber().contains("."))
                sheetsCount++;
            if(workOrder.getLayoutName()!=null && !workOrder.getLayoutName().equals(""))
                layoutsCount++;
            if(workOrder.getProdName()!=null && !workOrder.getProdName().equals(""))
                designsProdCounts++;
            if(workOrder.getDespatchDC()!=null && !workOrder.getDespatchDC().equals(""))
                despatchCount++;
            if(workOrder.getScrapDC()!=null && !workOrder.getScrapDC().equals(""))
                scrapCount++;
        }
        this.numOfSheets=sheetsCount;
        this.layoutsAssigned=layoutsCount;
        this.designsProduced=designsProdCounts;
        this.designsDespatched=despatchCount;
        this.scrapsDespatched=scrapCount;
    }
}
