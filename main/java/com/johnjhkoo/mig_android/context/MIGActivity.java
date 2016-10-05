package com.johnjhkoo.mig_android.context;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by JohnKoo on 7/6/16.
 */
public class MIGActivity extends AppCompatActivity{
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
