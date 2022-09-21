package com.example.vit_x.ui.events;

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

public class EventsFragment extends Fragment {

    private RecyclerView eventRecyclerView;
    private LinearProgressIndicator eventProgressBar;
    private ArrayList<EventData> list;
    private EventAdapter adapter;

    private DatabaseReference dbReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        eventRecyclerView = view.findViewById(R.id.eventRecyclerView);
        eventProgressBar = view.findViewById(R.id.eventProgressBar);

        eventProgressBar.setVisibility(View.VISIBLE);

        dbReference = FirebaseDatabase.getInstance().getReference().child("Events");

        eventRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventRecyclerView.setHasFixedSize(true);

        getEvent();

        return view;
    }

    private void getEvent() {
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list = new ArrayList<>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    EventData data = dataSnapshot.getValue(EventData.class);
                    list.add(0,data);
                }
                adapter = new EventAdapter(getContext(), list);
                adapter.notifyDataSetChanged();

                eventProgressBar.setVisibility(View.INVISIBLE);
                eventRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                eventProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}