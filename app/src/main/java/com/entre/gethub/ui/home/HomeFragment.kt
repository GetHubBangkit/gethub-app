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
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.NewPartnerResponse
import com.entre.gethub.data.remote.response.projects.AcceptedProjectBidResponse
import com.entre.gethub.ui.detailpartner.DetailPartnerActivity
import com.entre.gethub.ui.home.caritalent.HomeCariTalentActivity
import com.entre.gethub.ui.home.deteksiproject.HomeProjectDetectorActivity
import com.entre.gethub.ui.home.informationall.HomeInformationAllActivity
import com.entre.gethub.ui.home.mygethub.HomeKelolaMyGethubActivity
import com.entre.gethub.ui.home.projectbids.HomeCariProjectBidsActivity
import com.entre.gethub.utils.ViewModelFactory

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
        observeNewPartner()
        getNewPartnerList()
        getUserProfile()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        // Tidak perlu memperbarui Reys Event di sini
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
        binding.tvSeeAllInformation.setOnClickListener {
            startActivity(Intent(requireContext(), HomeInformationAllActivity::class.java))
        }
    }

    private fun setupRecyclerViews() {
        setupRecyclerViewInformationHub()
        getOnWorkingProjectBids()
    }

    private fun getUserProfile() {
        homeViewModel.getUserProfile().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    // Handle loading state
                }

                is Result.Success -> {
                    val userProfile = result.data.data
                    val profession = userProfile?.profession ?: ""
                    if (profession.isNotEmpty()) {
                        getReysEvent(profession)
                    }
                }

                is Result.Error -> {
                    // Handle error
                }

                else -> {
                    // Handle other cases
                }
            }
        }
    }

    private fun getReysEvent(profession: String) {
        homeViewModel.getReysEvent(profession).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoadingInformationHub(true)
                }

                is Result.Success -> {
                    showLoadingInformationHub(false)
                    val eventData = result.data.data ?: emptyList()
                    if (eventData.isEmpty()) {
                        showEmptyEvent(true) // Panggil showEmptyEvent jika data kosong
                    } else {
                        showEmptyEvent(false) // Sembunyikan pesan kosong jika data tidak kosong
                    }
                    (binding.recyclerViewInformationHub.adapter as HomeInformationHubAdapter).updateData(eventData)
                }
                is Result.Error -> {
                    showLoadingInformationHub(false)
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
                is Result.Empty -> {
                    showLoadingInformationHub(false)
                    showEmptyEvent(true)
                    getReysEvent("") // Panggil getReysEvent("") kembali untuk memperbarui data saat kosong
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
                gethubpartner.id?.let { partnerId ->
                    navigateToDetailPartner(partnerId)
                } ?: run {
                    showToast("Partner ID is missing")
                }
            }
        }
    }

    private fun navigateToDetailPartner(partnerId: String?) {
        partnerId?.let {
            val intent = Intent(requireContext(), DetailPartnerActivity::class.java).apply {
                putExtra("PARTNER_ID", it)
            }
            startActivity(intent)
        } ?: run {
            showToast("Partner ID is missing")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun getOnWorkingProjectBids() {
        homeViewModel.getAcceptedBid().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoadingOnBiddingDikerjakan(true)
                    is Result.Success -> {
                        showLoadingOnBiddingDikerjakan(false)
                        val onWorkingProjectList =
                            result.data.data.filter { dataItem -> dataItem.project.statusProject == "CLOSE" }
                        Log.d("HomeFragment", "OnWorkingProjectBids: $onWorkingProjectList")

                        if (onWorkingProjectList.isEmpty()) {
                            showEmptyOnBiddingDikerjakan(true)
                        } else {
                            setupRecyclerViewBiddingDikerjakan(onWorkingProjectList)
                        }
                    }
                    is Result.Empty -> {
                        showLoadingOnBiddingDikerjakan(false)
                        showEmptyOnBiddingDikerjakan(true)
                    }
                    is Result.Error -> {
                        showLoadingOnBiddingDikerjakan(false)
                        showEmptyOnBiddingDikerjakan(true)
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
                val intent = Intent(requireContext(), HomeDetailInformationHubHomeActivity::class.java)
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
        binding.clEmptyGethubPartner.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun showEmptyEvent(isEmpty: Boolean) {
        binding.clEmptyEvent.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun showLoadingOnBiddingDikerjakan(isLoading: Boolean) {
        binding.progressBarOnBiddingDikerjakan.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyOnBiddingDikerjakan(isEmpty: Boolean) {
        binding.clEmptyBiddingDikerjakan.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
}
