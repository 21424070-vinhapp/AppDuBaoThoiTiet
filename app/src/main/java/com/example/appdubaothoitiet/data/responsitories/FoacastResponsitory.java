package com.example.appdubaothoitiet.data.responsitories;

import com.example.appdubaothoitiet.data.datasources.remote.ApiService;
import com.example.appdubaothoitiet.data.datasources.remote.RetrofitClient;
import com.example.appdubaothoitiet.data.models.Example;

import retrofit2.Call;

public class FoacastResponsitory {

    private ApiService apiService;

    public FoacastResponsitory()
    {
        apiService= RetrofitClient.getInstance().getApiService();
    }

    public Call<Example> getFocastByLocation(Double lat,Double lon,String ApiKey)
    {
        return apiService.getWeatherInLocation(lat,lon,ApiKey);
    }
}
