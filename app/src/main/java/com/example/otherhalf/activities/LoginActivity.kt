package com.example.otherhalf.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.otherhalf.R
import com.example.otherhalf.utils.Utils
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AnimationActivity() {

    lateinit var callbackManager : CallbackManager
    //Firebase Auth
    private lateinit var auth : FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create()

        //Initializing Firebase variables
        auth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        facebook_btn.setPermissions("email", "public_profile")
        facebook_btn.registerCallback(callbackManager, object : FacebookCallback<LoginResult>{
            override fun onSuccess(loginResult: LoginResult?) {
                Log.d("FaceBook Login", "Facebook Login Success")
                handleFacebookAccessToken(loginResult?.accessToken)

            }

            override fun onCancel() {
                Log.d("Login Cancelled", "Facebook Login Cancelled")
            }

            override fun onError(error: FacebookException?) {
                Log.d("Login Error", "$error")
            }

        })


        login_btn.setOnClickListener {
            Utils.hideSoftKeyboard(this, it)
            loginUser()
        }

        tv_forgot_password.setOnClickListener {

        }
        tv_join_us.setOnClickListener {

            var joinUsIntent = Intent(this, JoinUsActivity::class.java)
            startActivity(joinUsIntent)
        }
    }


    private fun loginUser(){

        if (validateFields()){
            progress_bar.visibility = View.VISIBLE
            login_btn.text = ""
            var email = txt_email.text.toString()
            var password = txt_password.text.toString()

            //authenticating the user to sign in
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        //checking if document for the signed in user exists in collection or not
                        firebaseFirestore.collection("users").document(auth.uid!!).get()
                            .addOnCompleteListener {task->
                                if (task.isSuccessful){
                                    var document = task.result
                                    if(document!!.exists()){
                                        progress_bar.visibility = View.GONE
                                        login_btn.text = "Login"
                                        var mainActivityIntent = Intent(this, MainActivity::class.java)
                                        startActivity(mainActivityIntent)
                                        finish()
                                    }else{
                                        progress_bar.visibility = View.GONE
                                        login_btn.text = "Login"
                                        Toast.makeText(this, "User Record Not Found", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            .addOnFailureListener {exception ->
                                println(exception)
                                progress_bar.visibility = View.GONE
                                login_btn.text = "Login"
                                Toast.makeText(this, "User Record Not Found", Toast.LENGTH_SHORT).show()
                            }
                    }else{
                        progress_bar.visibility = View.GONE
                        login_btn.text = "Login"
                        Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show()
                    }
                }

                .addOnFailureListener {exception ->
                    println(exception)
                    Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show()
                }




        }



    }


    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null)
        val currentUser = auth.currentUser
        saveUserInfo(currentUser)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    fun handleFacebookAccessToken(token: AccessToken?){

        val credentials = FacebookAuthProvider.getCredential(token!!.token)
        auth.signInWithCredential(credentials)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    // Sign in success, update UI with the signed-in user's information
                    val currentUser = auth.currentUser
                    saveUserInfo(currentUser)
                }else{
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Authentication failed.",Toast.LENGTH_SHORT).show()
                    saveUserInfo(null)
                }
            }

    }

    fun saveUserInfo(user : FirebaseUser?){

        if(user != null){

        }else{

        }
    }

    fun validateFields() : Boolean{

        var email = txt_email.text.toString()
        var password = txt_password.text.toString()

        if(email == ""){
            txt_email.error = "Email is required"
            txt_email.requestFocus()
            return false
        }else if(!Utils.isEmailValid(email)){
            txt_email.error = "Valid email is required"
            txt_email.requestFocus()
            return false
        }
        if (password == ""){
            txt_password.error = "Email is required"
            txt_password.requestFocus()
            return false
        }
        return true
    }
}
