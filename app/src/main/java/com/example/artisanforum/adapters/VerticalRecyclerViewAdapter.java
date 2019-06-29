package com.example.artisanforum.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.artisanforum.R;
import com.example.artisanforum.models.Retrivingposts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VerticalRecyclerViewAdapter extends
        RecyclerView.Adapter<VerticalRecyclerViewAdapter.VerticalTextHolder> {
    private Context mContext;
    private List<Retrivingposts> mRetrivingTexts;
    public RecyclerView newRecycler;
    DatabaseReference dataRef;
    private ArrayList<String> images;
    private HorizontaRecyclerViewAdapter mAdapter;

    public VerticalRecyclerViewAdapter(Context context, List<Retrivingposts> retrivingTexts) {
        mContext = context;
        mRetrivingTexts = retrivingTexts;
    }

    @NonNull
    @Override
    public VerticalTextHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_vertical,
                parent, false);
        return new VerticalTextHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VerticalTextHolder holder, int position) {
        final Retrivingposts retrivingTextsCurrent = mRetrivingTexts.get(position);
        holder.textViewDesc.setText(retrivingTextsCurrent.getDescrip());
        holder.textViewstamp.setText(retrivingTextsCurrent.getTimestamp());
        holder.callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + retrivingTextsCurrent.getTimestamp()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                Toast.makeText(mContext, retrivingTextsCurrent.getTimestamp(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        String d = retrivingTextsCurrent.getTimestamp();
        dataRef = FirebaseDatabase.getInstance().getReference().child("post");
        dataRef.keepSynced(true);
        images = new ArrayList<>();
        newRecycler.setHasFixedSize(true);
        newRecycler.setLayoutManager(new GridLayoutManager(mContext, 2));
        newRecycler.setItemAnimator(new DefaultItemAnimator());
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.hasChildren()) {
                        images.clear();
                        Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
                        for (DataSnapshot snapshot : snapshots) {
                            images.add(snapshot.getValue(String.class));
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        mAdapter = new HorizontaRecyclerViewAdapter(mContext, images);
        newRecycler.setAdapter(mAdapter);
    }

    @Override
    public int getItemCount() {
        return mRetrivingTexts.size();
    }

    class VerticalTextHolder extends RecyclerView.ViewHolder {
        TextView textViewDesc, textViewstamp;
        ImageButton callbtn;

        VerticalTextHolder(@NonNull View itemView) {
            super(itemView);
            textViewDesc = itemView.findViewById(R.id.text_view_name);
            textViewstamp = itemView.findViewById(R.id.timestamp);
            callbtn = itemView.findViewById(R.id.phnbtn);
            newRecycler = (RecyclerView) itemView.findViewById(R.id.recyclerview1);
        }
    }
}
