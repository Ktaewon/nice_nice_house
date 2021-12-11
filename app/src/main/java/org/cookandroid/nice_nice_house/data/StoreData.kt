package org.cookandroid.nice_nice_house.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StoreData (

    @SerializedName("연번")
    val id:String,
    @SerializedName("지정연월")
    val date:String,
    @SerializedName("업종")
    val storeType:String,
    @SerializedName("업소명")
    val storeName:String,
    @SerializedName("업주명")
    val manager:String,
    @SerializedName("구군")
    val bigAddr:String,
    @SerializedName("주소")
    val Addr:String,
    @SerializedName("연락처")
    val phoneNum:String,
    @SerializedName("메뉴1")
    val menu1:String,
    @SerializedName("가격1")
    val price1:String,
    @SerializedName("비고")
    val extra:String


    ):Serializable
