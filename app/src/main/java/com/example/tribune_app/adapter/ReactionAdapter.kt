package com.example.tribune_app.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tribune_app.dto.PostModel
import com.example.tribune_app.dto.ReactionModel
import kotlinx.coroutines.CoroutineScope

class ReactionAdapter (
    private val coroutineScope: CoroutineScope,
    var list: MutableList<ReactionModel>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}