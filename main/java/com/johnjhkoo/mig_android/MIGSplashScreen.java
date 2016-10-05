package com.johnjhkoo.mig_android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.JsonObject;
import com.johnjhkoo.mig_android.context.MIGActivity;
import com.johnjhkoo.mig_android.context.MIGUtils;
import com.johnjhkoo.mig_android.dao.MIGRetroDAO;
import com.johnjhkoo.mig_android.frangments.CategoryFragment;
import com.johnjhkoo.mig_android.frangments.GridPageFragment;
import com.johnjhkoo.mig_android.pojos.MIGEventVO;

import java.util.List;

/**
 * Created by JohnKoo on 7/6/16.
 */
public class MIGSplashScreen extends MIGActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Thread splashThread = new Thread(){

            @Override
            public void run() {
                super.run();

                try {
                    super.run();
                    sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Intent i = new Intent(MIGSplashScreen.this, GridListActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        splashThread.start();
    }
}