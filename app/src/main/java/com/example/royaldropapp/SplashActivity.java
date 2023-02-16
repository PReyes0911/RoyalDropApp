package com.example.royaldropapp;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;

    //Variables
    Animation topAnim, bottomAnim;
    ImageView image;
    TextView logo,slogan;
    MediaPlayer player;

    SharedPreferences sharedPreference;

    public static final String fileName = "login";
    public static final String Username = "username";
    public static final String Employee1 = "employee1";
    public static final String Employee2 = "employee2";
    public static final String Rider3 = "ridername3";
    public static final String Password = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        //Animations
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Hooks
        image = findViewById(R.id.imageView);
        logo = findViewById(R.id.textView2);
        slogan = findViewById(R.id.textView3);

        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);
        slogan.setAnimation(bottomAnim);

        player = MediaPlayer.create(this,R.raw.waterdrop);

        new CountDownTimer(1500, 1000) {
            public void onFinish() {
                player.start();
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();

        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                sharedPreference = getSharedPreferences(fileName, Context.MODE_PRIVATE);
                if(sharedPreference.contains(Username)){
                    Intent i = new Intent(getApplicationContext(),AdminActivity.class);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }else if(sharedPreference.contains(Employee1)||sharedPreference.contains(Employee2)) {
                    Intent i = new Intent(getApplicationContext(),AdminRiderActivity.class);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }else if(firebaseUser != null) {
                    Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this);
                    startActivity(i, options.toBundle());
                    finish();
                }else {
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    Pair[] pairs = new Pair[2];
                    pairs[0] = new Pair<View,String>(image,"logo_image");
                    pairs[1] = new Pair<View,String>(logo,"logo_text");

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this,pairs);
                    startActivity(intent, options.toBundle());
                    finish();
                }
            }

        }.start();

    }
}