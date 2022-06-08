package com.example.appdubaothoitiet.presentation.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appdubaothoitiet.data.models.Example;
import com.example.appdubaothoitiet.data.responsitories.FoacastResponsitory;

public class MainViewModel extends ViewModel {
    private FoacastResponsitory foacastResponsitory=null;
    private MutableLiveData<Example> focastData=new MutableLiveData<>();
    private MutableLiveData<String> message=new MutableLiveData<>();

    //constructor
    public MainViewModel(FoacastResponsitory foacastResponsitory)
    {
        this.foacastResponsitory=foacastResponsitory;
    }

    public MutableLiveData<Example> getFocastData() {
        return focastData;
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }

    public void queryFoacastByLocation(Double lat, Double lon)
    {
        //foacastResponsitory.getFocastByLocation()
    }

}
