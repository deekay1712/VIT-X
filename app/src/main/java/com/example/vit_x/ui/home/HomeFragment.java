package com.example.vit_x.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.vit_x.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView homeRecyclerView;
    private ArrayList<PostData> list;
    private PostAdapter adapter;

    private DatabaseReference dbReference;

    private LinearProgressIndicator lpi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        homeRecyclerView = view.findViewById(R.id.homeRecyclerView);
        lpi = view.findViewById(R.id.progressBar);

        lpi.setVisibility(View.VISIBLE);

        dbReference = FirebaseDatabase.getInstance().getReference().child("Posts");

        homeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        homeRecyclerView.setHasFixedSize(true);

        getPosts();

        return view;
    }

    private void getPosts() {
        dbReference.addValueEventListener(new ValueEventListener() {
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
                homeRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                lpi.setVisibility(View.GONE);
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}