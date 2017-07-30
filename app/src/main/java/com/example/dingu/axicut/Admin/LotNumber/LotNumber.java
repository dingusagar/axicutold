package com.example.dingu.axicut.Admin.LotNumber;

/**
 * Created by root on 29/7/17.
 */

public class LotNumber {
    private String lotNum;
    public LotNumber(int lotNum) {
        this.lotNum=String.valueOf(lotNum);
    }
    public LotNumber(){}
    public LotNumber(String lotNum){
        this.lotNum=lotNum;
    }

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }
}
