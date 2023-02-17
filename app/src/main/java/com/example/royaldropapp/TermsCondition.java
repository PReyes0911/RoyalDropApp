package com.example.royaldropapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;



public class TermsCondition extends AppCompatActivity {
    WebView webView;
    Button btn_back;

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.terms_condition);

        image = findViewById(R.id.header);
        btn_back = findViewById(R.id.Backbtn);
        webView = findViewById(R.id.termsCondition);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://doc-hosting.flycricket.io/royaldrop-terms-of-use/f56a04f1-3e24-4c41-a14d-25297de1256f/terms");

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TermsCondition.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }
}
