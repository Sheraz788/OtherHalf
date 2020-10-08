package com.example.otherhalf.ui.profile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.otherhalf.R
import com.example.otherhalf.activities.MainActivity
import com.example.otherhalf.ui.explore.ExploreFragment
import com.example.otherhalf.utils.Utils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {


    var context : MainActivity? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context = activity as MainActivity
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUserProfileData()
    }

    override fun onStart() {
        super.onStart()
        context?.hideBottomNavigation()
    }

    fun setUpUserProfileData(){

        var currentUser = ExploreFragment.currentUser

        //setting up profile images
        if(currentUser != null) {
            if (currentUser.profileImage != "") {
                Picasso.get().load(currentUser.profileImage).into(img_profile_conver)
                Picasso.get().load(currentUser.profileImage).into(profile_image)
            }
            tv_username.text = currentUser.userName
            tv_age.text = currentUser.userAge
            tv_occupation.text = Utils.getLocationFromAddress(currentUser.userLocation, context)

            if(currentUser.aboutUser == ""){
                rel_profile_details.visibility = View.GONE
            }else{
                rel_profile_details.visibility = View.VISIBLE
                tv_about.text = currentUser.aboutUser
            }

            tv_basic_age.text = currentUser.userAge
            if(currentUser.lookingFor == ""){
                tv_looking_for.text = getString(R.string.not_set_value)
            }else{
                tv_looking_for.text = currentUser.lookingFor
            }

            tv_current_city.text = Utils.getLocationFromAddress(currentUser.userLocation, context)

        }


    }


}
