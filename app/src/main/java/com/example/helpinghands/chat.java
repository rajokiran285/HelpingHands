package com.example.helpinghands;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class chat extends AppCompatActivity {

    TextView name;
    ImageView image;

    int img[]={R.drawable.one,R.drawable.two,R.drawable.three,R.drawable.four,R.drawable.five,R.drawable.six,R.drawable.seven,R.drawable.eight,R.drawable.nine,R.drawable.ten};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_chat);

        SharedPreferences prf=getApplicationContext().getSharedPreferences("pref",MODE_PRIVATE);
        String c_name=prf.getString("name",null);
        name=findViewById(R.id.chatname);
        name.setText(c_name);
//        String pos=prf.getString("image", null);
//        image=findViewById(R.id.chatimg);
//        image.setImageResource();
    }
}
