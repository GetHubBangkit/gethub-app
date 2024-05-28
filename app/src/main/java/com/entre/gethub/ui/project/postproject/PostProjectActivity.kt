package com.entre.gethub.ui.project.postproject

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.databinding.ActivityPostProjectBinding
import com.entre.gethub.ui.MainActivity
import com.entre.gethub.ui.adapter.CategoryAdapter
import com.entre.gethub.ui.project.postedproject.PostedProjectStatusActivity
import com.entre.gethub.utils.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.Calendar
import kotlin.math.min

class PostProjectActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPostProjectBinding.inflate(layoutInflater) }
    private val postProjectViewModel by viewModels<PostProjectViewModel> {
        ViewModelFactory.getInstance(
            this
        )
    }
    private var selectedCategoryId: String? = null
    private var selectedMinDate: String? = null
    private var selectedMaxDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getCategories()
        setupDatePicker()

        setupView()
    }

    private fun setupView() {
        with(binding) {
            iconBack.setOnClickListener {
                finish()
            }

            val minBudgetEditText = minBudgetBidTextField.editText
            val maxBudgetEditText = maxBudgetBidTextField.editText
            val titleEditText = projectTitleTextField.editText
            val descriptionEditText = descriptionProjectTextField.editText

            minBudgetEditText?.doOnTextChanged { text, start, before, count ->
                if (text.toString().isEmpty()) {
                    minBudgetEditText.setError(getString(R.string.field_couldnt_be_empty))
                } else {
                    minBudgetEditText.error = null
                }
            }

            maxBudgetEditText?.doOnTextChanged { text, start, before, count ->
                if (text.toString().isEmpty()) {
                    maxBudgetEditText.setError(getString(R.string.field_couldnt_be_empty))
                } else {
                    maxBudgetEditText.error = null
                }
            }

            titleEditText?.doOnTextChanged { text, start, before, count ->
                if (text.toString().isEmpty()) {
                    titleEditText.setError(getString(R.string.field_couldnt_be_empty))
                } else {
                    titleEditText.error = null
                }
            }

            descriptionEditText?.doOnTextChanged { text, start, before, count ->
                if (text.toString().isEmpty()) {
                    descriptionEditText.setError(getString(R.string.field_couldnt_be_empty))
                } else {
                    descriptionEditText.error = null
                }
            }

            btnPost.setOnClickListener {
                val minBudget = minBudgetEditText?.text.toString().toInt()
                val maxBudget = maxBudgetEditText?.text.toString().toInt()
                val title = titleEditText?.text.toString()
                val description = descriptionEditText?.text.toString()

                val condition =
                    (minBudgetEditText?.text?.isNotEmpty()!! || maxBudgetEditText?.text?.isNotEmpty()!! || title.isNotEmpty() || description.isNotEmpty() || !selectedCategoryId.isNullOrEmpty() || !selectedMinDate.isNullOrEmpty() || !selectedMaxDate.isNullOrEmpty())

                if (condition) {
                    postProject(
                        title,
                        selectedCategoryId.toString(),
                        description,
                        minBudget,
                        maxBudget,
                        selectedMinDate.toString(),
                        selectedMaxDate.toString()
                    )
                } else {
                    showToast(getString(R.string.field_couldnt_be_empty))
                }
            }
        }
    }

    private fun getCategories() {
        postProjectViewModel.getCategories()
            .observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            val categories = result.data.data
                            val adapter = CategoryAdapter(
                                this,
                                categories
                            )
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.spinnerKategori.adapter = adapter

                            binding.spinnerKategori.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        selectedCategoryId = adapter.getCategoryId(position)
                                    }

                                    override fun onNothingSelected(p0: AdapterView<*>?) {
                                        // Nothing
                                    }

                                }
                        }

                        is Result.Error -> {
                            showLoading(false)
                            showToast(getString(com.entre.gethub.R.string.failed_to_fetch_categories))
                        }

                        else -> {
                            //
                        }
                    }
                }
            }
    }

    private fun setupDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        with(binding) {
            startDateProjectTextField.editText?.setOnClickListener {
                val datePickerDialog = DatePickerDialog(
                    this@PostProjectActivity,
                    { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                        // Add 1 to month because month is zero-based
                        val formattedDate = "$selectedYear-${selectedMonth + 1}-$selectedDayOfMonth"
                        startDateProjectTextField.editText?.setText(formattedDate)
                        selectedMinDate = formattedDate
                    },
                    year,
                    month,
                    day
                )
                datePickerDialog.show()
            }

            endDateProjectTextField.editText?.setOnClickListener {
                val datePickerDialog = DatePickerDialog(
                    this@PostProjectActivity,
                    { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                        // Add 1 to month because month is zero-based
                        val formattedDate = "$selectedYear-${selectedMonth + 1}-$selectedDayOfMonth"
                        endDateProjectTextField.editText?.setText(formattedDate)
                        selectedMaxDate = formattedDate
                    },
                    year,
                    month,
                    day
                )
                datePickerDialog.show()
            }
        }
    }

    private fun postProject(
        title: String,
        categoryId: String,
        description: String,
        minBudget: Int,
        maxBudget: Int,
        minDeadline: String,
        maxDeadline: String,
    ) {
        postProjectViewModel.postProject(
            title,
            categoryId,
            description,
            minBudget,
            maxBudget,
            minDeadline,
            maxDeadline
        ).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        showToast(result.data.message!!)
                        val intent = Intent(this, PostedProjectStatusActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    is Result.Error -> {
                        showLoading(false)
                        showToast(result.error)
                    }

                    is Result.Empty -> {
                        showLoading(false)
                        showDialog(
                            this@PostProjectActivity,
                            getString(R.string.account_is_not_verified),
                            result.emptyError
                        )
                    }
                }
            }
        }
    }

    private fun showDialog(context: Context, title: String, message: String) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Kirim") { dialog, _ ->
                postProjectViewModel.regenerateVerifyEmail()
                dialog.dismiss()
                showToast("Email berhasil dikirim")
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
                showToast("Verifikasi batal")
            }
            .show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}