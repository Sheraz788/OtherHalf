package com.example.otherhalf.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var uid : String = "",
    var gender : String = "",
    var userAge : String = "",
    var userLocation : String ="",
    var userName: String = "",
    var userEmail : String = "",
    var password : String ="",
    var profileImage : String = "",
    var onlineStatus : Boolean = false,
    var aboutUser : String = "",
    var lookingFor : String = "",
    var lookingForDescription : String = "",
    var education : String = "",
    var occupation : String = "",
    var personalStatus : String = "",
    var kids : String = "",
    var livingStyle : String = "",
    var hobbies : String = "",
    var music : String = "",
    var sports : String =""


) : Parcelable{

    override fun toString(): String {
        return "User(gender=$gender," +
                "userAge=$userAge," +
                "userLocation=$userLocation," +
                "userName=$userName," +
                "userEmail=$userEmail," +
                "password=$password," +
                "profileImage=$profileImage," +
                "onlineStatus=$onlineStatus," +
                "aboutUser=$aboutUser," +
                "lookingFor=$lookingFor," +
                "education=$education," +
                "occupation=$occupation," +
                "personalStatus=$personalStatus," +
                "kids=$kids," +
                "livingStyle=$livingStyle," +
                "hobbies=$hobbies," +
                "music=$music," +
                "sports=$sports)"
    }
}