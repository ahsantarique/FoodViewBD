package com.example.ahsan.foodviewbd;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle e){
        super.onCreate(e);
        setContentView(R.layout.activity_main);

        Thread t = new Thread();
        try {
            t.sleep(5000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        Intent x = new Intent("android.intent.action.LOGIN");

        startActivity(x);
    }

}