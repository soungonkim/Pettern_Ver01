package com.example.pettern_ver01;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        EasySplashScreen config = new EasySplashScreen(SplashScreen.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(5000)
                .withBackgroundColor(Color.parseColor("#ffffff"))
                .withLogo(R.mipmap.petternlogo)
                .withHeaderText("Welcome Guest!")
                .withFooterText("Copyright 2019")
                .withAfterLogoText("Hardware, DB Testing 1.0");

        config.getHeaderTextView().setTextColor(Color.BLUE);
        config.getFooterTextView().setTextColor(Color.BLUE);
        config.getAfterLogoTextView().setTextColor(Color.BLUE);

        View view = config.create();

        setContentView(view);
    }
}
