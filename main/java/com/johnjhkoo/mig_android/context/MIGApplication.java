package com.johnjhkoo.mig_android.context;

import android.graphics.Typeface;
import android.support.multidex.MultiDexApplication;

import com.tsengvn.typekit.Typekit;

/**
 * Created by JohnKoo on 7/6/16.
 */
public class MIGApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Typekit.getInstance()
                .addCustom1(Typekit.createFromAsset(this, "NanumSquareOTFR.otf"))
                .addCustom2(Typekit.createFromAsset(this, "NanumSquareOTFB.otf"))
                .addCustom3(Typekit.createFromAsset(this, "Seoulnamsan_B.otf"))
                .addCustom4(Typekit.createFromAsset(this, "FiraSans-Bold.ttf"));
    }
}
