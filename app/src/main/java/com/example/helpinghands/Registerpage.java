package com.example.helpinghands;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registerpage extends AppCompatActivity {

    TextInputLayout username,emailid,password,re_password;
    Button reg;

    private FirebaseAuth mAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_registerpage);

        username=findViewById(R.id.R_username);
        emailid=findViewById(R.id.R_emailid);
        password=findViewById(R.id.R_pass);
        re_password=findViewById(R.id.RT_pass);
        reg=findViewById(R.id.regbttn);

        mAuth =FirebaseAuth.getInstance();

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = username.getEditText().getText().toString();
                String id = emailid.getEditText().getText().toString();
                String pass = password.getEditText().getText().toString();
                String re_pass = re_password.getEditText().getText().toString();


                if (name.isEmpty() || id.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter All Fields", Toast.LENGTH_SHORT).show();
                } else if (pass.length() < 6) {
                    Toast.makeText(getApplicationContext(), "password too short", Toast.LENGTH_SHORT).show();
                }
                else if(!pass.equals(re_pass))
                {
                    Toast.makeText(getApplicationContext(),"check password",Toast.LENGTH_SHORT).show();
                }
                else{
                Toast.makeText(getApplicationContext(), "Welcome" + name, Toast.LENGTH_LONG).show();

                mAuth.createUserWithEmailAndPassword(id, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Log.d("sucess", "createUserWithEmail:success");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", name);
                            hashMap.put("imageurl", "default");
                            hashMap.put("status", "offline");
                            hashMap.put("search", name.toLowerCase());

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent i5 = new Intent(getApplicationContext(), Loginpage.class);
                                        i5.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i5);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                        }


                    }
                });


            }
        }
        });

    }
}
