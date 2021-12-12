package org.cookandroid.nice_nice_house.data

import com.google.android.libraries.places.api.model.Place
import org.json.JSONObject

data class CompositeData(
    var placeId: String,
    var location:JSONObject,
    var data:StoreData
)
//placeId,
//        lat
//        log