package org.cookandroid.nice_nice_house.Services

import com.google.android.libraries.places.api.model.Place
import com.google.gson.JsonObject
import org.cookandroid.nice_nice_house.data.ResponseData
import org.intellij.lang.annotations.Language
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PlaceAPI {


    @GET("json?")
    fun getPlaceID(
        @Query("address") address:String,
        @Query("key") key:String,
        @Query("language") language: String

    ): Call<JsonObject>
}