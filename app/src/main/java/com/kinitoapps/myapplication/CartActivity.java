package com.kinitoapps.myapplication;

import android.Manifest;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    String URL_REGISTER = "http://canteenautomationsystem.000webhostapp.com/api/order_food.php";
    private DBManager dbManager;

    private ListView listView;

    final String[] from = new String[] {
            DatabaseHelper.NAME, DatabaseHelper.AMOUNT, DatabaseHelper.MONEY};

    final int[] to = new int[] {  R.id.cartitemname, R.id.cartitemamount, R.id.cartitemprice };


    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Button proceed = findViewById(R.id.proceed);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                builder.setTitle("Set Daily Limit");
                final LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.table_dialog,null);
                builder.setView(v);
                final EditText input = v.findViewById(R.id.daily_limit_value);
                input.selectAll();
                listView.setEmptyView(findViewById(R.layout.emptyview));
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int table = 1;
                        if(!isNumeric(input.getText().toString())){
                            if(TextUtils.isEmpty(input.getText().toString()))
                                dialog.dismiss();
                            else{
                                Toast.makeText(CartActivity.this,"Please enter a numeric value as the limit",Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            if(Integer.parseInt(input.getText().toString())>0 && Integer.parseInt(input.getText().toString())<10) {
                                table = Integer.parseInt(input.getText().toString());
                                try {
                                    placeSQLOrder(table);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            else
                                Toast.makeText(CartActivity.this, "Please select a table number between 1 to 10", Toast.LENGTH_SHORT).show();
                        }
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.RESULT_UNCHANGED_HIDDEN,0);
                    }
                });
                builder.show();
            }


        });
        dbManager = new DBManager(this);
        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Cursor cursor = dbManager.fetch();
        dbManager.close();
        listView = findViewById(R.id.list_view);
        adapter = new SimpleCursorAdapter(this, R.layout.activity_cartitem, cursor, from, to, 0);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }

    public boolean isNumeric(String s) {
        return s.matches("[+]?\\d*\\.?\\d+");
    }

        private void placeOrder(final int table, final int itemid, final  int itemquantity) {
            // Tag used to cancel the request
            String tag_string_req = "req_register";


            StringRequest strReq = new StringRequest(Request.Method.POST,
                    URL_REGISTER, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("TAG", "Register Response: " + response.toString());

                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");
                        if (error) {
                            // User successfully stored in MySQL
                            // Now store the user in sqlite


                            String errorMsg = jObj.getString("error_msg");
                            Toast.makeText(getApplicationContext(),
                                    errorMsg, Toast.LENGTH_LONG).show();





//                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                            // Launch login activity
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TAG", "Registration Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
//                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("tableno", String.valueOf(table));
                params.put("itemid", String.valueOf(itemid));
                params.put("itemquantity", String.valueOf(itemquantity));

                return params;

                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }

        public void placeSQLOrder(int table) throws SQLException {
        dbManager.open();
        if(dbManager.countAll()>0) {
            DatabaseHelper dbHelper = new DatabaseHelper(CartActivity.this);

            Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select * from CART", null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    int foodid = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.FOOD_ID));
                    int amount = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.AMOUNT));

                    placeOrder(table, foodid, amount);
                    dbManager.delete(foodid);
                    cursor.moveToNext();
                }
            }


            AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
            builder.setTitle("Order Placed");
            builder.setMessage("Please pay your bill to have your order accepted.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    finish();


                }
            });

            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    finish();
                }
            });

            builder.show();
        }
        else {
            Toast.makeText(CartActivity.this, "Please add items in cart to continue", Toast.LENGTH_SHORT).show();
        }

        }


}
