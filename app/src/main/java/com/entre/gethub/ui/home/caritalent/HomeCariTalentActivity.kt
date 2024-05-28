package com.entre.gethub.ui.home.caritalent

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.databinding.ActivityHomeCariTalentBinding
import com.entre.gethub.ui.adapter.CariTalentAdapter
import com.entre.gethub.utils.ViewModelFactory

class HomeCariTalentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeCariTalentBinding
    private lateinit var viewModel: HomeCariTalentViewModel
    private lateinit var cariTalentAdapter: CariTalentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeCariTalentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerViewCariTalent()

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.iconsearch.setOnClickListener {
            searchCariTalent()
        }

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory).get(HomeCariTalentViewModel::class.java)

        // Fetch all talents when activity is created
        fetchAllTalents()
    }

    private fun setupRecyclerViewCariTalent() {
        cariTalentAdapter = CariTalentAdapter(ArrayList()) { _, _ -> }
        binding.rvHomeCariTalent.apply {
            layoutManager = LinearLayoutManager(this@HomeCariTalentActivity)
            adapter = cariTalentAdapter
        }
    }

    private fun fetchAllTalents() {
        viewModel.getAllTalents().observe(this, Observer { response ->
            Log.d("HomeCariTalentActivity", "Observed Response: $response")
            if (response != null && response.data != null && response.data.isNotEmpty()) {
                cariTalentAdapter.addAll(response.data)
            } else {
                Toast.makeText(this@HomeCariTalentActivity, "No talents found", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun searchCariTalent() {
        val profession = binding.professionTextField.editText?.text.toString()
        if (profession.isNotBlank()) {
            viewModel.searchCariTalent(profession).observe(this, Observer { response ->
                Log.d("HomeCariTalentActivity", "Observed Response: $response")
                if (response != null && response.data != null && response.data.isNotEmpty()) {
                    cariTalentAdapter.addAll(response.data)
                } else {
                    Toast.makeText(this@HomeCariTalentActivity, "No talents found", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Please enter a profession", Toast.LENGTH_SHORT).show()
        }
    }
}
