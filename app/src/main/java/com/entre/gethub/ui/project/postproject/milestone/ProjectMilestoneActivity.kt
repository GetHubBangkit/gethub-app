package com.entre.gethub.ui.project.postproject.milestone

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.projects.AllProjectMilestoneResponse
import com.entre.gethub.databinding.ActivityProjectMilestoneBinding
import com.entre.gethub.ui.adapter.ProjectMilestoneAdapter
import com.entre.gethub.ui.project.ownerpostedproject.PostedProjectStatusActivity
import com.entre.gethub.utils.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProjectMilestoneActivity : AppCompatActivity() {

    private val binding by lazy { ActivityProjectMilestoneBinding.inflate(layoutInflater) }
    private val projectMilestoneViewModel by viewModels<ProjectMilestoneViewModel> {
        ViewModelFactory.getInstance(
            this
        )
    }

    private var projectId: String = ""
    private var milestoneListLength: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        projectId = intent.getStringExtra(EXTRA_PROJECT_ID).toString()

        binding.cvAddMilestone.setOnClickListener {
            val intent = Intent(this, ProjectMilestoneFormActivity::class.java).apply {
                putExtra(ProjectMilestoneFormActivity.EXTRA_PROJECT_ID, projectId)
                putExtra(ProjectMilestoneFormActivity.EXTRA_TASK_NUMBER, milestoneListLength + 1)
            }
            startActivity(intent)
        }

        binding.btnSimpan.setOnClickListener {
            if (milestoneListLength == 0) {
                showDialog(
                    this,
                    "Belum menambahkan milestone",
                    "Anda harus menambahkan project milestone untuk melanjutkan pembuatan project"
                )
                return@setOnClickListener
            }

            val intent = Intent(this, PostedProjectStatusActivity::class.java).apply {
                putExtra(PostedProjectStatusActivity.EXTRA_ID_FROM_POST_PROJECT_ACTIVITY, 100)
            }
            startActivity(intent)
            finish()
            showToast("Project berhasil dibuat")
        }
    }

    override fun onResume() {
        super.onResume()
        getProjectMilestone(projectId)
    }

    private fun getProjectMilestone(projectId: String) {
        projectMilestoneViewModel.getMilestone(projectId).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        showEmpty(false, "")
                        binding.rvMilestone.visibility = View.VISIBLE
                        binding.btnSimpan.visibility = View.VISIBLE
                        milestoneListLength = result.data.data.size
                        setupMilestoneRecyclerView(result.data.data.sortedBy { it.taskNumber })
                    }

                    is Result.Error -> {
                        showLoading(false)
                    }

                    is Result.Empty -> {
                        showLoading(false)
                        showEmpty(true, result.emptyError)
                        binding.rvMilestone.visibility = View.GONE
                        binding.btnSimpan.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setupMilestoneRecyclerView(projectMilestoneList: List<AllProjectMilestoneResponse.DataItem>) {
        binding.rvMilestone.apply {
            layoutManager = LinearLayoutManager(
                this@ProjectMilestoneActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = ProjectMilestoneAdapter(
                projectMilestoneList,
                deleteMilestoneListener = { dataItem ->
                    showDialogOnDeleteMilestone(
                        this@ProjectMilestoneActivity,
                        "Hapus milestone",
                        "Apakah Anda yakin ingin menghapus Milestone ${dataItem.taskNumber}?",
                        dataItem
                    )
                })
        }
    }

    private fun showDialogOnDeleteMilestone(
        context: Context,
        title: String,
        message: String,
        dataItem: AllProjectMilestoneResponse.DataItem
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Yakin") { dialog, _ ->
                projectMilestoneViewModel.deleteMilestoneById(
                    projectId = projectId,
                    taskId = dataItem.id.toString(),
                ).observe(this@ProjectMilestoneActivity) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> showLoading(true)
                            is Result.Success -> {
                                showLoading(false)
                                getProjectMilestone(projectId)
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
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showDialog(
        context: Context,
        title: String,
        message: String,
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmpty(isEmpty: Boolean, message: String) {
        binding.empty.llEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.empty.tvEmpty.text = message
    }

    companion object {
        const val EXTRA_PROJECT_ID = "extra_project_id"
    }
}