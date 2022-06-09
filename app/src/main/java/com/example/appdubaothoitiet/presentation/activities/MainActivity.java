package com.example.appdubaothoitiet.presentation.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import static com.example.appdubaothoitiet.data.datasources.ultils_datas.constain.TAG;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.appdubaothoitiet.data.models.Example;
import com.example.appdubaothoitiet.data.models.Weather;
import com.example.appdubaothoitiet.data.responsitories.FoacastResponsitory;
import com.example.appdubaothoitiet.databinding.ActivityMainBinding;
import com.example.appdubaothoitiet.presentation.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mMainBinding;
    MainViewModel mMainViewModel;

    LocationManager locationManager;
    LocationListener locationListener;

    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;
    String Location_Provider = LocationManager.GPS_PROVIDER;

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

        //get focast
        mMainViewModel.getFocastData().observe(this, new Observer<Example>() {
            @Override
            public void onChanged(Example example) {
                bindingControll(example);
            }
        });

        //get message
        mMainViewModel.getMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d(TAG, "onChanged: " + s);
            }
        });

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


    @Override
    protected void onResume() {
        super.onResume();
        getWeatherForCurrentLocation();
    }

    private void getWeatherForCurrentLocation() {
        locationManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                mMainViewModel.queryFoacastByLocation(location.getLatitude(), location.getLongitude());
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        locationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(MainActivity.this,"Locationget Succesffully",Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            }
            else
            {
                //user denied the permission
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(locationManager!=null)
        {
            locationManager.removeUpdates(locationListener);
        }
    }
}