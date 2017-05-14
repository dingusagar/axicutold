package com.example.dingu.axicut.Admin.Company;

import java.util.concurrent.ConcurrentMap;

/**
 * Created by root on 14/5/17.
 */

public class Company {
    private String comapanyName;
    private String companyId;

    public Company(){}
    public Company(String name,String ID){
        this.comapanyName=name;
        this.companyId=ID;
    }

    public String getComapanyName() {
        return comapanyName;
    }

    public void setComapanyName(String comapanyName) {
        this.comapanyName = comapanyName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
