package org.cookandroid.nice_nice_house

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var btnMapOpen = findViewById<Button>(R.id.btnMapOpen)
        btnMapOpen.setOnClickListener {
            var intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
    }
}