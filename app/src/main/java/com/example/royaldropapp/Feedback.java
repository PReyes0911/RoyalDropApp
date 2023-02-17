package com.example.royaldropapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;


public class Feedback extends AppCompatActivity {

    ImageView image_header, imageBack;
    TextInputLayout namedata,emailAddress,feedbackDetails ;
    Button send_btn, details_btn;

    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.feedback_layout);

        imageBack = findViewById(R.id.btnback);
        image_header = findViewById(R.id.header);
        namedata = findViewById(R.id.name);
        emailAddress = findViewById(R.id.email);
        feedbackDetails = findViewById(R.id.message);
        send_btn = findViewById(R.id.send_btn);


        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //String name = namedata.getEditText().toString();
                //String email = emailAddress.getEditText().toString();
                //String message = feedbackDetails.getEditText().toString();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/html");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"royaldrop2023@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT,"Feedback from order");
                intent.putExtra(Intent.EXTRA_TEXT, "Name: " + namedata.getEditText().toString() + "\n Email: " + emailAddress.getEditText().toString() + "\n Message: " + feedbackDetails.getEditText().toString());
                try {
                    startActivity(Intent.createChooser(intent, "Please select email"));
                }
                catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(Feedback.this, "There are no Email Clients", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
