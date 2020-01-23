package com.example.tribune_app

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tribune_app.utils.API_SHARED_FILE
import com.example.tribune_app.utils.AUTHENTICATED_SHARED_KEY
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private var dialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (isAuthenticated()) {
            val token = getSharedPreferences(
                API_SHARED_FILE,
                Context.MODE_PRIVATE
            )
                .getString(AUTHENTICATED_SHARED_KEY, "")
            Repository.createRetrofitWithAuth(requireNotNull(token))

            val feedActivityIntent = Intent(this@MainActivity, FeedActivity::class.java)
            startActivity(feedActivityIntent)
            finish()
        } else {
            btn_login.setOnClickListener {
                launch {
                    dialog =
                        indeterminateProgressDialog(
                            message = R.string.please_wait,
                            title = R.string.authentication
                        ) {
                            setCancelable(false)
                        }
                    val response =
                        Repository.authenticate(
                            edt_login.text.toString(),
                            edt_password.text.toString()
                        )
                    dialog?.dismiss()
                    if (response.isSuccessful) {
                        toast(R.string.success)
                        setUserAuth(requireNotNull(response.body()).token)
                        Repository.createRetrofitWithAuth(requireNotNull(response.body()).token)
                        val feedActivityIntent =
                            Intent(this@MainActivity, FeedActivity::class.java)
                        startActivity(feedActivityIntent)
                        finish()
                    } else {
                        toast(R.string.authentication_failed)
                    }
                }
            }
        }

        btn_registration.setOnClickListener {
            val registrationIntent = Intent(this@MainActivity, RegistrationActivity::class.java)
            startActivity(registrationIntent)
        }

    }

    override fun onStart() {
        super.onStart()
        if (isAuthenticated()) {
            startActivity<FeedActivity>()
            finish()
        }
    }

    private fun isAuthenticated() =
        getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE).getString(
            AUTHENTICATED_SHARED_KEY, ""
        )?.isNotEmpty() ?: false

    private fun setUserAuth(token: String) =
        getSharedPreferences(API_SHARED_FILE, Context.MODE_PRIVATE)
            .edit()
            .putString(AUTHENTICATED_SHARED_KEY, token)
            .commit()

    @InternalCoroutinesApi
    override fun onStop() {
        super.onStop()
        cancel()
        dialog?.dismiss()
    }

}
