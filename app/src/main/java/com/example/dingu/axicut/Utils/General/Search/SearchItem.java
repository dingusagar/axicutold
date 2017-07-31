package com.example.dingu.axicut.Utils.General.Search;

import java.util.ArrayList;

/**
 * Created by root on 29/7/17.
 */

public class SearchItem {
    private String saleOrderNum;
    private ArrayList<String> workOrderNumbers;

    public String getSaleOrderNum() {
        return saleOrderNum;
    }

    public void setSaleOrderNum(String saleOrderNum) {
        this.saleOrderNum = saleOrderNum;
    }

    public ArrayList<String> getWorkOrderNumbers() {
        return workOrderNumbers;
    }

    public void setWorkOrderNumbers(ArrayList<String> workOrderNumbers) {
        this.workOrderNumbers = workOrderNumbers;
    }
}
