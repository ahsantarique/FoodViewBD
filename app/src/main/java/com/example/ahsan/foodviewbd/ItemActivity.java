package com.example.ahsan.foodviewbd;

/**
 * Created by Ahsan on 6/7/2016.
 */


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ItemActivity extends Activity {


    int r_id = Config.SHARED_FOOD.r_id;
    String contact;

    String email=Config.EMAIL_SHARED_PREF;
    String username;
    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        TextView fname = (TextView) findViewById(R.id.textView6);
        TextView price = (TextView) findViewById(R.id.textView7);

        fname.setText("Food Item: "+Config.SHARED_FOOD.food_name);
        price.setText("Price : "+Config.SHARED_FOOD.price);


        RatingBar ratingbar=(RatingBar) findViewById(R.id.ratingBar);
        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                                   @Override
                                                   public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                                       Toast.makeText(ItemActivity.this,String.valueOf(rating),Toast.LENGTH_SHORT).show();
                                                   }
                                               }
        );
        Button b = (Button)findViewById(R.id.button1);

        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog.Builder builder = new AlertDialog.Builder(ItemActivity.this);

                builder.setTitle("Order");

                builder.setMessage("are you sure to order ? ");

                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        getRestaurant();
                        finish();
                    }
                });

                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });


                AlertDialog alert = builder.create();

                alert.show();
            }
        });
    }


    public void sendSMS(String phoneNo, String msg){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    public void getRestaurant(){
        final String SEARCH_URL = Config.SERVER+"test/getRestaurant.php";
        final String KEY_ID = "r_id";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SEARCH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("empty"))
                            Toast.makeText(ItemActivity.this, "Unsuccessful!", Toast.LENGTH_LONG).show();
                        else {
                            Toast.makeText(ItemActivity.this, response, Toast.LENGTH_LONG).show();
                            parseRestaurant(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ItemActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_ID, String.valueOf(Config.SHARED_RESTAURANT.r_id));
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    void parseRestaurant(String response){
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response);
            for(int i=0; i < jsonArray.length(); i++){
                //Toast.makeText(SearchActivity.this, "restaurant",Toast.LENGTH_LONG).show();
                FoodItem r = new FoodItem();

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                contact = jsonObject.optString("contact");


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendSMS(contact, "I want a"+Config.SHARED_FOOD.food_name);
        Toast.makeText(ItemActivity.this, "sending message to" +contact, Toast.LENGTH_LONG).show();
        //Iterate the jsonArray and print the info of JSONObjects

    }
    public void getUser(){
        final String SEARCH_URL = Config.SERVER+"test/getUser.php";
        final String KEY_ID = "email";

    StringRequest stringRequest = new StringRequest(Request.Method.POST, SEARCH_URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("empty"))
                        Toast.makeText(ItemActivity.this, "Unsuccessful!", Toast.LENGTH_LONG).show();
                    else {
                        Toast.makeText(ItemActivity.this, response, Toast.LENGTH_LONG).show();
                        parseRestaurant(response);
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ItemActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }
            }) {
        @Override
        protected Map<String, String> getParams() {
            Map<String, String> params = new HashMap<String, String>();
            params.put(KEY_ID, String.valueOf(Config.EMAIL_SHARED_PREF));
            return params;
        }

    };

    RequestQueue requestQueue = Volley.newRequestQueue(this);
    requestQueue.add(stringRequest);

}

    void parseUser(String response){
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response);
            for(int i=0; i < jsonArray.length(); i++){
                //Toast.makeText(SearchActivity.this, "restaurant",Toast.LENGTH_LONG).show();
                FoodItem r = new FoodItem();

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                contact = jsonObject.optString("user_name");
                location = jsonObject.optString("location");


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendSMS(contact, "I want a"+Config.SHARED_FOOD.food_name);
        Toast.makeText(ItemActivity.this, "sending message to" +contact, Toast.LENGTH_LONG).show();
        //Iterate the jsonArray and print the info of JSONObjects

    }
}
