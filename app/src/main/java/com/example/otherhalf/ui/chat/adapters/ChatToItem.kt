package com.example.otherhalf.ui.chat.adapters

import com.example.otherhalf.R
import com.example.otherhalf.model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatToItem(var message : String, var currentUser : User?) : Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.textview_to_row.text = message
        val imageUrl = currentUser?.profileImage

        if (imageUrl != ""){
            Picasso.get().load(imageUrl).into(viewHolder.itemView.imageview_chat_to_row)
        }else{
            viewHolder.itemView.imageview_chat_to_row.setImageResource(R.drawable.img_user_profile)
        }
    }

}