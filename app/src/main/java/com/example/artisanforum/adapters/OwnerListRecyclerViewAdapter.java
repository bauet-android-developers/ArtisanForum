package com.example.artisanforum.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.artisanforum.R;
import com.example.artisanforum.models.RetrivingOwnerList;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OwnerListRecyclerViewAdapter extends
        RecyclerView.Adapter<OwnerListRecyclerViewAdapter.OwnerListHolder> {
    public OwnerListRecyclerViewAdapter(Context mContext, List<RetrivingOwnerList> mRetrivingUsers) {
        this.mContext = mContext;
        this.mRetrivingUsers = mRetrivingUsers;
    }

    private Context mContext;
    private List<RetrivingOwnerList> mRetrivingUsers;

    @NonNull
    @Override
    public OwnerListRecyclerViewAdapter.OwnerListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.ownerlist, viewGroup, false);
        return new OwnerListHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OwnerListRecyclerViewAdapter.OwnerListHolder holder, int position) {
        final RetrivingOwnerList ownerList = mRetrivingUsers.get(position);
        holder.ownerName.setText("Name: " + ownerList.getName());
        holder.ownerType.setText("Type: " + ownerList.getType());
        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + ownerList.getPhone()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        if (ownerList.getProimg() == "null") {
            holder.proImg.setBackgroundResource(R.drawable.ic_man);

        } else {
            Picasso.with(this.mContext)
                    .load(ownerList.getProimg())
                    .centerCrop()
                    .fit()
                    .into(holder.proImg);
        }
    }

    @Override
    public int getItemCount() {
        return mRetrivingUsers.size();
    }

    class OwnerListHolder extends RecyclerView.ViewHolder {
        CircularImageView proImg;
        TextView ownerName, ownerType;
        ImageButton btnCall;

        OwnerListHolder(@NonNull View itemView) {
            super(itemView);
            proImg = itemView.findViewById(R.id.profileid);
            ownerName = itemView.findViewById(R.id.ownername);
            ownerType = itemView.findViewById(R.id.ownertype);
            btnCall = itemView.findViewById(R.id.phnbtn);

        }
    }
}
