package com.example.dingu.axicut;

import java.io.Serializable;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by dingu on 6/5/17.
 */

public class WorkOrder implements Serializable{
    private int WorkOrderNumber;
    private String MaterialType;
    private String LotNumber;
    private float Thickness;
    private float Length;
    private float Breadth;
    private String InspectionRemark;
    private Date layoutDate;
    private String layoutName;
    private Date prodDate;
    private String prodName;
    private String prodTime;



    public String getProdTime() {
        return prodTime;
    }

    public void setProdTime(String prodTime) {
        this.prodTime = prodTime;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public Date getLayoutDate() {
        return layoutDate;
    }

    public void setLayoutDate(Date layoutDate) {
        this.layoutDate = layoutDate;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }
    public WorkOrder(){}
  
    public String getDespatchDC() {
        return despatchDC;
    }

    public void setDespatchDC(String despatchDC) {
        this.despatchDC = despatchDC;
    }

    public String getDespatchDate() {
        return despatchDate;
    }

    public void setDespatchDate(String despatchDate) {
        this.despatchDate = despatchDate;
    }

    public String getScrapDC() {
        return scrapDC;
    }

    public void setScrapDC(String scrapDC) {
        this.scrapDC = scrapDC;
    }

    public String getScrapDate() {
        return scrapDate;
    }

    public void setScrapDate(String scrapDate) {
        this.scrapDate = scrapDate;
    }

    private String despatchDC = "";
    private String despatchDate = "";

    private String scrapDC = "";
    private String scrapDate = "";

    public WorkOrder(int workOrderNumber, String materialType, String lotNumber, float thickness, float length, float breadth, String inspectionRemark) {
        WorkOrderNumber = workOrderNumber;
        MaterialType = materialType;
        LotNumber = lotNumber;
        Thickness = thickness;
        Length = length;
        Breadth = breadth;
        InspectionRemark = inspectionRemark;

    }

    public int getWorkOrderNumber() {
        return WorkOrderNumber;
    }

    public void setWorkOrderNumber(int workOrderNumber) {
        WorkOrderNumber = workOrderNumber;
    }

    public String getMaterialType() {
        return MaterialType;
    }

    public void setMaterialType(String materialType) {
        MaterialType = materialType;
    }

    public String getLotNumber() {
        return LotNumber;
    }

    public void setLotNumber(String lotNumber) {
        LotNumber = lotNumber;
    }

    public float getThickness() {
        return Thickness;
    }

    public void setThickness(float thickness) {
        Thickness = thickness;
    }

    public float getLength() {
        return Length;
    }

    public void setLength(float length) {
        Length = length;
    }

    public float getBreadth() {
        return Breadth;
    }

    public void setBreadth(float breadth) {
        Breadth = breadth;
    }

    public String getInspectionRemark() {
        return InspectionRemark;
    }

    public void setInspectionRemark(String inspectionRemark) {
        InspectionRemark = inspectionRemark;
    }

    @Override
    public String toString() // for debugging
    {
        return ("\n[" + getWorkOrderNumber() + "   " + getLotNumber() + "  ]" );
    }
}
