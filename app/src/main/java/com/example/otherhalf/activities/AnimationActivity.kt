package com.example.otherhalf.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.otherhalf.R

open class AnimationActivity : AppCompatActivity() {

    var onStartCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_animation)

        onStartCount = 1
        if (savedInstanceState == null){
            this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }else{
            onStartCount = 2
        }

    }

    override fun onStart() {
        super.onStart()
        if(onStartCount > 1){
            this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right)
        }else if (onStartCount == 1){
            onStartCount++
        }
    }
}
