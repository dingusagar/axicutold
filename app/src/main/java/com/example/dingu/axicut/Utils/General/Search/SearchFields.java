package com.example.dingu.axicut.Utils.General.Search;

import java.io.Serializable;

/**
 * Created by dingu on 29/7/17.
 */

public class SearchFields implements Serializable{
    Float thickness;
    String materialType;
    String custID;
    Long fromTimeStamp;
    Long toTimeStamp;
    int limitNumber;

    public Float getThickness() {
        return thickness;
    }

    public void setThickness(Float thickness) {
        if(thickness == null)
            this.thickness = null;
        this.thickness = thickness;
    }

    public String getMaterialType() {
        return materialType;
    }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getCustID() {
        return custID;
    }

    public void setCustID(String custID) {
        this.custID = custID;
    }

    public Long getFromTimeStamp() {
        return fromTimeStamp;
    }

    public void setFromTimeStamp(Long fromTimeStamp) {
        this.fromTimeStamp = fromTimeStamp;
    }

    public Long getToTimeStamp() {
        return toTimeStamp;
    }

    public void setToTimeStamp(Long toTimeStamp) {
        this.toTimeStamp = toTimeStamp;
    }

    public int getLimitNumber() {
        return limitNumber;
    }

    public void setLimitNumber(int limitNumber) {
        this.limitNumber = limitNumber;
    }
}
