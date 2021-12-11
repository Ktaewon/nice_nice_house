package org.cookandroid.nice_nice_house.Services

import org.cookandroid.nice_nice_house.data.ResponseData
import org.cookandroid.nice_nice_house.data.StoreData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface ItemAPI {


    @GET("15069131/v1/uddi:6bda6e5b-81c3-4b3e-ab1f-f6d3cbfb7ebb")
    fun getItem(
        @Header("Authorization") Authorization :String,
        @Query("serviceKey") serviceKey:String,
        @Query("page") page:Int?=null,
        @Query("perPage") perPage:Int?=null,
    ): Call<ResponseData>

}