package com.dhananjay.imageuploadapp.remote;

import com.dhananjay.imageuploadapp.model.UploadImageResponse;

import java.util.HashMap;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface ApiInterface {

    @Multipart
    @POST("{fileName}")
    Call<UploadImageResponse> postImageNew(@Part MultipartBody.Part image, @Path("fileName") String fileName);

    @POST("images.json")
    Call<Void> uploadImageData(@Body UploadImageResponse uploadImageResponse);

    @GET("images.json")
    Call<HashMap<String, UploadImageResponse>> getUploadedImages();
}
