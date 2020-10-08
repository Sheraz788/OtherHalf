package com.example.otherhalf.ui.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.otherhalf.R
import com.example.otherhalf.activities.MainActivity
import com.example.otherhalf.model.ChatMessage
import com.example.otherhalf.model.User
import com.example.otherhalf.ui.chat.adapters.ChatFromItem
import com.example.otherhalf.ui.chat.adapters.ChatToItem
import com.example.otherhalf.ui.explore.ExploreFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_chat_log.*

/**
 * A simple [Fragment] subclass.
 */
class ChatLogFragment : Fragment() {
    var userInfo : User? = null
    var context : MainActivity? = null
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseFirestore: FirebaseFirestore
    val adapter = GroupAdapter<ViewHolder>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_log, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()
        recyclerview_chat_log.layoutManager = LinearLayoutManager(activity)
        recyclerview_chat_log.adapter = adapter
        context = activity as MainActivity
        userInfo = arguments?.getParcelable<User>("user")!!

        if(userInfo != null){
            tv_username_to.text = userInfo!!.userName
            if(userInfo!!.profileImage != ""){
                Picasso.get().load(userInfo!!.profileImage).into(user_profile_img)
            }else{
                user_profile_img.setImageResource(R.drawable.img_user_profile)
            }
        }

        setEventListener()
        listenForMessages()
       // addChatItems()
    }

    override fun onStart() {
        super.onStart()
        context?.hideBottomNavigation()
    }


    private fun setEventListener(){
        send_button_chat_log.setOnClickListener {
            performSendMessage()
        }
    }

    private fun listenForMessages(){
        val fromId =  firebaseAuth.uid
        val toId =  userInfo?.uid

        val firestoreRef = firebaseFirestore.collection("user-messages/$fromId/$toId")

        firestoreRef.addSnapshotListener { querySnapshot, firebaseFirestoreException ->

            for (chatDocument in querySnapshot!!.documentChanges){

                when(chatDocument.type){

                    DocumentChange.Type.ADDED -> {
                        val chatMessage = chatDocument.document.toObject(ChatMessage::class.java)

                        if(chatMessage.fromId == firebaseAuth.uid){
                            val currentUser = ExploreFragment.currentUser
                            adapter.add(ChatToItem(chatMessage.message, currentUser))

                        }else{
                            adapter.add(ChatFromItem(chatMessage.message, userInfo!!))
                        }
                        recyclerview_chat_log.scrollToPosition(adapter.itemCount -1)
                    }

                    DocumentChange.Type.REMOVED -> {

                    }
                    else -> {

                    }
                }
            }
        }
    }


    private fun performSendMessage(){
        val  message = edittext_chat_log.text.toString()

        val fromId =  firebaseAuth.uid
        val toId =  userInfo?.uid

        val firestoreRef = firebaseFirestore.collection("user-messages/$fromId/$toId").document()

        var chatMessage = ChatMessage(firestoreRef.id, message, fromId!!, toId!!, System.currentTimeMillis()/1000)

        val tofirestoreRef = firebaseFirestore.collection("user-messages/$toId/$fromId").document()

        firestoreRef.set(chatMessage)
            .addOnSuccessListener {
                edittext_chat_log.text.clear()
            }

            .addOnFailureListener {
                Toast.makeText(context, "Unable to send message", Toast.LENGTH_SHORT).show()
            }


        tofirestoreRef.set(chatMessage)


        //saving latest-messages
        val latestMessagesRef = firebaseFirestore.collection("latest-messages/$fromId/$fromId").document("$toId")
        latestMessagesRef.set(chatMessage)
        val latestMessagesToRef = firebaseFirestore.collection("latest-messages/$toId/$toId").document("$fromId")
        latestMessagesToRef.set(chatMessage)
    }

}


