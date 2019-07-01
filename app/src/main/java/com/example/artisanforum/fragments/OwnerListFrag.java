package com.example.artisanforum.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.artisanforum.R;
import com.example.artisanforum.adapters.OwnerListRecyclerViewAdapter;
import com.example.artisanforum.models.RetrivingOwnerList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OwnerListFrag extends Fragment {
    RecyclerView recyclerView;
    private List<RetrivingOwnerList> mRetrivingTexts;
    OwnerListRecyclerViewAdapter mAdapter;
    public OwnerListFrag() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.ownerlistfrag, container, false);
        recyclerView = v.findViewById(R.id.recyclecycle);
        recyclerView.setHasFixedSize(true);
        mRetrivingTexts = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
                mAdapter = new OwnerListRecyclerViewAdapter(getContext(), mRetrivingTexts);
                recyclerView.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return v;
    }
}
