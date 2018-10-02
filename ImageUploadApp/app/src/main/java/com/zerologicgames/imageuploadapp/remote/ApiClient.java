package com.zerologicgames.imageuploadapp.remote;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL_STORAGE = "https://firebasestorage.googleapis.com/v0/b/imageuploadapp-1b842.appspot.com/o/";
    private static final String BASE_URL_DATABASE = "https://imageuploadapp-1b842.firebaseio.com/";
    private static ApiClient sInstance = null;
    private OkHttpClient okHttpClient;

    private ApiClient() {

    }

    public static ApiClient getInstance() {
        if (sInstance == null)
            sInstance = new ApiClient();

        return sInstance;
    }

    public <T> T getServiceStorage(final Class<T> service) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL_STORAGE)
                .build();

        return retrofit.create(service);
    }

    public <T> T getDatabaseService(final Class<T> service) {
        Retrofit retrofit = new Retrofit.Builder()
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL_DATABASE)
                .build();

        return retrofit.create(service);
    }

    private OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();
        }
        return okHttpClient;
    }


}
