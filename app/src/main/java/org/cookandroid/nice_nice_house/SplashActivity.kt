package org.cookandroid.nice_nice_house

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import javax.xml.datatype.DatatypeConstants.DURATION

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar!!.hide()

        Handler().postDelayed({
                finish()
        },7000)

    }
    companion object {
        private const val DURATION : Long = 3000
    }




}