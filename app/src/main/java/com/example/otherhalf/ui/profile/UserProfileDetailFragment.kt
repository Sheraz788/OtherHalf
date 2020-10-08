package com.example.otherhalf.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

import com.example.otherhalf.R
import com.example.otherhalf.activities.MainActivity
import com.example.otherhalf.model.User
import com.example.otherhalf.utils.Utils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_user_profile_detail.*

/**
 * A simple [Fragment] subclass.
 */
class UserProfileDetailFragment : Fragment() {

    var userInfo : User? = null
    var context : MainActivity? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context = activity as MainActivity
        userInfo = arguments?.getParcelable<User>("user")!!
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUserProfileData()
        setUpEventListeners()
    }

    override fun onStart() {
        super.onStart()
        context?.hideBottomNavigation()
    }

    fun setUpEventListeners(){
        chat_btn.setOnClickListener {view->
            var bundle = bundleOf("user" to userInfo)
            view.findNavController().navigate(R.id.action_user_profile_detail_to_chat_log, bundle)
        }

        like_btn.setOnClickListener {

        }

    }


    fun setUpUserProfileData(){


        //setting up profile images
        userInfo?.let{userInfo->
            if (userInfo.profileImage != "") {
                Picasso.get().load(userInfo.profileImage).into(img_profile_conver)
                Picasso.get().load(userInfo.profileImage).into(profile_image)
            }
            tv_username.text = userInfo.userName
            tv_age.text = userInfo.userAge
            tv_occupation.text = Utils.getLocationFromAddress(userInfo.userLocation, context)

            if(userInfo.aboutUser == ""){
                rel_profile_details.visibility = View.GONE
            }else{
                rel_profile_details.visibility = View.VISIBLE
                tv_about.text = userInfo.aboutUser
            }

            tv_basic_age.text = userInfo.userAge
            if(userInfo.lookingFor == ""){
                tv_looking_for.text = getString(R.string.not_set_value)
            }else{
                tv_looking_for.text = userInfo.lookingFor
            }

            tv_current_city.text = Utils.getLocationFromAddress(userInfo.userLocation, context)

        }


    }

}
