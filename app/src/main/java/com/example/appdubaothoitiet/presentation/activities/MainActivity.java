package com.example.appdubaothoitiet.presentation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import static com.example.appdubaothoitiet.data.datasources.ultils_datas.constain.TAG;

import android.os.Bundle;
import android.util.Log;

import com.example.appdubaothoitiet.data.models.Example;
import com.example.appdubaothoitiet.data.models.Weather;
import com.example.appdubaothoitiet.data.responsitories.FoacastResponsitory;
import com.example.appdubaothoitiet.databinding.ActivityMainBinding;
import com.example.appdubaothoitiet.presentation.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding mMainBinding;
    MainViewModel mMainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mMainBinding.getRoot());

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

        mMainViewModel.queryFoacastByLocation(35.0, 139.0);
    }

    private void bindingControll(Example example) {
        mMainBinding.txtTenThanhPho.setText(example.getName());
        mMainBinding.txtNhietDo.setText(example.getMain().getTemp() + "°");
        String findDesciption = findDesciptions(example);
        mMainBinding.txtTrangThai.setText(UppercaseFirstLetters(findDesciption));
        mMainBinding.txtNhietDoCaoNhat.setText("H: "+example.getMain().getTempMax()+"°");
        mMainBinding.txtNhietDoNhoNhat.setText("L: "+example.getMain().getTempMin()+"°");
        mMainBinding.txtDoAm.setText(example.getMain().getHumidity()+" %");
        mMainBinding.txtTangSuatMay.setText(example.getClouds().getAll()+" %");
        mMainBinding.txtSucGio.setText(example.getWind().getSpeed()+" m/s");
    }

    private String findDesciptions(Example example) {
        String description = "";
        for (Weather i : example.getWeather()) {
            description = i.getDescription();
        }
        return description;
    }


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