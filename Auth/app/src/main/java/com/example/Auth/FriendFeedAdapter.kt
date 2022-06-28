package com.example.Auth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.Auth.databinding.FriendItemBinding

class FriendFeedAdapter(var clickListener: OnFriendClickListener) :
    RecyclerView.Adapter<FriendFeedAdapter.FriendHolder>() {
    var friendList = ArrayList<User>()

    class FriendHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = FriendItemBinding.bind(item)
        fun bind(friend: User, action: OnFriendClickListener) = with(binding) { // simplify binding. access
            nicknameTextView.setText(friend.nickname)
            nicknameTextView.setOnLongClickListener {
                action.onItemLongClick(friend, adapterPosition)
                return@setOnLongClickListener true
            }
            nicknameTextView.setOnClickListener {
                action.onItemClick(friend, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_item, parent, false)
        return FriendHolder(view)
    }

    override fun onBindViewHolder(holder: FriendHolder, position: Int) {
        holder.bind(friendList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return friendList.size
    }

    fun addFriend(friend: User): FriendFeedAdapter {
        friendList.add(friend)
        notifyDataSetChanged()
        return this
    }
}

interface OnFriendClickListener {
    fun onItemLongClick(item: User, position: Int)
    fun onItemClick(item: User, position: Int)
}