package adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.helpinghands.R;
import com.example.helpinghands.chat;
import com.example.helpinghands.model.User;
import com.example.helpinghands.profile;

import java.util.List;

public class recycleradapter extends RecyclerView.Adapter<recycleradapter.Viewholder> {

        List<User> mUser;
        Context mContext;
        boolean chat;

    public recycleradapter(List<User> mUser, Context mContext,boolean chat) {
        this.mUser = mUser;
        this.mContext = mContext;
        this.chat=chat;
    }

    @NonNull
    @Override
    public recycleradapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.data,viewGroup,false);
        return new recycleradapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull recycleradapter.Viewholder viewHolder, int i) {

        final User user=mUser.get(i);
        viewHolder.textview.setText(user.getUsername());
            if (user.getImageurl().equals("default"))
            {
                viewHolder.image.setImageResource(R.drawable.person);
            }
            else
            {
                Glide.with(mContext).load(user.getImageurl()).into(viewHolder.image);

            }
            final Drawable img=viewHolder.image.getDrawable();

       viewHolder.image.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                 LayoutInflater inflater=LayoutInflater.from(mContext);
                 View dialogLayout = inflater.inflate(R.layout.chat_image, null);
//                    builder.setPositiveButton("OK", null);
                 ImageView previewimage=dialogLayout.findViewById(R.id.chatimage);
                 previewimage.setImageDrawable(img);
                 builder.setView(dialogLayout);
                 AlertDialog alert=builder.create();
                 alert.show();
             }
         });

       if (chat)
       {
           if (user.getStatus().equals("online"))
           {
               viewHolder.img_on.setVisibility(View.VISIBLE);
           }

       }


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,chat.class);
                String userid=user.getId();
                intent.putExtra("userid",userid);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

     public class Viewholder extends RecyclerView.ViewHolder{

        public TextView textview,textview2;
        ImageView image;
        ImageView img_on;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            textview=itemView.findViewById(R.id.name);
//            textview2=(TextView)itemView.findViewById(R.id.msg);
            image=itemView.findViewById(R.id.img);
            img_on=itemView.findViewById(R.id.img_on);
        }
    }
}
