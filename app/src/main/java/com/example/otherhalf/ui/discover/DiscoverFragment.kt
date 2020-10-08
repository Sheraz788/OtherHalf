package com.example.otherhalf.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.otherhalf.R
import com.example.otherhalf.activities.MainActivity
import com.example.otherhalf.model.User
import com.example.otherhalf.ui.explore.ExploreFragment
import com.example.otherhalf.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_discover.*
import java.util.*
import kotlin.collections.ArrayList

class DiscoverFragment : Fragment() {

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseFireStore : FirebaseFirestore
    lateinit var usersList : ArrayList<User>
    var userInfo : User? = null
    var context : MainActivity? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_discover, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFireStore = FirebaseFirestore.getInstance()
        usersList = ArrayList()
        context = activity as MainActivity

        getUsers()
        setEventListeners()

    }

    override fun onResume() {
        super.onResume()
        context?.showBottomNavigation()
    }

    fun getUsers(){

        val firestoreRef = firebaseFireStore.collection("users")
        firestoreRef.get()
            .addOnCompleteListener {task ->
                if (task.isSuccessful){
                    for (userDocument in task.result!!){
                        val user = userDocument.toObject(User::class.java)
                        if (user.uid != ExploreFragment.currentUser?.uid){
                            usersList.add(user)
                        }
                    }

                    setUserData(usersList[0]) //setting default user
                    userInfo = usersList[0]
                }
            }
            .addOnFailureListener {

            }
    }

    fun setEventListeners(){
        close_btn.setOnClickListener {
            userInfo = getNextUser()
            setUserData(userInfo)
        }

        back_btn.setOnClickListener {
            userInfo = getNextUser()
            setUserData(userInfo)
        }

        info_btn.setOnClickListener {view->
            val bundle = bundleOf("user" to userInfo)
            view.findNavController().navigate(R.id.action_navigation_discover_to_user_profile_detail, bundle)
        }

        chat_btn.setOnClickListener {view->
            val bundle = bundleOf("user" to userInfo)
            view.findNavController().navigate(R.id.action_navigation_discover_to_chat_log, bundle)
        }

    }

    fun getNextUser() : User{
        var randomUsersList : ArrayList<User> = ArrayList()

        //for (userItem in usersList){
            val user = usersList[Random().nextInt(usersList.size)]
//            if(!randomUsersList.contains(user)){
//                randomUsersList.add(user)
//            }
//        }

        return user
    }

    fun setUserData(user : User?){

        if(user != null) {
            if (user.profileImage != "") {
                Picasso.get().load(user.profileImage).into(user_profile_image)
            } else if (user.profileImage == "" && user.gender == "Man") {
                user_profile_image.setImageResource(R.drawable.img_user_profile)
            } else {
                user_profile_image.setImageResource(R.drawable.user_image)
            }

            tv_username.text = user.userName
            tv_location.text = Utils.getLocationFromAddress(user.userLocation, context)
        }
    }
}