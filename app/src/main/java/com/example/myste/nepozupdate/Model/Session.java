package com.example.myste.nepozupdate.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {


    private SharedPreferences prefs;
    public static final String MyPREFERENCES = "MyPrefs" ;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setuserId(String _userID) {
        prefs.edit().putString("userId", _userID).apply();
    }

    public String getuserId() {
        String userID = prefs.getString("userId","");
        return userID;
    }

    public void clearSession(){
        prefs.edit().clear();
        prefs.edit().commit();

    }
}
