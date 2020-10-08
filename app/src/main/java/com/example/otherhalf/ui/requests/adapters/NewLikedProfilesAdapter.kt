package com.example.otherhalf.ui.requests.adapters

import com.example.otherhalf.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

class NewLikedProfilesAdapter : Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.requests_liked_items_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
    }

}