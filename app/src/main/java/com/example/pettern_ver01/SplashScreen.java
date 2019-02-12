package com.example.pettern_ver01;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    Thread myThread = new Thread(){
        @Override
        public void run(){
            try{
                sleep(2000);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    };
        myThread.start();
    }
}