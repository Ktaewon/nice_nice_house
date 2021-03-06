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
import com.google.android.gms.maps.model.LatLng

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
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    val Author:String ="s6uCol2G%2F9kDZDHSm1qm7B7tEzlxymTvk3HNYpdJ1TKK4eUmcW%2F5Lu2mSsBOh%2FOP%2F1ZLytfgLjGK60CnlOJL8w%3D%3D"
    val ServiceKey="s6uCol2G/9kDZDHSm1qm7B7tEzlxymTvk3HNYpdJ1TKK4eUmcW/5Lu2mSsBOh/OP/1ZLytfgLjGK60CnlOJL8w=="
    var sampleData:ArrayList<StoreData>?=null
    var AllFood=ArrayList<CompositeData>()  //모든 데이터
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
    lateinit var btnMapOpen:Button


    val placNames_api_key = "AIzaSyAVEjRyS5VmNZmKS6iyXMrlddjZGnnFGF8"



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)


        btnMapOpen = findViewById<Button>(R.id.btnMapOpen) //지도열기 버튼

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
        btnMapOpen.setOnClickListener(ButtonListener())
        e_food.setOnClickListener(ButtonListener())
        j_food.setOnClickListener(ButtonListener())
        c_food.setOnClickListener(ButtonListener())
        k_food.setOnClickListener(ButtonListener())
        km_food.setOnClickListener(ButtonListener())
        ks_food.setOnClickListener(ButtonListener())
        kb_food.setOnClickListener(ButtonListener())
        kn_food.setOnClickListener(ButtonListener())
        all_food.setOnClickListener(ButtonListener())

        var placNameRetrofit:Retrofit= GoogleMapAPI.getInstance();
        var placNameApi=placNameRetrofit.create(PlaceAPI::class.java)

        var retrofit:Retrofit= ApiRequest.getInstance();
        val api = retrofit.create(ItemAPI::class.java)
        val Result= api.getItem(Author,ServiceKey,1,400)
        Result.enqueue(object : Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                if (response.isSuccessful) {

                    var result: ResponseData? = response.body()
                    Log.i("test", response.body().toString())
                    sampleData=result!!.data;
                    thread(start = true) {
                        parseData(sampleData!!,placNameApi);
                    }
                    //parseData(sampleData!!,placNameApi);


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
                R.id.all_food,R.id.btnMapOpen -> {

                    intent.putExtra("food",AllFood)
                }
            }

            startActivity(intent)
        }
    }

    fun parseData(data: ArrayList<StoreData>, placeApi: PlaceAPI)
    {
        Log.d("method:printData","총 받은  데이터 갯수: "+data.size.toString())

        var count = 0
        var not = 0
        for (d in data){
            var placName:String?=null
            Log.d("data",d.storeType.toString())
            var result=placeApi.getPlaceID(d.Addr+ "?" +d.storeName,"AIzaSyA-QQQIaULw-TI4BIXjY8PchV2l2IRFRas","ko")
            result.enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {

                        Log.i("testplacName", response.body().toString())

                        val jObject = JSONObject(response.body().toString())
                        val jArray = jObject.getJSONArray("results")



                        for (i in 0 until jArray.length()) {
                            val obj = jArray.getJSONObject(i)
                            val placNameName = obj.getString("place_id")
                            var Geometry=obj.getJSONObject("geometry")
                            var location=Geometry["location"] as JSONObject



                            Log.d(TAG, "title($i): $placNameName, ${location is JSONObject}")
                            divideCat(d, location,placNameName)

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


        }
        Log.d("parseData",KBFood?.toString())




    }
    fun divideCat(d: StoreData, location: JSONObject, placName: String){
        var lat = location.getDouble("lat")
        var long = location.getDouble("lng")
        AllFood?.add(CompositeData(placName!!,lat, long, d))
        when(d.storeType) {
            "양식", "기타양식" -> {
                if (placName != null)
                    EFood?.add(CompositeData(placName!!,lat, long, d))
            }
            "중식" -> {
                if (placName != null)
                    CFood?.add(CompositeData(placName!!,lat, long, d))
            }
            "한식_일반" -> {
                if (placName != null)
                    KGFood?.add(CompositeData(placName!!,lat, long, d))
            }
            "한식_육류" -> {
                if (placName != null)
                    KMFood?.add(CompositeData(placName!!,lat, long, d))
            }
            "한식_찌개류", "한식_면류" -> {
                if (placName != null)
                    KNFood?.add(CompositeData(placName!!,lat, long, d))
            }
            "일식" -> {
                if (placName != null)
                    JFood?.add(CompositeData(placName!!,lat, long, d))
            }
            "한식_해산물" -> {
                if (placName != null)
                    KSFood?.add(CompositeData(placName!!,lat, long, d))
            }
            "한식_분식" -> {
                if (placName != null)
                    KBFood?.add(CompositeData(placName!!,lat, long, d))
            }
        }
    }

}