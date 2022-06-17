package com.example.Auth

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.Auth.databinding.KnopkaItemBinding

class KnopkaFeedAdapter(var clickListener: OnKnopkaClickListener) :
    RecyclerView.Adapter<KnopkaFeedAdapter.KnopkaHolder>() {
    var knopkaList = ArrayList<Knopka>()

    class KnopkaHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = KnopkaItemBinding.bind(item)
        fun bind(knopka: Knopka, action: OnKnopkaClickListener) = with(binding) { // simplify binding. access
            button.text = knopka.name
            clicksOnButton.text = (knopka.pushes.toString())
            button.setOnLongClickListener {
                action.onItemLongClick(knopka, adapterPosition)
                return@setOnLongClickListener true
            }
            button.setOnClickListener {
                action.onItemClick(knopka, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KnopkaHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.knopka_item, parent, false)
        return KnopkaHolder(view)
    }

    override fun onBindViewHolder(holder: KnopkaHolder, position: Int) {
        holder.bind(knopkaList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return knopkaList.size
    }

    fun addKnopka(knopka: Knopka): KnopkaFeedAdapter {
        knopkaList.add(knopka)
        notifyDataSetChanged()
        return this
    }
}

interface OnKnopkaClickListener {
    fun onItemLongClick(item: Knopka, position: Int)
    fun onItemClick(item: Knopka, position: Int)
}