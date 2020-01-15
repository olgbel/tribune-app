package com.example.tribune_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tribune_app.R
import com.example.tribune_app.dto.ReactionModel

class ReactionAdapter (
    var items: MutableList<ReactionModel>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ReactionViewHolder(this,  LayoutInflater.from(parent.context).inflate(R.layout.voted_item, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ReactionViewHolder).bind(items[position])
    }
}