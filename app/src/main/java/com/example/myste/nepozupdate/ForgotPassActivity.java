package com.example.myste.nepozupdate;


/*
 Author: Raj Silwal 11630172
 This activity is when user forget their password
 the user has to give valid loginId and registered emailID
 to get the system generated password which is sent via email
*/

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myste.nepozupdate.Mailing.GMailSender;
import com.example.myste.nepozupdate.Mailing.MailSenderCred;
import com.example.myste.nepozupdate.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class ForgotPassActivity extends AppCompatActivity {

    EditText etEmailConfirm, etLoginID;
    Button btnSubmit;
    String mailSender,senderPassword;
    String Subject="New password for Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        etEmailConfirm = (EditText)findViewById(R.id.etEmailConfirm);
        etLoginID = (EditText)findViewById(R.id.etFgtLogin);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        MailSenderCred msender = new MailSenderCred();
        mailSender= msender.getEmailId();
        senderPassword = msender.getPassword();

    }

    public void setEmailConfirm(View view){

        final String logInId = etLoginID.getText().toString();
        final String emailId = etEmailConfirm.getText().toString();

        ValueEventListener postListener = new ValueEventListener() {
            String post;
            User user1 = null;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {

                    User user = dataSnapshot1.getValue(User.class);

                    if(user.getLogInId().equals(logInId)&&user.getEmail().equals(emailId)){
                        user1 = user;
                    }


                }

                if(user1!=null){
                    Random random = new Random();
                    String alphabet="qwertyuiopasdfghjklzxcvbnm";
                    String temppass ="";
                    for(int j = 0; j < 8; j++) {

                        temppass = temppass + alphabet.charAt(random.nextInt(alphabet.length()));
                    }

                    int temp = random.nextInt(50)+1;

                    String newPassword = temppass + String.valueOf(temp);

                    String msg="Hi "+user1.getFirstName()+",\n" +"\n"+ "Your new Password is \""+ newPassword
                            +"\" Thank you.";
                    testmailsend(user1.getEmail(),msg);
                    FirebaseDatabase.getInstance().getReference("User")
                            .child(user1.getUserId()).child("password").setValue(newPassword);
                    Toast.makeText(getApplicationContext(), "New password is sent to your Email!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }

                else{
                    Toast.makeText(getApplicationContext(), "Given LoginId or Email Id is invalid", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
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
        FirebaseDatabase.getInstance().getReference("User").addListenerForSingleValueEvent(postListener);

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
