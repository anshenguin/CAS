package com.kinitoapps.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListOfFoodActivity extends AppCompatActivity {
    private static final String URL_INSCATEGORIES = "https://canteenautomationsystem.000webhostapp.com/api/list_of_food.php?cat=";
    RecyclerView recyclerView;
    ArrayList<FoodItem> foodList;
    InsideCategoryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_food);
        String cat = getIntent().getStringExtra("category");
        recyclerView = findViewById(R.id.recyclerfood);
        foodList = new ArrayList<>();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ListOfFoodActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new InsideCategoryAdapter(ListOfFoodActivity.this,foodList);
        recyclerView.setAdapter(adapter);
        loadInsideCategories(cat);

    }

    private void loadInsideCategories(String category) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_INSCATEGORIES+category,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {
                                Log.v("tagged","running ith time: "+i);

                                JSONObject product = array.getJSONObject(i);
                                foodList.add(new FoodItem(
                                        product.getInt("ID"),
                                        product.getString("NAME"),
                                        product.getInt("PRICE"),
                                        product.getInt("AVAIL")
                                ));

                            }

                            InsideCategoryAdapter adapter = new InsideCategoryAdapter(ListOfFoodActivity.this, foodList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        Volley.newRequestQueue(ListOfFoodActivity.this).add(stringRequest);
    }

}
