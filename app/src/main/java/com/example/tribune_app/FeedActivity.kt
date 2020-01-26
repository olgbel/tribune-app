package com.example.tribune_app

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tribune_app.adapter.PostAdapter
import com.example.tribune_app.dto.PostModel
import com.example.tribune_app.dto.ReactionModel
import com.example.tribune_app.utils.postId
import com.example.tribune_app.utils.userId
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import retrofit2.Response
import java.io.IOException

class FeedActivity : AppCompatActivity(), CoroutineScope by MainScope(),
    PostAdapter.OnLikeBtnClickListener, PostAdapter.OnDislikeBtnClickListener,
    PostAdapter.OnViewsBtnClickListener, PostAdapter.OnAvatarClickListener {

    private var dialog: ProgressDialog? = null
    private var adapter: PostAdapter = PostAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        addPostBtn.setOnClickListener {
            startActivity(Intent(this, CreatePostActivity::class.java))
        }

        avatarEditBtn.setOnClickListener {
            startActivity(Intent(this, ProfilePostActivity::class.java))
        }

        swipeContainerFeed.setOnRefreshListener {
            refreshData()
        }

        loadMoreBtn.setOnClickListener {
            loadMore()
        }

        errorButton.setOnClickListener {
            swipeContainerFeed.isRefreshing = true
            refreshData()
        }

        requestToken()
    }

    private fun requestToken() {
        with(GoogleApiAvailability.getInstance()) {
            val code = isGooglePlayServicesAvailable(this@FeedActivity)
            if (code == ConnectionResult.SUCCESS) {
                return@with
            }
            if (isUserResolvableError(code)) {
                getErrorDialog(this@FeedActivity, code, 9000).show()
                return
            }
            longToast(getString(R.string.google_play_unavailable))
            return
        }

        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            launch {
                println(it.token)
                try {
                    Repository.registerPushToken(it.token)
                } catch (e: IOException) {
                    toast(R.string.push_token_error)
                }
            }
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
            try {
                val currentUser = Repository.getCurrentUser()
                if (currentUser.isSuccessful && currentUser.body()?.isReadOnly!!) {
                    addPostBtn.hide()
                } else {
                    addPostBtn.show()
                }

                val result: Response<List<PostModel>>
                val userId = intent.userId

                result = if (userId != 0L) {
                    Repository.getPostsByUserId(userId)
                } else {
                    Repository.getRecentPosts()
                }
                dialog?.dismiss()
                if (result.isSuccessful) {
                    val posts = result.body()?.toMutableList() ?: mutableListOf()
                    if (posts.isEmpty()) {
                        showErrorState(R.string.empty)
                        return@launch
                    }
                    with(containerFeed) {
                        layoutManager = LinearLayoutManager(this@FeedActivity)
                        adapter = PostAdapter(
                            posts
                        ).apply {
                            likeBtnClickListener = this@FeedActivity
                            dislikeBtnClickListener = this@FeedActivity
                            viewsBtnClickListener = this@FeedActivity
                            avatarClickListener = this@FeedActivity
                        }
                    }

                    //updateList(posts)
                } else {
                    showErrorState()
                    toast(R.string.error_occured)
                }
            } catch (e: IOException) {
                showErrorState()
                toast(R.string.dowloading_post_error)
            }
        }
    }

    override fun onLikeBtnClicked(item: PostModel, position: Int) {
        launch {
            try {
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

                            if (item.author.isReadOnly) {
                                addPostBtn.hide()
                            } else {
                                addPostBtn.show()
                            }
                        }
                    } else {
                        toast(R.string.current_user_error)
                    }
                }
            } catch (e: IOException) {
                toast(R.string.like_process_error)
            }
        }
    }

    override fun onDislikeBtnClicked(item: PostModel, position: Int) {
        launch {
            try {
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
                            if (item.author.isReadOnly) {
                                addPostBtn.hide()
                            } else {
                                addPostBtn.show()
                            }
                        }
                    } else {
                        toast(R.string.current_user_error)
                    }
                }
            } catch (e: IOException) {
                toast(R.string.like_process_error)
            }
        }
    }

    override fun onViewsBtnClicked(item: PostModel) {
        val intent = Intent(this, VotedPostActivity::class.java)
        intent.postId = item.id

        startActivity(intent)
    }

    override fun onAvatarClicked(item: PostModel, position: Int) {
        val intent = Intent(this@FeedActivity, FeedActivity::class.java)
        intent.userId = item.author.id
        startActivity(intent)
    }

    private fun isReactedByMe(userId: Long, reactionSet: Set<ReactionModel>) =
        reactionSet.any { it.user.id == userId }

    private fun refreshData() {
        launch {
            try {
                val response = Repository.getRecentPosts()
                swipeContainerFeed.isRefreshing = false
                if (response.isSuccessful) {
                    updateList(response.body() ?: mutableListOf())
                }
            } catch (e: IOException) {
                swipeContainerFeed.isRefreshing = false
                toast(R.string.error_occured)
            }
        }
    }

    private fun updateList(newItems: List<PostModel>) {
        adapter.list.clear()
        adapter.list.addAll(newItems)
        adapter.notifyDataSetChanged()
        if (newItems.isNotEmpty()) {
            hideErrorState()
        }
    }

    private fun loadMore() {
        launch {
            try {
                val response = Repository.getPostsAfter(adapter.list.size.toLong())
                swipeContainerFeed.isRefreshing = false
                if (response.isSuccessful) {
                    val newItems = response.body() ?: mutableListOf()
                    adapter.list.addAll(adapter.list.size, newItems)
                    adapter.notifyDataSetChanged()
                }
            } catch (e: IOException) {
                toast(R.string.load_more_error)
            }
        }
    }

    private fun showErrorState(text: Int = R.string.error_occured) {
        dialog?.dismiss()
        errorGroup.visibility = View.VISIBLE
        errorTitle.text = getString(text)
    }

    private fun hideErrorState() {
        errorGroup.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}