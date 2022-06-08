package com.example.appdubaothoitiet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.appdubaothoitiet.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding mMainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mMainBinding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mMainBinding.getRoot());
    }
}