package com.example.otherhalf.ui.profile.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.otherhalf.R
import kotlinx.android.synthetic.main.adapter_age_selection_item.view.*

class AgeSelectionAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {

        override fun areItemsTheSame(oldItem:String, newItem:String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem:String, newItem:String): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return AgeSelectionViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_age_selection_item,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AgeSelectionViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<String>) {
        differ.submitList(list)
    }

    class AgeSelectionViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: String) = with(itemView) {
            itemView.age_btn.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }


            itemView.age_btn.text = item
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item:String)
    }
}