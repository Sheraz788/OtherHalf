package com.example.otherhalf.ui.profile

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.otherhalf.R
import com.example.otherhalf.model.User
import com.example.otherhalf.ui.explore.ExploreFragment
import com.example.otherhalf.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_edit_profile_description.*

/**
 * A simple [Fragment] subclass.
 */
class EditProfileDescription : Fragment() {
    private lateinit var auth : FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        setUserData()
        setEventListeners()
    }

    fun setUserData(){
        val currentUser = ExploreFragment.currentUser
        if(currentUser != null){
            txt_about_me.setText(currentUser.aboutUser)
            txt_looking_for.setText(currentUser.lookingForDescription)
        }
    }
    fun setEventListeners(){
        save_btn.setOnClickListener {
            showProgressBar()
            save_btn.text = ""
            updateUserDescription()
        }
    }

    fun updateUserDescription(){

        if (validatesFields()){
            val about = txt_about_me.text.toString()
            val lookingForDescription = txt_looking_for.text.toString()
            val currentUser = ExploreFragment.currentUser
            val fireStoreRef = firebaseFirestore.collection("users").document(auth.uid!!)

            fireStoreRef.update("aboutUser", about)
            fireStoreRef.update("lookingForDescription", lookingForDescription)
                .addOnSuccessListener {
                    hideProgressBar()
                    save_btn.text = "Save"
                    if(currentUser != null) {
                        currentUser.aboutUser = about
                        currentUser.lookingForDescription = lookingForDescription
                    }
                    Toast.makeText(activity, "Information Updated", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    println("Exception in updating $it")
                    Toast.makeText(activity, "Failed to Update", Toast.LENGTH_SHORT).show()
                }
        }


    }

    fun showProgressBar(){
        var progressBar = activity?.findViewById<ProgressBar>(R.id.progress_bar)
//        //changing progressBar color
        progressBar?.indeterminateDrawable
            ?.setColorFilter(ContextCompat.getColor(requireActivity(), R.color.white), PorterDuff.Mode.SRC_IN )
        //saving profile image to firebase, disabling backpress and touch events
        progress_bar.visibility = View.VISIBLE

        Utils.disableBackpress = true
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    fun hideProgressBar(){
        progress_bar.visibility = View.GONE
        Utils.disableBackpress = false
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }


    fun validatesFields() : Boolean{
        val about = txt_about_me.text.toString()
        val lookingForDescription = txt_looking_for.text.toString()

        if(about == ""){
            txt_about_me.error = getString(R.string.field_cannot_empty)
            return false
        }
        if(lookingForDescription == ""){
            txt_looking_for.error = getString(R.string.field_cannot_empty)
            return false
        }

        if(about == ExploreFragment.currentUser?.aboutUser && lookingForDescription == ExploreFragment.currentUser?.lookingFor){
            Toast.makeText(activity, getString(R.string.nothing_to_update), Toast.LENGTH_SHORT).show()
            hideProgressBar()
            return false
        }
        return true
    }
}