package com.entre.gethub.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.R
import com.entre.gethub.databinding.FragmentHomeBinding
import com.entre.gethub.ui.adapter.HomeBiddingDikerjakanAdapter
import com.entre.gethub.ui.adapter.HomeGethubPartnerAdapter
import com.entre.gethub.ui.adapter.HomeInformationHubAdapter
import com.entre.gethub.utils.ViewModelFactory
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.NewPartnerResponse
import com.entre.gethub.data.remote.response.projects.AcceptedProjectBidResponse
import com.entre.gethub.ui.home.caritalent.HomeCariTalentActivity
import com.entre.gethub.ui.home.deteksiproject.HomeProjectDetectorActivity
import com.entre.gethub.ui.home.mygethub.HomeKelolaMyGethubActivity
import com.entre.gethub.ui.home.projectbids.HomeCariProjectBidsActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupClickListeners()
        setupRecyclerViews()
        getInformationList()
        observeNewPartner()
        getNewPartnerList()
        getOnWorkingProjectBids()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        getInformationList()
    }

    private fun setupClickListeners() {
        binding.ivCariTalent.setOnClickListener {
            startActivity(Intent(requireContext(), HomeCariTalentActivity::class.java))
        }
        binding.ivKelolaMyGethub.setOnClickListener {
            startActivity(Intent(requireContext(), HomeKelolaMyGethubActivity::class.java))
        }
        binding.ivCariProjectBid.setOnClickListener {
            startActivity(Intent(requireContext(), HomeCariProjectBidsActivity::class.java))
        }
        binding.ivProjectDetector.setOnClickListener {
            startActivity(Intent(requireContext(), HomeProjectDetectorActivity::class.java))
        }
    }

    private fun setupRecyclerViews() {
        setupRecyclerViewInformationHub()
    }

    private fun getInformationList() {
        homeViewModel.informationHubs.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    showLoadingInformationHub(false)
                    val data = result.data
                    binding.empty.llEmpty.visibility = View.GONE
                    (binding.recyclerViewInformationHub.adapter as HomeInformationHubAdapter).updateData(
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

    private fun observeNewPartner() {
        homeViewModel.getNewPartner().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    showLoadingNewGethubPartner(false)
                    val data = result.data?.data ?: emptyList()
                    setupRecyclerViewHomeGethubPartner(data)
                }

                is Result.Error -> {
                    showLoadingNewGethubPartner(false)
                }

                is Result.Loading -> {
                    showLoadingNewGethubPartner(true)
                }

                is Result.Empty -> {
                    showLoadingNewGethubPartner(false)
                    showEmptyNewGethubPartner(true)
                }
            }
        }
    }

    private fun getNewPartnerList() {
        homeViewModel.getNewPartner()
    }

    private fun setupRecyclerViewHomeGethubPartner(partnerList: List<NewPartnerResponse.Data>) {
        binding.recyclerViewHomeGethubPartner.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = HomeGethubPartnerAdapter(partnerList) { gethubpartner, position ->
                Toast.makeText(
                    this@HomeFragment.requireContext(),
                    "Clicked on actor: ${gethubpartner.fullName}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getOnWorkingProjectBids() {
        homeViewModel.getAcceptedBid().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading ->
                        showLoadingOnBiddingDikerjakan(true)

                    is Result.Success -> {
                        showLoadingOnBiddingDikerjakan(false)
                        val onWorkingProjectList =
                            result.data.data.filter { dataItem -> dataItem.project.statusProject == "CLOSE" }
                        Log.d("HomeFragment", "OnWorkingProjectBids: $onWorkingProjectList")

                        setupRecyclerViewBiddingDikerjakan(onWorkingProjectList)
                    }

                    is Result.Empty -> {
                        showLoadingOnBiddingDikerjakan(false)
                        showEmptyOnBiddingDikerjakan(true)
                    }

                    else -> {
                        //
                    }
                }
            }
        }
    }


    private fun setupRecyclerViewBiddingDikerjakan(onWorkingProjectList: List<AcceptedProjectBidResponse.DataItem>) {
        binding.recyclerViewBiddingDikerjakan.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = HomeBiddingDikerjakanAdapter(onWorkingProjectList)
        }
    }

    private fun setupRecyclerViewInformationHub() {
        binding.recyclerViewInformationHub.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = HomeInformationHubAdapter(emptyList()) { informationhub, position ->
                val intent = Intent(requireContext(), HomeDetailInformationHubActivity::class.java)
                val informationHubList = listOf(informationhub)
                intent.putParcelableArrayListExtra("information_hub", ArrayList(informationHubList))
                startActivity(intent)
            }
        }
    }

    private fun showLoadingInformationHub(isLoading: Boolean) {
        binding.progressBarOnInformationHub.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showLoadingNewGethubPartner(isLoading: Boolean) {
        binding.progressBarOnNewGethubPartner.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyNewGethubPartner(isEmpty: Boolean) {
        binding.tvEmptyNewGethubPartner.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun showLoadingOnBiddingDikerjakan(isLoading: Boolean) {
        binding.progressBarOnBiddingDikerjakan.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyOnBiddingDikerjakan(isEmpty: Boolean) {
        binding.tvEmptyOnBiddingDikerjakan.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
}
