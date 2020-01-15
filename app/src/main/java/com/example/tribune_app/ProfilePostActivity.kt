package com.example.tribune_app

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.example.tribune_app.dto.AttachmentModel
import com.example.tribune_app.utils.REQUEST_IMAGE_CAPTURE
import com.example.tribune_app.utils.loadImage
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast

class ProfilePostActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private var attachmentModel: AttachmentModel? = null
    private var userId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        avatarImg.setOnClickListener {
            dispatchTakePictureIntent()
        }

        updateProfileBtn.setOnClickListener {
            launch {
                if (attachmentModel != null) {
                    val result = Repository.updateUser(userId, attachmentModel!!)

                    if (result.isSuccessful) {
                        handleSuccessfullResult()
                    } else {
                        handleFailedResult()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        launch {
            val response = Repository.getCurrentUser()
            if (response.isSuccessful) {
                val userModel = requireNotNull(response.body())
                usernameTv.text = userModel.username
                badgeTv.text = userModel.badge
                userId = userModel.id

                if (userModel.avatar != null){
                    loadImage(avatarImg, userModel.avatar!!.url)
                }
                else {
                    avatarImg.setImageResource(R.drawable.ic_avatar_48dp)
                }

            } else {
                toast(R.string.error_occured)
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int, data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap?
            imageBitmap?.let {
                launch {
                    val imageUploadResult = Repository.upload(it)
                    if (imageUploadResult.isSuccessful) {
                        attachmentModel = imageUploadResult.body()
                    } else {
                        toast("Can't upload image")
                    }
                }
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun handleSuccessfullResult() {
        toast(R.string.user_info_updated)
        finish()
    }

    private fun handleFailedResult() {
        toast(R.string.error_occured)
    }
}