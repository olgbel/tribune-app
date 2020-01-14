package com.example.tribune_app

import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tribune_app.adapter.PostAdapter
import com.example.tribune_app.adapter.ReactionAdapter
import kotlinx.android.synthetic.main.activity_feed.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast

class VotedPostActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voted)
    }

    override fun onStart() {
        super.onStart()
        launch {
            dialog = ProgressDialog(this@VotedPostActivity).apply {
                setMessage(this@VotedPostActivity.getString(R.string.please_wait))
                setTitle(R.string.downloading_posts)
                setCancelable(false)
                setProgressBarIndeterminate(true)
                show()
            }
            val postId = intent.getLongExtra("postId", 0L)
            val result = Repository.getReactionsById(postId)

            dialog?.dismiss()
            if (result.isSuccessful) {
                with(container) {
                    layoutManager = LinearLayoutManager(this@VotedPostActivity)
                    adapter = ReactionAdapter(
                        this@VotedPostActivity,
                        requireNotNull(result.body()).toMutableList()
                    )
                }
            } else {
                toast(R.string.error_occured)
            }
        }

    }
}