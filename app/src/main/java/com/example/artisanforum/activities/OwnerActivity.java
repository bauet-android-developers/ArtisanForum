package com.example.artisanforum.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

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
    private DatabaseReference mDatabaseRef;
    private VerticalRecyclerViewAdapter mAdapter;
    private List<Retrivingposts> mRetrivingTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        final Vibrator v = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OwnerActivity.this, PopPost.class));
                Snackbar.make(view, "Post Something", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        v.vibrate(100);
        mRecycler = findViewById(R.id.recyclerview);
        mRecycler.setHasFixedSize(true);
        mRetrivingTexts = new ArrayList<>();
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("post");
        mDatabaseRef.keepSynced(true);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

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
