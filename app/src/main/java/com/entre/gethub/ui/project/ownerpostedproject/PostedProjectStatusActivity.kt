package com.entre.gethub.ui.project.ownerpostedproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.projects.PostedProjectResponse
import com.entre.gethub.databinding.ActivityPostedProjectStatusBinding
import com.entre.gethub.ui.MainActivity
import com.entre.gethub.ui.adapter.PostedProjectAdapter
import com.entre.gethub.ui.project.DigitalContractActivity
import com.entre.gethub.ui.project.chat.ChatActivity
import com.entre.gethub.ui.project.ownerpostedproject.review.FreelancerReviewActivity
import com.entre.gethub.ui.userpublicprofile.UserPublicProfileActivity
import com.entre.gethub.utils.ViewModelFactory

class PostedProjectStatusActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPostedProjectStatusBinding.inflate(layoutInflater) }
    private val postedProjectStatusViewModel by viewModels<PostedProjectStatusViewModel> {
        ViewModelFactory.getInstance(
            this
        )
    }
    private var postProjectId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getPostedProject()

        postProjectId = intent.getIntExtra(EXTRA_ID_FROM_POST_PROJECT_ACTIVITY, 0)

        binding.iconBack.setOnClickListener {
            if (postProjectId!!.equals(100)) {
                startActivity(Intent(this@PostedProjectStatusActivity, MainActivity::class.java))
                finish()
                return@setOnClickListener
            }

            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        getPostedProject()
    }

    private fun setupRecyclerView(postedProjectList: List<PostedProjectResponse.ProjectsItem>) {
        binding.rvPostedProject.apply {
            layoutManager = LinearLayoutManager(
                this@PostedProjectStatusActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = PostedProjectAdapter(
                this@PostedProjectStatusActivity,
                postedProjectList,
                listener = { project, _ ->
                    val intent = Intent(
                        this@PostedProjectStatusActivity,
                        PostedProjectStatusDetailActivity::class.java
                    );
                    intent.putExtra(PostedProjectStatusDetailActivity.EXTRA_PROJECT_ID, project.id)
                    startActivity(intent)
                },
                chatButtonListener = { project ->
                    val intent =
                        Intent(this@PostedProjectStatusActivity, ChatActivity::class.java).apply {
                            putExtra(ChatActivity.EXTRA_RECEIVER_ID, project.selectedUserBid.userId)
                            putExtra(ChatActivity.EXTRA_SENDER_ID, project.ownerId)
                            putExtra(ChatActivity.EXTRA_CHANNEL_ID, project.chatroomId)
                            putExtra(
                                ChatActivity.EXTRA_RECEIVER_NAME,
                                project.selectedUserBid.usersBid?.fullName
                            )
                            putExtra(
                                ChatActivity.EXTRA_RECEIVER_PHOTO,
                                project.selectedUserBid.usersBid?.photo
                            )
                        }
                    startActivity(intent)
                },
                reviewButtonListener = { project ->
                    val intent = Intent(
                        this@PostedProjectStatusActivity,
                        FreelancerReviewActivity::class.java
                    ).apply {
                        putExtra(FreelancerReviewActivity.EXTRA_PROJECT_ID, project.id)
                        putExtra(
                            FreelancerReviewActivity.EXTRA_FREELANCER_ID,
                            project.selectedUserBid.userId
                        )
                        putExtra(
                            FreelancerReviewActivity.EXTRA_FREELANCER_NAME,
                            project.selectedUserBid.usersBid?.fullName
                        )
                        putExtra(
                            FreelancerReviewActivity.EXTRA_FREELANCER_PROFESSION,
                            project.selectedUserBid.usersBid?.profession
                        )
                        putExtra(
                            FreelancerReviewActivity.EXTRA_FREELANCER_PHOTO,
                            project.selectedUserBid.usersBid?.photo
                        )
                    }
                    startActivity(intent)
                },
                usernameTextListener = { project ->
                    navigateToUserProfile(project.selectedUserBid.usersBid?.username.toString())
                },
                seeContractListener = { project ->
                    val url = getString(R.string.digital_contract_url)
                    val digitalContractUrl =
                        url.replace("{endpoint}", project.chatroomId.toString())
                    val intent = Intent(
                        this@PostedProjectStatusActivity,
                        DigitalContractActivity::class.java
                    ).apply {
                        putExtra(DigitalContractActivity.EXTRA_REDIRECT_URL, digitalContractUrl)
                        putExtra(DigitalContractActivity.EXTRA_CONTRACT_ID, project.chatroomId)
                    }
                    startActivity(intent)
                }
            )
        }
    }

    private fun getPostedProject() {
        postedProjectStatusViewModel.getPostedProjects().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        setupRecyclerView(result.data.data?.projects ?: emptyList())
                        binding.tvTotalPostedProject.text =
                            result.data.data?.projects?.size.toString()
                    }

                    is Result.Error -> {
                        showLoading(false)
//                        showToast(result.error)
                    }

                    is Result.Empty -> {
                        binding.tvTotalPostedProject.text = "0"
                        showEmptyError(true, result.emptyError)
                    }
                }
            }
        }
    }

    private fun navigateToUserProfile(username: String) {
        val intent = Intent(this, UserPublicProfileActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showEmptyError(isEmpty: Boolean, message: String) {
        binding.empty.llEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.empty.tvEmpty.text = message
    }

    companion object {
        const val EXTRA_ID_FROM_POST_PROJECT_ACTIVITY =
            "extra_id_from_post_project_activity"
    }
}