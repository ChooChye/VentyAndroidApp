package com.venty.venty

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.venty.venty.Home.Home
import com.venty.venty.helpers.Utils


class MainActivity : AppCompatActivity() {
    private var utils = Utils
    private var TIME_OUT: Long = 4000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide() //Remove Action Bar


        //val fadeInTop  = AnimationUtils.loadAnimation(this, R.anim.fade_in_top)
        //val fadeInBottom  = AnimationUtils.loadAnimation(this, R.anim.fade_in_bottom)

        //Declare Var
        var logo    = findViewById<ImageView>(R.id.splash_screen_logo)
        var pBar    = findViewById<ProgressBar>(R.id.splash_screen_progressBar)


        //Declare Animation
        utils.declareAnim(this)

        logo.startAnimation(utils.fadeInTop)
        pBar.startAnimation(utils.fadeInBottom)


        //start activity
        Handler().postDelayed(Runnable {
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }, TIME_OUT)
    }
}