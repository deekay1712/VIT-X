package com.example.vit_x.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vit_x.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    //Views from xml
    private ImageView profileDisplayPicture, profileLinkedInBtn, profileEmailBtn;
    private TextView profileName, profileCollegeInfo;
    private RecyclerView profileRecyclerView;

    private LinearProgressIndicator lpi;

    private String name, collegeInfo, email, linkedinUrl, displayPictureUrl;
    private ArrayList<PostData> list;
    private PostAdapter adapter;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference dbReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        dbReference = FirebaseDatabase.getInstance().getReference();

        //init views
        profileDisplayPicture = view.findViewById(R.id.profileDisplayPicture);
        profileLinkedInBtn = view.findViewById(R.id.profileLinkedinBtn);
        profileEmailBtn = view.findViewById(R.id.profileMailBtn);
        profileName = view.findViewById(R.id.profileName);
        profileCollegeInfo = view.findViewById(R.id.profileCollegeInfo);
        profileRecyclerView = view.findViewById(R.id.profileRecyclerView);
        lpi = view.findViewById(R.id.progressBar);

        //setting recycler view
        profileRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        profileRecyclerView.setHasFixedSize(true);

        //set profile data
        getProfile();

        return view;
    }

    private void getProfile() {
        lpi.setVisibility(View.VISIBLE);
        Query queryProfile = dbReference.child("Users").child(user.getUid());
        queryProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                name = "" + snapshot.child("name").getValue();
                collegeInfo = "" + snapshot.child("programme").getValue() + " " + snapshot.child("passOutYear").getValue();
                displayPictureUrl = "" + snapshot.child("imageUrl").getValue();
                linkedinUrl = "" + snapshot.child("linkedinUrl").getValue();
                email = "mailto:" + snapshot.child("email").getValue();

                try {
                    Picasso.get().load(displayPictureUrl).into(profileDisplayPicture);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                profileName.setText(name);
                profileCollegeInfo.setText(collegeInfo);

                profileEmailBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(email);
                        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                        startActivity(intent);
                    }
                });

                profileLinkedInBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri uri = Uri.parse(linkedinUrl);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                lpi.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

        //set posts in profile
        getProfilePosts();
    }

    private void getProfilePosts() {
        Query queryPosts = dbReference.child("Posts").orderByChild("uid").equalTo(user.getUid());
        queryPosts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    PostData data = dataSnapshot.getValue(PostData.class);
                    list.add(0, data);
                }

                adapter = new PostAdapter(getContext(), list);
                adapter.notifyDataSetChanged();

                lpi.setVisibility(View.GONE);
                profileRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                lpi.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}