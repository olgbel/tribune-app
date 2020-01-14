package com.example.tribune_app

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tribune_app.adapter.PostAdapter
import com.example.tribune_app.dto.PostModel
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast

class FeedActivity : AppCompatActivity(), CoroutineScope by MainScope(),
    PostAdapter.OnLikeBtnClickListener, PostAdapter.OnDislikeBtnClickListener,
    PostAdapter.OnViewsBtnClickListener {

    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
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
                with(container) {
                    layoutManager = LinearLayoutManager(this@FeedActivity)
                    adapter = PostAdapter(
                        this@FeedActivity,
                        requireNotNull(result.body()).toMutableList()
                    ).apply {
                        likeBtnClickListener = this@FeedActivity
                        dislikeBtnClickListener = this@FeedActivity
                        viewsBtnClickListener = this@FeedActivity
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
            with(container) {
                adapter?.notifyItemChanged(position)

                val response = Repository.likedByMe(item.id)
                item.likeActionPerforming = false
                if (response.isSuccessful) {
                    item.updateLikes(requireNotNull(response.body()))
                }
                adapter?.notifyItemChanged(position)
            }
        }
    }

    override fun onDislikeBtnClicked(item: PostModel, position: Int) {
        launch {
            item.dislikeActionPerforming = true
            with(container) {
                adapter?.notifyItemChanged(position)

                val response = Repository.dislikedByMe(item.id)
                item.dislikeActionPerforming = false
                if (response.isSuccessful) {
                    item.updateDislikes(requireNotNull(response.body()))
                }
                adapter?.notifyItemChanged(position)
            }
        }
    }

    override fun onViewsBtnClicked(item: PostModel, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}