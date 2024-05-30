package com.entre.gethub.ui.gethub

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.SearchingPartnerResponse
import com.entre.gethub.data.remote.response.partners.GetHubPartner
import com.entre.gethub.data.remote.response.partners.GetHubPartnerListResponse
import com.entre.gethub.databinding.ActivityGethubPartnerListBinding
import com.entre.gethub.ui.adapter.GethubPartnerListAdapter
import com.entre.gethub.ui.adapter.GethubPartnerSearchListAdapter
import com.entre.gethub.utils.ViewModelFactory

class GethubPartnerListActivity : AppCompatActivity() {
    private val binding by lazy { ActivityGethubPartnerListBinding.inflate(layoutInflater) }
    private lateinit var getHubPartnerListViewModel: GethubPartnerListViewModel

    private var isSearching = false // Penanda apakah pengguna sedang melakukan pencarian

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViewModel()

        // Ketika tombol cari ditekan
        binding.btnSimpan.setOnClickListener {
            val query = binding.linkTextField.editText?.text.toString()
            if (query.isNotBlank()) {
                isSearching = true // Mengubah status menjadi sedang mencari
                searchPartner(query) // Memanggil fungsi pencarian
            } else {
                isSearching = false // Mengubah status menjadi tidak mencari
                getPartnerList() // Memanggil fungsi untuk mendapatkan daftar partner
            }
        }

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

    private fun setupRecyclerGethubPartnerSearchList(searchingPartnerList: List<SearchingPartnerResponse.Partner>) {
        binding.rvGethubPartner.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = GethubPartnerSearchListAdapter(searchingPartnerList) { partner, position ->
                Toast.makeText(
                    this@GethubPartnerListActivity,
                    "Clicked on actor: ${partner.fullName}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getPartnerList() {
        getHubPartnerListViewModel.getPartnerList().observe(this) { result ->
            handleResult(result)
        }
    }

    private fun searchPartner(name: String) {
        getHubPartnerListViewModel.searchPartner(name).observe(this) { result ->
            handleSearchResult(result)
        }
    }

    private fun handleResult(result: Result<GetHubPartnerListResponse>) {
        when (result) {
            is Result.Loading -> showLoading(true)
            is Result.Success -> {
                val gethubPartnerListResponse = result.data
                val gethubPartnerList = gethubPartnerListResponse.data
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

    private fun handleSearchResult(result: Result<SearchingPartnerResponse>) {
        when (result) {
            is Result.Loading -> showLoading(true)
            is Result.Success -> {
                val searchingPartnerResponse = result.data
                val searchingPartnerList = searchingPartnerResponse.data.partners
                showLoading(false)
                setupRecyclerGethubPartnerSearchList(searchingPartnerList)
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
