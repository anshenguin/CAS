package com.kinitoapps.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.List;

public class InsideCategoryAdapter extends RecyclerView.Adapter<InsideCategoryAdapter.ViewHolder> {
    private Context mCtx;
    private List<FoodItem> categoryList;
    ListOfFoodActivity activity;
    Boolean isAvail;
    int id;
    public InsideCategoryAdapter(Context mCtx, List<FoodItem> categoryList) {
        this.mCtx = mCtx;
        this.categoryList = categoryList;
        activity = (ListOfFoodActivity) mCtx;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.smaller_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final FoodItem item = categoryList.get(i);
        viewHolder.textViewName.setText(item.getName());
        viewHolder.rate.setText(String.valueOf(item.getPrice()));
        id = item.getId();
        if(item.getAvail()==0)
            isAvail = false;
        else
            isAvail = true;
        DBManager db = new DBManager(mCtx);
        try {
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(db.doesExist(item.getId())==0)
            viewHolder.amount.setText("0");
        else {
            int x =db.countAmount(id);
            viewHolder.amount.setText(String.valueOf(x));
        }
        db.close();
        viewHolder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amount = Integer.parseInt(viewHolder.amount.getText().toString());
                if(amount<10){
                    amount++;
                    viewHolder.amount.setText(String.valueOf(amount));
                    DBManager dbManager = new DBManager(mCtx);
                    try {
                        dbManager.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if((amount-1)>0)
                    dbManager.update(amount,item.getId());
                    else
                    dbManager.insert(item.getName(),item.getId(),amount,item.getPrice());
                    dbManager.close();
                }
                else{
                    Toast.makeText(mCtx, "Limit Exceeded", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewHolder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amount = Integer.parseInt(viewHolder.amount.getText().toString());
                if(amount>0){
                    amount--;
                    viewHolder.amount.setText(String.valueOf(amount));
                    DBManager dbManager = new DBManager(mCtx);
                    try {
                        dbManager.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if(amount!=0)
                    dbManager.update(amount,item.getId());
                    else
                        dbManager.delete(item.getId());
                    dbManager.close();


                }
                else{
                    Toast.makeText(mCtx, "Cannot go less than zero", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView rate;
        Button plus,minus;
        TextView amount;



        ViewHolder(final View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.item_name);
            rate = itemView.findViewById(R.id.item_price);
            plus = itemView.findViewById(R.id.plus_button);
            minus = itemView.findViewById(R.id.minus_button);
            amount = itemView.findViewById(R.id.item_quantity);
            //            textViewPrice = itemView.findViewById(R.id.textViewPrice);
        }
    }
}