package com.example.dingu.axicut;

import java.io.Serializable;
import java.util.Date;

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
