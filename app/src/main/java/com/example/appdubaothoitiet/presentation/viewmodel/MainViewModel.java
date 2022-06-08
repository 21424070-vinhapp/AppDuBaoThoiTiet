package com.example.appdubaothoitiet.presentation.viewmodel;

import static com.example.appdubaothoitiet.data.datasources.ultils_datas.constain.KEY;
import static com.example.appdubaothoitiet.data.datasources.ultils_datas.constain.TAG;
import static com.example.appdubaothoitiet.data.datasources.ultils_datas.constain.UNITS;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appdubaothoitiet.data.models.Example;
import com.example.appdubaothoitiet.data.responsitories.FoacastResponsitory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {
    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;


    private FoacastResponsitory foacastResponsitory;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;

    private MutableLiveData<Example> focastData = new MutableLiveData<>();
    private MutableLiveData<String> message = new MutableLiveData<>();
    private MutableLiveData<Location> mLocation = new MutableLiveData<>();

    //constructor
    public MainViewModel(FoacastResponsitory foacastResponsitory) {
        this.foacastResponsitory = foacastResponsitory;
    }

    public LiveData<Example> getFocastData() {
        return focastData;
    }

    public LiveData<String> getMessage() {
        return message;
    }

    public LiveData<Location> getLocation() {
        return mLocation;
    }

    //cau lenh lay du lieu thoi tiet khi truyen vao kinh do, vi do
    public void queryFoacastByLocation(Double lat, Double lon) {
        foacastResponsitory.getFocastByLocation(lat, lon, KEY, UNITS).enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                //neu respone co du lieu
                if (response.isSuccessful()) {
                    //set cho focastData de UI nhan biet duoc co du lieu truyen vao
                    Example example = response.body();
                    focastData.setValue(example);
                } else {
                    if (response.errorBody() != null) {
                        //chi la thong bao cho UI biet co loi
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String textMessage = jsonObject.getString("message");
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

    //cau lenh lay location cua thie bi
    public void querGetLocation(Context context)
    {
        //set all properties of locationrequest
        //thiet lap tat ca thuoc tinh cua locationrequest
        locationRequest = LocationRequest
                .create()
                .setInterval(1000 * DEFAULT_UPDATE_INTERVAL) //how often does the default location check occur
                .setFastestInterval(1000 * FAST_UPDATE_INTERVAL)//how often does the default location check occur when set the most frequent update?
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY);

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(context);
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            //user provide the permission
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    mLocation.setValue(location);
                }
            });
        }

    }
}
