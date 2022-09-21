package com.example.vit_x.ui.profile;

import android.content.Context;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vit_x.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
        View view = LayoutInflater.from(context).inflate(R.layout.profile_post_feed_layout, parent, false);
        return new PostViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewAdapter holder, int position) {
        PostData currentItem = list.get(position);

        String time = "";
        Date past = null, now;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
        try {
            past = formatter.parse(currentItem.getPostedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        now = new Date();

        if (past != null) {
            long dateDiff = now.getTime() - past.getTime();
            long second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            long minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            long hour = TimeUnit.MILLISECONDS.toHours(dateDiff);
            long day = TimeUnit.MILLISECONDS.toDays(dateDiff);

            if (second < 60) {
                time = second + "s" + " ago";
            } else if (minute < 60) {
                time = minute + "min" + " ago";
            } else if (hour < 24) {
                time = hour + "hr" + " ago";
            } else if (day < 7) {
                time = day + "d" + " ago";
            } else {
                if (day > 360) {
                    time = (int) (day / 360) + "y" + " ago";
                } else if (day > 30) {
                    time = (int) (day / 30) + "m" + " ago";
                } else {
                    time = (int) (day / 7) + "w" + " ago";
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

        holder.deletePostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.deletePostBtn.setEnabled(false);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to delete the post?");
                builder.setCancelable(true);
                builder.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child("Posts");
                                StorageReference sReference = FirebaseStorage.getInstance().getReference().child("Posts");
                                String image = currentItem.getImageUrl().substring(78, 118);
                                dbReference.child(currentItem.getPid()).removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                try {
                                                    sReference.child(image).delete();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                Toast.makeText(context, "Post Deleted", Toast.LENGTH_SHORT).show();
                                                holder.deletePostBtn.setEnabled(true);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                holder.deletePostBtn.setEnabled(true);
                                            }
                                        });
//                                notifyItemRemoved(position);
                                notifyItemRemoved(holder.getAdapterPosition());
                            }
                        }
                );
                builder.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                holder.deletePostBtn.setEnabled(true);
                            }
                        }
                );

                AlertDialog dialog = null;
                try {
                    dialog = builder.create();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(dialog!=null)
                    dialog.show();
            }

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PostViewAdapter extends RecyclerView.ViewHolder {

        private ImageView postAuthorImageView, postImageView;
        private TextView postAuthorName, postTime, postDescription;
        private LinearLayout postHeader;
        private Button deletePostBtn;

        public PostViewAdapter(@NonNull View itemView) {
            super(itemView);
            postAuthorName = itemView.findViewById(R.id.postAuthorName);
            postAuthorImageView = itemView.findViewById(R.id.postAuthorImageView);
            postImageView = itemView.findViewById(R.id.postImageView);
            postTime = itemView.findViewById(R.id.postTime);
            postDescription = itemView.findViewById(R.id.postDescription);
            postHeader = itemView.findViewById(R.id.postHeader);
            deletePostBtn = itemView.findViewById(R.id.deletePostBtn);
        }
    }
}
