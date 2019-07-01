package com.example.artisanforum.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.artisanforum.R;
import com.example.artisanforum.adapters.VerticalRecyclerViewAdapter;
import com.example.artisanforum.models.Retrivingposts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OwnerActivity extends AppCompatActivity {
    RecyclerView mRecycler;
    private VerticalRecyclerViewAdapter mAdapter;
    private List<Retrivingposts> mRetrivingTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        mRecycler = findViewById(R.id.recyclerowner);
        mRecycler.setHasFixedSize(true);
        mRetrivingTexts = new ArrayList<>();
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("post");
        mDatabaseRef.keepSynced(true);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mRetrivingTexts.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Retrivingposts retrivingposts = dataSnapshot1.getValue(Retrivingposts.class);
                    mRetrivingTexts.add(retrivingposts);
                }
                mAdapter = new VerticalRecyclerViewAdapter(getApplicationContext(), mRetrivingTexts);
                mRecycler.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
