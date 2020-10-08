package com.example.otherhalf.utils

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class CustomViewPager : ViewPager {
    private var swipeable = false

    constructor(context: Context?) : super(context!!) {}

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
    ) {
        swipeable = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (swipeable) {
            super.onTouchEvent(event)
        } else false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (swipeable) {
            super.onInterceptTouchEvent(event)
        } else false
    }

    fun setSwipeable(swipeable: Boolean) {
        this.swipeable = swipeable
    }
}