package com.example.myste.nepozupdate.Model;

import java.io.Serializable;

public class User implements Serializable{
    String userId;
    String firstName;
    String lastName;
    String email;
    String phoneNo;
    String address;
    String profImage;
    String logInId;
    String password;



    public User(){}


    public User( String fName, String lName, String email, String phnNo,String profImage,String logInId,String password){
       // this.userId = userId;
        this.firstName = fName;
        this.lastName = lName;
        this.email = email;
        this.phoneNo = phnNo;
        this.address = "Campsie";
        this.logInId = logInId;
        this.password = password;
        this.profImage = profImage;
    }
    public User( String userId, String fName, String lName, String email, String phnNo,String profImage,String logInId,String password){
        this.userId = userId;
        this.firstName = fName;
        this.lastName = lName;
        this.email = email;
        this.phoneNo = phnNo;
        this.address = "Campsie";
        this.logInId = logInId;
        this.password = password;
        this.profImage = profImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfImage() {
        return profImage;
    }

    public void setProfImage(String profImage) {
        this.profImage = profImage;
    }

    public String getLogInId() {
        return logInId;
    }

    public void setLogInId(String logInId) {
        this.logInId = logInId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
