package com.example.otherhalf.ui.signup


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.otherhalf.R
import com.example.otherhalf.activities.JoinUsActivity
import kotlinx.android.synthetic.main.fragment_join_us_page_one.*
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class FragmentJoinUsPageOne : Fragment() {

    lateinit var context : JoinUsActivity
    companion object{
        var userGender = ""
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_join_us_page_one, container, false)
        }catch (e : Exception){
            println("Exception : $e")
        }
        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.context = JoinUsActivity()

        setupOnClickListener()
    }

    private fun setupOnClickListener(){
        try {
            man_btn.setOnClickListener {
                userGender = man_btn.text.toString()
                loadNextPage()
            }
            woman_btn.setOnClickListener {
                userGender = man_btn.text.toString()
                loadNextPage()
            }
            back_btn.setOnClickListener {
                var currentViewPagerItem = JoinUsActivity.viewPager.currentItem
                if (currentViewPagerItem > 0) {
                    JoinUsActivity.viewPager.currentItem = currentViewPagerItem - 1
                } else {
                    requireActivity().finish()
                }
            }
        }catch (e : Exception){
            println("ECEPTION : $e")
        }

    }

    private fun loadNextPage(){
        JoinUsActivity.viewPager.currentItem = JoinUsActivity.viewPager.currentItem + 1
    }

}
