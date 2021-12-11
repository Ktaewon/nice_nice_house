package org.cookandroid.nice_nice_house

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.GroundOverlayOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import android.widget.TextView as TextView1


class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var gMap: GoogleMap
    lateinit var mapFrag: MapFragment
    lateinit var videoMark: GroundOverlayOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_main)
        supportActionBar!!.setDisplayShowHomeEnabled(false)
        //supportActionBar!!.setIcon(R.drawable.googlemap_icon)
        title = "Google 지도 활용"

        mapFrag = fragmentManager.findFragmentById(R.id.map) as MapFragment
        mapFrag.getMapAsync(this)

        var main=MainActivity()
        Log.d("map",main.sampleData.toString())
    }

    override fun onMapReady(p0: GoogleMap) {
        gMap = p0!!
        gMap.uiSettings.isZoomControlsEnabled = true
        gMap.setOnMapClickListener { point ->
            videoMark = GroundOverlayOptions().image(
                BitmapDescriptorFactory.fromResource(R.drawable.presence_video_busy))
                .position(point, 100f, 100f)
            gMap.addGroundOverlay(videoMark)
        }

        var mMap = gMap

        // for loop를 통한 n개의 마커 생성

        // for loop를 통한 n개의 마커 생성
        for (idx in 0..9) {
            // 1. 마커 옵션 설정 (만드는 과정)
            val makerOptions = MarkerOptions()
            makerOptions // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
                .position(LatLng(37.52487 + idx, 126.92723))
                .title("마커$idx") // 타이틀.

            // 2. 마커 생성 (마커를 나타냄)
            mMap.addMarker(makerOptions)
//            mMap.setOnMarkerClickListener { marker ->
//                card_view.visibility = View.VISIBLE
//                var parkname = findViewById<TextView1>(R.id.park_name)
//                var parkwhat = findViewById<TextView1>(R.id.park_what)
//                var parkadd1 = findViewById<TextView1>(R.id.park_add_lot)
//                var parkadd2 = findViewById<TextView1>(R.id.park_add_road)
//                var parkphone = findViewById<TextView1>(R.id.phone_num)
//                var parkequip = findViewById<TextView1>(R.id.equip)
//                var arr = marker.tag.toString().split("/") //마커에 붙인 태그
//                parkname.text = marker.title
//                parkwhat.text = marker.snippet
//                parkadd1.text = arr[0]
//                parkadd2.text = arr[1]
//                parkphone.text = arr[2]
//                parkequip.text = arr[3]
//                //                Log.d("parkinfo", "parkname->"+marker.title+"___pakrwhat->")
//                false
//            }
        }
        // 카메라를 위치로 옮긴다.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(37.52487, 126.92723)))

        //return false
    }


    override fun openOrCreateDatabase(
        name: String?,
        mode: Int,
        factory: SQLiteDatabase.CursorFactory?
    ): SQLiteDatabase {
        return super.openOrCreateDatabase(name, mode, factory)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menu.add(0, 1, 0, "위성 지도")
        menu.add(0, 2, 0, "일반 지도")
        menu.add(0, 3, 0, "월드컵경기장 바로가기")
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            1 -> {
                gMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                return true
            }
            2 -> {
                gMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                return true
            }
            3 -> {
                gMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                    37.568256, 126.897240), 15f))
                return true
            }
        }
        return false
    }
    fun addrToPoint(context: Context?): Location? {
        val location = Location("")
        val geocoder = Geocoder(context)
        var addresses: List<Address>? = null
        try {
            addresses = geocoder.getFromLocationName("포천시청", 3)
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
}