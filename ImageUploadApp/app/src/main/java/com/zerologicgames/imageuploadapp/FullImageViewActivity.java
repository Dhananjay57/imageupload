package com.zerologicgames.imageuploadapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class FullImageViewActivity extends AppCompatActivity {
    ImageView imageView ;
    String imageUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image_activtiy);
        imageView = findViewById(R.id.iv_fullImageView);
        Intent intent = getIntent();
        imageUrl = intent.getExtras().getString("imageUrl");
        if(imageUrl!= null){
            Glide.with(this).load(imageUrl).into(imageView);
        }

    }
}
