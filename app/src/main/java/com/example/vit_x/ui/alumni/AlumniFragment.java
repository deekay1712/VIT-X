package com.example.vit_x.ui.alumni;

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

public class AlumniFragment extends Fragment {

    private RecyclerView alumniRecyclerView;
    private AlumniAdapter alumniAdapter;
    private ArrayList<AlumniData> list;

    private DatabaseReference dbReference;

    private LinearProgressIndicator lpi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alumni, container, false);

        alumniRecyclerView = view.findViewById(R.id.alumniRecyclerView);
        lpi = view.findViewById(R.id.progressBar);

        //set recycler view properties
        alumniRecyclerView.setHasFixedSize(true);
        alumniRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //init firebase
        dbReference = FirebaseDatabase.getInstance().getReference().child("Users");

        //init alumni list
        list = new ArrayList<>();

        //get all alumni
        getAlumni();

        return view;
    }

    private void getAlumni() {
        lpi.setVisibility(View.VISIBLE);
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    if(dataSnapshot.child("isVerified").getValue().toString().equals("true")){
                        AlumniData data = dataSnapshot.getValue(AlumniData.class);
                        list.add(data);
                    }
                }
                //adapter
                alumniAdapter = new AlumniAdapter(getContext(), list);
                alumniAdapter.notifyDataSetChanged();
                //set adapter to recycler view
                lpi.setVisibility(View.GONE);
                alumniRecyclerView.setAdapter(alumniAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                lpi.setVisibility(View.GONE);
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}