package com.example.ahsan.foodviewbd;

/**
 * Created by Ahsan on 6/7/2016.
 */


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.RatingBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        TextView name = (TextView) findViewById(R.id.editText);
        TextView loc = (TextView) findViewById(R.id.editText2);
        TextView contact = (TextView) findViewById(R.id.editText4);
        RatingBar ratingbar=(RatingBar) findViewById(R.id.ratingBar1);

        Button foodbtn = (Button) findViewById(R.id.button2);

        name.setText(Config.SHARED_RESTAURANT.r_name);
        loc.setText(Config.SHARED_RESTAURANT.location);
        contact.setText(Config.SHARED_RESTAURANT.contact);
        ratingbar.setRating(Config.SHARED_RESTAURANT.r_rating);

        foodbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodList();
            }
        });


        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                                   @Override
                                                   public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                                       Toast.makeText(RListActivity.this,String.valueOf(rating),Toast.LENGTH_SHORT).show();
                                                   }
                                               }
        );


    }

    public void gotoitemspage(View view)
    {
        Intent itemspage= new Intent(RListActivity.this, ItemActivity.class);
        startActivity(itemspage);

    }


    private static final String SEARCH_URL = Config.SERVER+"test/sendFoodInfo.php";
    public static final String KEY_ID = "r_id";

    public void foodList() {
        setContentView(R.layout.list_view);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SEARCH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("empty"))
                            Toast.makeText(RListActivity.this, "No food Yet!", Toast.LENGTH_LONG).show();
                        else {
                            //Toast.makeText(RListActivity.this,response,Toast.LENGTH_LONG).show();
                            listFood(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RListActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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


    ArrayList <FoodItem> fdata;

    void listFood(String response){
        fdata = new ArrayList<FoodItem>();
        ArrayList<String> list = new ArrayList<String>();
        ListView listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Config.SHARED_FOOD = fdata.get(position);
                Intent x = new Intent("android.intent.action.ITEM");
                startActivity(x);
                return;
            }
        });

        try {
            //JSONObject jsonRootObject = new JSONObject(response);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = new JSONArray(response);

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++){
                //Toast.makeText(SearchActivity.this, "restaurant",Toast.LENGTH_LONG).show();
                FoodItem r = new FoodItem();

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                r.item_no = Integer.parseInt(jsonObject.optString("item_no").toString());
                r.food_name=jsonObject.optString("food_name").toString();
                r.food_type= jsonObject.optString("food_type").toString();
                r.r_id = Integer.parseInt(jsonObject.optString("r_id"));
                r.price = Integer.parseInt(jsonObject.optString("price"));
                fdata.add(r);
                list.add(r.food_name+" ( "+r.food_type + ")" );

            }
            ArrayAdapter<String> rs = new ArrayAdapter<String>(RListActivity.this, R.layout.list_text, list);

            listView.setAdapter(rs);
        } catch (JSONException e) {e.printStackTrace();}
    }
}
