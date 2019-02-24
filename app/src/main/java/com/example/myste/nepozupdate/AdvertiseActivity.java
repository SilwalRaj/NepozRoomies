package com.example.myste.nepozupdate;


/*
 Author: Raj Silwal 11630172
 This activity is used for posting the advertise or for
 editing the posted advertise.
 the advertiser can add three images and detail of the
 unit he/she wants to share
*/

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myste.nepozupdate.Model.Advertise;
import com.example.myste.nepozupdate.Model.Session;

import com.example.myste.nepozupdate.DAO.AdvertiseDAO;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdvertiseActivity extends MenuActivity {

    final int REQUEST_CODE_GALLERY=999;
    EditText etUnitNo;
    EditText etHouseNo;
    EditText etStreetName;
    EditText etSuburb;
    EditText eTDescription, eTDate;
    AdvertiseDAO advertiseDAO;
    //AdvImageDAO advImageDAO;
    Session session;
    ImageView ivAdImage,ivAdImage1,ivAdImage2,ivAdImage3;
    ArrayList<Uri> filePaths = new ArrayList<Uri>();
    ArrayList<Uri> downloadUris = new ArrayList<Uri>();
    Advertise advertise;
    String dateRegex ="^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))" +
            "(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3" +
            "(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|" +
            "(?:(?:16|[2468][048]|[3579][26])00))))" +
            "$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|" +
            "(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference mStorageRef;
    ProgressDialog progressDialog;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    boolean isIvunit2=false, isIvunit3=false, isIvunit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertise);

        etUnitNo = (EditText)findViewById(R.id.eTUnitNo) ;
        etHouseNo = (EditText)findViewById(R.id.etHouseNo);
        etStreetName = (EditText)findViewById(R.id.etStreetName);
        etSuburb = (EditText)findViewById(R.id.etSuburbName);
        eTDate = (EditText)findViewById(R.id.etDate);
        eTDescription = (EditText)findViewById(R.id.eTDescription);
        ivAdImage1 = (ImageView)findViewById(R.id.iVUnit);
        ivAdImage2 = (ImageView)findViewById(R.id.iVUnit2);
        ivAdImage3 = (ImageView)findViewById(R.id.iVUnit3);

        advertiseDAO = new AdvertiseDAO();
        //advImageDAO = new AdvImageDAO(this);
        session = new Session(getApplicationContext());
        mStorageRef = firebaseStorage.getReference();
        myRef = database.getReference("Advertise");

        Intent i = getIntent();
        advertise= (Advertise) i.getSerializableExtra("advertise");
        if (advertise != null) {
            setEditText();
        }
    }

    private void setEditText() {
        etUnitNo.setText(advertise.getUnitNo()> 0 ? String.valueOf(advertise.getUnitNo()):"");
        etHouseNo.setText(String.valueOf(advertise.getHouseNo()));
        etStreetName.setText(advertise.getStrtName());
        etSuburb.setText(advertise.getSuburb());
        eTDate.setText(advertise.getInspectDate()!=null?advertise.getInspectDate():"");
        eTDescription.setText(advertise.getDesc());
        if(advertise.getPics()!=null && advertise.getPics().length()!=0) {
            String[] images = advertise.getPics().split(":::");
            for (int i = 0; i < images.length; i++) {
                if (i == 0) {
                    Picasso.get().load(images[i]).into(ivAdImage2);
                    ivAdImage2.setEnabled(false);
                } else if (i == 1) {
                    Picasso.get().load(images[i]).into(ivAdImage3);
                    ivAdImage2.setEnabled(false);
                } else if (i == 2) {
                    Picasso.get().load(images[i]).into(ivAdImage1);
                    ivAdImage2.setEnabled(false);
                }
            }
        }

    }


    public void addImage (View view) {


        if(view.getId()== R.id.iVUnit){
            ivAdImage = (ImageView)findViewById(R.id.iVUnit);

        }
        else if(view.getId()== R.id.iVUnit2){
            ivAdImage = (ImageView)findViewById(R.id.iVUnit2);

        }
        else if(view.getId()== R.id.iVUnit3){
            ivAdImage = (ImageView)findViewById(R.id.iVUnit3);

        }
        else{
            ivAdImage = (ImageView)findViewById(R.id.iVUnit);

        }

        ActivityCompat.requestPermissions
                (AdvertiseActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
        Intent intent = new Intent(Intent.ACTION_PICK);

        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_CODE_GALLERY);
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {

        Uri uri = data.getData();
        filePaths.add(uri);
        try{
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ivAdImage.setImageBitmap(bitmap);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        super.onActivityResult(requestCode,resultCode,data);
    }


    public void addAdvPost(View view) throws ParseException {
        int houseNo = Integer.parseInt(etHouseNo.getText().toString());
        String streetName = etStreetName.getText().toString();
        String suburbName = etSuburb.getText().toString();
        String unitDesc = eTDescription.getText().toString();
        String inspectDate = eTDate.getText().toString();

        if(etHouseNo.getText().toString().isEmpty()){
            etHouseNo.requestFocus();
            etHouseNo.setError("Required field");
        }
        else if(streetName.length()==0){

            etStreetName.requestFocus();
            etStreetName.setError("Required field");

        }
        else if(suburbName.length()==0){
            etSuburb.requestFocus();
            etSuburb.setError("Required field");
        }
        else if(unitDesc.length()==0){
            eTDescription.requestFocus();
            eTDescription.setError("Required field");
        }
        else if(inspectDate.length()==0){
            eTDate.requestFocus();
            eTDate.setError("Required field");
        }
        else if(!inspectDate.matches(dateRegex)){
            eTDate.requestFocus();
            eTDate.setError("Give Valid date dd/mm/yyyy");
        }

        else{
            if(inspectDate.contains("."))
                inspectDate = inspectDate.replace('.','/');
            else if(inspectDate.contains("-"))
                inspectDate = inspectDate.replace('-','/');
            if(new SimpleDateFormat("dd/MM/yyyy").parse(inspectDate).compareTo( new Date())<=0){

                eTDate.requestFocus();
                eTDate.setError("Inspection date cannot be past date");
            }
            else {

                addvalidAdvPost(houseNo, streetName, suburbName, unitDesc, inspectDate);
            }
        }
    }

    public void addvalidAdvPost(int houseNo,String streetName,String suburbName,String unitDesc,String inspectDate){
        String imagesName;
        int unitNo;
        if(!etUnitNo.getText().toString().isEmpty()) {
            unitNo = Integer.parseInt(etUnitNo.getText().toString());
        }
        else{
            unitNo = 0;
        }

        String userId = session.getuserId();

        imagesName = "";


        Advertise advertise1 = new Advertise(unitNo,houseNo,streetName,suburbName,imagesName,unitDesc,inspectDate,userId);
        String advId="";
        if(advertise!=null){
            advId= advertise.getAdvID();
            advertise1.setAdvID(advId);
            String[] imageUris = advertise.getPics().split(":::");

            advertise1.setPics(advertise.getPics());
            advertiseDAO.editAdvertise(advertise1);
            for(int i = 0; i<filePaths.size();i++) {
                //uploadFile(filePaths.get(i), "Advertise/" + advId + "/image" +String.valueOf(i+1));
                uploadfile2(filePaths.get(i), "Advertise/" + advId + "/image" +String.valueOf(imageUris.length +i+1), advId, i+1, filePaths.size());
            }
        }
        else {

            advId = advertiseDAO.AddAdvertise(advertise1);
            for(int i = 0; i<filePaths.size();i++) {
                //uploadFile(filePaths.get(i), "Advertise/" + advId + "/image" +String.valueOf(i+1));
                uploadfile2(filePaths.get(i), "Advertise/" + advId + "/image" +String.valueOf(i+1), advId, i+1, filePaths.size());
            }
        }

        if(filePaths.size()<1){
            Toast.makeText(getApplicationContext(), "Record added Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);


        }


    }

    private byte[] ImageViewtoByte(ImageView image) {

        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    private void uploadfile2(Uri filePath, final String imagePath, final String advId, final int i, final int size){

        UploadTask uploadTask = mStorageRef.child(imagePath).putFile(filePath);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return FirebaseStorage.getInstance().getReference().child(imagePath).getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    downloadUris.add(downloadUri);
                    String imageUris="";
                    if(downloadUris.size()>0){
                        imageUris = downloadUris.get(0).toString();
                        for(int i=1;i<downloadUris.size();i++){
                            imageUris = imageUris + ":::" +downloadUris.get(i).toString();
                        }
                        Toast.makeText(getApplicationContext(),"Uploading Records...",Toast.LENGTH_LONG).show();
                    }
                    if(size == i) {
                        if(advertise!=null){
                            if(advertise.getPics()!=null && advertise.getPics().length()!=0) {
                                imageUris = advertise.getPics() + ":::" + imageUris;
                            }
                        }

                        myRef.child(advId).child("pics").setValue(imageUris);

                        Toast.makeText(getApplicationContext(), "Record added Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);

                    }
                } else {
                    // Handle failures
                    // ...
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

}
