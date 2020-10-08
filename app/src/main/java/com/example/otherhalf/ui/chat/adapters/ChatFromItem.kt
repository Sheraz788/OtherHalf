package com.example.otherhalf.ui.chat.adapters

import com.example.otherhalf.R
import com.example.otherhalf.model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_from_row.view.*

class ChatFromItem(var message : String, var user : User) : Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.textview_from_row.text = message
        val imageUrl = user.profileImage

        if(imageUrl != ""){
            Picasso.get().load(imageUrl).into(viewHolder.itemView.imageview_chat_from_row)
        }else{
            viewHolder.itemView.imageview_chat_from_row.setImageResource(R.drawable.img_user_profile)
        }

    }

}