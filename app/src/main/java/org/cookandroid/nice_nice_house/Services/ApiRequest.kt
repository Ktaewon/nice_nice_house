package org.cookandroid.nice_nice_house.Services


import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiRequest {

    lateinit var api:ItemAPI
    var retrofit:Retrofit?=null;
    var base_url="https://api.odcloud.kr/api/";

//    init {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(base_url)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//    }


    fun getInstance(): Retrofit {
        if (retrofit==null)
        {
            retrofit = Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            Log.d("success","실행1");
        }

        return retrofit as Retrofit;
    }



}
