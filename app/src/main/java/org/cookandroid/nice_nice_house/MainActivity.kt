package org.cookandroid.nice_nice_house

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.gson.JsonObject
import org.cookandroid.nice_nice_house.Services.ApiRequest
import org.cookandroid.nice_nice_house.Services.GoogleMapAPI
import org.cookandroid.nice_nice_house.Services.ItemAPI
import org.cookandroid.nice_nice_house.Services.PlaceAPI
import org.cookandroid.nice_nice_house.data.CompositeData
import org.cookandroid.nice_nice_house.data.ResponseData
import org.cookandroid.nice_nice_house.data.StoreData
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    val Author:String ="s6uCol2G%2F9kDZDHSm1qm7B7tEzlxymTvk3HNYpdJ1TKK4eUmcW%2F5Lu2mSsBOh%2FOP%2F1ZLytfgLjGK60CnlOJL8w%3D%3D"
    val ServiceKey="s6uCol2G/9kDZDHSm1qm7B7tEzlxymTvk3HNYpdJ1TKK4eUmcW/5Lu2mSsBOh/OP/1ZLytfgLjGK60CnlOJL8w=="
    var sampleData:ArrayList<StoreData>?=null
    var EFood=ArrayList<CompositeData>()   //양식, 기타양식
    var CFood=ArrayList<CompositeData>()
    var KGFood=ArrayList<CompositeData>()
    var KMFood=ArrayList<CompositeData>()
    var KNFood=ArrayList<CompositeData>() //한식 찌개류,한식 면류
    var JFood=ArrayList<CompositeData>()
    var KSFood=ArrayList<CompositeData>()
    var KBFood=ArrayList<CompositeData>()
    var addrList=ArrayList<LatLng>()
    var CategoryList=ArrayList<String>(arrayListOf(
            "양식",
            "중식",
            "한식_일반",
            "한식_육류",
            "한식_찌개류",
            "한식_면류",
            "일식",
            "기타양식",
            "한식_해산물",
            "한식_분식"
        )
    )

    lateinit var e_food:ImageView
    lateinit var j_food:ImageView
    lateinit var c_food:ImageView
    lateinit var k_food:ImageView
    lateinit var kn_food:ImageView
    lateinit var km_food:ImageView
    lateinit var ks_food:ImageView
    lateinit var kb_food:ImageView
    lateinit var all_food:ImageView


    val places_api_key = "AIzaSyAVEjRyS5VmNZmKS6iyXMrlddjZGnnFGF8"



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()
        var btnMapOpen = findViewById<Button>(R.id.btnMapOpen)
        btnMapOpen.setOnClickListener {
            var intent = Intent(this, MapActivity::class.java)
            intent.putExtra("food", sampleData)
//            intent.putExtra("addrList",addrList)
            startActivity(intent)
        }

        //각버튼 아이디 매칭
        e_food = findViewById(R.id.e_food)
        j_food=findViewById(R.id.j_food)
        c_food = findViewById(R.id.c_food)
        k_food = findViewById(R.id.k_food)
        kn_food = findViewById(R.id.kn_food)
        km_food = findViewById(R.id.km_food)
        ks_food = findViewById(R.id.ks_food)
        kb_food = findViewById(R.id.kb_food)
        all_food = findViewById(R.id.all_food)

        e_food.setOnClickListener(ButtonListener())
        j_food.setOnClickListener(ButtonListener())
        c_food.setOnClickListener(ButtonListener())
        k_food.setOnClickListener(ButtonListener())
        km_food.setOnClickListener(ButtonListener())
        ks_food.setOnClickListener(ButtonListener())
        kb_food.setOnClickListener(ButtonListener())
        kn_food.setOnClickListener(ButtonListener())
        all_food.setOnClickListener(ButtonListener())

        var placeRetrofit:Retrofit= GoogleMapAPI.getInstance();
        var placeApi=placeRetrofit.create(PlaceAPI::class.java)

        var retrofit:Retrofit= ApiRequest.getInstance();
        val api = retrofit.create(ItemAPI::class.java)
        val Result= api.getItem(Author,ServiceKey,1,400)
        Result.enqueue(object : Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                if (response.isSuccessful) {

                    var result: ResponseData? = response.body()
                    Log.i("test", response.body().toString())
                    sampleData=result!!.data;
                    parseData(sampleData!!,placeApi);


                } else {
                    Log.d("success","실행4");

                }
            }
            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                val dlg: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
                dlg.setTitle("Message") //제목
                dlg.setMessage("죄송합니다. 다시 시도해 주세요.") // 메시지
                dlg.setPositiveButton("닫기", null)
                dlg.show()
            }


        })

    }



    inner class ButtonListener: View.OnClickListener {
        override fun onClick(v: View?) {

            var intent = Intent(this@MainActivity, MapActivity::class.java)
            when (v?.id) {
                R.id.j_food -> {
                    Log.d("TestLog", "Button2")
                    intent.putExtra("food",JFood)

                }
                R.id.e_food -> {
                    Log.d("TestLog", "Button3")
                    intent.putExtra("food",EFood)
                }
                R.id.c_food -> {
                    Log.d("TestLog", "Button3")
                    intent.putExtra("food",CFood)
                }
                R.id.k_food -> {
                    Log.d("TestLog", "Button3")
                    intent.putExtra("food",KGFood)
                }
                R.id.kb_food -> {
                    Log.d("TestLog", "Button3")
                    intent.putExtra("food",KBFood)
                }
                R.id.kn_food -> {
                    Log.d("TestLog", "Button3")
                    intent.putExtra("food",KNFood)
                }
                R.id.km_food -> {
                    Log.d("TestLog", "Button3")
                    intent.putExtra("food",KMFood)
                }
                R.id.ks_food -> {
                    Log.d("TestLog", KSFood.toString())
                    intent.putExtra("food",KSFood)
                }
                R.id.all_food -> {

                    intent.putExtra("food",sampleData)
                }
            }

            startActivity(intent)
        }
    }

    fun parseData(data: ArrayList<StoreData>, placeApi: PlaceAPI)
    {
        Log.d("method:printData","총 받은  데이터 갯수: "+data.size.toString())


        for (d in data){
            var place:Place?=null
            Log.d("data",d.storeType.toString())
            var result= placeApi.getPlaceID(d.Addr,"AIzaSyA-QQQIaULw-TI4BIXjY8PchV2l2IRFRas","ko")
            result.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {

                        Log.i("testPlace", response.body().toString())

                        val jObject = JSONObject(response.body().toString())
                        val jArray = jObject.getJSONArray("results")

                        for (i in 0 until jArray.length()) {
                            val obj = jArray.getJSONObject(i)
                            val placeName = obj.getString("place_id")

                            Log.d(TAG, "title($i): $placeName")


                            // Define a Place ID.
                            val placeId = placeName

// Specify the fields to return.
                            val placeFields = listOf(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.PHOTO_METADATAS)

// Construct a request object, passing the place ID and fields array.
                            val request = FetchPlaceRequest.newInstance(placeId, placeFields)

                            Places.initialize(applicationContext, places_api_key)
                            var placesClient = Places.createClient(this@MainActivity)
                            placesClient.fetchPlace(request)
                                .addOnSuccessListener { response: FetchPlaceResponse ->
                                     place = response.place
                                }.addOnFailureListener { exception: Exception ->
                                    if (exception is ApiException) {
                                        Log.e(TAG, "Place not found: ${exception.message}")
                                        val statusCode = exception.statusCode
                                    }
                                }


                        }


                    } else {
                        Log.d("success","실행4");

                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    val dlg: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
                    dlg.setTitle("Message") //제목
                    dlg.setMessage("죄송합니다. 다시 시도해 주세요.") // 메시지
                    dlg.setPositiveButton("닫기", null)
                    dlg.show()
                }


            })
            when(d.storeType)
            {
                "양식","기타양식" -> {
                    if (place != null)
                        EFood?.add(CompositeData(place!!, d))
                }
                "중식" -> {
                    if (place != null)
                        CFood?.add(CompositeData(place!!, d))
                }
                "한식_일반" -> {
                    if (place != null)
                        KGFood?.add(CompositeData(place!!, d))
                }
                "한식_육류" ->{
                    if (place != null)
                        KMFood?.add(CompositeData(place!!, d))
                }
                "한식_찌개류","한식_면류" -> {
                    if (place != null)
                        KNFood?.add(CompositeData(place!!, d))
                }
                "일식" -> {
                    if (place != null)
                        JFood?.add(CompositeData(place!!, d))
                }
                "한식_해산물" -> {
                    if (place != null)
                        KSFood?.add(CompositeData(place!!, d))
                }
                "한식_분식" -> {
                    if (place != null)
                        KBFood?.add(CompositeData(place!!, d))
                }

            }


        }
        Log.d("parseData",KBFood?.toString())





//        thread(start = true) { //'kotlin.concurrent.thread' 를 import 해야함.
//            var count = 0
//            for (i in 1..(size - 1)){
//                Log.d("프로젝트_스레드1 + $count", data[i].storeName + data[i].Addr)
//                count += 1
//                val sdLocation = mapApi.addrToPoint(this, data[i].Addr)
//                val latLng = LatLng(sdLocation!!.latitude, sdLocation!!.longitude)
//                addrList1.add(latLng)
//            }
//        }
//        thread(start = true) { //'kotlin.concurrent.thread' 를 import 해야함.
//            var count = 0
//            var datasize = size*2
//            for (i in size..datasize - 1){
//                Log.d("프로젝트_스레드2 + $count", data[i].storeName + data[i].Addr)
//                count += 1
//                val sdLocation = mapApi.addrToPoint(this, data[i].Addr)
//                val latLng = LatLng(sdLocation!!.latitude, sdLocation!!.longitude)
//                addrList2.add(latLng)
//            }
//        }
//        thread(start = true) { //'kotlin.concurrent.thread' 를 import 해야함.
//            var count = 0
//            var datasize = size*3
//            for (i in size*2..datasize - 1){
//                Log.d("프로젝트_스레드3 + $count", data[i].storeName + data[i].Addr)
//                count += 1
//                val sdLocation = mapApi.addrToPoint(this, data[i].Addr)
//                val latLng = LatLng(sdLocation!!.latitude, sdLocation!!.longitude)
//                addrList3.add(latLng)
//            }
//        }
//        thread(start = true) { //'kotlin.concurrent.thread' 를 import 해야함.
//            var count = 0
//            var datasize = size*4
//            for (i in size*3..datasize - 1){
//                Log.d("프로젝트_스레드4 + $count", data[i].storeName + data[i].Addr)
//                count += 1
//                val sdLocation = mapApi.addrToPoint(this, data[i].Addr)
//                val latLng = LatLng(sdLocation!!.latitude, sdLocation!!.longitude)
//                addrList4.add(latLng)
//            }
//        }

//        addrList.addAll(addrList1)
//        addrList.addAll(addrList2)
//        addrList.addAll(addrList3)
//        addrList.addAll(addrList4)



//        for (sd in data){
//            Log.d("프로젝트 + $count", sd.storeName + sd.Addr)
//            count += 1
//            var context = this
//            val sdLocation = mapApi.addrToPoint(context, sd.Addr)
//            val latLng = LatLng(sdLocation!!.latitude, sdLocation!!.longitude)
//            addrList.add(latLng)
//
//        }


    }



}