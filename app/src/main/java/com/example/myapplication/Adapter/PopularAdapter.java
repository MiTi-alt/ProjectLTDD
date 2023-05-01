package com.example.myapplication.Adapter;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Activity.ShowDetailActivity;
import com.example.myapplication.Domain.FoodDomain;
import com.example.myapplication.R;

import java.util.ArrayList;

public class PopularAdapter  extends RecyclerView.Adapter<PopularAdapter.ViewHolder> {
    ArrayList<FoodDomain> popularDomains;

    public PopularAdapter(ArrayList<FoodDomain> foodDomains) {
        this.popularDomains = foodDomains;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_popular, parent, false);
        return new ViewHolder(inflate)  ;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(popularDomains.get(position).getTitle());
        holder.fee.setText(String.valueOf(popularDomains.get(position).getFee()));

        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(popularDomains.get(position).getPic(),"drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext()).load(drawableResourceId).into(holder.pic);

        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), ShowDetailActivity.class);
                intent.putExtra("object", popularDomains.get(position));
                holder.itemView.getContext().startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return popularDomains.size();
    }


    public class ViewHolder extends  RecyclerView.ViewHolder {
        TextView title, fee;
        ImageView pic;
        TextView addBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            fee = itemView.findViewById(R.id.fee);
            pic = itemView.findViewById(R.id.picFood);
            addBtn = itemView.findViewById(R.id.addFood);
        }
    }
}
