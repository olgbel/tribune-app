package com.example.tribune_app.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tribune_app.R
import com.example.tribune_app.dto.ReactionModel
import com.example.tribune_app.dto.ReactionType
import com.example.tribune_app.utils.loadImage
import kotlinx.android.synthetic.main.voted_item.view.*
import kotlinx.android.synthetic.main.voted_item.view.authorTv
import kotlinx.android.synthetic.main.voted_item.view.badgeTv
import kotlinx.android.synthetic.main.voted_item.view.createdTv
import java.text.SimpleDateFormat
import java.util.*

class ReactionViewHolder(val adapter: ReactionAdapter, view: View) : RecyclerView.ViewHolder(view) {

    init {
        itemView.setOnClickListener {
            val currentPosition = adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                val item = adapter.items[currentPosition]

                adapter.voitedItemClickListener?.onVoitedItemClicked(item, currentPosition)
            }
        }
    }
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

            val dateFormatted = SimpleDateFormat(
                "dd MMM",
                Locale.US
            ).format(Date(reaction.date))
            createdTv.text = dateFormatted

            if (reaction.user.avatar != null) {
                loadImage(avatarIv, reaction.user.avatar!!.url)
            } else {
                avatarIv.setImageResource(R.drawable.ic_avatar_48dp)
            }
        }

    }
}