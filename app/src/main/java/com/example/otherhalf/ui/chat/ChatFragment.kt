package com.example.otherhalf.ui.chat


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.otherhalf.R
import com.example.otherhalf.activities.MainActivity
import com.example.otherhalf.model.ChatMessage
import com.example.otherhalf.ui.chat.adapters.LatestMessageItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_chat.*

/**
 * A simple [Fragment] subclass.
 */
class ChatFragment : Fragment() {

    var context : MainActivity? = null

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseFirestore: FirebaseFirestore
    val adapter = GroupAdapter<ViewHolder>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        context = activity as MainActivity
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()

        recyclerview_chat_list.layoutManager = LinearLayoutManager(activity)
        recyclerview_chat_list.adapter = adapter
        recyclerview_chat_list.addItemDecoration(
            DividerItemDecoration(activity,
                DividerItemDecoration.VERTICAL)
        )

        setUpLatestMessages()
        setUpUsersList()
        tv_swiping.setOnClickListener {view ->
            Navigation.createNavigateOnClickListener(R.id.action_navigation_chat_to_chat_log)
        }

        tv_swiping1.setOnClickListener {view ->
            Navigation.createNavigateOnClickListener(R.id.action_navigation_chat_to_chat_log)
        }
    }

    override fun onResume() {
        super.onResume()
        context?.showBottomNavigation()
    }

    private fun setUpUsersList(){

        adapter.setOnItemClickListener { item, view ->

            val latestMessageItem = item as LatestMessageItem
            val chatPartner = bundleOf("user" to latestMessageItem.chatPartnerUser)
            view.findNavController().navigate(R.id.action_navigation_chat_to_chat_log, chatPartner)
        }
    }
    var latestMessagesMap = HashMap<String, ChatMessage>()
    private fun setUpLatestMessages(){

        val fromId = firebaseAuth.uid

        val fireStoreRef = firebaseFirestore.collection("latest-messages").document("$fromId").collection("$fromId")


        fireStoreRef.addSnapshotListener { querySnapshot, firebaseFirestoreException ->

            for(chatDocument in querySnapshot!!.documentChanges){

                when(chatDocument.type){

                    DocumentChange.Type.ADDED -> {
                        val chatMessage = chatDocument.document.toObject(ChatMessage::class.java)
                        latestMessagesMap[chatDocument.document.id] = chatMessage
                        refreshRecylerViewMessages()
                    }

                    DocumentChange.Type.MODIFIED -> {
                        val chatMessage = chatDocument.document.toObject(ChatMessage::class.java)
                        latestMessagesMap[chatDocument.document.id] = chatMessage
                        refreshRecylerViewMessages()
                    }

                    DocumentChange.Type.REMOVED ->{

                    }
                }
            }

        }

    }

    private fun refreshRecylerViewMessages(){
        adapter.clear()
        latestMessagesMap.values.forEach {chatMessage->
            adapter.add(LatestMessageItem(chatMessage))
        }
    }

}
