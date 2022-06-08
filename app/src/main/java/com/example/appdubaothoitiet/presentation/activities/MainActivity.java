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
        mMainBinding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mMainBinding.getRoot());

        mMainViewModel=new ViewModelProvider(this, new ViewModelProvider.Factory() {
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

        mMainViewModel.queryFoacastByLocation(35.0,139.0);
    }

    private void bindingControll(Example example) {
        Log.d(TAG, "bindingControll: ");
        mMainBinding.txtTenThanhPho.setText(example.getName());
    }
}