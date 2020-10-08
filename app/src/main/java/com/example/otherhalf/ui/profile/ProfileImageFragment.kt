package com.example.otherhalf.ui.profile

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.otherhalf.R
import com.example.otherhalf.model.User
import com.example.otherhalf.ui.explore.ExploreFragment
import com.example.otherhalf.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.choose_profile_image_dialog.*
import kotlinx.android.synthetic.main.fragment_profile_image.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileImageFragment : Fragment() {

    var selectedPhotoUri : Uri? = null
    lateinit var firebaseStorage : FirebaseStorage
    lateinit var firestoreDB : FirebaseFirestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseStorage = FirebaseStorage.getInstance()
        firestoreDB = FirebaseFirestore.getInstance()
        showUserImage()
        setEventListeners()
    }

    private fun showUserImage(){
        if(ExploreFragment.currentUser?.profileImage != "") {
            Picasso.get().load(ExploreFragment.currentUser?.profileImage).into(user_profile_img)
        }else{
            user_profile_img.setImageResource(R.drawable.img_user_profile)
        }
    }

    private fun setEventListeners(){

        add_image_btn.setOnClickListener {

            val dialog = Dialog(requireContext()).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                setCancelable(false)
                setContentView(R.layout.choose_profile_image_dialog)
            }

            val params = dialog.window?.attributes
            params?.gravity = Gravity.CENTER
            dialog.window?.attributes = params


            dialog.gallery_btn.setOnClickListener{
                chooseProfileImageFromGallery()
                dialog.dismiss()
            }

            dialog.take_shot_btn.setOnClickListener {

            }

            dialog.close_btn.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

    }

    private fun chooseProfileImageFromGallery(){

        if(ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                1
            )
        }else {
            var imageIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(imageIntent, 0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val imageIntent = Intent(Intent.ACTION_PICK)
            startActivityForResult(imageIntent, 0)
        }

    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            showProgressBar()
            //retrieving image data
            selectedPhotoUri = data.data

            var bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, selectedPhotoUri)

            user_profile_img.setImageBitmap(bitmap)
            //ly_add_profile_Image.alpha = 0f

            uploadImageToFirebaseStorage()
        }

    }

    fun uploadImageToFirebaseStorage(){

        if(selectedPhotoUri == null){
            Toast.makeText(activity, "Photo must be selected", Toast.LENGTH_SHORT).show()
        }else{

            val fileName = UUID.randomUUID().toString()

            val storageRef = firebaseStorage.getReference("images/$fileName")

            storageRef.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    storageRef.downloadUrl
                        .addOnSuccessListener {
                            saveProfileImage(it.toString())
                        }

                }

                .addOnFailureListener {
                    hideProgressBar()
                    Log.d("Upload", "Failed to upload image to storage: ${it.message}")
                    Toast.makeText(activity, "Failed to upload Image", Toast.LENGTH_SHORT).show()
                }
        }
    }

    fun saveProfileImage(profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().uid
        var currentUser = ExploreFragment.currentUser!!

        firestoreDB.collection("users").document(uid!!).update("profileImage", profileImageUrl)
            .addOnSuccessListener {
                ExploreFragment.currentUser!!.profileImage = profileImageUrl
                if(loading_progress_bar != null){
                    hideProgressBar()
                }
            }
            .addOnFailureListener {
                hideProgressBar()
                Log.d("Upload", "Failed to upload image to storage: ${it.message}")
                Toast.makeText(activity, "Failed to Save Image", Toast.LENGTH_SHORT).show()
            }
    }

    fun showProgressBar(){
        //saving profile image to firebase, disabling backpress and touch events
        loading_progress_bar.visibility = View.VISIBLE
        Utils.disableBackpress = true
        activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    fun hideProgressBar(){
        loading_progress_bar.visibility = View.GONE
        Utils.disableBackpress = false
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}
