package com.entre.gethub.ui.project

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.projects.ProjectStatsResponse
import com.entre.gethub.data.remote.response.TopTalent
import com.entre.gethub.databinding.FragmentProjectBinding
import com.entre.gethub.ui.adapter.UserProjectBidAdapter
import com.entre.gethub.ui.adapter.TopTalentAdapter
import com.entre.gethub.ui.project.freelanceracceptedproject.AcceptedBidProjectActivity
import com.entre.gethub.ui.project.freelancerbidproject.BidProjectStatusActivity
import com.entre.gethub.ui.project.freelancerbidproject.BidProjectStatusDetailActivity
import com.entre.gethub.ui.project.ownerpostedproject.PostedProjectStatusActivity
import com.entre.gethub.ui.project.postproject.PostProjectActivity
import com.entre.gethub.ui.userpublicprofile.UserPublicProfileActivity
import com.entre.gethub.utils.ViewModelFactory

class ProjectFragment : Fragment() {

    private var _binding: FragmentProjectBinding? = null
    private val binding get() = _binding!!

    private val projectViewModel by viewModels<ProjectViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupClickListener()

        getUserProjectStats()
        getTopTalent()

        return root
    }

    override fun onResume() {
        super.onResume()
        getUserProjectStats()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupClickListener() {
        with(binding) {
            ivBidProject.setOnClickListener {
                navigateToActivity(BidProjectStatusActivity())
            }

            ivPostProject.setOnClickListener {
                navigateToActivity(PostedProjectStatusActivity())
            }

            ivAcceptedProject.setOnClickListener {
                navigateToActivity(AcceptedBidProjectActivity())
            }

            fabPostProject.setOnClickListener {
                navigateToActivity(PostProjectActivity())
            }
        }
    }

    private fun getUserProjectStats() {
        projectViewModel.getUserProjectStats().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoadingOnProjectBids(true)
                    is Result.Success -> {
                        showLoadingOnProjectBids(false)
                        with(binding) {
                            val resultData = result.data.data
                            tvPostProject.text = resultData?.jobPosted.toString()
                            tvBidProject.text = resultData?.bidsMade.toString()
                            tvTerimaProject.text = resultData?.bidsAccepted.toString()
                        }
                        if (result.data.data?.bidProjects?.isEmpty() == true) {
                            showEmptyOnProjectBids(true, "Anda belum melakukan bidding projek")
                            binding.rvRekomendasiProjectBid.visibility = View.GONE
                        } else {
                            setupRecyclerViewProjectBid(result.data.data?.bidProjects ?: emptyList())
                            binding.rvRekomendasiProjectBid.visibility = View.VISIBLE
                        }
                    }
                    is Result.Error -> {
                        showLoadingOnProjectBids(false)
//                        showToast(result.error)
                    }
                    is Result.Empty -> {
                        showLoadingOnProjectBids(false)
                        showEmptyOnProjectBids(true, result.emptyError)
                    }
                }
            }
        }
    }

    private fun navigateToActivity(activity: AppCompatActivity) {
        startActivity(Intent(requireContext(), activity::class.java))
    }

    private fun getTopTalent() {
        projectViewModel.getTopTalent().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> showLoadingTopTalent(true)
                is Result.Success -> {
                    showLoadingTopTalent(false)
                    result.data?.data?.let { topTalentList ->
                        setupTopTalentRecyclerView(topTalentList)
                    }
                }
                is Result.Error -> {
                    showLoadingTopTalent(false)
//                    showToast(result.error)
                }
                is Result.Empty -> {
                    showLoadingTopTalent(false)
                    showEmptyTopTalent(true, result.emptyError)
                }
            }
        }
    }

    private fun setupTopTalentRecyclerView(topTalentList: List<TopTalent>) {
        binding.recyclerViewTop.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = TopTalentAdapter(topTalentList) { topTalent, position ->
                // Handle click event
                val intent = Intent(requireContext(), UserPublicProfileActivity::class.java)
                intent.putExtra("username", topTalent.username)
                startActivity(intent)
            }
        }
    }


    private fun setupRecyclerViewProjectBid(projectBidList: List<ProjectStatsResponse.BidProjectsItem>) {
        binding.rvRekomendasiProjectBid.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = UserProjectBidAdapter(projectBidList) { projectBid, _ ->
                val intent = Intent(requireContext(), BidProjectStatusDetailActivity::class.java)
                intent.putExtra(BidProjectStatusDetailActivity.EXTRA_PROJECT_BID_ID, projectBid.projectId)
                startActivity(intent)
            }
        }
    }

    private fun showLoadingTopTalent(isLoading: Boolean) {
        binding.progressBarOnTopTalent.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyTopTalent(isEmpty: Boolean, message: String) {
        binding.tvEmptyTopTalent.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun showLoadingOnProjectBids(isLoading: Boolean) {
        binding.progressBarOnProjectBids.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyOnProjectBids(isEmpty: Boolean, message: String) {
        binding.clEmptyProject.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
