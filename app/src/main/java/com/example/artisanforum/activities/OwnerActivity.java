package com.example.artisanforum.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.artisanforum.R;
import com.example.artisanforum.models.RetrivingOwnerList;
import com.example.artisanforum.adapters.VerticalRecyclerViewAdapter;
import com.example.artisanforum.models.Retrivingposts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OwnerActivity extends AppCompatActivity {
    RecyclerView mRecycler;
    ImageView image_profile, customer;
    TextView username;
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
        customer = findViewById(R.id.customer);
        username = findViewById(R.id.username);
        mRetrivingTexts = new ArrayList<>();
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("post");
        mDatabaseRef.keepSynced(true);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mRetrivingTexts.clear();
                RetrivingOwnerList user = dataSnapshot.getValue(RetrivingOwnerList.class);
                username.setText(user.getName());
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
        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OwnerActivity.this, CustomerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.item2:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(OwnerActivity.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                return true;
        }
        return true;
    }
}
