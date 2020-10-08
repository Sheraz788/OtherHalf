package com.example.otherhalf.ui.explore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.otherhalf.R
import com.example.otherhalf.activities.MainActivity
import com.example.otherhalf.model.User
import com.example.otherhalf.utils.Utils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.explore_items_adapter.view.*

class ExploreItemsAdapter(var context: MainActivity?, var usersList: MutableList<User>) : RecyclerView.Adapter<ExploreItemsViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreItemsViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.explore_items_adapter, parent, false)
        return ExploreItemsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    override fun onBindViewHolder(holder: ExploreItemsViewHolder, position: Int) {

        var userData = usersList[position]

        holder.itemView.tv_username.text = userData.userName
        holder.itemView.tv_user_location.text = Utils.getLocationFromAddress(userData.userLocation, context)

        //displaying user Image
        if(userData.profileImage != ""){
            val profileImageView = holder.itemView.img_profile
            Picasso.get().load(userData.profileImage).into(profileImageView)
        }else{
            holder.itemView.img_profile.setImageResource(R.drawable.img_user_profile)
        }

        holder.itemView.setOnClickListener {view: View? ->
            val pos = holder.adapterPosition
            val bundle = bundleOf("user" to usersList.get(pos))
            view?.findNavController()?.navigate(R.id.action_navigation_explore_to_user_profile_detail, bundle)
        }
    }

}
class ExploreItemsViewHolder(view:View) : RecyclerView.ViewHolder(view){

}