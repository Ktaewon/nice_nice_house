package org.cookandroid.nice_nice_house

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.security.identity.ResultData
import android.util.Log
import android.widget.Button
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
    var addrList=ArrayList<LatLng>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var btnMapOpen = findViewById<Button>(R.id.btnMapOpen)
        btnMapOpen.setOnClickListener {
            var intent = Intent(this, MapActivity::class.java)
            intent.putExtra("storedData", sampleData)
            intent.putExtra("addrList",addrList)
            startActivity(intent)
        }


        var retrofit:Retrofit= ApiRequest.getInstance();
        val api = retrofit.create(ItemAPI::class.java)
        val Result= api.getItem(Author,ServiceKey,1,400)
        Result.enqueue(object : Callback<ResponseData> {
            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                Log.d("success","실행2");
                if (response.isSuccessful) {

                    Log.d("success","실행3");
                    var result: ResponseData? = response.body()
                    Log.i("test", response.body().toString())
                    sampleData=result!!.data;

                    printData(sampleData!!);



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

    fun printData( data:ArrayList<StoreData>)
    {
        Log.d("print",data.size.toString())
        var mapApi=MapActivity()
        var count = 0
        var size = data.size / 4
        var addrList1 = ArrayList<LatLng>()
        var addrList2 = ArrayList<LatLng>()
        var addrList3 = ArrayList<LatLng>()
        var addrList4 = ArrayList<LatLng>()
        var temp = ArrayList<String>()
        val duplicateData: HashSet<String> = HashSet<String>()
        for (d in data){
            duplicateData.add(d.storeType)
        }
        Log.d("프로젝트", duplicateData.toString())



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