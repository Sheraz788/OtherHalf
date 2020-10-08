package com.example.otherhalf.ui.profile

import android.app.Dialog
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.otherhalf.R
import com.example.otherhalf.ui.explore.ExploreFragment
import com.example.otherhalf.ui.profile.adapters.AgeSelectionAdapter
import com.example.otherhalf.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.choose_looking_for_dialog.*
import kotlinx.android.synthetic.main.choose_age_dialog.*
import kotlinx.android.synthetic.main.fragment_edit_profile_basics.*

/**
 * A simple [Fragment] subclass.
 */
class EditProfileBasics : Fragment(), AgeSelectionAdapter.Interaction {

    lateinit var ageSelectionAdapter: AgeSelectionAdapter
    private lateinit var auth : FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile_basics, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        setCurrentUserData()
        setEventListners()
    }

    private fun setCurrentUserData(){
        val currentUser = ExploreFragment.currentUser
        if(currentUser != null){
            txt_gender.setText(currentUser.gender)
            txt_age.setText(currentUser.userAge)
            txt_looking_for.setText(currentUser.lookingFor)
            txt_location.setText(currentUser.userLocation)
        }
    }

    private fun setEventListners(){
        save_btn.setOnClickListener {
            showProgressBar()
            save_btn.text = ""
            updateBasicInformation()
        }
        txt_looking_for.setOnClickListener {
            lookingForDialog()
        }
        txt_age.setOnClickListener {
            ageDialog()
        }
    }


    private fun lookingForDialog() {

        var selectionOption = ""
        val dialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.choose_looking_for_dialog)
        }

        val params = dialog.window?.attributes
        params?.gravity = Gravity.CENTER
        dialog.window?.attributes = params


        dialog.marriage_btn.setOnClickListener{
            selectionOption = activity?.getString(R.string.marriage)!!
            txt_looking_for.setText(selectionOption)
            dialog.dismiss()
        }

        dialog.marriage_misyar_btn.setOnClickListener {
            selectionOption = activity?.getString(R.string.marriage_misyar)!!
            txt_looking_for.setText(selectionOption)
            dialog.dismiss()
        }

        dialog.socialising_btn.setOnClickListener {
            selectionOption = activity?.getString(R.string.socialising)!!
            txt_looking_for.setText(selectionOption)
            dialog.dismiss()
        }

        dialog.online_friends_btn.setOnClickListener {
            selectionOption = activity?.getString(R.string.online_friends)!!
            txt_looking_for.setText(selectionOption)
            dialog.dismiss()
        }

        dialog.close_btn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    lateinit var ageDialog: Dialog
    private fun ageDialog(){


        ageDialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.choose_age_dialog)
        }

        val params = ageDialog.window?.attributes
        params?.gravity = Gravity.CENTER
        ageDialog.window?.attributes = params

        //init recyclerview
        ageDialog.age_list.apply {
            layoutManager = LinearLayoutManager(this@EditProfileBasics.context)
            ageSelectionAdapter = AgeSelectionAdapter(this@EditProfileBasics)
            adapter = ageSelectionAdapter
        }

        //setting up list items
        var ageList : ArrayList<String> = ArrayList()
        for (age in 19..80){
            if(age == 80){
                ageList.add("$age+ years")
            }else{
                ageList.add("$age years")
            }
        }
        ageSelectionAdapter.submitList(ageList)

        ageDialog.age_close_btn.setOnClickListener {
            ageDialog.dismiss()
        }
        ageDialog.show()

    }

    override fun onItemSelected(position: Int, item: String) {
        txt_age.setText(item)
        ageDialog.dismiss()
    }

    private fun updateBasicInformation(){

        if (validatesFields()){
            val fireStoreRef = firebaseFirestore.collection("users").document(auth.uid!!)
            val userAge = txt_age.text.toString()
            val lookingFor = txt_looking_for.text.toString()
            val userLocation = txt_location.text.toString()
            val currentUser = ExploreFragment.currentUser
            fireStoreRef.update("userAge", userAge)
            fireStoreRef.update("lookingFor", lookingFor)
            fireStoreRef.update("userLocation", userLocation)
                .addOnSuccessListener {
                    hideProgressBar()
                    save_btn.text = "Save"
                    if(currentUser != null) {
                        currentUser.userAge = userAge
                        currentUser.lookingFor = lookingFor
                        currentUser.userLocation = userLocation
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


    private fun validatesFields() : Boolean{
        val userAge = txt_age.text.toString()
        val lookingFor = txt_looking_for.text.toString()
        val userLocation = txt_location.text.toString()
        val currentUser = ExploreFragment.currentUser

        if(userAge == ""){
            txt_age.error = getString(R.string.field_cannot_empty)
            return false
        }

        if (userLocation == ""){
            txt_location.error = getString(R.string.field_cannot_empty)
            return false
        }

        if (currentUser != null && userAge == currentUser.userAge && lookingFor == currentUser.lookingFor && userLocation == currentUser.userLocation){
            Toast.makeText(activity, getString(R.string.nothing_to_update), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}