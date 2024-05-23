package com.entre.gethub.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.BiddingDikerjakan
import com.entre.gethub.HomeGethubPartner
import com.entre.gethub.R
import com.entre.gethub.databinding.FragmentHomeBinding
import com.entre.gethub.ui.adapter.HomeBiddingDikerjakanAdapter
import com.entre.gethub.ui.adapter.HomeGethubPartnerAdapter
import com.entre.gethub.ui.adapter.HomeInformationHubAdapter
import com.entre.gethub.utils.ViewModelFactory
import com.entre.gethub.data.Result

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Use the ViewModelFactory to get the ViewModel
        val factory = ViewModelFactory.getInstance(requireContext())
        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        setupClickListeners()
        setupRecyclerViews()
        observeViewModel()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupClickListeners() {

    }

    private fun setupRecyclerViews() {
        setupRecyclerViewHomeGethubPartner()
        setupRecyclerViewBiddingDikerjakan()
        setupRecyclerViewInformationHub()
    }

    private fun observeViewModel() {
        homeViewModel.informationHubs.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    val data = result.data
                    (binding.recyclerViewInformationHub.adapter as HomeInformationHubAdapter).updateData(data)
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    // Handle loading state
                }
                is Result.Empty -> {
                    // Handle empty state
                }
            }
        }
    }


    private fun setupRecyclerViewHomeGethubPartner() {
        binding.recyclerViewHomeGethubPartner.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = HomeGethubPartnerAdapter(createHomeGethubPartnerList()) { gethubpartner, position ->
                Toast.makeText(
                    this@HomeFragment.requireContext(),
                    "Clicked on actor: ${gethubpartner.profilename}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun createHomeGethubPartnerList(): ArrayList<HomeGethubPartner> {
        return arrayListOf(
            HomeGethubPartner("Budi Santoso", R.drawable.profilepic1, "Software Engineer"),
            HomeGethubPartner("Budi Santoso", R.drawable.profilepic1, "Software Engineer"),
            HomeGethubPartner("Budi Santoso", R.drawable.profilepic1, "Software Engineer"),
            HomeGethubPartner("Budi Santoso", R.drawable.profilepic1, "Software Engineer"),
            HomeGethubPartner("Budi Santoso", R.drawable.profilepic1, "Software Engineer")
        )
    }

    private fun setupRecyclerViewBiddingDikerjakan() {
        binding.recyclerViewBiddingDikerjakan.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = HomeBiddingDikerjakanAdapter(createBiddingDikerjakanList()) { biddingdikerjakan, position ->
                Toast.makeText(
                    this@HomeFragment.requireContext(),
                    "Clicked on actor: ${biddingdikerjakan.profilename}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun createBiddingDikerjakanList(): ArrayList<BiddingDikerjakan> {
        return arrayListOf(
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
                val informationHubList = listOf(informationhub)HomeDetailInformationHubActivity
                intent.putParcelableArrayListExtra("information_hub", ArrayList(informationHubList))
                startActivity(intent)
            }
        }
    }
}
