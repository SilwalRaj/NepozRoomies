package com.example.myste.nepozupdate;


/*
 Author: Raj Silwal 11630172
 This activity lists out the advertises posted by
 users. Users can navigate to detail screen by clicking
 listitem for long time.
*/

//import android.media.Image;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myste.nepozupdate.DAO.AdvertiseDAO;
import com.example.myste.nepozupdate.Model.Advertise;
import com.example.myste.nepozupdate.Model.RecyclerViewAdapter;
import com.example.myste.nepozupdate.Model.Session;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
        import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

        import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends MenuActivity  {

    private List<Advertise> advertises = new ArrayList<Advertise>();
    private List<Advertise> searchAdvertises = new ArrayList<Advertise>();

    private AdvertiseDAO advertiseDAO;
    private ListView list;
    private RecyclerView recyclerview;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference mStorageRef;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    EditText etSearch;
    ImageButton iBtnSearch,iBtnFavSearch;
    Session session;
    String userId;

    final ArrayList<String> uris =  new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        session = new Session(this);
        userId = session.getuserId();

        etSearch = (EditText)findViewById(R.id.eTSearch);
        iBtnSearch = (ImageButton)findViewById(R.id.iBtnSearch);
        iBtnFavSearch= (ImageButton) findViewById(R.id.iBtnFavSearch);
        iBtnFavSearch.setTag(android.R.drawable.btn_star_big_off);
        //list = (ListView) findViewById(R.id.lvAdvertise);
        recyclerview = (RecyclerView) findViewById(R.id.recycleView);
        advertiseDAO = new AdvertiseDAO();
        myRef = database.getReference("Advertise");
        //populateAdvertiseList();
        PopulateAvertiseRecyclerView();
        //populateListView();
        //list.setOnItemClickListener(this);

    }


    private void PopulateAvertiseRecyclerView(){

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //list = new ArrayList<>();
                // StringBuffer stringbuffer = new StringBuffer();
                for(DataSnapshot dataSnapshot1 :dataSnapshot.getChildren()){

                    Advertise advertise = dataSnapshot1.getValue(Advertise.class);
                    advertises.add(advertise);

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

    public void searchUnit(View view){

        String searchTxt = etSearch.getText().toString();
        if(searchTxt.length()>0) {
            InputMethodManager inputManager =
                    (InputMethodManager) getApplicationContext().
                            getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(
                    this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);


            searchAdvertises.clear();


            for (Advertise adv : advertises) {

                if (adv.getSuburb().toLowerCase().equals(searchTxt.toLowerCase())
                        || adv.getStrtName().toLowerCase().equals(searchTxt.toLowerCase())
                        || (adv.getStrtName() + " " + adv.getSuburb()).toLowerCase().equals(searchTxt.toLowerCase())
                        || (adv.getHouseNo() + " " + adv.getStrtName().toLowerCase() + " " + adv.getSuburb().toLowerCase()).equals(searchTxt.toLowerCase())
                        || (adv.getUnitNo() + "/" + adv.getHouseNo() + " " + adv.getStrtName() + " " + adv.getSuburb()).toLowerCase().equals(searchTxt.toLowerCase())) {

                    searchAdvertises.add(adv);


                }

                CallRecyclerView(searchAdvertises);

            }
        }
        else{
            CallRecyclerView(advertises);
        }


    }

    public void FavSearch(View view){
        if(Integer.parseInt(iBtnFavSearch.getTag().toString())== android.R.drawable.btn_star_big_off) {
            iBtnFavSearch.setImageResource(android.R.drawable.btn_star_big_on);
            iBtnFavSearch.setTag(android.R.drawable.btn_star_big_on);

            final List<Advertise> favAdvertises = new ArrayList<Advertise>();

            final ValueEventListener postListener = new ValueEventListener() {
                String post="";

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Get Post object and use the values to update the UI
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if (dataSnapshot1.getKey().toString().equals(userId)) {
                            post = dataSnapshot1.getValue(String.class);

                        }
                    }
                    if (!post.isEmpty()) {
                        String[] ads = post.split(":::");

                        for (Advertise ad1 : advertises) {
                            for (String ad : ads) {
                                if (ad1.getAdvID().equals(ad)) {
                                    favAdvertises.add(ad1);
                                }
                            }
                        }
                    }

                    CallRecyclerView(favAdvertises);
                    // ...
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    // Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    // ...
                }
            };
            database.getReference("FlagAdvertise").addListenerForSingleValueEvent(postListener);
        }
        else{
            iBtnFavSearch.setImageResource(android.R.drawable.btn_star_big_off);
            iBtnFavSearch.setTag(android.R.drawable.btn_star_big_off);
            CallRecyclerView(advertises);
        }
    }


    public void CallRecyclerView(List<Advertise> advList){
        RecyclerViewAdapter recycler = new RecyclerViewAdapter(advList,getApplicationContext());
        RecyclerView.LayoutManager layoutmanager = new LinearLayoutManager(HomeActivity.this);
        recyclerview.setLayoutManager(layoutmanager);
        recyclerview.setItemAnimator( new DefaultItemAnimator());
        recyclerview.setAdapter(recycler);
    }



}
