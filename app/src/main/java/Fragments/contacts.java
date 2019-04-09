package Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.helpinghands.R;
import com.example.helpinghands.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapter.recycleradapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class contacts extends Fragment {

    private RecyclerView recyclerView;
    List<User> mUser;
    recycleradapter adapter;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    EditText search;


    public contacts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_contacts, container, false);

        recyclerView=view.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUser= new ArrayList<User>();



        readuser();


        search=view.findViewById(R.id.users_search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchusers(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;
    }

    private void searchusers(String toString) {

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        Query query=FirebaseDatabase.getInstance().getReference("Users").orderByChild("search")
                .startAt(toString)
                .endAt(toString+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUser.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);


                    String uid=firebaseUser.getUid();
                    assert user != null;
                    if (!(user.getId()).equals(uid)) {

                        mUser.add(user);
                    }
                }
                adapter=new recycleradapter(mUser, getContext(),false);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readuser() {

         firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
//        String uid = firebaseUser.getUid();
        reference= FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(search.getText().toString().equals("")) {
                    mUser.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);


                        String uid = firebaseUser.getUid();
                        assert user != null;
                        if (!(user.getId()).equals(uid)) {

                            mUser.add(user);
                        }
                    }
                    adapter = new recycleradapter(mUser, getContext(), false);
                    recyclerView.setAdapter(adapter);
                }
//
//
                }
//            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


}
