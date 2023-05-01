package com.example.myapplication.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Domain.FoodDomain;
import com.example.myapplication.Helper.ManagementCart;
import com.example.myapplication.Interface.ChangeNameberItemsListener;
import com.example.myapplication.R;

import java.util.ArrayList;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {
    private ArrayList<FoodDomain> foodDomains;
    private ManagementCart managementCart;
    private ChangeNameberItemsListener changeNameberItemsListener;

    public CartListAdapter(ArrayList<FoodDomain> foodDomains, Context context, ChangeNameberItemsListener changeNameberItemsListener) {
        this.foodDomains = foodDomains;
        this.managementCart = new ManagementCart(context);
        this.changeNameberItemsListener = changeNameberItemsListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder,parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(foodDomains.get(position).getTitle());
        holder.feeEachItem.setText(String.valueOf(foodDomains.get(position).getFee()));
        holder.totalEachItem.setText(String.valueOf(Math.round(foodDomains.get(position).getNumberInCart()*foodDomains.get(position).getFee()*100) /100));
        holder.num.setText(String.valueOf(foodDomains.get(position).getNumberInCart()));
        int drawableResourceId = holder.itemView.getContext().getResources().getIdentifier(foodDomains.get(position).getPic(),"drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext()).load(drawableResourceId).into(holder.pic);
        holder.plusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                managementCart.plusNumberFood(foodDomains, position, new ChangeNameberItemsListener() {
                    @Override
                    public void changed() {
                        notifyDataSetChanged();
                        changeNameberItemsListener.changed();
                    }
                });
            }
        });
        holder.minusItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                managementCart.minusNumberFood(foodDomains, position, new ChangeNameberItemsListener() {
                    @Override
                    public void changed() {
                        notifyDataSetChanged();
                        changeNameberItemsListener.changed();
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return foodDomains.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView title, feeEachItem;
        ImageView pic, plusItem, minusItem;
        TextView totalEachItem, num;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTxt);
            feeEachItem = itemView.findViewById(R.id.feeEachItem);
            pic = itemView.findViewById(R.id.imageTxt);
            totalEachItem = itemView.findViewById(R.id.totalEach);
            num = itemView.findViewById(R.id.textView16);
            plusItem = itemView.findViewById(R.id.plusCartBtn);
            minusItem = itemView.findViewById(R.id.minusCartBtn);
        }
    }

}
