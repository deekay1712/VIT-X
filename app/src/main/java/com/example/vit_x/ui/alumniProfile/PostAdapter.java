package com.example.vit_x.ui.alumniProfile;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vit_x.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewAdapter> {

    private Context context;
    private ArrayList<PostData> list;

    public PostAdapter(Context context, ArrayList<PostData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PostViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.posts_feed_layout, parent, false);
        return new PostViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewAdapter holder, int position) {

        PostData currentItem = list.get(position);

        String time = "";
        Date past=null, now;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
        try {
            past = formatter.parse(currentItem.getPostedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        now = new Date();

        if(past!=null) {
            long dateDiff = now.getTime() - past.getTime();
            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if(second<60){
                time = second + "s" + " ago";
            } else if(minute<60){
                time = minute + "min" + " ago";
            } else if(hour<24){
                time = hour + "hr" + " ago";
            } else if(day<7){
                time = day +"d" +" ago";
            }else{
                if(day>360){
                    time = (int)(day/360)+"y"+" ago";
                }else if(day>30){
                    time = (int)(day/30)+"m"+" ago";
                }else {
                    time = (int)(day/7)+"w"+" ago";
                }
            }
        }

        //set data
        holder.postAuthorName.setText(currentItem.getAuthor());
        holder.postTime.setText(time);
        holder.postDescription.setText(currentItem.getDescription());

        try {
            Picasso.get().load(currentItem.getAuthorImageUrl()).into(holder.postAuthorImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Picasso.get().load(currentItem.getImageUrl()).into(holder.postImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PostViewAdapter extends RecyclerView.ViewHolder {

        private ImageView postAuthorImageView, postImageView;
        private TextView postAuthorName, postTime, postDescription;
        private LinearLayout postHeader;

        public PostViewAdapter(@NonNull View itemView) {
            super(itemView);
            postAuthorName = itemView.findViewById(R.id.postAuthorName);
            postAuthorImageView  =itemView.findViewById(R.id.postAuthorImageView);
            postImageView = itemView.findViewById(R.id.postImageView);
            postTime = itemView.findViewById(R.id.postTime);
            postDescription = itemView.findViewById(R.id.postDescription);
            postHeader = itemView.findViewById(R.id.postHeader);
        }
    }
}
