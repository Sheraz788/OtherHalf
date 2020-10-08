package com.example.otherhalf.ui.explore

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.otherhalf.R
import com.example.otherhalf.activities.MainActivity
import com.example.otherhalf.model.User
import com.example.otherhalf.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_explore.*

class ExploreFragment : Fragment() {

    private lateinit var dashboardViewModel: ExploreViewModel
    lateinit var exploreItemsAdapter: ExploreItemsAdapter
    var context : MainActivity? = null

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseFirestore: FirebaseFirestore
    companion object{
        var currentUser : User? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        dashboardViewModel = ViewModelProviders.of(this).get(ExploreViewModel::class.java)
        context = activity as MainActivity
        val root = inflater.inflate(R.layout.fragment_explore, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgressBar()
        fetchCurrentUser()
        listenForDataChanges()
        fetchUsers()
    }
    private fun setExploreAdapter(usersList: MutableList<User>){
        exploreItemsAdapter = ExploreItemsAdapter(context, usersList)
        val gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        explore_users_list.layoutManager = gridLayoutManager
        explore_users_list.adapter = exploreItemsAdapter
    }


    override fun onResume() {
        super.onResume()
        context?.showBottomNavigation()
    }

    fun fetchCurrentUser(){
        val uid = firebaseAuth.uid
        firebaseFirestore.collection("users").document("$uid").get()
            .addOnSuccessListener {userDocument ->
                currentUser = userDocument.toObject(User::class.java)
            }
            .addOnFailureListener {
                Log.d("Current User", "Failed to fetch Current User: ${it.message}")
                Toast.makeText(activity, "Failed to set Current User", Toast.LENGTH_SHORT).show()
            }

    }

    fun listenForDataChanges(){

        firebaseFirestore.collection("users")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if(firebaseFirestoreException != null){
                    Toast.makeText(context, "Error in fetching users", Toast.LENGTH_SHORT).show()
                }else{

                }
            }


    }

    fun fetchUsers(){
        firebaseFirestore.collection("users").get()
            .addOnSuccessListener { users ->
                val usersList : MutableList<User> = ArrayList()
                for (user in users){
                    usersList.add(user.toObject(User::class.java))
                }
                setExploreAdapter(usersList)
                hideProgressBar()
            }

            .addOnFailureListener {

            }
    }

    fun showProgressBar(){
        //saving profile image to firebase, disabling backpress and touch events
        loading_users.visibility = View.VISIBLE
        Utils.disableBackpress = true
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    fun hideProgressBar(){
        loading_users.visibility = View.GONE
        Utils.disableBackpress = false
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}


