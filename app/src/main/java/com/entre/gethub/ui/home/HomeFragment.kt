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
import com.entre.gethub.ui.models.BiddingDikerjakan
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.NewPartnerResponse
import com.entre.gethub.data.remote.response.ReysEventResponse
import com.entre.gethub.ui.home.caritalent.HomeCariTalentActivity
import com.entre.gethub.ui.home.deteksiproject.HomeProjectDetectorActivity
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
        getInformationList()
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
        setupRecyclerViewBiddingDikerjakan()
        setupRecyclerViewInformationHub()
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
                    // Handle loading state if needed
                }
                is Result.Success -> {
                    val eventData = result.data.data ?: emptyList()
                    (binding.recyclerViewInformationHub.adapter as HomeInformationHubAdapter).updateData(eventData)
                }
                is Result.Error -> {
                    // Handle error
                }
                is Result.Empty -> {
                    // Handle empty state if needed
                }
            }
        }
    }

    private fun getInformationList() {
        homeViewModel.getReysEventResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    showLoadingInformationHub(false)
                    val eventData = result.data.data ?: emptyList()
                    binding.empty.llEmpty.visibility = View.GONE
                    (binding.recyclerViewInformationHub.adapter as HomeInformationHubAdapter).updateData(
                        eventData
                    )
                }
                is Result.Error -> {
                    showLoadingInformationHub(false)
                     Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
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
//                    Log.d("EmptyError", result.emptyError ?: "Empty Error Message")
//
//                    showEmptyInformationHub(true, result.emptyError)
                    Toast.makeText(requireContext(), result.emptyError, Toast.LENGTH_SHORT).show()
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
                    // Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    showLoadingNewGethubPartner(true)
                }
                is Result.Empty -> {
                    showLoadingNewGethubPartner(false)
                    showEmptyNewGethubPartner(true, result.emptyError)
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
                Toast.makeText(this@HomeFragment.requireContext(), "Clicked on actor: ${gethubpartner.fullName}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerViewBiddingDikerjakan() {
        binding.recyclerViewBiddingDikerjakan.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = HomeBiddingDikerjakanAdapter(createBiddingDikerjakanList()) { biddingdikerjakan, position ->
                Toast.makeText(this@HomeFragment.requireContext(), "Clicked on actor: ${biddingdikerjakan.profilename}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createBiddingDikerjakanList(): ArrayList<BiddingDikerjakan> {
        return arrayListOf(
            BiddingDikerjakan("Ajay Devgan", R.drawable.profilepic1, "Software Engineer", "UI UX Designer", "Rp 3,000,000 - 4,500,000", "Deadline: 10"),
                    BiddingDikerjakan("Ajay Devgan", R.drawable.profilepic1, "Software Engineer", "UI UX Designer", "Rp 3,000,000 - 4,500,000", "Deadline: 10 Days"),
            BiddingDikerjakan("Ajay Devgan", R.drawable.profilepic1, "Software Engineer", "UI UX Designer", "Rp 3,000,000 - 4,500,000", "Deadline: 10 Days"),
            BiddingDikerjakan("Ajay Devgan", R.drawable.profilepic1, "Software Engineer", "UI UX Designer", "Rp 3,000,000 - 4,500,000", "Deadline: 10 Days")
        )
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
        binding.progressBarOnNewGethubPartner.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyNewGethubPartner(isEmpty: Boolean, message: String) {
        binding.tvEmptyNewGethubPartner.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
    private fun showEmptyInformationHub(isEmpty: Boolean, message: String) {
        binding.tvEmptyInformationHub.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }
}
