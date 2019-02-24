package com.example.myste.nepozupdate.Model;

public class FlaggedAdvertise {


    int _flagAdID;
    int _userId;
    int _advertiseId;

    public FlaggedAdvertise(){

    }

    public FlaggedAdvertise(int _flagAdID) {
        this._flagAdID = _flagAdID;
    }

    public int get_flagAdID() {
        return _flagAdID;
    }

    public void set_flagAdID(int _flagAdID) {
        this._flagAdID = _flagAdID;
    }

    public int get_userId() {
        return _userId;
    }

    public void set_userId(int _userId) {
        this._userId = _userId;
    }

    public int get_advertiseId() {
        return _advertiseId;
    }

    public void set_advertiseId(int _advertiseId) {
        this._advertiseId = _advertiseId;
    }

}
