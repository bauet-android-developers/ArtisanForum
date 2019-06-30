package com.example.artisanforum.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.artisanforum.R;
import com.example.artisanforum.adapters.OwnerListRecyclerViewAdapter;
import com.example.artisanforum.adapters.VerticalRecyclerViewAdapter;
import com.example.artisanforum.models.RetrivingOwnerList;
import com.example.artisanforum.models.Retrivingposts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OwnerActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private List<RetrivingOwnerList> mRetrivingTexts;
    OwnerListRecyclerViewAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recyclecycle);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        recyclerView.setHasFixedSize(true);
        mRetrivingTexts = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("post");
        mDatabaseRef.keepSynced(true);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    RetrivingOwnerList retrivingposts = dataSnapshot1.getValue(RetrivingOwnerList.class);
                    mRetrivingTexts.add(retrivingposts);
                }
                mAdapter = new OwnerListRecyclerViewAdapter(getApplicationContext(), mRetrivingTexts);
                recyclerView.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
