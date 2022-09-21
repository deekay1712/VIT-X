package com.example.vit_x.ui.alumniProfile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vit_x.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AlumniProfileActivity extends AppCompatActivity {

    private DatabaseReference dbReference;

    private ImageView alumniProfilePicture, alumniProfileLinkedinBtn, alumniProfileEmailBtn;
    private TextView alumniProfileName, alumniProfileCollegeInfo;
    private RecyclerView alumniProfileRecyclerView;

    private ArrayList<PostData> list;
    private PostAdapter adapter;

    private MaterialToolbar topAppBar;

    private String uid, alumniName, alumniCollegeInfo, alumniPictureUrl, alumniLinkedinUrl, alumniEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumni_profile);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        dbReference = FirebaseDatabase.getInstance().getReference();

        //init views
        alumniProfilePicture = findViewById(R.id.alumniProfilePicture);
        alumniProfileLinkedinBtn = findViewById(R.id.alumniProfileLinkedinBtn);
        alumniProfileEmailBtn = findViewById(R.id.alumniProfileEmailBtn);
        alumniProfileName = findViewById(R.id.alumniProfileName);
        alumniProfileCollegeInfo = findViewById(R.id.alumniProfileCollegeInfo);
        alumniProfileRecyclerView = findViewById(R.id.alumniProfileRecyclerView);
        topAppBar = findViewById(R.id.topAppBar);

        //setting recycler view
        alumniProfileRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        alumniProfileRecyclerView.setHasFixedSize(true);

        //get & set profile details
        Query queryProfile = dbReference.child("Users").child(uid);
        queryProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                alumniName = "" + snapshot.child("name").getValue();
                alumniCollegeInfo = "" + snapshot.child("programme").getValue() + " " + snapshot.child("passOutYear").getValue();
                alumniPictureUrl = "" + snapshot.child("imageUrl").getValue();
                alumniLinkedinUrl = "" + snapshot.child("linkedinUrl").getValue();
                alumniEmail = "mailto:" + snapshot.child("email").getValue();

                //set data
                try {
                    Picasso.get().load(alumniPictureUrl).into(alumniProfilePicture);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                alumniProfileName.setText(alumniName);
                alumniProfileCollegeInfo.setText(alumniCollegeInfo);

                alumniProfileEmailBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(alumniEmail);
                        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                        startActivity(intent);
                    }
                });

                alumniProfileLinkedinBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(alumniLinkedinUrl);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AlumniProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //get alumni posts
        getAlumniPosts();
    }

    private void getAlumniPosts() {
        Query queryPosts = dbReference.child("Posts").orderByChild("uid").equalTo(uid);
        queryPosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PostData data = dataSnapshot.getValue(PostData.class);
                    list.add(0, data);
                }

                adapter = new PostAdapter(AlumniProfileActivity.this, list);
                adapter.notifyDataSetChanged();

                alumniProfileRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}