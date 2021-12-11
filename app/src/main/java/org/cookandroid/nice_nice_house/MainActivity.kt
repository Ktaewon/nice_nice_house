package org.cookandroid.nice_nice_house

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
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
                if (response.isSuccessful) {

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
        for (sd in data){
            Log.d("프로젝트", sd.storeName + sd.Addr)
            var context = this
            val markerOptions = MarkerOptions()
            val sdLocation = mapApi.addrToPoint(context, sd.Addr)
            val latLng = LatLng(sdLocation!!.latitude, sdLocation!!.longitude)
            addrList.add(latLng)





        }


    }
}