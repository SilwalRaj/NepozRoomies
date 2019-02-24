package com.example.myste.nepozupdate;


/*
 Author: Raj Silwal 11630172
 This activity is to display the menu list in each activity
 and used as parent Activity
 other activity inherits this activity
*/

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myste.nepozupdate.Model.RequestUnit;
import com.example.myste.nepozupdate.Model.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    /*private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private String userid;
    private Session session;

    private List<RequestUnit> requestUnitList = new ArrayList<RequestUnit>();*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


       /* myRef= firebaseDatabase.getReference("Requests");

        session = new Session(getApplicationContext());

        loadNotification();*/

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.commonmenus,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.adPost) {

            Intent intent = new Intent(this,AdvertiseActivity.class);
            startActivity(intent);
            //return true;
            //Toast.makeText(this,"")
        }

        else if(id == R.id.Profile){

            Intent intent = new Intent(this,ProfileActivity.class);
            startActivity(intent);

        }
        else if(id == R.id.Logout){

            Session session = new Session(this);
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();

        }
        else if(id == R.id.AdList){


            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
            finish();

        }
        else if(id == R.id.RequestNotif){


            Intent intent = new Intent(this,NotificationActivity.class);
            startActivity(intent);
            finish();

        }
        return super.onOptionsItemSelected(item);
        //return false;
    }



}
