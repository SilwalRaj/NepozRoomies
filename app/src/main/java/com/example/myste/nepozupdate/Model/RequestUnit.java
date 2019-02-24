package com.example.myste.nepozupdate.Model;

public class RequestUnit {
    private String requestId;
    private String reqFromUserId;
    private String reqFromUserName;
    private String reqFrmUsrPhnNo;
    private String advId;
    private String completeAdress;
    private String reqToUserId;
    private String reqFrmImage;
    private int reqStatus;


    public RequestUnit(){

    }
    public RequestUnit(String reqFromUserId, String reqFromUserName, String reqFrmUsrPhnNo, String advId, String completeAdress,String reqToUserId, int reqStatus, String reqFrmImage) {
        //this.requestId = requestId;
        this.reqFromUserId = reqFromUserId;
        this.reqFromUserName = reqFromUserName;
        this.reqFrmUsrPhnNo = reqFrmUsrPhnNo;
        this.advId = advId;
        this.completeAdress = completeAdress;
        this.reqToUserId=reqToUserId;
        this.reqStatus = reqStatus;
        this.reqFrmImage = reqFrmImage;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getReqToUserId() {
        return reqToUserId;
    }

    public void setReqToUserId(String reqToUserId) {
        this.reqToUserId = reqToUserId;
    }

    public String getReqFromUserId() {
        return reqFromUserId;
    }

    public void setReqFromUserId(String reqFromUserId) {
        this.reqFromUserId = reqFromUserId;
    }

    public String getReqFromUserName() {
        return reqFromUserName;
    }

    public void setReqFromUserName(String reqFromUserName) {
        this.reqFromUserName = reqFromUserName;
    }

    public String getReqFrmUsrPhnNo() {
        return reqFrmUsrPhnNo;
    }

    public void setReqFrmUsrPhnNo(String reqFrmUsrPhnNo) {
        this.reqFrmUsrPhnNo = reqFrmUsrPhnNo;
    }

    public String getAdvId() {
        return advId;
    }

    public void setAdvId(String advId) {
        this.advId = advId;
    }

    public String getCompleteAdress() {
        return completeAdress;
    }

    public void setCompleteAdress(String completeAdress) {
        this.completeAdress = completeAdress;
    }

    public int getReqStatus() {
        return reqStatus;
    }

    public void setReqStatus(int reqStatus) {
        this.reqStatus = reqStatus;
    }

    public String getReqFrmImage() {
        return reqFrmImage;
    }

    public void setReqFrmImage(String reqFrmImage) {
        this.reqFrmImage = reqFrmImage;
    }
}
