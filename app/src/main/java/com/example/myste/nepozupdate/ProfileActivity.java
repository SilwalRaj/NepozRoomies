package com.example.myste.nepozupdate;


/*
 Author: Raj Silwal 11630172
 This activity is used to display the profile
 of the user. it will display the info of the user
 and also the list of the advertise posted by the user
*/

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myste.nepozupdate.Model.Advertise;
import com.example.myste.nepozupdate.Model.RecyclerViewAdapter;
import com.example.myste.nepozupdate.Model.Session;
import com.example.myste.nepozupdate.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends MenuActivity {

    FirebaseDatabase firebaseDatabase;
    User user = new User();
    Session session;
    String userId;
    RecyclerView recyclerView;
    private List<Advertise> advertises = new ArrayList<Advertise>();

    Button btnEditInfo;
    TextView tvName2, tvEmail, tvLogin,tvAddress, tvPhone;
    ImageView ivUserPic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName2 =(TextView)findViewById(R.id.tvName);
        tvEmail =(TextView)findViewById(R.id.tvEmail);
        tvLogin =(TextView)findViewById(R.id.tvLogin);
        tvPhone =(TextView)findViewById(R.id.tvPhoneNo);
        tvAddress =(TextView)findViewById(R.id.tvAddress);
        ivUserPic = (ImageView)findViewById(R.id.ivProfile);
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewUser);
        btnEditInfo = (Button) findViewById(R.id.btnEdit);
        firebaseDatabase = FirebaseDatabase.getInstance();
        session = new Session(getApplicationContext());
        userId=session.getuserId();



        getUser();
    }

    public void getUser(){
        ValueEventListener postListener = new ValueEventListener() {
            String post;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    if(dataSnapshot1.getKey().toString().equals(userId)) {
                    user = dataSnapshot1.getValue(User.class);


                    }
                }

                if(user.getUserId().equals(userId)){
                    setTextView();
                }

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        firebaseDatabase.getReference("User").addValueEventListener(postListener);

    }

    private void setTextView() {
        tvName2.setText(user.getFirstName() + " " +user.getLastName());
        tvAddress.setText(user.getAddress());
        tvLogin.setText(user.getLogInId());
        tvEmail.setText(user.getEmail());
        tvPhone.setText(user.getPhoneNo());
        if(user.getProfImage()!=null){
            if(!user.getProfImage().isEmpty()) {
                Picasso.get().load(user.getProfImage()).into(ivUserPic);
            }
        }
        else{
            ivUserPic.setImageResource(R.drawable.noimage);
        }

        PopulateAvertiseRecyclerView();
    }

    private void PopulateAvertiseRecyclerView(){

        firebaseDatabase.getReference("Advertise").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //list = new ArrayList<>();
                // StringBuffer stringbuffer = new StringBuffer();
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                    Advertise advertise = dataSnapshot1.getValue(Advertise.class);
                    if(advertise.getUserId().equals(userId)) {
                        advertises.add(advertise);
                    }

                    // Toast.makeText(MainActivity.this,""+name,Toast.LENGTH_LONG).show();

                }

                CallRecyclerView(advertises);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //  Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void CallRecyclerView(List<Advertise> advList){
        RecyclerViewAdapter recycler = new RecyclerViewAdapter(advList,getApplicationContext());
        RecyclerView.LayoutManager layoutmanager = new LinearLayoutManager(ProfileActivity.this);
        recyclerView.setLayoutManager(layoutmanager);
        recyclerView.setItemAnimator( new DefaultItemAnimator());
        recyclerView.setAdapter(recycler);
    }

    public void EditBtnClick(View view){
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}
