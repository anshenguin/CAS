package com.kinitoapps.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class InsideCategoryAdapter extends RecyclerView.Adapter<InsideCategoryAdapter.ViewHolder> {
    private Context mCtx;
    private List<FoodItem> categoryList;
    MainActivity activity;
    Boolean isAvail;
    int id;
    public InsideCategoryAdapter(Context mCtx, List<FoodItem> categoryList) {
        this.mCtx = mCtx;
        this.categoryList = categoryList;
        activity = (MainActivity) mCtx;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.smaller_item_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final FoodItem item = categoryList.get(i);
        viewHolder.textViewName.setText(item.getName());
        viewHolder.rate.setText(String.valueOf(item.getPrice()));
        id = item.getId();
        if(item.getAvail()==0)
            isAvail = false;
        else
            isAvail = true;


    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView rate;



        ViewHolder(final View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.item_name);
            rate = itemView.findViewById(R.id.item_price);
//            textViewPrice = itemView.findViewById(R.id.textViewPrice);
        }
    }
}
