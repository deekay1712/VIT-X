package com.example.vit_x.ui.events;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vit_x.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewAdapter> {

    private Context context;
    private ArrayList<EventData> list;

    public EventAdapter(Context context, ArrayList<EventData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public EventViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.events_feed_layout, parent, false);
        return new EventViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewAdapter holder, int position) {

        EventData currentItem = list.get(position);

        holder.eventDescription.setText(currentItem.getDescription());

        try {
            Picasso.get().load(currentItem.getImage()).into(holder.eventImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.eventRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentItem.getLink()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class EventViewAdapter extends RecyclerView.ViewHolder {

        private ImageView eventImageView;
        private TextView eventDescription;
        private Button eventRegisterBtn;

        public EventViewAdapter(@NonNull View itemView) {
            super(itemView);
            eventImageView = itemView.findViewById(R.id.eventImageView);
            eventDescription = itemView.findViewById(R.id.eventDescription);
            eventRegisterBtn = itemView.findViewById(R.id.eventRegisterBtn);
        }
    }
}
