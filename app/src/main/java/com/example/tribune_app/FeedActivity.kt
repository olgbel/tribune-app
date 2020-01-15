package com.example.tribune_app

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tribune_app.adapter.PostAdapter
import com.example.tribune_app.dto.PostModel
import com.example.tribune_app.dto.ReactionModel
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast

class FeedActivity : AppCompatActivity(), CoroutineScope by MainScope(),
    PostAdapter.OnLikeBtnClickListener, PostAdapter.OnDislikeBtnClickListener,
    PostAdapter.OnViewsBtnClickListener, PostAdapter.OnAvatarClickListener {

    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        addPostBtn.setOnClickListener {
            startActivity(Intent(this, CreatePostActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        launch {
            dialog = ProgressDialog(this@FeedActivity).apply {
                setMessage(this@FeedActivity.getString(R.string.please_wait))
                setTitle(R.string.downloading_posts)
                setCancelable(false)
                setProgressBarIndeterminate(true)
                show()
            }
            val result = Repository.getRecentPosts()

            dialog?.dismiss()
            if (result.isSuccessful) {
                with(containerFeed) {
                    layoutManager = LinearLayoutManager(this@FeedActivity)
                    adapter = PostAdapter(
                        this@FeedActivity,
                        requireNotNull(result.body()).toMutableList()
                    ).apply {
                        likeBtnClickListener = this@FeedActivity
                        dislikeBtnClickListener = this@FeedActivity
                        viewsBtnClickListener = this@FeedActivity
                        avatarClickListener = this@FeedActivity
                    }
                }
            } else {
                toast(R.string.error_occured)
            }
        }
    }

    override fun onLikeBtnClicked(item: PostModel, position: Int) {
        launch {
            item.likeActionPerforming = true
            with(containerFeed) {
                val userResponse = Repository.getCurrentUser()
                if (userResponse.isSuccessful) {
                    if (isReactedByMe(
                            requireNotNull(userResponse.body()?.id),
                            item.likes.plus(item.dislikes)
                        )
                    ) {
                        toast(R.string.already_voted)
                    } else {
                        adapter?.notifyItemChanged(position)

                        val response = Repository.likedByMe(item.id)
                        item.likeActionPerforming = false
                        if (response.isSuccessful) {
                            item.updateLikes(requireNotNull(response.body()))
                        }
                        adapter?.notifyItemChanged(position)
                    }
                } else {
                    toast("Error with current user request")
                }
            }
        }
    }

    override fun onDislikeBtnClicked(item: PostModel, position: Int) {
        launch {
            item.dislikeActionPerforming = true
            with(containerFeed) {
                val userResponse = Repository.getCurrentUser()
                if (userResponse.isSuccessful) {
                    if (isReactedByMe(
                            requireNotNull(userResponse.body()?.id),
                            item.likes.plus(item.dislikes)
                        )
                    ) {
                        toast(R.string.already_voted)
                    } else {
                        adapter?.notifyItemChanged(position)

                        val response = Repository.dislikedByMe(item.id)
                        item.dislikeActionPerforming = false
                        if (response.isSuccessful) {
                            item.updateDislikes(requireNotNull(response.body()))
                        }
                        adapter?.notifyItemChanged(position)
                    }
                } else {
                    toast("Error with current user request")
                }
            }
        }
    }

    override fun onViewsBtnClicked(item: PostModel) {
        val intent = Intent(this, VotedPostActivity::class.java)
        intent.putExtra("postId", item.id)

        startActivity(intent)
    }

    override fun onAvatarClicked(item: PostModel, position: Int) {
        launch {
            with(containerFeed) {
                val response = Repository.getPostsByUserId(item.author.id)
                if (response.isSuccessful) {
                    layoutManager = LinearLayoutManager(this@FeedActivity)
                    adapter = PostAdapter(
                        this@FeedActivity,
                        requireNotNull(response.body()).toMutableList()
                    ).apply {
                        likeBtnClickListener = this@FeedActivity
                        dislikeBtnClickListener = this@FeedActivity
                        viewsBtnClickListener = this@FeedActivity
                        avatarClickListener = this@FeedActivity
                    }
                }
            }
        }
    }

    private fun isReactedByMe(userId: Long, reactionSet: Set<ReactionModel>) =
        reactionSet.any { it.user.id == userId }
}