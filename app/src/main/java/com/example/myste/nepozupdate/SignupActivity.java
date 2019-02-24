package com.example.myste.nepozupdate;


/*
 Author: Raj Silwal 11630172
 This activity is used to register the new user in
 the system and the user have to give their detail and valid emailId
 and password. then system will sends the loginId to the user via email
*/

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myste.nepozupdate.DAO.UserDAO;
import com.example.myste.nepozupdate.Mailing.GMailSender;
import com.example.myste.nepozupdate.Mailing.MailSenderCred;
import com.example.myste.nepozupdate.Model.Advertise;
import com.example.myste.nepozupdate.Model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class SignupActivity extends AppCompatActivity {

    EditText etFirstName;
    EditText etLastName;
    EditText etEmail;
    EditText etPhoneNo;
    EditText etPassword, etConfirmPassword;
    Button btnRegister;
    ImageView ivProfilePic;
    //UserDAO userDAO;
    //LogInDAO logInDAO;
    String Subject="UserId for LogIn";
    String mailSender;
    String senderPassword, dbReturnId;
    User user;
    final int REQUEST_CODE_GALLERY=111;
    Uri uri;

    ArrayList<User> repeatedUser = new ArrayList<>();
    UserDAO userDAO;

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference mStorageRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etFirstName = (EditText)findViewById(R.id.etFirstName) ;
        etLastName = (EditText)findViewById(R.id.etLastName);
        etEmail = (EditText)findViewById(R.id.etEmailId);
        etPhoneNo = (EditText)findViewById(R.id.etPhoneNo) ;
        etPassword = (EditText)findViewById(R.id.eTPassword) ;
        etConfirmPassword = (EditText)findViewById(R.id.eTconfirmPass) ;
        btnRegister = (Button)findViewById(R.id.btnUserSignUp);
        ivProfilePic = (ImageView)findViewById(R.id.ivProfilePic);
        MailSenderCred msender = new MailSenderCred();
        userDAO = new UserDAO();
        mailSender= msender.getEmailId();
        senderPassword = msender.getPassword();
        mStorageRef =firebaseStorage.getReference();
        Intent i = getIntent();
        user = (User) i.getSerializableExtra("user");

        if (user != null) {
            setEditText();
        }


    }

    private void setEditText() {
        etFirstName.setText(user.getFirstName());
        etLastName.setText(user.getLastName());
        etEmail.setText(user.getEmail());
        etEmail.setEnabled(false);
        etPhoneNo.setText(user.getPhoneNo());
        etPassword.setHint("New Password");
        etConfirmPassword.setHint("Confirm Password");
        etPhoneNo.setText(user.getPhoneNo());
        etPhoneNo.setText(user.getPhoneNo());
        btnRegister.setText("Edit");

    }

    public void addImage (View view) {

        ivProfilePic = (ImageView)findViewById(R.id.ivProfilePic);

        ActivityCompat.requestPermissions
                (SignupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
        Intent intent = new Intent(Intent.ACTION_PICK);

        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_CODE_GALLERY);
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {

        uri = data.getData();
        try{
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ivProfilePic.setImageBitmap(bitmap);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    public void clickRegister(View view){
        String fName = etFirstName.getText().toString();
        String lName = etLastName.getText().toString();
        String email = etEmail.getText().toString();
        String phnNo = etPhoneNo.getText().toString();
        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        if(fName.length()==0){
            etFirstName.requestFocus();
            etFirstName.setError("Required Field");
        }
        else if(lName.length()==0){
            etLastName.requestFocus();
            etLastName.setError("Required Field");
        }
        else if(email.length()==0){
            etEmail.requestFocus();
            etEmail.setError("Required Field");

        }
        else if (!email.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+" +
                "@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$")){
            etEmail.requestFocus();
            etEmail.setError("Invalid Email");

        }
        else if(phnNo.length()==0){
            etPhoneNo.requestFocus();
            etPhoneNo.setError("Required Field");

        }
        else if (!phnNo.matches("\\d{10}|(?:\\d{3}-){2}\\d{4}|\\(\\d{3}\\)\\d{3}-?\\d{4}")){
            etPhoneNo.requestFocus();
            etPhoneNo.setError("Invalid Phone No.");
        }
        else if(password.length()==0){
            etPassword.requestFocus();
            etPassword.setError("Required Field");

        }
        else if(confirmPassword.length()==0){
            etConfirmPassword.requestFocus();
            etConfirmPassword.setError("Required Field");

        }
        else if(!password.equals(confirmPassword)){
            etPassword.requestFocus();
            etPassword.setError("Not Matching");
            etConfirmPassword.requestFocus();
            etConfirmPassword.setError("Not Matching");
        }
        else{
            validRegister(fName,lName,email,phnNo,password);

        }


    }

    public void validRegister(final String fName, final String lName, final String email, final String phnNo, final String password){

        Random rand = new Random();
        int temp = rand.nextInt(50)+1;
        String logInid = email.split("@")[0] + String.valueOf(temp);

        final String tempLogInid = logInid;


        String tempId="";
        if(user==null) {
            int numofIds = userDAO.checkEmailidExist(email);
            //final ArrayList<User> users = new ArrayList<User>();
            repeatedUser.clear();
            ValueEventListener valueEventListener = new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        User logIn = ds.getValue(User.class);
                        if(logIn.getEmail().equals(email)){
                            repeatedUser.add(logIn);
                        }

                    }
                    if(repeatedUser.size()==0) {
                        //checkEmailexist(fName, lName, email, phnNo, tempLogInid, password);
                        dbReturnId = userDAO.userEntry(new User(fName, lName, email, phnNo, "", tempLogInid, password));
                        sendEmail_fileupload( tempLogInid,  email);
                    }
                    else{
                        create_dialogBox();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            FirebaseDatabase.getInstance().getReference("User").addListenerForSingleValueEvent(valueEventListener);//ValueEventListener(valueEventListener);
            //tempId = dbReturnId;
        }
        else {
            userDAO.editUser(new User(user.getUserId(), fName, lName, user.getEmail(), phnNo, "", user.getLogInId(), password));
            tempId = user.getUserId();
            if(uri!=null){
                uploadfile2(uri,"User/"+tempId,tempId);
            }
            else{
                if(user==null) {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(this, ProfileActivity.class);
                    startActivity(intent);
                }
            }

        }




    }
    private void create_dialogBox(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Email Exists!");
        dialogBuilder.setMessage("The given email already existed in system!!");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialogEmailCheck = dialogBuilder.create();
        dialogEmailCheck.show();
    }


    private void sendEmail_fileupload(String logInid, String email){
        String tempId = dbReturnId;

        if(tempId!=null && !tempId.isEmpty()) {


            String msg="Hi,"+"\n" +"\n"+ "Your LoginId is \""+ logInid
                    +"\"  and password is one you have given."
                    +"Thank you and Welcome to Nepoz.";
            testmailsend(email,msg);
            Toast.makeText(this, "Welcome to Nepoz!",
                    Toast.LENGTH_LONG).show();
            if(uri!=null){
                uploadfile2(uri,"User/"+tempId,tempId);
            }
            else{
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    private void uploadfile2(Uri filePath, final String imagePath, final String userId){

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

                    FirebaseDatabase.getInstance().getReference("User").child(userId).child("profImage").setValue(downloadUri.toString());
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    if(user==null) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
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


    private void testmailsend(final String recp, final String msgBody){
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String msg = msgBody;
                    GMailSender sender = new GMailSender(mailSender,senderPassword);
                    sender.sendMail(Subject, msg, mailSender, recp);
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }

        }).start();
    }
}
