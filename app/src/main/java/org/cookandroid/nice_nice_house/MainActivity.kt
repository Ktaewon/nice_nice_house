package org.cookandroid.nice_nice_house

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.security.identity.ResultData
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.cookandroid.nice_nice_house.Services.ApiRequest
import org.cookandroid.nice_nice_house.Services.ApiRequest.retrofit
import org.cookandroid.nice_nice_house.Services.ItemAPI
import org.cookandroid.nice_nice_house.data.ResponseData
import org.cookandroid.nice_nice_house.data.StoreData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    val Author:String ="s6uCol2G%2F9kDZDHSm1qm7B7tEzlxymTvk3HNYpdJ1TKK4eUmcW%2F5Lu2mSsBOh%2FOP%2F1ZLytfgLjGK60CnlOJL8w%3D%3D"
    val ServiceKey="s6uCol2G/9kDZDHSm1qm7B7tEzlxymTvk3HNYpdJ1TKK4eUmcW/5Lu2mSsBOh/OP/1ZLytfgLjGK60CnlOJL8w=="
    var sampleData:ArrayList<StoreData>?=null
    var EFood=ArrayList<StoreData>()   //양식, 기타양식
    var CFood=ArrayList<StoreData>()
    var KGFood=ArrayList<StoreData>()
    var KMFood=ArrayList<StoreData>()
    var KNFood=ArrayList<StoreData>() //한식 찌개류,한식 면류
    var JFood=ArrayList<StoreData>()
    var KSFood=ArrayList<StoreData>()
    var KBFood=ArrayList<StoreData>()
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


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        e_food.setOnClickListener(ButtonListener())
        j_food.setOnClickListener(ButtonListener())
        c_food.setOnClickListener(ButtonListener())
        k_food.setOnClickListener(ButtonListener())
        km_food.setOnClickListener(ButtonListener())
        ks_food.setOnClickListener(ButtonListener())
        kb_food.setOnClickListener(ButtonListener())
        kn_food.setOnClickListener(ButtonListener())



        var retrofit:Retrofit= ApiRequest.getInstance();
        val api = retrofit.create(ItemAPI::class.java)
        val Result= api.getItem(Author,ServiceKey,1,400)
        Result.enqueue(object : Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                if (response.isSuccessful) {

                    var result: ResponseData? = response.body()
                    Log.i("test", response.body().toString())
                    sampleData=result!!.data;
                    parseData(sampleData!!);

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
            }

            startActivity(intent)
        }
    }

    fun parseData( data:ArrayList<StoreData>)
    {
        Log.d("method:printData","총 받은  데이터 갯수: "+data.size.toString())

        val duplicateData: HashSet<String> = HashSet<String>()
        for (d in data){
            //Log.d("data",d.storeType.toString())
            when(d.storeType)
            {
                "양식","기타양식" -> EFood?.add(d)
                "중식" -> CFood?.add(d)
                "한식_일반" -> KGFood?.add(d)
                "한식_육류" -> KMFood?.add(d)
                "한식_찌개류","한식_면류" -> KNFood?.add(d)
                "일식" -> JFood?.add(d)
                "한식_해산물" -> KSFood?.add(d)
                "한식_분식" -> KBFood?.add(d)

            }
            duplicateData.add(d.storeType)
        }
        //Log.d("parseData",CFood?.toString())





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