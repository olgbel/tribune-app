package com.example.tribune_app.adapter

import android.content.Intent
import android.net.Uri
import android.transition.Visibility
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tribune_app.R
import com.example.tribune_app.dto.PostModel
import com.example.tribune_app.utils.loadImage
import kotlinx.android.synthetic.main.item_post.view.*
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

            openLinkBtn.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val item = adapter.list[currentPosition]
                    if (item.openLinkBtnClickListener) {
                        context.toast(R.string.open_link_in_progress)
                    } else {
                        adapter.openLinkBtnClickListener?.onOpenLinkBtnClicked(
                            item,
                            currentPosition
                        )
                    }
                }
            }
        }
    }

    fun bind(post: PostModel) {
        with(itemView) {

            authorTv.text = post.author.username
            badgeTv.text = post.badge
            createdTv.text = post.dtCreation.toString()
            contentTv.text = post.content
            likesTv.text = post.likes.size.toString()
            dislikesTv.text = post.dislikes.size.toString()

            if (post.author.avatar != null){
                loadImage(avatarImg, post.author.avatar!!.url)
            }
            else {
                TODO()
            }

            if (post.attachment != null) {
                loadImage(photoImg, post.attachment.url)
            }

            if (post.linkURL != null) {
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
                post.likes.contains(post.author.id) -> {
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
                post.dislikes.contains(post.author.id) -> {
                    dislikeBtn.setImageResource(R.drawable.ic_thumb_down_active_24dp)
                    dislikesTv.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
                }
                else -> {
                    dislikeBtn.setImageResource(R.drawable.ic_thumb_down_inactive_24dp)
                    dislikesTv.setTextColor(ContextCompat.getColor(context, R.color.colorGrey))
                }
            }

            viewsBtn.setOnClickListener {
                TODO()
            }

        }
    }
}