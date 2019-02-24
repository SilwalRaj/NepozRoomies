package com.example.myste.nepozupdate;


/*
 Author: Raj Silwal 11630172
 This activity is used for displaying Advertise detail
 user can also request for the inspection to the advertiser
 Advertiser can edit the advertise navigating to advertiseActivity from here
 and Advertiser can even delete the advertise
 can switch to signup and forget password activity
*/

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myste.nepozupdate.DAO.AdvertiseDAO;
import com.example.myste.nepozupdate.Model.Advertise;
import com.example.myste.nepozupdate.Model.CustomSwipeAdapter;
import com.example.myste.nepozupdate.Model.RequestUnit;
import com.example.myste.nepozupdate.Model.Session;
import com.example.myste.nepozupdate.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdvDetailActivity extends MenuActivity {

    TextView tVSuburb, tvAddress, tVDate, tVDesc, tvRequestStat;
    ImageButton iBtnFavourite;
    Button btnRequest,btnAdEdit, btnDelete;
    AlertDialog.Builder dialogBuilder;

    //ArrayList<byte[]> images = new ArrayList<byte[]>();
    List<String> imageUris;
    final List<Advertise> advertises = new ArrayList<>();
    Advertise advertise;
    AdvertiseDAO advertiseDAO;
    ViewPager viewPager;
    CustomSwipeAdapter adapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    Session session;
    String userId;
    String favAdvId="";
    User reqUser = new User();
    List<RequestUnit> requestUnits = new ArrayList<RequestUnit>();
    String dateRegex ="^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|" +
            "(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))" +
            "(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3" +
            "(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|" +
            "(?:(?:16|[2468][048]|[3579][26])00))))" +
            "$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|" +
            "(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv_detail);
        myRef = database.getReference("FlagAdvertise");
        advertiseDAO = new AdvertiseDAO();

        viewPager = (ViewPager)findViewById(R.id.viewPager);

        tVSuburb = (TextView) findViewById(R.id.tVSuburb);
        tvAddress = (TextView)findViewById(R.id.tVAddress);
        tVDate = (TextView) findViewById(R.id.tVDate);
        tVDesc = (TextView) findViewById(R.id.tVDesc);
        tvRequestStat= (TextView) findViewById(R.id.tvRequestStat);
        iBtnFavourite = (ImageButton) findViewById(R.id.iBtnFavorite);
        iBtnFavourite.setTag(android.R.drawable.btn_star_big_off);
        btnRequest = (Button) findViewById(R.id.btnRequest);
        btnAdEdit = (Button) findViewById(R.id.btnEditAdv);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        imageUris = new ArrayList<>();

        Intent i = getIntent();
        advertise= (Advertise) i.getSerializableExtra("adObject");

        session = new Session(getApplicationContext());

        userId = session.getuserId();
        //getAdvertiseById(advId);

        if(userId.equals(advertise.getUserId())){

            try {
                getImageLoaded();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            iBtnFavourite.setVisibility(View.INVISIBLE);
            btnRequest.setVisibility(View.INVISIBLE);
            btnAdEdit.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
        }
        else {
            getRequestStatus();

        }
        //getFavAdvertise();


    }

    private void getFavAdvertise(){
        ValueEventListener postListener = new ValueEventListener() {
            String post;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    if(dataSnapshot1.getKey().toString().equals(userId)) {
                        post = dataSnapshot1.getValue(String.class);
                        favAdvId=post;
                    }
                }

                try {
                    getImageLoaded();
                } catch (ParseException e) {
                    e.printStackTrace();
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
        myRef.addListenerForSingleValueEvent(postListener);
    }

    private void setTextView() throws ParseException {
        tVSuburb.setText("Suburb: " + advertise.getSuburb());
        tvAddress.setText("Address: " + (advertise.getUnitNo()==0?"":advertise.getUnitNo()+ "/" )+ advertise.getHouseNo() + " " +advertise.getStrtName() );
        tVDate.setText("Date: " + (advertise.getInspectDate()!=null? advertise.getInspectDate().toString(): "N/A"));
        tVDesc.setText(advertise.getDesc());
        if(!favAdvId.isEmpty()) {
            String[] favAdvs = favAdvId.split(":::");


            for (String favAdv : favAdvs) {

                if (favAdv.equals(advertise.getAdvID())) {
                    iBtnFavourite.setImageResource(android.R.drawable.btn_star_big_on);
                    iBtnFavourite.setTag(android.R.drawable.btn_star_big_on);
                }

            }
        }
        if(!advertise.getUserId().equals(userId)) {
            if (requestUnits.size() > 0) {
                for (RequestUnit rq : requestUnits) {
                    if (rq.getReqStatus() == 0) {
                        btnRequest.setText("Request");
                        tvRequestStat.setText("Request Status: N/A");

                    } else if (rq.getReqStatus() == 1) {
                        btnRequest.setText("Pending");
                        btnRequest.setEnabled(false);
                        tvRequestStat.setText("Request Status: Pending");
                        tvRequestStat.setTextColor(Color.parseColor("#F9E79F"));
                    } else {
                        btnRequest.setText("Accepted");
                        btnRequest.setEnabled(false);
                        tvRequestStat.setText("Request Status: Accepted");
                        tvRequestStat.setTextColor(Color.parseColor("#27AE60"));
                    }
                }

            }
        }
        else{
            if(advertise.getInspectDate()!=null && advertise.getInspectDate().matches(dateRegex) ){

                if(new SimpleDateFormat("dd/MM/yyyy").parse(advertise.getInspectDate()).compareTo( new Date())<=0) {
                    tvRequestStat.setText("Status: Expired");
                    tvRequestStat.setTextColor(Color.parseColor("#CB4335"));

                }
                else{
                    tvRequestStat.setText("Status: Active");
                    tvRequestStat.setTextColor(Color.parseColor("#27AE60"));
                }
            }
            else{
                tvRequestStat.setText("Status: N/A");
            }

        }


    }

    private void getRequestStatus(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    RequestUnit rqUnit= ds.getValue(RequestUnit.class);
                    if(rqUnit.getAdvId().equals(advertise.getAdvID()) && rqUnit.getReqFromUserId().equals(userId)){
                       requestUnits.add(rqUnit);
                    }
                }

                getFavAdvertise();
                //getAdvertiseDetail();



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        FirebaseDatabase.getInstance().getReference("Requests").child(advertise.getUserId()).addListenerForSingleValueEvent(valueEventListener);

    }
    private void getImageLoaded() throws ParseException {

        //advertise=advertises.get(0);

        if(advertise.getPics()!=null && !advertise.getPics().isEmpty()) {
            String [] imageUrls= advertise.getPics().split(":::");
            for(int i=0;i<imageUrls.length;i++) {
                imageUris.add(imageUrls[i]);
            }
        }
        else{
            String uri = "android.resource://com.example.myste.nepozupdate/drawable/noimage";
            imageUris.add(uri);

        }
        setTextView();
        adapter = new CustomSwipeAdapter(this,imageUris);
        viewPager.setAdapter(adapter);

    }


    public void AddFavorites(View view){
        if(Integer.parseInt(iBtnFavourite.getTag().toString()) == android.R.drawable.btn_star_big_off ) {
            iBtnFavourite.setImageResource(android.R.drawable.btn_star_big_on);
            iBtnFavourite.setTag(android.R.drawable.btn_star_big_on);
            favAdvId = favAdvId.isEmpty()? advertise.getAdvID() : favAdvId + ":::" +advertise.getAdvID();
        }
        else {
            iBtnFavourite.setImageResource(android.R.drawable.btn_star_big_off);
            iBtnFavourite.setTag(android.R.drawable.btn_star_big_off);
            favAdvId = favAdvId.replace(":::"+advertise.getAdvID(),"");
            favAdvId = favAdvId.replace(advertise.getAdvID()+":::","");
        }

        myRef.child(userId).setValue(favAdvId);
        //iBtnFavourite.setImageResource(android.R.drawable.btn_star_big_on);

    }

    public void RequestForUnit(View view){


        ValueEventListener postListener = new ValueEventListener() {
            String post;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                    //if(dataSnapshot1.getKey().toString().equals(userId)) {
                        reqUser = dataSnapshot1.getValue(User.class);
                        if(reqUser.getUserId().equals(userId)){
                            sendRequest(reqUser.getFirstName()+ " " +reqUser.getLastName(), reqUser.getProfImage());
                        }

                    //}
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
        database.getReference("User").addValueEventListener(postListener);

    }

    public void sendRequest(final String userName, final String profImage){
        RequestUnit tmpReqUnit;
        //final
        String unitNo = advertise.getUnitNo()>0? String.valueOf(advertise.getUnitNo()) +"-": "";
        String compAddress = unitNo + advertise.getHouseNo()+" "+ advertise.getStrtName()+" "+advertise.getSuburb();
        int reqStatus;
        if(btnRequest.getText().toString().toLowerCase().equals("request")){
            reqStatus = 1;
            btnRequest.setText("Pending");
            btnRequest.setEnabled(false);
        }
        else{
            reqStatus =0;
        }


            RequestUnit rqUnit = new RequestUnit(userId, userName
                    , reqUser.getPhoneNo(), advertise.getAdvID(),
                    compAddress, advertise.getUserId(), reqStatus, profImage);

            advertiseDAO.RequestUnit(rqUnit);

    }

    public void editInfo(View view){
        Intent intent = new Intent(getApplicationContext(), AdvertiseActivity.class);
        intent.putExtra("advertise", advertise);
        startActivity(intent);
    }

    public void deleteAdvertise (View view){
        /*FirebaseDatabase.getInstance().getReference("Advertise").child(advertise.getAdvID()).removeValue();
        Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
        startActivity(intent);*/

        dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Delete Advertise?");
        dialogBuilder.setMessage("Are you sure you want to delete this advertise?");
        dialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseDatabase.getInstance().getReference("Advertise").child(advertise.getAdvID()).removeValue();
                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(intent);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialogDelete = dialogBuilder.create();
        dialogDelete.show();
    }

}

