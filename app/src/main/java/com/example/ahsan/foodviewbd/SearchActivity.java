package com.example.ahsan.foodviewbd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ahsan on 5/31/2016.
 */
public class SearchActivity extends AppCompatActivity {

    EditText searchtext;
    Button searchButton;
    Spinner searchCategory;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchtext = (EditText) findViewById(R.id.searchtext);
        searchButton=(Button) findViewById(R.id.searchbtn);
        searchCategory=(Spinner) findViewById(R.id.dropDown);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

    }

    private static final String SEARCH_URL = Config.SERVER+"test/volleySearch.php";

    public static final String KEY_CATEGORY = "category";
    public static final String KEY_TEXT = "text";

    void search(){
        final String cat = (String) searchCategory.getSelectedItem();
        final String search = searchtext.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SEARCH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("empty")) Toast.makeText(SearchActivity.this,"Search Didn't Match Anything!",Toast.LENGTH_LONG).show();
                        else{
                            //Toast.makeText(SearchActivity.this,response,Toast.LENGTH_LONG).show();
                            showResult(cat,response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SearchActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_CATEGORY,cat);
                params.put(KEY_TEXT,search);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    ArrayList <Restaurant> rdata;
    ArrayList <FoodIndex> fdata;


    void showResult(String category, String response){
        //System.out.println(response);
        setContentView(R.layout.list_view);

        if(category.equals("Restaurant")){

            listRestaurant(response);

        }

        if(category.equals("Food Item")){
            listFood(response);
        }
        if(category.equals("Food Type")){
            listFood(response);
        }
        if(category.equals("Location")){
            listRestaurant(response);
        }

    }

    void listRestaurant(String response){
        rdata = new ArrayList<Restaurant>();
        ArrayList<String> list = new ArrayList<String>();
        final ListView listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SearchActivity.this,"working"+position,Toast.LENGTH_LONG).show();

                Config.SHARED_RESTAURANT = rdata.get(position);
                Intent x = new Intent("android.intent.action.RLIST");
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
                Restaurant r = new Restaurant();

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                r.r_id = Integer.parseInt(jsonObject.optString("r_id").toString());
                r.r_name=jsonObject.optString("r_name").toString();
                r.r_rating= Integer.parseInt(jsonObject.optString("r_rating").toString());
                r.location = jsonObject.optString("location").toString();
                r.vote_count = Integer.parseInt(jsonObject.optString("vote_count").toString());
                r.contact= jsonObject.optString("contact").toString();

                rdata.add(r);
                list.add(r.r_name+", "+r.location);
            }
            ArrayAdapter<String> rs = new ArrayAdapter<String>(SearchActivity.this, R.layout.list_text, list);

            listView.setAdapter(rs);
        } catch (JSONException e) {e.printStackTrace();}
    }









    void listFood(String response){
        fdata = new ArrayList<FoodIndex>();
        ArrayList<String> list = new ArrayList<String>();
        ListView listView = (ListView) findViewById(R.id.listView);

        try {
            //JSONObject jsonRootObject = new JSONObject(response);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = new JSONArray(response);

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++){
                //Toast.makeText(SearchActivity.this, "restaurant",Toast.LENGTH_LONG).show();
                FoodIndex r = new FoodIndex();

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                r.item_no = Integer.parseInt(jsonObject.optString("item_no").toString());
                r.food_name=jsonObject.optString("food_name").toString();
                r.food_type= jsonObject.optString("food_type").toString();

                fdata.add(r);
                list.add(r.food_name+" ( "+r.food_type + ")" );

            }
            ArrayAdapter<String> rs = new ArrayAdapter<String>(SearchActivity.this, R.layout.list_text, list);

            listView.setAdapter(rs);
        } catch (JSONException e) {e.printStackTrace();}
    }
}
