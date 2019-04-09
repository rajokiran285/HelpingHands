package com.example.helpinghands;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.helpinghands.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Chatpage extends AppCompatActivity {

    private RecyclerView horizontal_recycler_view;

    private ArrayList<String> horizontal_list,msg_list;
    ArrayList<User> mUser;

    private HorizontalAdapter horizontalAdapter;

    int img[]={R.drawable.one,R.drawable.two,R.drawable.three,R.drawable.four,R.drawable.five,R.drawable.six,R.drawable.seven,R.drawable.eight,R.drawable.nine,R.drawable.ten};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_chatpage);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle("Main Page");
//        }
//        toolbar.setSubtitle("Test Subtitle");
        toolbar.inflateMenu(R.menu.data_menu);

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

        horizontal_recycler_view=(RecyclerView)findViewById(R.id.list);
        horizontal_list=new ArrayList<String>();
        msg_list=new ArrayList<String>();
        mUser=new ArrayList<User>();

        horizontal_list.add(" 1 ");
        horizontal_list.add(" 2 ");
        horizontal_list.add(" 3 ");
        horizontal_list.add(" 4 ");
        horizontal_list.add(" 5 ");
        horizontal_list.add(" 6 ");
        horizontal_list.add(" 7 ");
        horizontal_list.add(" 8 ");
        horizontal_list.add(" 9 ");
        horizontal_list.add(" 10 ");

        msg_list.add("hi");
        msg_list.add("hello");
        msg_list.add("gud morning");
        msg_list.add("gud afternoon");
        msg_list.add("noonz");
        msg_list.add("eveningz");
        msg_list.add("gud night");
        msg_list.add("hmmm");
        msg_list.add("k");
        msg_list.add("hey");

//        horizontal_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

//        readuser();

//        horizontalAdapter=new HorizontalAdapter(getApplicationContext(),mUser);

        horizontalAdapter=new HorizontalAdapter(horizontal_list);

        LinearLayoutManager horizontalLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);

        horizontal_recycler_view.setAdapter(horizontalAdapter);

    }

    private void readuser() {

        final FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                mUser.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    User user=snapshot.getValue(User.class);

                    assert user != null;
                    assert firebaseUser != null;
                    if(!user.getId().equals(firebaseUser.getUid())){
                        mUser.add(user);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    public  class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {
        private List<String> horizontallist;
//        List<User> mUser;
//        Context mContext;


        public class MyViewHolder extends RecyclerView.ViewHolder
        {
            public TextView textview,textview2;
            ImageView image;


            public MyViewHolder(View view)
            {
                super(view);
                textview=(TextView)view.findViewById(R.id.name);
//                textview2=(TextView)view.findViewById(R.id.msg);
                image=view.findViewById(R.id.img);
            }


        }
//        public HorizontalAdapter(Context mContext,List<User> mUser)
//        {
//            this.mContext=mContext;
//            this.mUser=mUser;
//
//        }

        public HorizontalAdapter(List<String> horizontallist)
        {
//            this.mContext=mContext;
//            this.mUser=mUser;
                this.horizontallist=horizontal_list;
        }
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            View itemview= LayoutInflater.from(getApplicationContext()).inflate(R.layout.data,parent,false);
            return new MyViewHolder(itemview);
        }



        public void onBindViewHolder(final MyViewHolder holder, final int position) {

//            User user=mUser.get(position);
//
//            holder.textview.setText(user.getUsername());
//            if (user.getImageurl().equals("default"))
//            {
//                holder.image.setImageResource(R.drawable.eight);
//            }
//            else
//            {
//                Glide.with(getApplicationContext()).load(user.getImageurl()).into(holder.image);
//            }
            holder.textview.setText(horizontal_list.get(position));
//            holder.textview2.setText(msg_list.get(position));
            holder.image.setImageResource(img[position]);

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Chatpage.this);
                    LayoutInflater inflater = Chatpage.this.getLayoutInflater();
                    View dialogLayout = inflater.inflate(R.layout.chat_image, null);
//                    builder.setPositiveButton("OK", null);
                    ImageView previewimage=dialogLayout.findViewById(R.id.chatimage);
                    previewimage.setImageResource(img[position]);
                    builder.setView(dialogLayout);
                    AlertDialog alert=builder.create();
                    alert.show();

                }
            });

            holder.textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),holder.textview.getText().toString(), Toast.LENGTH_SHORT).show();
                    Intent i3=new Intent(getApplicationContext(),chat.class);
                    SharedPreferences prf=getApplicationContext().getSharedPreferences("pref",MODE_PRIVATE);
                    SharedPreferences.Editor edt=prf.edit();
                    String Name=holder.textview.getText().toString();
                    edt.putString("name",Name);
//                    edt.putString("image",holder.image.getDrawable().toString());
                    edt.commit();
                    startActivity(i3);
                }
            });
        }

        @Override
        public int getItemCount() {
            return horizontallist.size();
        }


    }
}
