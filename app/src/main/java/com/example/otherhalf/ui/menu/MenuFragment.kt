package com.example.otherhalf.ui.menu


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController

import com.example.otherhalf.R
import com.example.otherhalf.activities.MainActivity
import com.example.otherhalf.ui.explore.ExploreFragment
import com.example.otherhalf.ui.profile.ProfileFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_menu.*

/**
 * A simple [Fragment] subclass.
 */
class MenuFragment : Fragment() {

    var context : MainActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        context = activity as MainActivity
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpUserProfileData()
        rel_my_profile.setOnClickListener {view ->
            view.findNavController().navigate(R.id.action_navigation_menu_to_profile_fragment)
        }

        rel_edit_profile.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_navigation_menu_to_edit_profile_fragment)
        }

        rel_settings.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_navigation_menu_to_settings_fragment)
        }

    }

    override fun onResume() {
        super.onResume()
        context?.showBottomNavigation()
    }

    fun setUpUserProfileData(){

        var currentUser = ExploreFragment.currentUser

        if(currentUser != null){

            if(currentUser.profileImage != "") {
                Picasso.get().load(currentUser.profileImage).into(profile_image)
            }else{
                profile_image.setImageResource(R.drawable.img_user_profile)
            }
            tv_username.text = currentUser.userName
        }
    }



}
