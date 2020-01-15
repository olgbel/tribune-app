package com.example.tribune_app.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tribune_app.R
import com.example.tribune_app.dto.ReactionModel
import com.example.tribune_app.dto.ReactionType
import com.example.tribune_app.utils.howLongAgo
import com.example.tribune_app.utils.loadImage
import kotlinx.android.synthetic.main.voted_item.view.*
import kotlinx.android.synthetic.main.voted_item.view.authorTv
import kotlinx.android.synthetic.main.voted_item.view.badgeTv
import kotlinx.android.synthetic.main.voted_item.view.createdTv

class ReactionViewHolder(val adapter: ReactionAdapter, view: View) : RecyclerView.ViewHolder(view) {

    fun bind(reaction: ReactionModel) {
        with(itemView) {
            when (reaction.type) {
                ReactionType.LIKE -> {
                    reactionIv.setImageResource(R.drawable.ic_thumb_up_active_24dp)
                }
                ReactionType.DISLIKE -> {
                    reactionIv.setImageResource(R.drawable.ic_thumb_down_active_24dp)
                }
            }

            authorTv.text = reaction.user.username
            badgeTv.text = reaction.user.badge
            createdTv.text = howLongAgo(reaction.date.toInt())

            if (reaction.user.avatar != null) {
                loadImage(avatarIv, reaction.user.avatar!!.url)
            } else {
                avatarIv.setImageResource(R.drawable.ic_avatar_48dp)
            }

        }

    }
}