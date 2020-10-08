package com.example.otherhalf.ui.requests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.otherhalf.R
import com.example.otherhalf.activities.MainActivity
import com.example.otherhalf.ui.requests.adapters.NewLikedProfilesAdapter
import com.example.otherhalf.ui.requests.adapters.NewMessageRequestAdapter
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_requests.*

class RequestsFragment : Fragment() {

    private lateinit var notificationsViewModel: RequestsViewModel
    var context : MainActivity? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(RequestsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_requests, container, false)
//        val textView: TextView = root.findViewById(R.id.text_notifications)
//        notificationsViewModel.text.observe(this, Observer {
//            textView.text = it
//        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context = activity as MainActivity
        setUpNewMessageList()
        setUpLikedUsersList()

    }

    override fun onResume() {
        super.onResume()
        context?.showBottomNavigation()
    }

    fun setUpNewMessageList(){

        var adapter = GroupAdapter<ViewHolder>()


        adapter.add(NewMessageRequestAdapter())
        adapter.add(NewMessageRequestAdapter())
        adapter.add(NewMessageRequestAdapter())

        var gridLayoutManager = GridLayoutManager(activity,1)
        gridLayoutManager.orientation = GridLayoutManager.HORIZONTAL
        message_requests_list.layoutManager = gridLayoutManager
        message_requests_list.adapter = adapter


        adapter.setOnItemClickListener { item, view ->
            view.findNavController().navigate(R.id.action_navigation_requests_to_chat_log)
        }


    }

    fun setUpLikedUsersList(){
        var adapter = GroupAdapter<ViewHolder>()


        adapter.add(NewLikedProfilesAdapter())
        adapter.add(NewLikedProfilesAdapter())
        adapter.add(NewLikedProfilesAdapter())

        var gridLayoutManager = GridLayoutManager(activity,2)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        recyclerview_new_liked_list.layoutManager = gridLayoutManager
        recyclerview_new_liked_list.adapter = adapter


        adapter.setOnItemClickListener { item, view ->
            view.findNavController().navigate(R.id.action_navigation_requests_to_user_profile_detail)
        }
    }


}