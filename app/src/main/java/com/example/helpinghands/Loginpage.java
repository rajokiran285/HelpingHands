package com.example.helpinghands;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Loginpage extends AppCompatActivity {

    TextView reg;
    TextInputLayout emailid,password;
    Button log;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(getApplicationContext());

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_loginpage);



        log=findViewById(R.id.login);
        reg=findViewById(R.id.regst);
        emailid=findViewById(R.id.L_id);
        password=findViewById(R.id.L_pass);

        auth=FirebaseAuth.getInstance();


        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Registerpage.class);
                startActivity(i);
            }
        });
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = emailid.getEditText().getText().toString();
                String pass = password.getEditText().getText().toString();

                if (id.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter All Fields", Toast.LENGTH_SHORT).show();
                } else {

                    auth.signInWithEmailAndPassword(id, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent i2 = new Intent(getApplicationContext(), chatpage2.class);
                                startActivity(i2);
                            }
                        }
                    });
                }
            }
        });
    }



    @Override
    public void onBackPressed() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(Loginpage.this);
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

//                FirebaseAuth.getInstance().signOut();
//                dialog.dismiss();
//                Intent i6=new Intent(getApplicationContext(),Loginpage.class);
//                startActivity(i6);

                finish();



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
}
