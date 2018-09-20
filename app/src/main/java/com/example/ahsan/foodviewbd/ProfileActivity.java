package com.example.ahsan.foodviewbd;

/**
 * Created by Ahsan on 5/30/2016.
*/
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class ProfileActivity extends AppCompatActivity {

    //Textview to show currently logged in user
    private TextView emailText;
    private TextView userText;
    private TextView locationText;
    private Button editbtn;
    private TextView findres;

    public String user_name;
    public String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Initializing textview
        emailText = (TextView) findViewById(R.id.emailText);
        userText=(TextView) findViewById(R.id.userText);
        locationText=(TextView) findViewById(R.id.locationText);
        editbtn = (Button) findViewById(R.id.editProfilebtn);
        findres=(TextView) findViewById(R.id.findres);



        findres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent("android.intent.action.SEARCH");
                startActivity(x);
                return;
            }
        });
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent("android.intent.action.EDITPROFILE");
            }
        });

        //Fetching email from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(Config.EMAIL_SHARED_PREF,"Not Available");

        //Showing the current logged in email to textview
        emailText.setText("Logged In As: " + email);
        //getUser(email);

        //userText.setText(user_name);
        //locationText.setText(location);
    }

    //Logout function
    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to email
                        editor.putString(Config.EMAIL_SHARED_PREF, "");

                        //Saving the sharedpreferences
                        editor.commit();

                        //Starting login activity
                        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adding our menu to toolbar
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuLogout) {
            //calling logout method when the logout button is clicked
            logout();
        }
        return super.onOptionsItemSelected(item);
    }


    public void getUser(String email){
        final String SEARCH_URL = Config.SERVER+"test/getUser.php";
        final String KEY_ID = "email";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, SEARCH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("empty"))
                            Toast.makeText(ProfileActivity.this, "Unsuccessful!", Toast.LENGTH_LONG).show();
                        else {
                            Toast.makeText(ProfileActivity.this, response, Toast.LENGTH_LONG).show();
                            //parseUser(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
                user_name = jsonObject.optString("user_name");
                location = jsonObject.optString("location");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Iterate the jsonArray and print the info of JSONObjects

    }

}