package Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.helpinghands.R;
import com.example.helpinghands.model.User;
import com.example.helpinghands.model.chatlist;
import com.example.helpinghands.model.chats;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import adapter.recycleradapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Chat extends Fragment {

    RecyclerView list;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    recycleradapter adapter;
    List<User> mUser;
    List<chatlist> userlist;

    public Chat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_chat, container, false);

        list=view.findViewById(R.id.list2);
        list.setLayoutManager(new LinearLayoutManager(getContext()));


        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        userlist=new ArrayList<>();




        reference=FirebaseDatabase.getInstance().getReference("Chatlist").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userlist.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    chatlist chatlistt=snapshot.getValue(com.example.helpinghands.model.chatlist.class);
                    userlist.add(chatlistt);
                }
                chatList();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//
//        reference= FirebaseDatabase.getInstance().getReference("chats");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for(DataSnapshot snapshot:dataSnapshot.getChildren())
//                {
//                    chats chat=snapshot.getValue(chats.class);
//
//                    if(chat.getSender().equals(firebaseUser.getUid()))
//                    {
//                        userlist.add(chat.getReceiver());
//                    }
//                    if (chat.getReceiver().equals(firebaseUser.getUid()))
//                    {
//                        userlist.add(chat.getSender());
//                    }
//                }

//                readchats();

//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        return view;
    }

    private void chatList() {

    mUser=new ArrayList<>();
    reference=FirebaseDatabase.getInstance().getReference("Users");
    reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            mUser.clear();
            for (DataSnapshot snapshot : dataSnapshot.getChildren())
            {
                User user=snapshot.getValue(User.class);
                for (chatlist chatlistt:userlist)
                {
                    if (user.getId().equals(chatlistt.getId()))
                    {
                        mUser.add(user);
                    }
                }
            }
            adapter=new recycleradapter(mUser,getContext(),true);
                list.setAdapter(adapter);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

    }

//    private void readchats() {
//
//        mUser=new ArrayList<>();
//
//        reference=FirebaseDatabase.getInstance().getReference("Users");
//
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot snapshot:dataSnapshot.getChildren())
//                {
//                    User user=snapshot.getValue(User.class);
//
//                    for(String id:userlist)
//                    {
//                        if (user.getId().equals(id))
//                        {
////                            if (mUser.size()!=0)
////                            {
////                                for (User user1:mUser)
////                                {
////                                    if(!user.getId().equals(user1.getId()))
////                                    {
////                                        mUser.add(user);
////                                    }
////                                }
////                            }
////                            else
////                            {
//                                mUser.add(user);
////                            }
//                        }
//                    }
//
//                }
//
//                adapter=new recycleradapter(mUser,getContext(),true);
//                list.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

}
