package com.example.lixiang.lab9.factory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 李翔 on 2017/12/25.
 */

public class ServiceFactory {
    private static OkHttpClient createOkHttp() {
        return new OkHttpClient.Builder()
                    .connectTimeout(10L, TimeUnit.SECONDS)
                    .readTimeout(30L, TimeUnit.SECONDS)
                    .writeTimeout(10L, TimeUnit.SECONDS)
                    .build();
    }

    private static Retrofit createRetrofit(String str) {
        return new Retrofit.Builder()
                    .baseUrl(str)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(createOkHttp())
                    .build();
    }

    public static Retrofit getmRetrofit(String str) {
        return createRetrofit(str);
    }
}
