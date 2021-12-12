package org.cookandroid.nice_nice_house.data

import com.google.android.libraries.places.api.model.Place
import com.google.gson.annotations.SerializedName
import org.json.JSONObject
import java.io.Serializable

data class CompositeData(
    var placeId: String,
    var lat:Double,
    var long:Double,
    var data:StoreData
): Serializable
//placeId,
//        lat
//        log