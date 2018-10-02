package com.zerologicgames.imageuploadapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.zerologicgames.imageuploadapp.model.UploadImageResponse;
import com.zerologicgames.imageuploadapp.remote.ApiClient;
import com.zerologicgames.imageuploadapp.remote.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DownloadedImageActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DownloadedImageAdapter downloadedImageAdapter;
    ArrayList<String> imageUrlList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.downloaded_image_activity);
        recyclerView = findViewById(R.id.recyclerView);
        getUploadedImages();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        setAdapterList();

    }

    private void setAdapterList() {
        if(imageUrlList.size()>0) {
            downloadedImageAdapter = new DownloadedImageAdapter(DownloadedImageActivity.this, imageUrlList);
            recyclerView.setAdapter(downloadedImageAdapter);
            downloadedImageAdapter.notifyDataSetChanged();
        }
    }

    private void getUploadedImages() {
        ApiClient.getInstance()
                .getDatabaseService(ApiInterface.class)
                .getUploadedImages().enqueue(new Callback<HashMap<String, UploadImageResponse>>() {
            @Override
            public void onResponse(Call<HashMap<String, UploadImageResponse>> call, Response<HashMap<String, UploadImageResponse>> response) {
                HashMap<String, UploadImageResponse> data = response.body();
                for (Map.Entry<String, UploadImageResponse> entry : data.entrySet()) {
                    imageUrlList.add(entry.getValue().getImage());
                }
                setAdapterList();
                Log.i("Images", response.message());

            }

            @Override
            public void onFailure(Call<HashMap<String, UploadImageResponse>> call, Throwable t) {
                Log.i("Images", "Error Message");

            }
        });
    }


}
