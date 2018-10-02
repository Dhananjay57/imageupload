package com.dhananjay.imageuploadapp.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.dhananjay.imageuploadapp.DownloadedImageAdapter;
import com.dhananjay.imageuploadapp.R;
import com.dhananjay.imageuploadapp.model.UploadImageResponse;
import com.dhananjay.imageuploadapp.remote.ApiClient;
import com.dhananjay.imageuploadapp.remote.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Downloaded Images");

        recyclerView = findViewById(R.id.recyclerView);
        getUploadedImages();
       // LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        setAdapterList();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setAdapterList() {
        if (imageUrlList.size() > 0) {
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
