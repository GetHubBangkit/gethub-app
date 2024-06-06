package com.entre.gethub.ui.project.ownerpostedproject.review

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.databinding.ActivityFreelancerReviewBinding
import com.entre.gethub.utils.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FreelancerReviewActivity : AppCompatActivity() {

    private val binding by lazy { ActivityFreelancerReviewBinding.inflate(layoutInflater) }
    private val freelancerReviewViewModel by viewModels<FreelancerReviewViewModel> {
        ViewModelFactory.getInstance(
            this
        )
    }
    private var projectId: String = ""
    private var freelancerId: String = ""
    private var freelancerName: String = ""
    private var freelancerProfession: String = ""
    private var freelancerPhoto: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        intent.apply {
            projectId = getStringExtra(EXTRA_PROJECT_ID).toString()
            freelancerId = getStringExtra(EXTRA_FREELANCER_ID).toString()
            freelancerName = getStringExtra(EXTRA_FREELANCER_NAME).toString()
            freelancerProfession = getStringExtra(EXTRA_FREELANCER_PROFESSION).toString()
            freelancerPhoto = getStringExtra(EXTRA_FREELANCER_PHOTO).toString()
        }

        setupFreelancerData(
            name = freelancerName,
            profession = freelancerProfession,
            photo = freelancerPhoto
        )

        reviewFreelancer()

        binding.iconBack.setOnClickListener {
            finish()
        }
    }

    private fun setupFreelancerData(name: String, profession: String, photo: String) {
        with(binding) {
            tvUserName.text = name
            tvUserJobName.text = profession
            Glide.with(this@FreelancerReviewActivity)
                .load(photo)
                .placeholder(R.drawable.profilepic1)
                .into(ivUserProfile)
        }
    }

    private fun reviewFreelancer() {
        val message = binding.messageTextField.editText?.text.toString()

        if (message.isEmpty()) {
            showToast("Review tidak boleh kosong")
            return
        }
        showDialog(
            this@FreelancerReviewActivity,
            "Periksa review Anda",
            "Apakah Anda yakin untuk memberikan review terhadap Freelancer?",
            projectId,
            freelancerId,
            message
        )
    }

    private fun showDialog(
        context: Context,
        title: String,
        message: String,
        projectId: String,
        freelancerId: String,
        review: String
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Yakin") { _, _ ->
                freelancerReviewViewModel.reviewFreelancer(
                    projectId = projectId,
                    targetUserId = freelancerId,
                    message = review,
                    reviewType = "freelancer"
                ).observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> showLoading(true)
                            is Result.Success -> {
                                showLoading(false)
                                showToast(result.data.message.toString())
                                finish()
                            }

                            is Result.Error -> {
                                showLoading(false)
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_PROJECT_ID = "extra_project_id"
        const val EXTRA_FREELANCER_ID = "extra_freelancer_id"
        const val EXTRA_FREELANCER_NAME = "extra_freelancer_name"
        const val EXTRA_FREELANCER_PROFESSION = "extra_freelancer_profession"
        const val EXTRA_FREELANCER_PHOTO = "extra_freelancer_photo"
    }
}