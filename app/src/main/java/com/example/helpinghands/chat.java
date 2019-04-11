package com.example.helpinghands;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.helpinghands.model.User;
import com.example.helpinghands.model.chats;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.chatadapter;
import de.hdodenhof.circleimageview.CircleImageView;

public class chat<storageReference> extends AppCompatActivity {

    TextView name;
    CircleImageView image;
    EditText msg;
    ImageButton attach,send;
    ImageView arrow;
    DatabaseReference reference,reference_msg;
    FirebaseUser firebaseUser;
    Intent intent;
    storageReference  storageReference;
    Uri imageUri;
    StorageTask upload;

    static final int image_rqst=1;
    RecyclerView list;
    chatadapter adapter;
    List<chats> mChat;

    int img[]={R.drawable.one,R.drawable.two,R.drawable.three,R.drawable.four,R.drawable.five,R.drawable.six,R.drawable.seven,R.drawable.eight,R.drawable.nine,R.drawable.ten};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        final int image_rqst;
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.inflateMenu(R.menu.chat_menu);



//        SharedPreferences prf=getApplicationContext().getSharedPreferences("pref",MODE_PRIVATE);
//        String c_name=prf.getString("name",null);
        name=findViewById(R.id.chatname);
        msg=findViewById(R.id.chattext);
        image=findViewById(R.id.chatimg);
        attach=findViewById(R.id.chatattachment);
        send=findViewById(R.id.chatsend);
        arrow=findViewById(R.id.arrow);
        Uri imageUri;


        

        list=findViewById(R.id.chatlst);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        list.setLayoutManager(linearLayoutManager);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();


         intent=getIntent();
//        Bundle extra=intent.getExtras();

        final String userid=intent.getStringExtra("userid");
        String id=userid;



        reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                name.setText(user.getUsername());
                if (user.getImageurl().equals("default"))
                {
                    image.setImageResource(R.drawable.eight);
                }
                else
                {
                    Glide.with(getApplicationContext()).load(user.getImageurl()).into(image);
                }
                mChat=new ArrayList<>();

                String currentuserid=firebaseUser.getUid();
                receivemessage(currentuserid,userid,user.getImageurl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"send",Toast.LENGTH_SHORT).show();

                String text=msg.getText().toString();
                sendmessage(firebaseUser.getUid(),userid,text);

            }
        });
        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"attach",Toast.LENGTH_SHORT).show();



            }
        });

//        name.setText(c_name);
//        String pos=prf.getString("image", null);
//        image=findViewById(R.id.chatimg);
//        image.setImageResource();


//        receivemessage();

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),chatpage2.class);
                startActivity(i);
            }
        });

    }

    private void receivemessage(final String myid, final String userid, final String imageurl) {


        reference_msg= FirebaseDatabase.getInstance().getReference("chats");

        reference_msg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    chats chat=snapshot.getValue(chats.class);
                    assert chat!=null;

                    String receiver=chat.getReceiver();
                    String sender=chat.getSender();

                    if (receiver.equals(myid)&&sender.equals(userid)
                            ||receiver.equals(userid)&&sender.equals(myid))
                    {
                        mChat.add(chat);
                    }
                    adapter=new chatadapter(mChat, chat.this,imageurl);
                    list.setAdapter(adapter);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    String getFileExetension(Uri uri)
    {
        ContentResolver contentResolver=getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }


    public void uploadimage()
    {
//        final ProgressDialog dialog=new ProgressDialog(getApplicationContext());
//        dialog.setMessage("uploading");
//        dialog.show();
//
            Toast.makeText(getApplicationContext(),"text",Toast.LENGTH_SHORT).show();

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        storageReference= (storageReference) FirebaseStorage.getInstance().getReference("Chat uploads");

            if (imageUri!=null)
            {
//                final StorageReference file=storageReference.(System.currentTimeMillis()+"."+getFileExetension(imageUri));
//                upload=file.putFile(imageUri);
                upload.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                    @Override
                    public Task then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();
                        }
//                        return file.getDownloadUrl();
                    };
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            Uri download=task.getResult();
                            String mUri=download.toString();

//                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                            HashMap<String,Object> map=new HashMap<>();
                            map.put("chatimage",mUri);
                            reference.updateChildren(map);

//                        dialog.dismiss();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
                    }
                });
            }else
            {
                Toast.makeText(getApplicationContext(),"no image selected",Toast.LENGTH_SHORT).show();

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


    private void sendmessage(String sender, final String receiver, String Message) {

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("Sender",sender);
        hashMap.put("Receiver",receiver);
        hashMap.put("Message",Message);

        reference.child("chats").push().setValue(hashMap);


        final DatabaseReference chatref=FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(firebaseUser.getUid())
                .child(receiver);

        chatref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())
                {
                    chatref.child("id").setValue(receiver);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if(requestCode == image_rqst && resultCode==RESULT_OK&&data!=null&&data.getData()!=null)
//        {
//            imageUri=data.getData();
//
//            if(upload!=null &&upload.isInProgress())
//            {
//                Toast.makeText(getApplicationContext(),"Upload in progress",Toast.LENGTH_SHORT).show();
//            }
//            else
//            {
//                uploadimage();
//            }
//        }
    }

    private void openimage() {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,image_rqst);
    }
}
