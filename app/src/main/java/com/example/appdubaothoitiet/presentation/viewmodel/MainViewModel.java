package com.example.appdubaothoitiet.presentation.viewmodel;

import static com.example.appdubaothoitiet.data.datasources.ultils_datas.constain.KEY;
import static com.example.appdubaothoitiet.data.datasources.ultils_datas.constain.TAG;
import static com.example.appdubaothoitiet.data.datasources.ultils_datas.constain.UNITS;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appdubaothoitiet.data.models.Example;
import com.example.appdubaothoitiet.data.responsitories.FoacastResponsitory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {
    private FoacastResponsitory foacastResponsitory;
    private MutableLiveData<Example> focastData=new MutableLiveData<>();
    private MutableLiveData<String> message=new MutableLiveData<>();

    //constructor
    public MainViewModel(FoacastResponsitory foacastResponsitory)
    {
        this.foacastResponsitory=foacastResponsitory;
    }

    public LiveData<Example> getFocastData() {
        return focastData;
    }

    public LiveData<String> getMessage() {
        return message;
    }

    //cau lenh lay du lieu thoi tiet khi truyen vao kinh do, vi do
    public void queryFoacastByLocation(Double lat, Double lon)
    {
        foacastResponsitory.getFocastByLocation(lat,lon,KEY,UNITS).enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                //neu respone co du lieu
                if(response.isSuccessful())
                {
                    //set cho focastData de UI nhan biet duoc co du lieu truyen vao
                    Example example=response.body();
                    focastData.setValue(example);
                }
                else
                {
                    if (response.errorBody()!=null)
                    {
                        //chi la thong bao cho UI biet co loi
                        try {
                            JSONObject jsonObject=new JSONObject(response.errorBody().string());
                            String textMessage=jsonObject.getString("message");
                            message.setValue(textMessage);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

}
