package com.example.appdubaothoitiet.data.datasources.remote;

import com.example.appdubaothoitiet.data.models.Example;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    //https://api.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=30bcb29684f8ccdb6d67cfbcfb2c451a&units=metric
    @GET("data/2.5/weather")
    Call<Example> getWeatherInLocation(
            @Query("lat") Double lat,
            @Query("lon") Double lon,
            @Query("appid") String appid,
            @Query("units") String units
    );
}
