package com.example.tribune_app

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.tribune_app.dto.AttachmentModel
import kotlinx.android.synthetic.main.activity_create_post.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.toast
import java.io.IOException

class CreatePostActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private var dialog: ProgressDialog? = null
    val REQUEST_IMAGE_CAPTURE = 1
    private var attachmentModel: AttachmentModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        attachImg.setOnClickListener {
            dispatchTakePictureIntent()
        }

        createPostBtn.setOnClickListener {
            launch {
                dialog = createProgressDialog()
                try {
                    if (attachmentModel == null) {
                        toast(R.string.empty_attachment)
                    } else if (contentEdt.text == null) {
                        toast(R.string.empty_content)
                    } else {
                        val result = Repository.createPost(
                            contentEdt.text.toString(),
                            linkURLEdt.text.toString(),
                            attachmentModel!!
                        )
                        if (result.isSuccessful) {
                            handleSuccessfullResult()
                        } else {
                            handleFailedResult()
                        }
                    }
                } catch (e: IOException) {
                    handleFailedResult()
                } finally {
                    dialog?.dismiss()
                }
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
                    dialog = createProgressDialog()
                    val imageUploadResult = Repository.upload(it)
                    dialog?.dismiss()
                    if (imageUploadResult.isSuccessful) {
                        imageUploaded()
                        attachmentModel = imageUploadResult.body()
                    } else {
                        toast("Can't upload image")
                    }
                }
            }
        }
    }

    private fun imageUploaded() {
        transparetAllIcons()
        attachPhotoDoneImg.visibility = View.VISIBLE
    }

    private fun transparetAllIcons() {
        attachImg.setImageResource(R.drawable.ic_add_a_photo_inactive)
    }

    private fun createProgressDialog(): ProgressDialog? {
        return ProgressDialog(this@CreatePostActivity).apply {
            setMessage(this@CreatePostActivity.getString(R.string.please_wait))
            setTitle(R.string.create_new_post)
            setCancelable(false)
            setProgressBarIndeterminate(true)
            show()
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
        toast(R.string.post_created_successfully)
        finish()
    }

    private fun handleFailedResult() {
        toast(R.string.error_occured)
    }
}