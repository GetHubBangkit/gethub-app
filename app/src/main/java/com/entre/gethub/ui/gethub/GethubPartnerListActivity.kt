package com.entre.gethub.ui.gethub

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.partners.GetHubPartner
import com.entre.gethub.databinding.ActivityGethubPartnerListBinding
import com.entre.gethub.ui.adapter.GethubPartnerListAdapter
import com.entre.gethub.utils.ViewModelFactory

class GethubPartnerListActivity : AppCompatActivity() {
    private val binding by lazy { ActivityGethubPartnerListBinding.inflate(layoutInflater) }
    private lateinit var getHubPartnerListViewModel: GethubPartnerListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViewModel()
        getPartnerList()

        binding.iconBack.setOnClickListener {
            finish()
        }

    }

    private fun initViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        getHubPartnerListViewModel =
            ViewModelProvider(this, factory)[GethubPartnerListViewModel::class.java]
    }

    private fun setupRecyclerGethubPartnerList(gethubPartnerList: List<GetHubPartner>) {
        binding.rvGethubPartner.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = GethubPartnerListAdapter(gethubPartnerList) { gethubpartnerlist, position ->
                Toast.makeText(
                    this@GethubPartnerListActivity,
                    "Clicked on actor: ${gethubpartnerlist.fullName}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getPartnerList() {
        getHubPartnerListViewModel.getPartnerList().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        val gethubPartnerList = result.data.data
                        showLoading(false)
                        setupRecyclerGethubPartnerList(gethubPartnerList)
                    }

                    is Result.Error -> {
                        showLoading(false)
                        showToast(result.error)
                    }

                    is Result.Empty -> {
                        binding.empty.apply {
                            llEmpty.visibility = View.VISIBLE
                            tvEmpty.text = result.emptyError
                        }
                    }

                    else -> {
                        showLoading(false)
                        showToast("Terjadi kesalahan")
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
}