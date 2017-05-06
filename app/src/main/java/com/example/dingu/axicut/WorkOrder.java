package com.example.dingu.axicut;

/**
 * Created by dingu on 6/5/17.
 */

public class WorkOrder {
    private int WorkOrderNumber;
    private String MaterialType;
    private String LotNumber;
    private float Thickness;
    private float Length;
    private float Breadth;
    private String InspectionRemark;

    public WorkOrder(int workOrderNumber, String materialType, String lotNumber, float thickness, float length, float breadth, String inspectionRemark) {
        WorkOrderNumber = workOrderNumber;
        MaterialType = materialType;
        LotNumber = lotNumber;
        Thickness = thickness;
        Length = length;
        Breadth = breadth;
        InspectionRemark = inspectionRemark;
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
        return ("\n[" + getWorkOrderNumber() + "   " + getLotNumber() + " " );
    }
}
