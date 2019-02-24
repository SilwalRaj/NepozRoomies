package com.example.myste.nepozupdate.Model;

import java.io.Serializable;

public class Advertise implements Serializable {

    String advID;
    int unitNo;
    int houseNo;
    String strtName;
    String suburb;
    String pics;
    String desc;
    String userId;
    String inspectDate;

    public Advertise() {
    }

    public Advertise(String advID, int unitNo, int houseNo, String strtName, String suburb, String pics, String desc, String date,String userId) {
        this.advID = advID;
        this.unitNo = unitNo;
        this.houseNo = houseNo;
        this.strtName = strtName;
        this.suburb = suburb;
        this.pics = pics;
        this.desc = desc;
        this.inspectDate = date;
        this.userId = userId;
    }

    public Advertise(int unitNo, int houseNo, String strtName, String suburb, String pics, String desc, String date, String userId) {
        this.advID = advID;
        this.unitNo = unitNo;
        this.houseNo = houseNo;
        this.strtName = strtName;
        this.suburb = suburb;
        this.pics = pics;
        this.desc = desc;
        this.inspectDate = date;
        this.userId = userId;
    }

    public String getInspectDate() {
        return inspectDate;
    }

    public void setInspectDate(String inspectDate) {
        this.inspectDate = inspectDate;
    }

    public String getAdvID() {
        return advID;
    }

    public void setAdvID(String advID) {
        this.advID = advID;
    }

    public int getUnitNo() {
        return unitNo;
    }

    public void setUnitNo(int unitNo) {
        this.unitNo = unitNo;
    }

    public int getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(int houseNo) {
        this.houseNo = houseNo;
    }

    public String getStrtName() {
        return strtName;
    }

    public void setStrtName(String strtName) {
        this.strtName = strtName;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
