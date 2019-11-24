package com.kinitoapps.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<String> categoryList;
    List<FoodItem> foodList;
    List<FoodItem> arrayList;
    CategoryAdapter adapter;
    RecyclerView recyclerView;
    private static final String URL_CATEGORIES = "https://canteenautomationsystem.000webhostapp.com/api/list_of_categories.php";
    private static final String URL_INSCATEGORIES = "https://canteenautomationsystem.000webhostapp.com/api/list_of_food.php?cat=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerMain);
        categoryList = new ArrayList<>();
        arrayList = new ArrayList<>();
        foodList = new ArrayList<>();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new CategoryAdapter(MainActivity.this,categoryList,foodList);
        recyclerView.setAdapter(adapter);
        loadCategories();


    }

    private void loadCategories() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_CATEGORIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject cat = array.getJSONObject(i);

                                //adding the product to product list
                                categoryList.add(cat.getString("CAT"));
//                                loadInsideCategories(cat.getString("CAT"));
                            }
                            for (String s: categoryList
                                 ) {
                                loadInsideCategories(s);
                            }
                            CategoryAdapter adapter = new CategoryAdapter(MainActivity.this, categoryList,foodList);
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

        Volley.newRequestQueue(MainActivity.this).add(stringRequest);
    }

    private void loadInsideCategories(String category) {
//works
        Toast.makeText(this, category, Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_INSCATEGORIES+category,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);
                                foodList.add(new FoodItem(
                                        product.getInt("ID"),
                                        product.getString("NAME"),
                                        product.getInt("PRICE"),
                                        product.getInt("AVAIL")
                                ));

                                Log.v("TAGME",arrayList.toString());
                                //adding the product to product list
//                                categoryList.add(cat.getString("CAT"));
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.v("TAGME","WORK");

                    }
                });

        Volley.newRequestQueue(MainActivity.this).add(stringRequest);
    }

}
