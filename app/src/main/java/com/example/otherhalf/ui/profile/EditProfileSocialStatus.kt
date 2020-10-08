package com.example.otherhalf.ui.profile

import android.app.Dialog
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.otherhalf.R
import com.example.otherhalf.ui.explore.ExploreFragment
import com.example.otherhalf.ui.profile.adapters.*
import com.example.otherhalf.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.choose_education_dialog.*
import kotlinx.android.synthetic.main.choose_occupation_dialog.*
import kotlinx.android.synthetic.main.fragment_edit_profile_social_status.*

/**
 * A simple [Fragment] subclass.
 */
class EditProfileSocialStatus : Fragment(), EducationSelectionAdapter.Interaction, OccupationSelectionAdapter.Interaction, StatusSelectionAdapter.Interaction, KidsSelectionAdapter.Interaction, LivingStyleSelectionAdapter.Interaction {

    lateinit var educationSelectionAdapter: EducationSelectionAdapter
    lateinit var occupationSelectionAdapter: OccupationSelectionAdapter
    lateinit var statusSelectionAdapter: StatusSelectionAdapter
    lateinit var kidsSelectionAdapter: KidsSelectionAdapter
    lateinit var livingStyleSelectionAdapter: LivingStyleSelectionAdapter
    private lateinit var auth : FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile_social_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        setCurrentUserData()
        setEventListeners()
    }

    private fun setCurrentUserData(){
        val currentUser = ExploreFragment.currentUser
        if(currentUser != null){
            txt_education.setText(currentUser.education)
            txt_occupation.setText(currentUser.occupation)
            txt_status.setText(currentUser.personalStatus)
            txt_kids.setText(currentUser.kids)
            txt_living_style.setText(currentUser.livingStyle)
        }
    }

    private fun setEventListeners(){
        txt_education.setOnClickListener {
            educationDialog()
        }

        txt_occupation.setOnClickListener {
            occupationDialog()
        }

        txt_status.setOnClickListener {
            statusDialog()
        }

        txt_kids.setOnClickListener {
            kidsDialog()
        }

        txt_living_style.setOnClickListener {
            livingStyleDialog()
        }

        save_btn.setOnClickListener {
            showProgressBar()
            save_btn.text = ""
            updateSocialStatusData()
        }
    }
    lateinit var educationDialog: Dialog
    private fun educationDialog(){

        educationDialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.choose_education_dialog)
        }

        val params = educationDialog.window?.attributes
        params?.gravity = Gravity.CENTER
        educationDialog.window?.attributes = params

        //init recyclerview
        educationDialog.education_list.apply {
            layoutManager = LinearLayoutManager(this@EditProfileSocialStatus.context)
            educationSelectionAdapter = EducationSelectionAdapter(this@EditProfileSocialStatus)
            adapter = educationSelectionAdapter
        }

        //setting up list items
        var educationsList : ArrayList<String> = ArrayList()

        educationsList.add("Not Set")
        educationsList.add("Elementary School")
        educationsList.add("Middle School")
        educationsList.add("High School")
        educationsList.add("Technical Education")
        educationsList.add("Bachelor's Degree")
        educationsList.add("Masters Degree")
        educationsList.add("PhD or Doctorate")
        educationsList.add("School of Life")
        educationSelectionAdapter.submitList(educationsList)

        educationDialog.education_close_btn.setOnClickListener {
            educationDialog.dismiss()
        }
        educationDialog.show()

    }

    override fun onItemSelected(position: Int, item: String) {
        txt_education.setText(item)
        educationDialog.dismiss()
    }


    lateinit var occupationDialog: Dialog
    private fun occupationDialog(){
        occupationDialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.choose_occupation_dialog)
        }

        val params = occupationDialog.window?.attributes
        params?.gravity = Gravity.CENTER
        occupationDialog.window?.attributes = params

        //init recyclerview
        occupationDialog.items_list.apply {
            layoutManager = LinearLayoutManager(this@EditProfileSocialStatus.context)
            occupationSelectionAdapter = OccupationSelectionAdapter(this@EditProfileSocialStatus)
            adapter = occupationSelectionAdapter
        }

        //setting up list items
        var occupationsList : ArrayList<String> = ArrayList()

        occupationsList.add("Not Set")
        occupationsList.add("Student")
        occupationsList.add("Public Sector")
        occupationsList.add("Private Sector")
        occupationsList.add("Manager")
        occupationsList.add("Self Employed")
        occupationsList.add("Unemployed")
        occupationsList.add("Retired")
        occupationSelectionAdapter.submitList(occupationsList)

        occupationDialog.close_btn.setOnClickListener {
            occupationDialog.dismiss()
        }
        occupationDialog.show()

    }

    override fun onItemSelected(position: Int, item: String, isOccupation : Boolean, isStatus : Boolean, isKids : Boolean, isLivingStyle : Boolean) {
       if(isOccupation){
           txt_occupation.setText(item)
           occupationDialog.dismiss()
       }else if(isStatus){
           txt_status.setText(item)
           statusDialog.dismiss()
       }else if(isKids){
           txt_kids.setText(item)
           kidsDialog.dismiss()
       }else if(isLivingStyle){
           txt_living_style.setText(item)
           livingStyleDialog.dismiss()
       }
    }

    lateinit var statusDialog : Dialog

    private fun statusDialog(){

        statusDialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.choose_occupation_dialog)
        }

        statusDialog.tv_title.text = getString(R.string.status)

        val params = statusDialog.window?.attributes
        params?.gravity = Gravity.CENTER
        statusDialog.window?.attributes = params

        //init recyclerview
        statusDialog.items_list.apply {
            layoutManager = LinearLayoutManager(this@EditProfileSocialStatus.context)
            statusSelectionAdapter = StatusSelectionAdapter(this@EditProfileSocialStatus)
            adapter = statusSelectionAdapter
        }

        //setting up list items
        var statusList : ArrayList<String> = ArrayList()

        statusList.add("Not Set")
        statusList.add("Signle")
        statusList.add("Married")
        statusList.add("Separated")
        statusList.add("Divorced")
        statusList.add("Widowed")
        statusSelectionAdapter.submitList(statusList)

        statusDialog.close_btn.setOnClickListener {
            statusDialog.dismiss()
        }
        statusDialog.show()
    }

    lateinit var kidsDialog : Dialog

    private fun kidsDialog(){

        kidsDialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.choose_occupation_dialog)
        }

        kidsDialog.tv_title.text = getString(R.string.kids)

        val params = kidsDialog.window?.attributes
        params?.gravity = Gravity.CENTER
        kidsDialog.window?.attributes = params

        //init recyclerview
        kidsDialog.items_list.apply {
            layoutManager = LinearLayoutManager(this@EditProfileSocialStatus.context)
            kidsSelectionAdapter = KidsSelectionAdapter(this@EditProfileSocialStatus)
            adapter = kidsSelectionAdapter
        }

        //setting up list items
        var kidsList : ArrayList<String> = ArrayList()

        kidsList.add("Not Set")
        kidsList.add("No")
        kidsList.add("Yes")
        kidsSelectionAdapter.submitList(kidsList)

        kidsDialog.close_btn.setOnClickListener {
            kidsDialog.dismiss()
        }
        kidsDialog.show()
    }


    lateinit var livingStyleDialog : Dialog

    private fun livingStyleDialog(){

        livingStyleDialog = Dialog(requireContext()).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.choose_occupation_dialog)
        }

        livingStyleDialog.tv_title.text = getString(R.string.living_situation)
        val params = livingStyleDialog.window?.attributes
        params?.gravity = Gravity.CENTER
        livingStyleDialog.window?.attributes = params

        //init recyclerview
        livingStyleDialog.items_list.apply {
            layoutManager = LinearLayoutManager(this@EditProfileSocialStatus.context)
            livingStyleSelectionAdapter = LivingStyleSelectionAdapter(this@EditProfileSocialStatus)
            adapter = livingStyleSelectionAdapter
        }

        //setting up list items
        var livingStyleList : ArrayList<String> = ArrayList()

        livingStyleList.add("Not Set")
        livingStyleList.add("I live along")
        livingStyleList.add("I live with friends")
        livingStyleList.add("I live with my family")
        livingStyleList.add("I live with my spouse")
        livingStyleList.add("I live with my kids")
        livingStyleList.add("Other")
        livingStyleSelectionAdapter.submitList(livingStyleList)

        livingStyleDialog.close_btn.setOnClickListener {
            livingStyleDialog.dismiss()
        }
        livingStyleDialog.show()
    }


    private fun updateSocialStatusData(){

        if(validateFields()){

            val education = txt_education.text.toString()
            val occupation = txt_occupation.text.toString()
            val personalStatus = txt_status.text.toString()
            val kids = txt_kids.text.toString()
            val livingStyle = txt_living_style.text.toString()
            val currentUser = ExploreFragment.currentUser
            val fireStoreRef = firebaseFirestore.collection("users").document(auth.uid!!)

            fireStoreRef.update("education", education)
            fireStoreRef.update("occupation", occupation)
            fireStoreRef.update("personalStatus", personalStatus)
            fireStoreRef.update("kids", kids)
            fireStoreRef.update("livingStyle", livingStyle)
                .addOnSuccessListener {
                    hideProgressBar()
                    save_btn.text = "Save"
                    if(currentUser != null) {
                        currentUser.education = education
                        currentUser.occupation = occupation
                        currentUser.personalStatus = personalStatus
                        currentUser.kids = kids
                        currentUser.livingStyle = livingStyle
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

    private fun validateFields() : Boolean{
        val currentUser = ExploreFragment.currentUser
        val education = txt_education.text.toString()
        val occupation = txt_occupation.text.toString()
        val personalStatus = txt_status.text.toString()
        val kids = txt_kids.text.toString()
        val livingStyle = txt_living_style.toString()

        if(currentUser != null) {
            if (education == currentUser.education && occupation == currentUser.occupation && personalStatus == currentUser.personalStatus && kids == currentUser.kids && livingStyle == currentUser.livingStyle) {
                Toast.makeText(activity, getString(R.string.nothing_to_update), Toast.LENGTH_SHORT).show()
                hideProgressBar()
                return false
            }
        }

        return true
    }
}