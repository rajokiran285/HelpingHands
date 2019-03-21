package com.example.helpinghands;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Chatpage extends AppCompatActivity {

    private RecyclerView horizontal_recycler_view;

    private ArrayList<String> horizontal_list,msg_list;

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

        horizontal_recycler_view=(RecyclerView)findViewById(R.id.list);
        horizontal_list=new ArrayList<String>();
        msg_list=new ArrayList<String>();

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



        horizontalAdapter=new HorizontalAdapter(horizontal_list);


        LinearLayoutManager horizontalLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);

        horizontal_recycler_view.setAdapter(horizontalAdapter);

    }

    public  class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {
        private List<String> horizontallist;
        public class MyViewHolder extends RecyclerView.ViewHolder
        {
            public TextView textview,textview2;
            ImageView image;
            public MyViewHolder(View view)
            {
                super(view);
                textview=(TextView)view.findViewById(R.id.name);
                textview2=(TextView)view.findViewById(R.id.msg);
                image=view.findViewById(R.id.img);
            }


        }
        public HorizontalAdapter(List<String> horizontallist)
        {
            this.horizontallist=horizontal_list;

        }
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int ViewType)
        {
            View itemview= LayoutInflater.from(parent.getContext()).inflate(R.layout.data,parent,false);
            return new MyViewHolder(itemview);
        }



        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.textview.setText(horizontallist.get(position));
//            holder.textview2.setText(msg_list.get(position));
            holder.image.setImageResource(img[position]);

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Chatpage.this);
                    LayoutInflater inflater = Chatpage.this.getLayoutInflater();
                    View dialogLayout = inflater.inflate(R.layout.chat_image, null);
                    builder.setPositiveButton("OK", null);
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
