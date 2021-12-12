package org.cookandroid.nice_nice_house

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.marginRight
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import org.cookandroid.nice_nice_house.data.StoreData
import java.io.IOException
import kotlin.concurrent.thread
import android.widget.TextView as TextView1



class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var gMap: GoogleMap
    lateinit var mapFrag: MapFragment
    lateinit var videoMark: GroundOverlayOptions
    lateinit var storedData:ArrayList<StoreData>
    lateinit var foodData:ArrayList<StoreData>
    lateinit var addrList:ArrayList<LatLng>

    private var map: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null

    // The entry point to the Places API.
    private lateinit var placesClient: PlacesClient

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    private var locationPermissionGranted = false

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var lastKnownLocation: Location? = null
    private var likelyPlaceNames: Array<String?> = arrayOfNulls(0)
    private var likelyPlaceAddresses: Array<String?> = arrayOfNulls(0)
    private var likelyPlaceAttributions: Array<List<*>?> = arrayOfNulls(0)
    private var likelyPlaceLatLngs: Array<LatLng?> = arrayOfNulls(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val places_api_key = "AIzaSyAVEjRyS5VmNZmKS6iyXMrlddjZGnnFGF8"
        setContentView(R.layout.map_main)
        supportActionBar!!.hide()
        //supportActionBar!!.setDisplayShowHomeEnabled(false)
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }
        //supportActionBar!!.setIcon(R.drawable.googlemap_icon)
        //title = "Google 지도 활용"
        Places.initialize(applicationContext, places_api_key)
        placesClient = Places.createClient(this)

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        //storedData = intent.getSerializableExtra("storedData") as ArrayList<StoreData>
        storedData = intent.getSerializableExtra("food") as ArrayList<StoreData>

        //addrList = intent.getSerializableExtra("addrList") as ArrayList<LatLng>
//        Log.d("프로젝트", storedData.size.toString())



        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment
        autocompleteFragment.requireView().setBackgroundResource(R.drawable.searchbar)
        autocompleteFragment.requireView()

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // TODO: Get info about the selected place.
                map!!.moveCamera(CameraUpdateFactory.newLatLng(place.latLng!!))
                Log.i(TAG, "Place: ${place.name}, ${place.id}")
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: $status")
            }
        })

        mapFrag = fragmentManager.findFragmentById(R.id.map) as MapFragment
        mapFrag.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0!!
        map!!.uiSettings.isZoomControlsEnabled = true
//        map!!.setOnMapClickListener { point ->
//            videoMark = GroundOverlayOptions().image(
//                BitmapDescriptorFactory.fromResource(R.drawable.presence_video_busy)
//            )
//                .position(point, 100f, 100f)
//            map!!.addGroundOverlay(videoMark)
//        }

        this.map?.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            override fun getInfoContents(marker: Marker): View? {
                val infoWindow = layoutInflater.inflate(R.layout.custom_info_contents,
                    findViewById<FrameLayout>(R.id.map), false)
                val title = infoWindow.findViewById<TextView1>(R.id.title)
                title.text = marker.title
                val snippet = infoWindow.findViewById<TextView1>(R.id.snippet)
                snippet.text = marker.snippet
                return infoWindow
            }

            // Return null here, so that getInfoContents() is called next.
            override fun getInfoWindow(arg0: Marker): View? {
                return null
            }



        })


        // Prompt the user for permission.
        getLocationPermission()

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()

        // 카메라를 위치로 옮긴다.
        //gMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(35.871435, 128.601445)))

        thread(start = true) { //'kotlin.concurrent.thread' 를 import 해야함.
            var count = 0
            for (i in 1..storedData.size - 1) {
                Log.d("프로젝트_스레드1 + $count", storedData[i].storeName + storedData[i].Addr)
                count += 1
                val sdLocation = addrToPoint(this, storedData[i].Addr)
                val latLng = LatLng(sdLocation!!.latitude, sdLocation!!.longitude)
                val markerOptions = MarkerOptions()
                markerOptions
                    .position(latLng)
                    .title(storedData[i].storeName)
                    .snippet(storedData[i].Addr)

                runOnUiThread {
                    map!!.addMarker(markerOptions)?.tag = storedData[i]
                }
            }
        }





        // for loop를 통한 n개의 마커 생성
//        for (i in 1..storedData.size-1){
//            Log.d("프로젝트", storedData.get(i).storeName + storedData.get(i).Addr)
//            var context = this
//            val markerOptions = MarkerOptions()
//
//            val latLng =addrList.get(i)
//            markerOptions
//                .position(latLng)
//                .title(storedData.get(i).storeName)
//            mMap.addMarker(markerOptions)
//        }

        // for loop를 통한 n개의 마커 생성
//        for (idx in 0..9) {
//            // 1. 마커 옵션 설정 (만드는 과정)
//            val makerOptions = MarkerOptions()
//            makerOptions // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
//                .position(LatLng(37.52487 + idx, 126.92723))
//                .title("마커$idx") // 타이틀.
//
//            // 2. 마커 생성 (마커를 나타냄)
//            mMap.addMarker(makerOptions)
////            mMap.setOnMarkerClickListener { marker ->
////                card_view.visibility = View.VISIBLE
////                var parkname = findViewById<TextView1>(R.id.park_name)
////                var parkwhat = findViewById<TextView1>(R.id.park_what)
////                var parkadd1 = findViewById<TextView1>(R.id.park_add_lot)
////                var parkadd2 = findViewById<TextView1>(R.id.park_add_road)
////                var parkphone = findViewById<TextView1>(R.id.phone_num)
////                var parkequip = findViewById<TextView1>(R.id.equip)
////                var arr = marker.tag.toString().split("/") //마커에 붙인 태그
////                parkname.text = marker.title
////                parkwhat.text = marker.snippet
////                parkadd1.text = arr[0]
////                parkadd2.text = arr[1]
////                parkphone.text = arr[2]
////                parkequip.text = arr[3]
////                //                Log.d("parkinfo", "parkname->"+marker.title+"___pakrwhat->")
////                false
////            }
//        }

        //35.871435,128.601445

        //return false
    }

    fun drawMark(stdata:ArrayList<StoreData>, adList:ArrayList<LatLng>){
        for (i in 1..stdata.size-1){
            Log.d("프로젝트", stdata[i].storeName + stdata[i].Addr)
            var context = this
            val markerOptions = MarkerOptions()

            val latLng =adList.get(i)
            markerOptions
                .position(latLng)
                .title(stdata[i].storeName)
            gMap.addMarker(markerOptions)
        }
    }


    override fun openOrCreateDatabase(
        name: String?,
        mode: Int,
        factory: SQLiteDatabase.CursorFactory?
    ): SQLiteDatabase {
        return super.openOrCreateDatabase(name, mode, factory)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return false
    }
    fun addrToPoint(context: Context?, address: String): Location? {
        val location = Location("")
        val geocoder = Geocoder(context)
        var addresses: List<Address>? = null



        try {
            addresses = geocoder.getFromLocationName(address, 1)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (addresses != null) {
            for (i in addresses.indices) {
                val lating: Address = addresses[i]
                location.setLatitude(lating.getLatitude())
                location.setLongitude(lating.getLongitude())
            }
        }
        return location
    }

    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                LatLng(lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude), DEFAULT_ZOOM.toFloat()))
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map?.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat()))
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        map?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }

    /**
     * Handles the result of the request for location permissions.
     */
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true
                }
            }
        }
        updateLocationUI()
    }

    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    @SuppressLint("MissingPermission")
    private fun showCurrentPlace() {
        if (map == null) {
            return
        }
        if (locationPermissionGranted) {
            // Use fields to define the data types to return.
            val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

            // Use the builder to create a FindCurrentPlaceRequest.
            val request = FindCurrentPlaceRequest.newInstance(placeFields)

            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            val placeResult = placesClient.findCurrentPlace(request)
            placeResult.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val likelyPlaces = task.result

                    // Set the count, handling cases where less than 5 entries are returned.
                    val count = if (likelyPlaces != null && likelyPlaces.placeLikelihoods.size < M_MAX_ENTRIES) {
                        likelyPlaces.placeLikelihoods.size
                    } else {
                        M_MAX_ENTRIES
                    }
                    var i = 0
                    likelyPlaceNames = arrayOfNulls(count)
                    likelyPlaceAddresses = arrayOfNulls(count)
                    likelyPlaceAttributions = arrayOfNulls<List<*>?>(count)
                    likelyPlaceLatLngs = arrayOfNulls(count)
                    for (placeLikelihood in likelyPlaces?.placeLikelihoods ?: emptyList()) {
                        // Build a list of likely places to show the user.
                        likelyPlaceNames[i] = placeLikelihood.place.name
                        likelyPlaceAddresses[i] = placeLikelihood.place.address
                        likelyPlaceAttributions[i] = placeLikelihood.place.attributions
                        likelyPlaceLatLngs[i] = placeLikelihood.place.latLng
                        i++
                        if (i > count - 1) {
                            break
                        }
                    }

                    // Show a dialog offering the user the list of likely places, and add a
                    // marker at the selected place.
                    openPlacesDialog()
                } else {
                    Log.e(TAG, "Exception: %s", task.exception)
                }
            }
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.")

            // Add a default marker, because the user hasn't selected a place.
            map?.addMarker(MarkerOptions()
                .title("현재위치")
                .position(defaultLocation))

            // Prompt the user for permission.
            getLocationPermission()
        }
    }

    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
    private fun openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        val listener = DialogInterface.OnClickListener { dialog, which -> // The "which" argument contains the position of the selected item.
            val markerLatLng = likelyPlaceLatLngs[which]
            var markerSnippet = likelyPlaceAddresses[which]
            if (likelyPlaceAttributions[which] != null) {
                markerSnippet = """
                    $markerSnippet
                    ${likelyPlaceAttributions[which]}
                    """.trimIndent()
            }

            // Add a marker for the selected place, with an info window
            // showing information about that place.
            map?.addMarker(MarkerOptions()
                .title(likelyPlaceNames[which])
                .position(markerLatLng!!)
                .snippet(markerSnippet))

            // Position the map's camera at the location of the marker.
            markerLatLng?.let {
                CameraUpdateFactory.newLatLngZoom(
                    it,
                    DEFAULT_ZOOM.toFloat())
            }?.let { map?.moveCamera(it) }
        }

        // Display the dialog.
        AlertDialog.Builder(this)
            .setItems(likelyPlaceNames, listener)
            .show()
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    companion object {
        private val TAG = this::class.java.simpleName
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"

        // Used for selecting the current place.
        private const val M_MAX_ENTRIES = 5
    }
}