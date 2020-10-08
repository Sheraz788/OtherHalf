package com.example.otherhalf.ui.signup


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.otherhalf.R
import com.example.otherhalf.activities.JoinUsActivity
import com.example.otherhalf.activities.LoginActivity
import com.example.otherhalf.model.User
import com.example.otherhalf.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_join_uspage_five.*

/**
 * A simple [Fragment] subclass.
 */
class FragmentJoinUSPageFive : Fragment() {


    lateinit var firebaseAuth : FirebaseAuth
    lateinit var firestoreDB : FirebaseFirestore

    companion object{
        var userEmail = ""
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_join_uspage_five, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firestoreDB = FirebaseFirestore.getInstance()

        setupOnClickListener()
    }

    private fun setupOnClickListener(){
        next_btn.setOnClickListener {
            createUser()
            //loadNextPage()
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

    private fun createUser(){
        if(validateField()) {
            progress_bar.visibility = View.VISIBLE
            next_btn.text = ""
            var userGender = FragmentJoinUsPageOne.userGender
            var userAge = FragmentJoinUsPageTwo.userAge
            var userLocation = FragmentJoinUsPageThree.userLocation
            var userName = FragmentJoinUsPageFour.userName
            var userEmail = txt_email.text.toString()
            var password = "123456"

            var user = User(firebaseAuth.uid!!,
                userGender,
                userAge,
                userLocation,
                userName,
                userEmail,
                password,
                "",
                false,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
            )

            firebaseAuth.createUserWithEmailAndPassword(userEmail, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firestoreDB.collection("users").document(firebaseAuth.uid!!).set(user)
                            .addOnSuccessListener {
                                progress_bar.visibility = View.GONE
                                next_btn.text = "Sign Up"
                                firebaseAuth.signOut()
                                val loginIntent = Intent(activity, LoginActivity::class.java)
                                startActivity(loginIntent)
                                activity?.finish()
                            }
                            .addOnFailureListener { exception ->
                                progress_bar.visibility = View.GONE
                                next_btn.text = "Sign Up"
                                Toast.makeText(activity, "Save User $exception", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }else if (task.exception is FirebaseAuthUserCollisionException){
                        progress_bar.visibility = View.GONE
                        next_btn.text = "Sign Up"
                        Toast.makeText(activity, "User with this email already exists ..", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    progress_bar.visibility = View.GONE
                    next_btn.text = "Sign Up"
                    Toast.makeText(activity, "Register User $exception", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun validateField() : Boolean{
        var email = txt_email.text.toString()
        if(email == ""){
            txt_email.error = "Email is required"
            txt_email.requestFocus()
            return false
        }else if(!Utils.isEmailValid(email)){
            txt_email.error = "Valid email is required"
            txt_email.requestFocus()
            return false
        }
        return true
    }
}
