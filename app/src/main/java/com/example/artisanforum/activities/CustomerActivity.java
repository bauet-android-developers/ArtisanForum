package com.example.artisanforum.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.artisanforum.R;
import com.example.artisanforum.adapters.OwnerListRecyclerViewAdapter;
import com.example.artisanforum.adapters.tabpagerAdapter;
import com.example.artisanforum.models.RetrivingOwnerList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private List<RetrivingOwnerList> mRetrivingTexts;
    OwnerListRecyclerViewAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager pager = findViewById(R.id.viewpager);
        tabpagerAdapter TabPagerAdapter = new tabpagerAdapter(getSupportFragmentManager());
        pager.setAdapter(TabPagerAdapter);
        tabLayout.setupWithViewPager(pager);
        recyclerView = findViewById(R.id.recyclecycle);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PopPost.class));
                Snackbar.make(view, "Post Something", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        recyclerView.setHasFixedSize(true);
        mRetrivingTexts = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseRef.keepSynced(true);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mRetrivingTexts.clear();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
//            case R.id.action_add:
//                addSomething();
//                return true;
            case R.id.action_settings:
                startSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startSettings() {
        startActivity(new Intent(this,editProfile.class));
    }
}
