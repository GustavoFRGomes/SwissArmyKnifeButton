package com.jaggedlabs.gustavogomes.swissarmyknifebutton;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by gustavogomes on 02/06/2017.
 */

public class DemoApplication extends Application {

    @Override
    public void onCreate()
    {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/JosefinSlab-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
