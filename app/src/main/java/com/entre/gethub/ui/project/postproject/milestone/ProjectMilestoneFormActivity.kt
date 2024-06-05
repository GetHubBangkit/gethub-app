package com.entre.gethub.ui.project.postproject.milestone

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.databinding.ActivityProjectMilestoneFormBinding
import com.entre.gethub.utils.ViewModelFactory

class ProjectMilestoneFormActivity : AppCompatActivity() {

    private val binding by lazy { ActivityProjectMilestoneFormBinding.inflate(layoutInflater) }
    private val projectMilestoneFormViewModel by viewModels<ProjectMilestoneFormViewModel> {
        ViewModelFactory.getInstance(
            this
        )
    }
    private var projectId: String = ""
    private var taskNumber: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        projectId = intent.getStringExtra(EXTRA_PROJECT_ID).toString()
        taskNumber = intent.getIntExtra(EXTRA_TASK_NUMBER, 0)

        binding.iconBack.setOnClickListener {
            finish()
        }

        setupEditText()

    }

    private fun setupEditText() {
        val edDescription = binding.descriptionTextField.editText

        edDescription?.doOnTextChanged { text, _, _, _ ->
            if (text.toString().isEmpty()) {
                edDescription.setError(getString(R.string.field_couldnt_be_empty))
            } else {
                edDescription.error = null
            }
        }

        binding.btnSimpan.setOnClickListener {
            val description = edDescription?.text.toString()
            if (description.isNotEmpty()) {
                addMilestone(
                    projectId = projectId,
                    taskNumber = taskNumber,
                    taskDescription = description
                )
            }
        }
    }

    private fun addMilestone(projectId: String, taskNumber: Int, taskDescription: String) {
        projectMilestoneFormViewModel.addMilestone(projectId, taskNumber, taskDescription)
            .observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                            binding.btnSimpan.isEnabled = false
                        }

                        is Result.Success -> {
                            showLoading(false)
                            finish()
                            showToast(result.data.message.toString())
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_PROJECT_ID = "extra_project_id"
        const val EXTRA_TASK_NUMBER = "extra_task_number"
    }
}