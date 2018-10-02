package com.dhananjay.imageuploadapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dhananjay.imageuploadapp.R;

public class FullImageViewActivity extends AppCompatActivity {
    ImageView imageView ;
    String imageUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image_activtiy);
        imageView = findViewById(R.id.iv_fullImageView);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("FullScreen ImageView");

        Intent intent = getIntent();
        imageUrl = intent.getExtras().getString("imageUrl");
        if(imageUrl!= null){
            Glide.with(this).load(imageUrl).into(imageView);
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
