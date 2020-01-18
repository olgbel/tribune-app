package com.example.tribune_app.adapter

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tribune_app.R
import com.example.tribune_app.dto.PostModel
import com.example.tribune_app.utils.howLongAgo
import com.example.tribune_app.utils.loadImage
import kotlinx.android.synthetic.main.main_info_post.view.*
import kotlinx.android.synthetic.main.reaction_buttons_footer.view.*
import org.jetbrains.anko.toast

class PostViewHolder(val adapter: PostAdapter, view: View) : RecyclerView.ViewHolder(view) {

    init {
        with(itemView) {
            likeBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[currentPosition]
                    if (item.likeActionPerforming) {
                        context.toast(R.string.like_in_progress)
                    } else {
                        adapter.likeBtnClickListener?.onLikeBtnClicked(item, currentPosition)
                    }
                }
            }

            dislikeBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[currentPosition]
                    if (item.dislikeActionPerforming) {
                        context.toast(R.string.dislike_in_progress)
                    } else {
                        adapter.dislikeBtnClickListener?.onDislikeBtnClicked(item, currentPosition)
                    }
                }
            }

            viewsBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[currentPosition]

                    adapter.viewsBtnClickListener?.onViewsBtnClicked(item)
                }
            }

            avatarImg.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION){
                    val item = adapter.list[currentPosition]

                    adapter.avatarClickListener?.onAvatarClicked(item, currentPosition)
                }
            }
        }
    }

    fun bind(post: PostModel) {
        with(itemView) {
            authorTv.text = post.author.username
            badgeTv.text = post.author.badge
            createdTv.text = howLongAgo(post.dtCreation)
            contentTv.text = post.content
            likesTv.text = post.likes.size.toString()
            dislikesTv.text = post.dislikes.size.toString()

            if (post.author.avatar != null) {
                loadImage(avatarImg, post.author.avatar!!.url)
            } else {
                avatarImg.setImageResource(R.drawable.ic_avatar_48dp)
            }

            loadImage(photoImg, post.attachment.url)

            if (!post.linkURL.isNullOrEmpty()) {
                openLinkBtn.visibility = View.VISIBLE

                openLinkBtn.setOnClickListener {
                    val address = Uri.parse(post.linkURL)
                    val openLinkIntent = Intent(Intent.ACTION_VIEW, address)

                    startActivity(context, openLinkIntent, null)
                }
            } else {
                openLinkBtn.visibility = View.INVISIBLE
            }



            when {
                post.likeActionPerforming -> {
                    likeBtn.setImageResource(R.drawable.ic_thumb_up_pending_24dp)
                }
                post.isLikedByMe -> {
                    likeBtn.setImageResource(R.drawable.ic_thumb_up_active_24dp)
                    likesTv.setTextColor(ContextCompat.getColor(context, R.color.colorGreen))
                }
                else -> {
                    likeBtn.setImageResource(R.drawable.ic_thumb_up_inactive_24dp)
                    likesTv.setTextColor(ContextCompat.getColor(context, R.color.colorGrey))
                }
            }

            when {
                post.dislikeActionPerforming -> {
                    dislikeBtn.setImageResource(R.drawable.ic_thumb_down_pending_24dp)
                }
                post.isDislikedByMe -> {
                    dislikeBtn.setImageResource(R.drawable.ic_thumb_down_active_24dp)
                    dislikesTv.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                }
                else -> {
                    dislikeBtn.setImageResource(R.drawable.ic_thumb_down_inactive_24dp)
                    dislikesTv.setTextColor(ContextCompat.getColor(context, R.color.colorGrey))
                }
            }
        }
    }


}