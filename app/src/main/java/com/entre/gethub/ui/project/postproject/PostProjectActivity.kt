package com.entre.gethub.ui.project.postproject

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.databinding.ActivityPostProjectBinding
import com.entre.gethub.ui.adapter.CategoryAdapter
import com.entre.gethub.utils.ViewModelFactory
import java.util.Calendar

class PostProjectActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPostProjectBinding.inflate(layoutInflater) }
    private val postProjectViewModel by viewModels<PostProjectViewModel> {
        ViewModelFactory.getInstance(
            this
        )
    }
    private var selectedCategoryId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getCategories()
        setupDatePicker()
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
                    this@PostProjectActivity, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                        // Add 1 to month because month is zero-based
                        val formattedDate = "$selectedYear-${selectedMonth + 1}-$selectedDayOfMonth"
                        startDateProjectTextField.editText?.setText(formattedDate)
                    }, year, month, day
                )
                datePickerDialog.show()
            }

            endDateProjectTextField.editText?.setOnClickListener {
                val datePickerDialog = DatePickerDialog(
                    this@PostProjectActivity, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                        // Add 1 to month because month is zero-based
                        val formattedDate = "$selectedYear-${selectedMonth + 1}-$selectedDayOfMonth"
                        endDateProjectTextField.editText?.setText(formattedDate)
                    }, year, month, day
                )
                datePickerDialog.show()
            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}