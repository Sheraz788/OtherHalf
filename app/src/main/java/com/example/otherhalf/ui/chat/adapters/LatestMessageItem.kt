package com.example.otherhalf.ui.chat.adapters

import com.example.otherhalf.R
import com.example.otherhalf.model.ChatMessage
import com.example.otherhalf.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_users_message_row.view.*

class LatestMessageItem(var chatMessage : ChatMessage) : Item<ViewHolder>(){
    var chatPartnerUser : User? = null
    override fun getLayout(): Int {
        return R.layout.latest_users_message_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        var chatPartnerId : String = if(chatMessage.fromId == FirebaseAuth.getInstance().uid){
             chatMessage.toId
        }else{
             chatMessage.fromId
        }

        val fireStoreRef = FirebaseFirestore.getInstance().collection("users").document(
            chatPartnerId
        )
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->

                chatPartnerUser = documentSnapshot?.toObject(User::class.java)

                if(chatPartnerUser != null) {
                    viewHolder.itemView.username_latest_message.text = chatPartnerUser?.userName
                    val imageUri = chatPartnerUser!!.profileImage

                    if(imageUri == ""){
                        viewHolder.itemView.imageview_latest_message.setImageResource(R.drawable.img_user_profile)
                    }else{
                        Picasso.get().load(imageUri).into(viewHolder.itemView.imageview_latest_message)
                    }
                }
            }

        viewHolder.itemView.message_textview_latest_message.text = chatMessage.message
    }

}