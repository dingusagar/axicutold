package com.example.dingu.axicut.Utils.General.Search;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by root on 29/7/17.
 */

public class SearchItem {
    private String saleOrder;
    private ArrayList<String> workOrderNumbers;

    public String getSaleOrder() {
        return saleOrder;
    }

    public void setSaleOrder(String saleOrder) {
        this.saleOrder = saleOrder;
    }

    public ArrayList<String> getWorkOrderNumbers() {
        return workOrderNumbers;
    }

    public void setWorkOrderNumbers(ArrayList<String> workOrderNumbers) {
        this.workOrderNumbers = workOrderNumbers;
    }
}
