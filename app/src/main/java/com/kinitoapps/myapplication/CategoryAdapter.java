package com.kinitoapps.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private Context mCtx;
    private List<String> categoryList;
    private List<FoodItem> foodList;
    MainActivity activity;
    public CategoryAdapter(Context mCtx, List<String> categoryList, List<FoodItem> foodList) {
        this.mCtx = mCtx;
        this.categoryList = categoryList;
        activity = (MainActivity) mCtx;
        this.foodList = foodList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.bigger_item_layout, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String cat = categoryList.get(i);
        viewHolder.textViewCategory.setText(cat);
        InsideCategoryAdapter adapter;
        adapter = new InsideCategoryAdapter(mCtx,foodList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mCtx);
        viewHolder.recyclerView.setLayoutManager(mLayoutManager);
        viewHolder.recyclerView.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCategory;
        RecyclerView recyclerView;

         ViewHolder(final View itemView) {
            super(itemView);

            textViewCategory = itemView.findViewById(R.id.foodname);
            recyclerView = itemView.findViewById(R.id.foodRecyclerView);
//            textViewPrice = itemView.findViewById(R.id.textViewPrice);
        }
    }
}
