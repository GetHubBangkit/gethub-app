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
        getPartnerList() // Memanggil fungsi untuk mendapatkan daftar partner

        // Ketika tombol cari ditekan
        binding.btnSimpan.setOnClickListener {
            val query = binding.namakTextField.editText?.text.toString()
            val profession = binding.profesiTextField.editText?.text.toString()
            if (query.isNotBlank() || profession.isNotBlank()) {
                isSearching = true // Mengubah status menjadi sedang mencari
                searchPartner(query, profession) // Memanggil fungsi pencarian
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
                    "Clicked on partner: ${gethubpartnerlist.fullName}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setupRecyclerGethubPartnerSearchList(partnerList: List<SearchingPartnerResponse.Partner>) {
        binding.rvGethubPartner.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = GethubPartnerSearchListAdapter(partnerList) { partner, position ->
                Toast.makeText(
                    this@GethubPartnerListActivity,
                    "Clicked on partner: ${partner.fullName}",
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

    private fun searchPartner(name: String, profession: String) {
        getHubPartnerListViewModel.searchPartner(name, profession).observe(this) { result ->
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
//                toggleEmptyView(gethubPartnerList.isEmpty())
            }
            is Result.Error -> {
                showLoading(false)
                showToast(result.error)
//                toggleEmptyView(true)
            }
            is Result.Empty -> {
                showLoading(false)
//                toggleEmptyView(true, result.emptyError)
            }
            else -> {
                showLoading(false)
                showToast("Terjadi kesalahan")
//                toggleEmptyView(true)
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
//                toggleEmptyView(searchingPartnerList.isEmpty())
            }
            is Result.Error -> {
                showLoading(false)
                showToast(result.error)
//                toggleEmptyView(true)
            }
            is Result.Empty -> {
                showLoading(false)
//                toggleEmptyView(true, result.emptyError)
            }
            else -> {
                showLoading(false)
                showToast("Terjadi kesalahan")
//                toggleEmptyView(true)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

//    private fun toggleEmptyView(isEmpty: Boolean, emptyMessage: String = "List Partner Masih Kosong") {
//        binding.empty.apply {
//            llEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
//            tvEmpty.text = emptyMessage
//        }
//    }
}
