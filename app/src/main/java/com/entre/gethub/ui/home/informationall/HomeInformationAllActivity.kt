package com.entre.gethub.ui.home.informationall

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.databinding.ActivityHomeInformationAllBinding
import com.entre.gethub.ui.adapter.HomeInformationHubAllAdapter
import com.entre.gethub.ui.home.HomeDetailInformationHubActivity
import com.entre.gethub.data.Result
import com.entre.gethub.data.repositories.InformationHubRepository
import com.entre.gethub.di.Injection
import com.entre.gethub.ui.MainActivity
import com.entre.gethub.ui.home.projectbids.HomeCariProjectBidsActivity
import com.entre.gethub.utils.ViewModelFactory

class HomeInformationAllActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeInformationAllBinding
    private lateinit var viewModelFactory: ViewModelFactory
    private val homeInformationAllViewModel: HomeInformationAllViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeInformationAllBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelFactory = ViewModelFactory.getInstance(this)
        setupRecyclerViewInformationHub()
        getInformationList()

        binding.iconBack.setOnClickListener {
            finish()
        }
    }


    private fun getInformationList() {
        homeInformationAllViewModel.getInformationHubs().observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    showLoadingInformationHub(false)
                    val data = result.data
                    binding.empty.llEmpty.visibility = View.GONE
                    (binding.recyclerViewInformationHubAll.adapter as HomeInformationHubAllAdapter).updateData(
                        data
                    )
                }

                is Result.Error -> {
                    showLoadingInformationHub(false)
                }

                is Result.Loading -> {
                    showLoadingInformationHub(true)
                }

                is Result.Empty -> {
                    showLoadingInformationHub(false)
                    binding.empty.apply {
                        llEmpty.visibility = View.VISIBLE
                        tvEmpty.text = result.emptyError
                    }
                }
            }
        }
    }

    private fun setupRecyclerViewInformationHub() {
        binding.recyclerViewInformationHubAll.apply {
            layoutManager = LinearLayoutManager(this@HomeInformationAllActivity)
            adapter = HomeInformationHubAllAdapter(emptyList()) { informationHub, _ ->
                val intent = Intent(
                    this@HomeInformationAllActivity,
                    HomeDetailInformationHubActivity::class.java
                )
                val informationHubList = listOf(informationHub)
                intent.putParcelableArrayListExtra("information_hub", ArrayList(informationHubList))
                startActivity(intent)
            }
        }
    }

    private fun showLoadingInformationHub(isLoading: Boolean) {
        binding.progressBarOnInformationHub.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
