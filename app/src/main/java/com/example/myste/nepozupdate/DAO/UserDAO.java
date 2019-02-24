package com.example.myste.nepozupdate.DAO;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.myste.nepozupdate.HomeActivity;
import com.example.myste.nepozupdate.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class UserDAO {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;// = database.getReference("Users");

    public UserDAO(){
        myRef = database.getReference("User");
    }

    public String userEntry(User user){


        String id = myRef.push().getKey();
        user.setUserId(id);
        myRef.child(id).setValue(user);
        return  id;
    }

    public void editUser(User user){

        myRef.child(user.getUserId()).setValue(user);
    }

    public boolean logInCheck(final String loginId, final String password, final Context context){
        //final boolean temp;

        return false;
    }

    public int checkEmailidExist(final String email){
        final ArrayList<User> users = new ArrayList<User>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User logIn = ds.getValue(User.class);
                    if(logIn.getEmail().equals(email)){
                        users.add(logIn);
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       return users.size();
    }


}

