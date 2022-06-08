package com.example.appdubaothoitiet.presentation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import static com.example.appdubaothoitiet.data.datasources.ultils_datas.constain.TAG;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.appdubaothoitiet.data.models.Example;
import com.example.appdubaothoitiet.data.models.Weather;
import com.example.appdubaothoitiet.data.responsitories.FoacastResponsitory;
import com.example.appdubaothoitiet.databinding.ActivityMainBinding;
import com.example.appdubaothoitiet.presentation.viewmodel.MainViewModel;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnSuccessListener;


public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    ActivityMainBinding mMainBinding;
    MainViewModel mMainViewModel;
    double mLatitude = 0.0, mLongitude = 0.0;

    //GG API for location, the majority of the app funtion use this class
    //la api cua gg cho location, phan lon app deu dung class nay
    FusedLocationProviderClient fusedLocationProviderClient;

    //Location request is config file for all setting related to fusedLocationProviderClient
    //locationRequest là tệp cấu hình cho tất cả cài đặt liên quan đến fusedLocationProviderClient
    LocationRequest locationRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mMainBinding.getRoot());


        //create mainViewModel
        mMainViewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new MainViewModel(new FoacastResponsitory());
            }
        }).get(MainViewModel.class);

        mMainViewModel.getFocastData().observe(this, new Observer<Example>() {
            @Override
            public void onChanged(Example example) {
                bindingControll(example);
            }
        });

        mMainViewModel.getMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d(TAG, "onChanged: " + s);
            }
        });

        mMainViewModel.getLocation().observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                if (location == null) {
                    Log.d(TAG, "onChanged: +BUG ");
                }
                else
                {
                    mLatitude = location.getLatitude();
                    mLongitude = location.getLongitude();
                }

            }
        });
        mMainViewModel.querGetLocation(MainActivity.this);
        mMainViewModel.queryFoacastByLocation(mLatitude, mLongitude);

    }


    //Binding len UI
    private void bindingControll(Example example) {
        mMainBinding.txtTenThanhPho.setText(example.getName());
        mMainBinding.txtNhietDo.setText(example.getMain().getTemp() + "°");
        String findDesciption = findDesciptions(example);
        mMainBinding.txtTrangThai.setText(UppercaseFirstLetters(findDesciption));
        mMainBinding.txtNhietDoCaoNhat.setText("H: " + example.getMain().getTempMax() + "°");
        mMainBinding.txtNhietDoNhoNhat.setText("L: " + example.getMain().getTempMin() + "°");
        mMainBinding.txtDoAm.setText(example.getMain().getHumidity() + " %");
        mMainBinding.txtTangSuatMay.setText(example.getClouds().getAll() + " %");
        mMainBinding.txtSucGio.setText(example.getWind().getSpeed() + " m/s");
    }

    //tim description trong example
    private String findDesciptions(Example example) {
        String description = "";
        for (Weather i : example.getWeather()) {
            description = i.getDescription();
        }
        return description;
    }

    //convert upcase
    public static String UppercaseFirstLetters(String str) {
        boolean prevWasWhiteSp = true;
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (Character.isLetter(chars[i])) {
                if (prevWasWhiteSp) {
                    chars[i] = Character.toUpperCase(chars[i]);
                }
                prevWasWhiteSp = false;
            } else {
                prevWasWhiteSp = Character.isWhitespace(chars[i]);
            }
        }
        return new String(chars);
    }


}