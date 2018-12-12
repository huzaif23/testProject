package com.example.mangotech2.a123ngo.Interfaces;
import com.example.mangotech2.a123ngo.DriverBooking;
import  com.example.mangotech2.a123ngo.Model.*;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Sameer on 11/22/2017.
 */

public interface RetrofitInterface{
@POST("driverDetailsSO")
    Call<DriverDetailsSO> createAccount(DriverDetailsSO driverDetailsSO);
}