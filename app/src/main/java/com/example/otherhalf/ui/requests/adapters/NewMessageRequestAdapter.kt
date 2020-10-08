package com.example.otherhalf.ui.requests.adapters

import com.example.otherhalf.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

class NewMessageRequestAdapter : Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.new_message_row_item
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
    }

}