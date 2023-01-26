package com.example.royaldropapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

public class ThankYouActivity extends AppCompatActivity {

    TextView countView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        countView = findViewById(R.id.countdown);

        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {
                countView.setText("This will redirect you to the main screen " + "("+l / 1000+")");
            }

            @Override
            public void onFinish() {
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        }.start();
    }
    @Override
    public void onBackPressed() {

    }
}