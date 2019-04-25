package com.example.helpinghands;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.example.helpinghands.model.Location;
import com.example.helpinghands.model.chats;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import Fragments.Chat;
import Fragments.contacts;

public class chatpage2 extends AppCompatActivity implements  LocationListener {

    TabLayout tab;
    ViewPager page;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Button a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_chatpage2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.data_menu);

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.profile)
                {
                    Intent i4=new Intent(getApplicationContext(),profile.class);
                    startActivity(i4);

                }
                else if (menuItem.getItemId()==R.id.logout)
                {
                    FirebaseAuth.getInstance().signOut();
                    Intent i6=new Intent(getApplicationContext(),Loginpage.class);
                    startActivity(i6);
                    finish();
                }
                return false;
            }
        });

        tab=findViewById(R.id.tablayout);
        page=findViewById(R.id.pager);

//        a=findViewById(R.id.floating);
        viewpager adapter=new viewpager(getSupportFragmentManager());

        adapter.addfragment(new Chat(),"chats");
        adapter.addfragment(new contacts(),"contacts");

        page.setAdapter(adapter);
        tab.setupWithViewPager(page);


        final String mUser=firebaseUser.getUid();
        final String msg="S.O.S";

        a=findViewById(R.id.floating);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//                Location location = ;

                sendmessage( mUser,msg);

            }
        });
    }

    private void sendmessage(final String mUser, final String msg) {

        final DatabaseReference reference= FirebaseDatabase.getInstance().getReference();

        final HashMap<String,Object> hashMap=new HashMap<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    chats chat=snapshot.getValue(chats.class);
                    assert chat!=null;

                    hashMap.put("Sender",mUser);
                    String receiver=chat.getReceiver();
                    hashMap.put("Receiver",receiver);
                    hashMap.put("Message",msg);

                    reference.child("chats").push().setValue(hashMap);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(chatpage2.this);
//        LayoutInflater inflater = chatpage2.this.getLayoutInflater();
//        View dialogLayout = inflater.inflate(R.layout.chat_image, null);
////                    builder.setPositiveButton("OK", null);
//        ImageView previewimage=dialogLayout.findViewById(R.id.chatimage);
//        previewimage.setImageDrawable(img);
//        builder.setView(dialogLayout);
        builder.setMessage("Log out");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                FirebaseAuth.getInstance().signOut();
                dialog.dismiss();
                Intent i6=new Intent(getApplicationContext(),Loginpage.class);
                startActivity(i6);




            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }

    @Override
    public void onLocationChanged(android.location.Location location) {

        double lat=location.getLatitude();
        double log=location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    class viewpager extends FragmentPagerAdapter{

        ArrayList<Fragment> fragments;
        ArrayList<String> titles;

        public viewpager(FragmentManager fm) {
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();

        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        void addfragment(Fragment fragment,String title)
        {
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    void status(String status)
    {
        reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status",status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }
}

