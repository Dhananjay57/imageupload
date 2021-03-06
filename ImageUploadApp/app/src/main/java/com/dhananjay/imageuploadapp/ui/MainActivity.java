package com.dhananjay.imageuploadapp.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dhananjay.imageuploadapp.Utils.FileUtils;
import com.dhananjay.imageuploadapp.R;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;
import com.theartofdev.edmodo.cropper.CropImage;
import com.dhananjay.imageuploadapp.Utils.PermissionManager;
import com.dhananjay.imageuploadapp.model.UploadImageResponse;
import com.dhananjay.imageuploadapp.remote.ApiClient;
import com.dhananjay.imageuploadapp.remote.ApiInterface;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_GET_SINGLE_FILE = 1;
    private static final int PERMISSION_READ_EXTERNAL_STORAGE = 2;
    private static final int CAMERA_PERMISSION = 3;
    ImageView imageView;
    FloatingActionMenu floatingActionMenu;
    FloatingActionButton fab_camera, fab_gallery;
    Button btn_upload ;
    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Home");
         floatingActionMenu = findViewById(R.id.floating_menu);
         fab_camera = findViewById(R.id.camera_fab);
         fab_gallery = findViewById(R.id.gallary_fab);
         imageView = findViewById(R.id.iv_picToUpload);
        fab_camera.setOnClickListener(view -> takePhoto());
        fab_gallery.setOnClickListener(view -> pickFromGallery());
        btn_upload = findViewById(R.id.btn_upload);

        Button btnRequestUploadedImages = findViewById(R.id.btnRequestUploadedImages);
        btnRequestUploadedImages.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, DownloadedImageActivity.class);
            startActivity(intent);
        });
    }

    @SuppressLint("CheckResult")
    private void takePhoto() {
        final FragmentActivity  activity = MainActivity.this;
            RxImagePicker.with(activity).requestImage(Sources.CAMERA).subscribe(uri -> {
                if (!activity.isFinishing()) {
                    CropImage.activity(uri).start(activity);
                }
            });
    }

    @SuppressLint("CheckResult")
    private void pickFromGallery() {
        final FragmentActivity  activity = MainActivity.this;
            RxImagePicker.with(activity).requestImage(Sources.GALLERY)
                    .subscribe(uri -> {
                        if (!activity.isFinishing()) {
                            CropImage.activity(uri).start(activity);
                        }
                    });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!PermissionManager.getInstance().hasGrantedPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            PermissionManager.getInstance().requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, PERMISSION_READ_EXTERNAL_STORAGE);
        }
        if(!PermissionManager.getInstance().hasGrantedPermission(this, Manifest.permission.CAMERA)){
            PermissionManager.getInstance().requestPermission(this, Manifest.permission.CAMERA, CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri uri = result.getUri();

                Drawable drawable = BitmapDrawable.createFromPath(uri.getPath());
                imageView.setImageDrawable(drawable);
                // glide needs to clear memory and disk cache in order to update image at same path
                final Glide glide = Glide.get(MainActivity.this);
                glide.clearMemory();

                // store to sd card on background thread
                final Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                FileUtils.storeProfileImageToStorage(MainActivity.this, bitmap);

                new Thread(() -> {
                    glide.clearDiskCache();

                    mHandler.post(() -> {
                        if (MainActivity.this != null) {
                            if (!MainActivity.this.isFinishing()) {
                                showProfileImage(this , imageView);
                            }
                        }
                    });
                }).start();

                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(FileUtils.getProfileImageFromStorage(this));
                    FileInputStream fileInputStream = new FileInputStream(FileUtils.getProfileImageFile(this));
                    uploadImage(getBytes(fileInputStream));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                if (error != null)
                    Toast.makeText(this, "Error MSG",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(this, "",Toast.LENGTH_LONG).show();
            }
        }
    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }


    private void uploadImage(byte[] imageBytes) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);

        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
        ApiClient.getInstance().getServiceStorage(ApiInterface.class)
                .postImageNew(body, UUID.randomUUID().toString() + ".jpeg")
                .enqueue(new Callback<UploadImageResponse>() {
                    @Override
                    public void onResponse(Call<UploadImageResponse> call, Response<UploadImageResponse> response) {
                        ApiClient.getInstance()
                                .getDatabaseService(ApiInterface.class)
                                .uploadImageData(response.body())
                                .enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        Toast.makeText(MainActivity.this, "Image Successfully Uploaded",Toast.LENGTH_LONG).show();

                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast.makeText(MainActivity.this, "Something went wrong",Toast.LENGTH_LONG).show();

                                    }
                                });
                    }

                    @Override
                    public void onFailure(Call<UploadImageResponse> call, Throwable t) {

                    }
                });
    }

    public  void showProfileImage(final Context context, final ImageView targetView) {
        Uri uri = FileUtils.getProfileImageFromStorage(context);
        if (uri != Uri.EMPTY) {
            Glide.with(context).load(uri).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    if (resource != null) {
                        targetView.setImageDrawable(resource);
                    }
                }
            });
        }
    }
}
