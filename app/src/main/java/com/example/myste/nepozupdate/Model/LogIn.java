package com.example.myste.nepozupdate.Model;

public class LogIn {


    String _logInId;
    String _password;
    int _userId;

    public LogIn(){

    }

    public LogIn(String logInId, String password , int userid) {
        this._logInId = logInId;
        this._password = password;
        this._userId = userid;
    }

    public String get_logInId() {
        return _logInId;
    }

    public void set_logInId(String _logInId) {
        this._logInId = _logInId;
    }

    public String get_password() {
        return _password;
    }

    public void set_password(String _password) {
        this._password = _password;
    }

    public int get_userId() {
        return _userId;
    }

    public void set_userId(int _userId) {
        this._userId = _userId;
    }


}
