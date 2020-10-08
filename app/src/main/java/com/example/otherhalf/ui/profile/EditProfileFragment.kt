package com.example.otherhalf.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController

import com.example.otherhalf.R
import com.example.otherhalf.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_edit_profile.*

/**
 * A simple [Fragment] subclass.
 */
class EditProfileFragment : Fragment() {
    var context : MainActivity? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context = activity as MainActivity

        rel_profile_photo.setOnClickListener {
            it.findNavController().navigate(R.id.action_edit_profile_fragment_to_profile_image_fragment)
        }

        rel_description.setOnClickListener {
            it.findNavController().navigate(R.id.action_edit_profile_fragment_to_editProfileDescription)
        }

        rel_basics.setOnClickListener {
            it.findNavController().navigate(R.id.action_edit_profile_fragment_to_editProfileBasics)
        }

        rel_social_status.setOnClickListener {
            it.findNavController().navigate(R.id.action_edit_profile_fragment_to_editProfileSocialStatus)
        }
    }

    override fun onStart() {
        super.onStart()
        context?.hideBottomNavigation()
    }

}
