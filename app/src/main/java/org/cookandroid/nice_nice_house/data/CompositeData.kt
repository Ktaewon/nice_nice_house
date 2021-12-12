package org.cookandroid.nice_nice_house.data

import com.google.android.libraries.places.api.model.Place

data class CompositeData(
    var place: Place,
    var data:StoreData
)
