package com.example.otherhalf.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.otherhalf.R
import com.ramotion.paperonboarding.PaperOnboardingFragment
import com.ramotion.paperonboarding.PaperOnboardingPage
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnRightOutListener
import kotlinx.android.synthetic.main.activity_on_boarding.*

class OnBoardingActivity : AnimationActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
        //hide get started button at the beginning
        get_started_btn.visibility = View.GONE

        var firstOnBoard = PaperOnboardingPage(
            "Welcome to OtherHalf",
            "Largest app to find loved ones",
            R.drawable.background,
            R.drawable.onboard_image1,
            R.drawable.gradiant_circle
        )
        var secondOnBoard = PaperOnboardingPage(
            "You Complete Us",
            "We give you complete Experience",
            R.drawable.background,
            R.drawable.onboard_image2,
            R.drawable.gradiant_circle
        )
        var thirdOnBoard = PaperOnboardingPage(
            "You are Our Focus",
            "We give you a match",
            R.drawable.background,
            R.drawable.onboard_image3,
            R.drawable.gradiant_circle
        )


        var onBoardingElements: ArrayList<PaperOnboardingPage> = ArrayList()
        onBoardingElements.add(firstOnBoard)
        onBoardingElements.add(secondOnBoard)
        onBoardingElements.add(thirdOnBoard)
        var onBoardingFragment = PaperOnboardingFragment.newInstance(onBoardingElements)

        //starting onboardfragment transaction so it will show on screen
        var fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.onboarding_fragment_container, onBoardingFragment)
        fragmentTransaction.commit()
        //when last slide appears show get started button
        onBoardingFragment.setOnChangeListener { prevSlide, nextSlide ->
            if (nextSlide == 2) {
                get_started_btn.visibility = View.VISIBLE
            } else {
                get_started_btn.visibility = View.GONE
            }
        }

        //start login screen when get started clicked
        get_started_btn.setOnClickListener {
            var loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
        }
    }
}
