package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.helpinghands.R;
import com.example.helpinghands.model.User;
import com.example.helpinghands.model.chats;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class chatadapter extends RecyclerView.Adapter<chatadapter.Viewholder> {


    public static final int Msg_left=0;
    public static final int Msg_right=1;
    List<chats> mChat;
    Context mContext;
    String imageurl;

    FirebaseUser firebaseUser;


    public chatadapter(List<chats> mChat, Context mContext,String imageurl) {
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageurl=imageurl;

    }

    @NonNull
    @Override
    public chatadapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i==Msg_right)
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_right, viewGroup, false);
            return new chatadapter.Viewholder(view);
        }
        else
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_left, viewGroup, false);
            return new chatadapter.Viewholder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull chatadapter.Viewholder viewHolder, int i) {


        chats chat=mChat.get(i);

        viewHolder.displaymessage.setText(chat.getMessage());

        if (imageurl.equals("default"))
        {
            viewHolder.image.setImageResource(R.drawable.eight);
        }
        else
        {
            Glide.with(mContext).load(imageurl).into(viewHolder.image);
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        public TextView displaymessage;
        ImageView image;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            displaymessage = itemView.findViewById(R.id.message_name);
//            textview2 = (TextView) itemView.findViewById(R.id.msg);
            image = itemView.findViewById(R.id.message_img);
        }
    }

    @Override
    public int getItemViewType(int position) {

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        String currentuser=firebaseUser.getUid();

        if (mChat.get(position).getSender().equals(currentuser))
        {
            return Msg_right;
        }
        else
        {
            return Msg_left;
        }
    }
}
