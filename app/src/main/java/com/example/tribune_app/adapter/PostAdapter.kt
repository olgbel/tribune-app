package com.example.tribune_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tribune_app.R
import com.example.tribune_app.dto.PostModel

class PostAdapter(
    var list: MutableList<PostModel>
) :
    RecyclerView.Adapter<PostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            this,
            LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(list[position])
    }

    var likeBtnClickListener: OnLikeBtnClickListener? = null
    var dislikeBtnClickListener: OnDislikeBtnClickListener? = null
    var viewsBtnClickListener: OnViewsBtnClickListener? = null
    var avatarClickListener: OnAvatarClickListener? = null

    interface OnLikeBtnClickListener {
        fun onLikeBtnClicked(item: PostModel, position: Int)
    }

    interface OnDislikeBtnClickListener {
        fun onDislikeBtnClicked(item: PostModel, position: Int)
    }

    interface OnViewsBtnClickListener {
        fun onViewsBtnClicked(item: PostModel)
    }

    interface OnAvatarClickListener {
        fun onAvatarClicked(item: PostModel, position: Int)
    }
}