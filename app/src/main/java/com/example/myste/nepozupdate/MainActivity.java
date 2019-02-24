package com.example.myste.nepozupdate;

/*
 Author: Raj Silwal 11630172
 This activity is used for login purpose
 can switch to signup and forget password activity
*/

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myste.nepozupdate.DAO.UserDAO;
import com.example.myste.nepozupdate.Model.LogIn;
import com.example.myste.nepozupdate.Model.Session;
import com.example.myste.nepozupdate.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.InetAddress;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    EditText etUserId;
    EditText etPassword;
    //TextView tvMSG;
    //UserDAO userDAO;
    //LogInDAO logInDAO;
    Session session;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;// = database.getReference("Users");

    ArrayList<User> loggedInUser = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //tvMSG=(TextView)findViewById(R.id.tvMsg) ;
        etUserId = (EditText)findViewById(R.id.etUserId);
        etPassword = (EditText)findViewById(R.id.etPassword);
        myRef = database.getReference("User");
        session = new Session(getApplicationContext());

    }


    public void clickLogInbtn(View view){

        loggedInUser.clear();

        final String loginId = etUserId.getText().toString();
        final String password = etPassword.getText().toString();
        //LogIn logIn = new LogIn(loginId,password,0);
        //UserDAO userDAO = new UserDAO();
        if(loginId.length()== 0){
            etUserId.requestFocus();
            etUserId.setError("LoginId is required");
        }
        else if(password.length() == 0){
            etPassword.requestFocus();
            etPassword.setError("Password is required");
        }
        else {

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    int count = 0;

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            User logIn = ds.getValue(User.class);

                            if (logIn.getLogInId().equals(loginId)) {
                                    count = 1;
                                if (logIn.getPassword().equals(password)) {
                                    loggedInUser.add(logIn);
                                }
                                else {
                                    count = -1;
                                }
                            }

                        }

                        if (loggedInUser.size() == 1) {
                            session.setuserId(loggedInUser.get(0).getUserId());
                            Toast.makeText(getApplicationContext(), "Logged in Succesfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                        }
                        if (count < 0) {
                            Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                        }
                        else if(count==0){
                            Toast.makeText(getApplicationContext(), "User not Registered", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        }


    }


    public void clickSignUp(View view){
        Intent intent = new Intent(this,SignupActivity.class);
        startActivity(intent);
    }

    public void forgotPassWord(View view){
        Intent intent = new Intent(this,ForgotPassActivity.class);
        startActivity(intent);
    }
}
