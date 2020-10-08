package com.example.otherhalf.activities

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.otherhalf.R
import com.example.otherhalf.ui.chat.ChatLogFragment
import com.example.otherhalf.ui.menu.MenuFragment
import com.example.otherhalf.ui.profile.ProfileFragment
import com.example.otherhalf.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AnimationActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_explore, R.id.navigation_discover, R.id.navigation_requests, R.id.navigation_chat, R.id.navigation_menu
            )
        )
       // setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        if(Utils.disableBackpress){
            Toast.makeText(this, "Please Wait ...", Toast.LENGTH_SHORT).show()
        }else{
            super.onBackPressed()
        }
    }

    fun hideBottomNavigation(){
        if(nav_view.visibility == View.VISIBLE){
            nav_view.visibility = View.GONE
        }
    }

    fun showBottomNavigation(){
        if(nav_view.visibility == View.GONE){
            nav_view.visibility = View.VISIBLE
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {

        var currentFocus = currentFocus
        if(currentFocus !is EditText || !isTouchInsideView(ev, currentFocus)){
            Utils.hideKeyboardFromActivity(this)
        }

        return super.dispatchTouchEvent(ev)
    }

    private fun isTouchInsideView(ev: MotionEvent, currentFocus: View): Boolean {
        val loc = IntArray(2)
        currentFocus.getLocationOnScreen(loc)
        return (ev.rawX > loc[0] && ev.rawY > loc[1] && ev.rawX < loc[0] + currentFocus.width
                && ev.rawY < loc[1] + currentFocus.height)
    }

}
