package com.example.otherhalf.ui.signup


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.otherhalf.R
import com.example.otherhalf.activities.JoinUsActivity
import kotlinx.android.synthetic.main.fragment_join_us_page_three.*

/**
 * A simple [Fragment] subclass.
 */
class FragmentJoinUsPageThree : Fragment() {

    companion object{
        var userLocation = ""
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_join_us_page_three, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnClickListener()
    }

    private fun setupOnClickListener(){
        next_btn.setOnClickListener {
            if(validateField()){
                userLocation = txt_city_name.text.toString()
            }
            loadNextPage()
        }
        back_btn.setOnClickListener {
            var currentViewPagerItem = JoinUsActivity.viewPager.currentItem
            if (currentViewPagerItem > 0){
                JoinUsActivity.viewPager.currentItem = currentViewPagerItem -1
            }else{
                requireActivity().finish()
            }
        }
    }

    private fun loadNextPage(){
        JoinUsActivity.viewPager.currentItem = JoinUsActivity.viewPager.currentItem + 1
    }

    private fun validateField() : Boolean{
        var age = txt_city_name.text.toString()
        if(age == ""){
            txt_city_name.error = "Location is required"
            txt_city_name.requestFocus()
            return false
        }
        return true
    }

}
