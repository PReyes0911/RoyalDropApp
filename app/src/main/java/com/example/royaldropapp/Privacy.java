package com.example.royaldropapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Privacy extends AppCompatActivity {
    WebView webView;
    Button btn_back;

    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.privacy_policy);

        btn_back = findViewById(R.id.Backbtn);
        webView = findViewById(R.id.privacyPolicy);
        image = findViewById(R.id.header);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://doc-hosting.flycricket.io/royaldrop-privacy-policy/b82cad72-682b-45ba-b4a1-cfe58cca45e3/privacy");


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Privacy.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }
}
