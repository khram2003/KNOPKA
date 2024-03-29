package com.example.Auth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.Auth.databinding.TagItemBinding

class TagAdapter(var clickListener: OnTagClickListener) : RecyclerView.Adapter<TagAdapter.TagHolder>() {
    var tagList = ArrayList<String>()

    class TagHolder(item: View) : RecyclerView.ViewHolder(item) {
        var pos = 0;
        val binding = TagItemBinding.bind(item)
        fun bind(s: String, action: OnTagClickListener) {
            binding.tagName.text = s
            binding.tagName.setOnLongClickListener {
                action.onItemLongClick(s, adapterPosition)
                return@setOnLongClickListener true
            }
            binding.tagName.setOnClickListener {
                action.onItemLongClick(s, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tag_item, parent, false)
        return TagHolder(view)
    }

    override fun onBindViewHolder(holder: TagHolder, position: Int) {
        holder.bind(tagList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return tagList.size
    }

    fun addTag(tag: String) {
        tagList.add(tag)
        notifyDataSetChanged()
    }
}

interface OnTagClickListener {
    fun onItemLongClick(item: String, position: Int)
    fun onItemClick(item: String, position: Int)
}