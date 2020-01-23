package com.example.tribune_app

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tribune_app.adapter.ReactionAdapter
import com.example.tribune_app.dto.ReactionModel
import com.example.tribune_app.utils.postId
import com.example.tribune_app.utils.userId
import kotlinx.android.synthetic.main.activity_voted.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast
import java.io.IOException

class VotedPostActivity : AppCompatActivity(), CoroutineScope by MainScope(),
    ReactionAdapter.OnVoitedItemClickListener {

    private var dialog: ProgressDialog? = null
    private var adapter: ReactionAdapter = ReactionAdapter(mutableListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voted)

        swipeContainerVoited.setOnRefreshListener {
            refreshData()
        }
    }

    override fun onStart() {
        super.onStart()
        launch {
            dialog = ProgressDialog(this@VotedPostActivity).apply {
                setMessage(this@VotedPostActivity.getString(R.string.please_wait))
                setTitle(R.string.dowloading_voted_list)
                setCancelable(false)
                setProgressBarIndeterminate(true)
                show()
            }
            val postId = intent.postId
            val result = Repository.getReactionsById(postId)
            dialog?.dismiss()
            if (result.isSuccessful) {
                with(containerVoted) {
                    layoutManager = LinearLayoutManager(this@VotedPostActivity)
                    adapter = ReactionAdapter(
                        requireNotNull(result.body()).toMutableList()
                    ).apply {
                        voitedItemClickListener = this@VotedPostActivity
                    }
                }
            } else {
                toast(R.string.error_occured)
            }
        }

    }

    override fun onVoitedItemClicked(item: ReactionModel, position: Int) {
        val intent = Intent(this@VotedPostActivity, FeedActivity::class.java)
        intent.userId = item.user.id
        startActivity(intent)
    }

    private fun refreshData() {
        launch {
            try {
                val postId = intent.postId
                val response = Repository.getReactionsById(postId)

                swipeContainerVoited.isRefreshing = false
                if (response.isSuccessful) {
                    val newItems = response.body() ?: mutableListOf()
                    adapter.items.clear()
                    adapter.items.addAll(0, newItems)
                    adapter.notifyDataSetChanged()
                }
            }
            catch (e: IOException) {
                toast(R.string.error_occured)
            }
        }
    }

}