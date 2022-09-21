package com.example.vit_x.ui.alumni;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vit_x.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AlumniAdapter extends RecyclerView.Adapter<AlumniAdapter.AlumniViewAdapter> {

    private Context context;
    private ArrayList<AlumniData> list;

    public AlumniAdapter(Context context, ArrayList<AlumniData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AlumniViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.alumni_feed_layout, parent, false);
        return new AlumniViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlumniViewAdapter holder, int position) {
        AlumniData currentItem = list.get(position);

        //set data
        holder.alumniName.setText(currentItem.getName());
        holder.alumniCollegeInfo.setText(String.format("%s %s", currentItem.getProgramme(), currentItem.getPassOutYear()));

        try {
            Picasso.get().load(currentItem.getImageUrl()).into(holder.alumniDisplayPicture);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //handle item click
        holder.alumniLinkedinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(currentItem.getLinkedinUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });

        holder.alumniEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("mailto:"+currentItem.getEmail());
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AlumniViewAdapter extends RecyclerView.ViewHolder {

        ImageView alumniDisplayPicture, alumniLinkedinBtn, alumniEmailBtn;
        TextView alumniName, alumniCollegeInfo;

        public AlumniViewAdapter(@NonNull View itemView) {
            super(itemView);
            //init views
            alumniDisplayPicture = itemView.findViewById(R.id.alumniDisplayPicture);
            alumniName = itemView.findViewById(R.id.alumniName);
            alumniCollegeInfo = itemView.findViewById(R.id.alumniCollegeInfo);
            alumniLinkedinBtn = itemView.findViewById(R.id.alumniLinkedinBtn);
            alumniEmailBtn = itemView.findViewById(R.id.alumniEmailBtn);

        }
    }
}
