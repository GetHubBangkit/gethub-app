package com.entre.gethub.ui.project.freelanceracceptedproject.review

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.databinding.ActivityOwnerReviewBinding
import com.entre.gethub.databinding.DialogLoadingBinding
import com.entre.gethub.utils.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class OwnerReviewActivity : AppCompatActivity() {

    private val binding by lazy { ActivityOwnerReviewBinding.inflate(layoutInflater) }
    private val ownerReviewViewModel by viewModels<OwnerReviewViewModel> {
        ViewModelFactory.getInstance(
            this
        )
    }
    private var projectId: String = ""
    private var ownerId: String = ""
    private var ownerName: String = ""
    private var ownerProfession: String = ""
    private var ownerPhoto: String = ""
    private var loadingDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        intent.apply {
            projectId = getStringExtra(EXTRA_PROJECT_ID).toString()
            ownerId = getStringExtra(EXTRA_OWNER_ID).toString()
            ownerName = getStringExtra(EXTRA_OWNER_NAME).toString()
            ownerProfession = getStringExtra(EXTRA_OWNER_PROFESSION).toString()
            ownerPhoto = getStringExtra(EXTRA_OWNER_PHOTO).toString()
        }

        binding.iconBack.setOnClickListener {
            finish()
        }

        setupView()

        binding.btnSend.setOnClickListener {
            reviewOwner()
        }
    }

    private fun setupView() {
        with(binding) {
            tvUserName.text = ownerName
            tvUserJobName.text = ownerProfession
            Glide.with(this@OwnerReviewActivity)
                .load(ownerPhoto)
                .placeholder(R.drawable.profilepic2)
                .into(ivUserProfile)
        }
    }

    private fun reviewOwner() {
        val message = binding.messageTextField.editText?.text.toString()

        if (message.isEmpty()) {
            showToast("Review tidak boleh kosong")
            return
        }
        showDialog(
            this@OwnerReviewActivity,
            "Periksa review Anda",
            "Apakah Anda yakin untuk memberikan review terhadap Owner?",
            projectId,
            ownerId,
            message
        )
    }

    private fun showDialog(
        context: Context,
        title: String,
        message: String,
        projectId: String,
        ownerId: String,
        review: String
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Yakin") { _, _ ->
                ownerReviewViewModel.reviewOwner(
                    projectId = projectId,
                    targetUserId = ownerId,
                    message = review,
                    reviewType = "owner"
                ).observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> showLoadingDialog("Sedang analisis review")
                            is Result.Success -> {
                                dismissLoadingDialog()
                                showToast(result.data.message.toString())
                                finish()
                            }

                            is Result.Error -> {
                                dismissLoadingDialog()
                                showToast(result.error)
                            }

                            else -> {
                                //
                            }
                        }
                    }
                }
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .setOnDismissListener {
                it.dismiss()
            }
            .show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoadingDialog(message: String) {
        if (loadingDialog == null) {
            val builder = AlertDialog.Builder(this)
            val dialogBinding = DialogLoadingBinding.inflate(layoutInflater)
            builder.setView(dialogBinding.root)
            builder.setCancelable(false)

            dialogBinding.tvLoadingMessage.text = message

            loadingDialog = builder.create()
            loadingDialog?.show()
        } else {
            updateLoadingMessage(message)
        }
    }

    private fun updateLoadingMessage(message: String) {
        loadingDialog?.findViewById<TextView>(R.id.tv_loading_message)?.text = message
    }

    private fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    companion object {
        const val EXTRA_PROJECT_ID = "extra_project_id"
        const val EXTRA_OWNER_ID = "extra_owner_id"
        const val EXTRA_OWNER_NAME = "extra_owner_name"
        const val EXTRA_OWNER_PROFESSION = "extra_owner_profession"
        const val EXTRA_OWNER_PHOTO = "extra_owner_photo"
    }
}