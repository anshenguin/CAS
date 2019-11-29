package com.kinitoapps.myapplication;

import android.content.Context;
import android.content.Intent;
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
    MainActivity activity;
    public CategoryAdapter(Context mCtx, List<String> categoryList) {
        this.mCtx = mCtx;
        this.categoryList = categoryList;
        activity = (MainActivity) mCtx;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.bigger_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final String cat = categoryList.get(i);
        viewHolder.textViewCategory.setText(cat);
        viewHolder.textViewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mCtx,ListOfFoodActivity.class);
                i.putExtra("category",cat);
                mCtx.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCategory;

         ViewHolder(final View itemView) {
            super(itemView);

            textViewCategory = itemView.findViewById(R.id.foodname);
//            textViewPrice = itemView.findViewById(R.id.textViewPrice);
        }
    }


}
