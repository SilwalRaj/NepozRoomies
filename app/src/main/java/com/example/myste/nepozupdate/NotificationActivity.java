package com.example.myste.nepozupdate;


/*
 Author: Raj Silwal 11630172
 This activity lists out the requests sent by other users
 and the user can reject or accept the request by clicking
 the accept/reject button in listitem
*/

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.myste.nepozupdate.Model.Advertise;
import com.example.myste.nepozupdate.Model.RecyclerViewAdapter;
import com.example.myste.nepozupdate.Model.RequestUnit;
import com.example.myste.nepozupdate.Model.RequestViewAdapter;
import com.example.myste.nepozupdate.Model.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends MenuActivity {;

    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private String userid;
    private Session session;
    private RecyclerView recyclerview;
    private List<RequestUnit> requestUnitList = new ArrayList<RequestUnit>();
    private TextView tvNotif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        myRef= firebaseDatabase.getReference("Requests");

        session = new Session(getApplicationContext());
        userid = session.getuserId();
        recyclerview = (RecyclerView)findViewById(R.id.reqRecyclerView);
        tvNotif =(TextView)findViewById(R.id.tvNotif);

        //loadNotification();
        PopulateNotifRecyclerView();

    }


    private void PopulateNotifRecyclerView(){

        myRef.child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String msg="";
                //list = new ArrayList<>();
                // StringBuffer stringbuffer = new StringBuffer();
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                    RequestUnit rqUnit = dataSnapshot1.getValue(RequestUnit.class);
                    if(rqUnit.getReqStatus()==1) {
                        requestUnitList.add(rqUnit);
                    }

                    // Toast.makeText(MainActivity.this,""+name,Toast.LENGTH_LONG).show();

                }

                if(requestUnitList.size()==0){
                    msg="No request for your Advertise";
                }
                else if(requestUnitList.size()==1){
                    msg="1 request for your Advertise";
                }
                else if(requestUnitList.size()>1){
                    msg=requestUnitList.size() +" requests for your Advertise";
                }
                tvNotif.setText(msg);
                RequestViewAdapter recycler = new RequestViewAdapter(requestUnitList,getApplicationContext());
                RecyclerView.LayoutManager layoutmanager = new LinearLayoutManager(NotificationActivity.this);
                recyclerview.setLayoutManager(layoutmanager);
                recyclerview.setItemAnimator( new DefaultItemAnimator());
                recyclerview.setAdapter(recycler);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //  Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

}
