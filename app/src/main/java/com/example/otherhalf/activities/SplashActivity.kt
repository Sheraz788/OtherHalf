package com.example.otherhalf.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import com.example.otherhalf.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    val defaultTimer = 3000
    val defaultCallbackTimer = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val animation = AnimationUtils.loadAnimation(this, R.anim.splash_anim)
        animation.interpolator = LinearInterpolator()
        animation.repeatCount = Animation.INFINITE
        animation.duration = 700
        splash_logo.startAnimation(animation)

        loadLoginScreen(defaultTimer.toLong(), defaultCallbackTimer.toLong())

    }

    fun loadLoginScreen(defaultTimer : Long, defaultCallbackTimer: Long){

        object : CountDownTimer(defaultTimer, defaultCallbackTimer){

            override fun onFinish() {
                var intent = Intent(this@SplashActivity, OnBoardingActivity::class.java)
                startActivity(intent)
                finish()
            }
            override fun onTick(millisUntilFinished: Long) {

            }
        }.start()
    }
}
