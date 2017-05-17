package com.example.dingu.axicut.Admin.Materials;

/**
 * Created by root on 14/5/17.
 */

public class Material {
    public Material(){}
    public Material(String desc,String id){
        this.desc=desc;
        this.id=id;
    }
    private String desc;
    private String id;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
