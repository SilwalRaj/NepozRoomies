package com.example.myste.nepozupdate.Mailing;

import android.content.Context;

public class MailSenderCred {
    String emailId , password="";
    int[] temp = {80,64,115,115,119,111,114,100,120,159};

    public MailSenderCred() {
        emailId="nepoz.roomies@gmail.com";

        for(int i =0; i <temp.length;i++){
            if(i<temp.length-2) {
                password = password + (char) temp[i];
            }
            else{
                int j =temp[i]%10;
                password = password + String.valueOf(j);
            }


        }

    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
