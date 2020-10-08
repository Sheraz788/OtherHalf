package com.example.otherhalf.utils

import android.app.Activity
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.otherhalf.activities.MainActivity
import java.util.regex.Pattern

class Utils {

    companion object {

        //to disable backpress when progressbar shown
        var disableBackpress = false


        fun isEmailValid(email: String?): Boolean {
            var isValid = false
            val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
            val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
            val matcher = pattern.matcher(email)
            if (matcher.matches()) {
                isValid = true
            }
            return isValid
        }

        fun getLocationFromAddress(address : String, context : MainActivity?) : String{
            var countryName = StringBuilder()
            var geoCoder = Geocoder(context)
            lateinit var addresses : MutableList<Address>

            try{
                addresses = geoCoder.getFromLocationName(address, 5)
                addresses?.let {
                    for (location in addresses) {
                        countryName.append(location.locality +", "+location.countryCode)
                    }
                }
            }catch (e : java.lang.Exception){
               println(e)
            }
            return countryName.toString()
        }

        fun hideKeyboardFromActivity(context: Activity) {
            // TODO Auto-generated method stub
            val im = context
                .applicationContext.getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            im.hideSoftInputFromWindow(
                context.window.decorView
                    .windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
        }

        fun hideSoftKeyboard(activity: Activity, view: View) {
            val imm = activity
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.applicationWindowToken, 0)
        }


    }
}