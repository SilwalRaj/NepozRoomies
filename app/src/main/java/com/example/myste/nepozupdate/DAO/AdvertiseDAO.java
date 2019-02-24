package com.example.myste.nepozupdate.DAO;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.myste.nepozupdate.HomeActivity;
import com.example.myste.nepozupdate.Model.Advertise;
import com.example.myste.nepozupdate.Model.RequestUnit;
import com.example.myste.nepozupdate.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class AdvertiseDAO {


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;// = database.getReference("Users");
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference mStorageRef;//=firebaseStorage.getReference();
    //mStorageRef = FirebaseStorage.getInstance().getReference();
    public AdvertiseDAO(){
        myRef = database.getReference("Advertise");
        mStorageRef = firebaseStorage.getReference();
    }

    public ArrayList<Advertise> getAdvertises(){
        //myRef = database.getReference("Advertise");
        final  ArrayList<Advertise> advertises = new ArrayList<Advertise>();


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren() ) {
                    Advertise advertise = ds.getValue(Advertise.class);
                    advertises.add(advertise);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return  advertises;
    }


    public Advertise getAdvertiseById(String advId){
        final List<Advertise> advertises = new ArrayList<>();
        myRef.child(advId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Advertise advertise = new Advertise();
                advertise = snapshot.getValue(Advertise.class);
                advertises.add(advertise);
                //prints "Do you have data? You'll love Firebase."
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        if(advertises.size()>0)
            return advertises.get(0);
        else
            return null;

    }

    public String AddAdvertise(Advertise advertise){

        String id = myRef.push().getKey();
        advertise.setAdvID(id);
        myRef.child(id).setValue(advertise);
        return  id;
    }

    public void RequestUnit(RequestUnit requestUnit){
        String requestId = database.getReference().push().getKey();
        requestUnit.setRequestId(requestId);
        database.getReference("Requests").child(requestUnit.getReqToUserId()).child(requestId).setValue(requestUnit);
    }


    public void updateImage(String advId, String imageUri){
        myRef.child(advId).child("pics").setValue(imageUri);

    }

    public void editAdvertise(Advertise advertise){
        myRef.child(advertise.getAdvID()).setValue(advertise);
    }


}
