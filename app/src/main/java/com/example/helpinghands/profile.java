package com.example.helpinghands;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.helpinghands.model.User;
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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile extends AppCompatActivity {

    TextView name;
    CircleImageView image;

     static final int image_rqst=1;
    Uri imageUri;
    StorageTask upload;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        toolbar.inflateMenu(R.menu.profile_menu);

        name=findViewById(R.id.pro_username);
        image=findViewById(R.id.pro_image);


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if(menuItem.getItemId()==R.id.edit)
                {
                    openimage();
                }

                return false;
            }
        });



        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        storageReference= FirebaseStorage.getInstance().getReference("Uploads");

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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

                final Drawable img=image.getDrawable();
                AlertDialog.Builder builder = new AlertDialog.Builder(profile.this);
                LayoutInflater inflater = profile.this.getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.chat_image, null);
//                    builder.setPositiveButton("OK", null);
                ImageView previewimage=dialogLayout.findViewById(R.id.chatimage);
                previewimage.setImageDrawable(img);
                builder.setView(dialogLayout);
                AlertDialog alert=builder.create();
                alert.show();
            }
        });

//        image.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//
//                openimage();
//
//                return true;
//            }
//        });


    }

    private void openimage() {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,image_rqst);
    }


    public void uploadimage() {
//        final ProgressDialog dialog=new ProgressDialog(getApplicationContext());
//        dialog.setMessage("uploading");
//        dialog.show();

        Toast.makeText(getApplicationContext(),"text",Toast.LENGTH_SHORT).show();

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        storageReference= FirebaseStorage.getInstance().getReference("Uploads");

        if (imageUri!=null)
        {
            final StorageReference file=storageReference.child(System.currentTimeMillis()+"."+getFileExetension(imageUri));
            upload=file.putFile(imageUri);
            upload.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                @Override
                public Task then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return file.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri download=task.getResult();
                        String mUri=download.toString();

//                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                        HashMap<String,Object> map=new HashMap<>();
                        map.put("imageurl",mUri);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == image_rqst && resultCode==RESULT_OK&&data!=null&&data.getData()!=null)
        {
            imageUri=data.getData();

            if(upload!=null &&upload.isInProgress())
            {
                Toast.makeText(getApplicationContext(),"Upload in progress",Toast.LENGTH_SHORT).show();
            }
            else
            {
                uploadimage();
            }
        }
    }

    String getFileExetension(Uri uri)
    {
        ContentResolver contentResolver=getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

}
}
