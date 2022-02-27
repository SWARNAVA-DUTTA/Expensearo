package com.example.expensearo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    ImageView imgView;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imgView=findViewById(R.id.imgView);



        //        Animation animation= AnimationUtils.loadAnimation(this,R.anim.animation_fade);
//        imgView.startAnimation(animation);


        Thread thread=new Thread(){

            @Override
            public void run() {
                try {
                    sleep(4000);
                    pref = getSharedPreferences("Reg", Context.MODE_PRIVATE);
                    if (pref.contains("Email") && pref.contains("Password")) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    }
                    else
                    {
                        Intent i = new Intent(getApplicationContext(), LogIn.class);
                        startActivity(i);
                    }
                    finish();
                    super.run();
                } catch (InterruptedException e) {
                    // e.printStackTrace();
                }
            }
        };
        thread.start();
    }

}
