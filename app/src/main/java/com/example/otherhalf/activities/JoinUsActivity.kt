package com.example.otherhalf.activities

import android.os.Bundle
import com.example.otherhalf.R
import com.example.otherhalf.utils.CustomViewPager
import com.example.otherhalf.ui.signup.*
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import com.ogaclejapan.smarttablayout.utils.v4.FragmentStatePagerItemAdapter
import java.lang.Exception

class JoinUsActivity : AnimationActivity() {

    companion object{
        lateinit var viewPager : CustomViewPager
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_us)
        initializeVariables()
        setFragmentsWithViewPager()
    }

    private fun initializeVariables(){
        viewPager = findViewById(R.id.joinus_view_pager)
    }

    private fun setFragmentsWithViewPager(){

        try {
            val pagerItems = FragmentPagerItems(this)
            pagerItems.add(FragmentPagerItem.of("Page 1", FragmentJoinUsPageOne::class.java))
            pagerItems.add(FragmentPagerItem.of("Page 2", FragmentJoinUsPageTwo::class.java))
            pagerItems.add(FragmentPagerItem.of("Page 3", FragmentJoinUsPageThree::class.java))
            pagerItems.add(FragmentPagerItem.of("Page 4", FragmentJoinUsPageFour::class.java))
            pagerItems.add(FragmentPagerItem.of("Page 5", FragmentJoinUSPageFive::class.java))

            //State Pager helps in saving the state of the page on page change
            var pagerAdapter =
                FragmentStatePagerItemAdapter(this.supportFragmentManager, pagerItems)

            viewPager.adapter = pagerAdapter
            viewPager.currentItem = 0
            viewPager.offscreenPageLimit = 5
            viewPager.setSwipeable(false)
        }catch (e : Exception){
            println("EXCEPTION : $e")
        }
    }


}
