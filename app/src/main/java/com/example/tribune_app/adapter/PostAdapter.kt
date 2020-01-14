package com.example.tribune_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tribune_app.R
import com.example.tribune_app.dto.PostModel
import kotlinx.coroutines.CoroutineScope

class PostAdapter (
    private val coroutineScope: CoroutineScope,
    var list: MutableList<PostModel>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PostViewHolder(this,  LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PostViewHolder).bind(list[position])
    }

    var likeBtnClickListener: OnLikeBtnClickListener? = null
    var dislikeBtnClickListener: OnDislikeBtnClickListener? = null
    var viewsBtnClickListener: OnViewsBtnClickListener? = null

    interface OnLikeBtnClickListener {
        fun onLikeBtnClicked(item: PostModel, position: Int)
    }

    interface OnDislikeBtnClickListener {
        fun onDislikeBtnClicked(item: PostModel, position: Int)
    }

    interface OnViewsBtnClickListener {
        fun onViewsBtnClicked(item: PostModel)
    }
}