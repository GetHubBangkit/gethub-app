package com.entre.gethub.ui.home.caritalent

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ml.CariTalentResponse
import com.entre.gethub.databinding.ActivityHomeCariTalentBinding
import com.entre.gethub.ui.adapter.CariTalentAdapter
import com.entre.gethub.ui.akun.membership.MembershipActivity
import com.entre.gethub.ui.userpublicprofile.UserPublicProfileActivity
import com.entre.gethub.utils.ViewModelFactory

class HomeCariTalentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeCariTalentBinding
    private lateinit var viewModel: HomeCariTalentViewModel
    private lateinit var cariTalentAdapter: CariTalentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeCariTalentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory).get(HomeCariTalentViewModel::class.java)

        // Check user profile before loading content
        checkUserProfile()
    }

    private fun checkUserProfile() {
        viewModel.getUserProfile().observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    val userProfileResponse = result.data
                    if (userProfileResponse.data.isPremium == true) {
                        // User is premium, proceed with loading the activity content
                        setupUI()
                    } else {
                        // User is not premium, redirect to MembershipActivity
                        val intent = Intent(this, MembershipActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                is Result.Error -> {
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    private fun setupUI() {
        setupRecyclerViewCariTalent()

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.iconsearch.setOnClickListener {
            searchCariTalent()
        }

        // Fetch all talents when activity is created
        fetchAllTalents()
    }

    private fun setupRecyclerViewCariTalent() {
        cariTalentAdapter = CariTalentAdapter(ArrayList()) { talent ->
            navigateToUserProfile(talent.username!!)
        }

        binding.rvHomeCariTalent.apply {
            layoutManager = LinearLayoutManager(this@HomeCariTalentActivity)
            adapter = cariTalentAdapter
        }
    }

    private fun fetchAllTalents() {
        viewModel.allTalentsResult.observe(this, Observer { result ->
            handleResult(result)
        })
        viewModel.getAllTalents()
    }

    private fun searchCariTalent() {
        val profession = binding.professionTextField.editText?.text.toString()
        if (profession.isNotBlank()) {
            viewModel.searchCariTalent(profession).observe(this, Observer { result ->
                handleResult(result)
            })
        } else {
            Toast.makeText(this, "Please enter a profession", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleResult(result: Result<CariTalentResponse>) {
        when (result) {
            is Result.Empty -> {
                showLoadingTalent(false)
                Toast.makeText(this, "No talents found", Toast.LENGTH_SHORT).show()
            }
            is Result.Loading -> {
                showLoadingTalent(true)
                Log.d("HomeCariTalentActivity", "Loading...")
            }
            is Result.Success -> {
                showLoadingTalent(false)
                Log.d("HomeCariTalentActivity", "Success: ${result.data}")
                result.data.data?.let {
                    if (it.isNotEmpty()) {
                        cariTalentAdapter.clearAll() // Clear the current list
                        cariTalentAdapter.addAll(it) // Add the new data
                    } else {
                        Toast.makeText(this, "No talents found", Toast.LENGTH_SHORT).show()
                    }
                } ?: run {
                    Toast.makeText(this, "No talents found", Toast.LENGTH_SHORT).show()
                }
            }
            is Result.Error -> {
                showLoadingTalent(false)
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToUserProfile(username: String) {
        val intent = Intent(this, UserPublicProfileActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
    }

    private fun showLoadingTalent(isLoading: Boolean) {
        binding.progressBarOnTalent.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
