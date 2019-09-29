package com.news.controller.webservices;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.news.BuildConfig;
import com.news.controller.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private static Retrofit retrofit = null;

    public static synchronized Retrofit getInstance() {
        if (retrofit == null) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            if (Constants.DEBUG_MODE)
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            else
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);

            OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder();
            okhttpBuilder.connectTimeout(3, TimeUnit.MINUTES);
            okhttpBuilder.readTimeout(3, TimeUnit.MINUTES);
            okhttpBuilder.retryOnConnectionFailure(true);
            okhttpBuilder.addInterceptor(httpLoggingInterceptor);
            okhttpBuilder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request requestOrignal = chain.request();
                    Request.Builder requestBuilder = requestOrignal.newBuilder()
                            .addHeader("Accept", "application/json")
                            .addHeader("Accept", "multipart/form-data")
                            .addHeader("Connection","close")
                            .method(requestOrignal.method(), requestOrignal.body());
                    Request requestFinal = requestBuilder.build();
                    return chain.proceed(requestFinal);
                }
            });
            OkHttpClient okHttpClient = okhttpBuilder.build();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();

        }
        return retrofit;
    }
}
