package com.example.appdubaothoitiet.data.datasources.remote;

import static com.example.appdubaothoitiet.data.datasources.ultils_datas.constain.URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    //design pattern Singleton

    //tao bien
    private static RetrofitClient instance = null;
    private Retrofit retrofit = null;
    private ApiService apiService=null;

    //constructor
    private RetrofitClient() {
        retrofit = createRetrofit();
        apiService = createApiService(retrofit);
    }

    //getinstance cho retrofit
    public static RetrofitClient getInstance()
    {
        if(instance==null)
        {
            instance=new RetrofitClient();
        }

        return instance;
    }

    private ApiService createApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

    private Retrofit createRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(100, TimeUnit.MILLISECONDS)
                .writeTimeout(100, TimeUnit.MILLISECONDS)
                .connectTimeout(100, TimeUnit.MILLISECONDS)
                .protocols(Arrays.asList(Protocol.HTTP_1_1))
                .build();

        Gson gson = new GsonBuilder().setLenient().create();

        return new Retrofit.Builder()
                .baseUrl(URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    //tra ve apiservice dang tao
    public ApiService getApiService()
    {
        return apiService;
    }
}
