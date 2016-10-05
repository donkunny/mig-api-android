package com.johnjhkoo.mig_android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DevMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_main);

        Button googleMapBtn = (Button) findViewById(R.id.dev_main_to_map_activity);
        googleMapBtn.setText("googleMapBtn");

        googleMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "googleMapBtn is clicked!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), GoogleMapActivity.class);
                startActivity(i);
            }
        });

        /**
         * Button to LinearListActivity
         * testing for Image Lading from URL
         */
        Button testListBtn = (Button) findViewById(R.id.dev_main_to_testlist_activity);
        testListBtn.setText("testListButton");
        testListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LinearListActivity.class);
                startActivity(i);
            }
        });

        /**
         * Button to GridListActivity
         * testing for Recyclerview, gridlist, SwipeRefreshLayout
         */
        Button gridListBtn = (Button) findViewById(R.id.dev_main_to_gridlist_activity);
        gridListBtn.setText("GridListButton");
        gridListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), GridListActivity.class);
                startActivity(i);
            }
        });

    }
}
