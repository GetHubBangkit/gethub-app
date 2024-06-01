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
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.projects.ProjectStatsResponse
import com.entre.gethub.databinding.FragmentProjectBinding
import com.entre.gethub.ui.adapter.UserProjectBidAdapter
import com.entre.gethub.ui.adapter.TopTalentAdapter
import com.entre.gethub.ui.models.TopTalent
import com.entre.gethub.ui.project.acceptedbidproject.AcceptedBidProjectActivity
import com.entre.gethub.ui.project.bidproject.BidProjectStatusActivity
import com.entre.gethub.ui.project.bidproject.BidProjectStatusDetailActivity
import com.entre.gethub.ui.project.postedproject.PostedProjectStatusActivity
import com.entre.gethub.ui.project.postproject.PostProjectActivity
import com.entre.gethub.utils.ViewModelFactory

class ProjectFragment : Fragment() {

    private var _binding: FragmentProjectBinding? = null
    private val binding get() = _binding!!

    private val projectViewModel by viewModels<ProjectViewModel> {
        ViewModelFactory.getInstance(
            requireContext()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProjectBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()

        setupClickListener()

        getUserProjectStats()

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
                            val result = result.data.data
                            tvPostProject.text = result?.jobPosted.toString()
                            tvBidProject.text = result?.bidsMade.toString()
                            tvTerimaProject.text = result?.bidsAccepted.toString()
                        }
                        if (result.data.data?.bidProjects?.size == 0) {
                            showEmptyOnProjectBids(true, "Anda belum melakukan bidding projek")
                            binding.rvRekomendasiProjectBid.visibility = View.GONE
                            return@observe
                        } else {
                            setupRecyclerViewProjectBid(
                                result.data.data?.bidProjects ?: emptyList()
                            )
                            binding.rvRekomendasiProjectBid.visibility = View.VISIBLE

                        }
                    }

                    is Result.Error -> {
                        showLoadingOnProjectBids(false)
                        showToast(result.error)
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

    private fun setupRecyclerView() {
        binding.recyclerViewTop.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = TopTalentAdapter(createTopTalentList()) { toptalent, position ->
                Toast.makeText(
                    this@ProjectFragment.requireContext(), // Gunakan requireContext() untuk mendapatkan Context yang benar
                    "Clicked on actor: ${toptalent.profilename}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun createTopTalentList(): ArrayList<TopTalent> {
        return arrayListOf<TopTalent>(
            TopTalent(
                "Ajay Devgan",
//                R.drawable.frame1,
                R.drawable.profilepic1,
                "Software Enginer"
            ),
            TopTalent(
                "Ajay Devgan",
//                R.drawable.frame1,
                R.drawable.profilepic1,
                "Software Enginer"
            ),
            TopTalent(
                "Ajay Devgan",
//                R.drawable.frame1,
                R.drawable.profilepic1,
                "Software Enginer"
            ),
            TopTalent(
                "Ajay Devgan",
//                R.drawable.frame1,
                R.drawable.profilepic1,
                "Software Enginer"
            ),
            TopTalent(
                "Ajay Devgan",
//                R.drawable.frame1,
                R.drawable.profilepic1,
                "Software Enginer"
            ),
            TopTalent(
                "Ajay Devgan",
//                R.drawable.frame1,
                R.drawable.profilepic1,
                "Software Enginer"
            )
        )
    }


    private fun setupRecyclerViewProjectBid(projectBidList: List<ProjectStatsResponse.BidProjectsItem>) {
        binding.rvRekomendasiProjectBid.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = UserProjectBidAdapter(projectBidList) { projectBid, _ ->
                val intent = Intent(
                    requireContext(),
                    BidProjectStatusDetailActivity::class.java
                )
                intent.putExtra(
                    BidProjectStatusDetailActivity.EXTRA_PROJECT_BID_ID,
                    projectBid.projectId
                )
                startActivity(intent)
            }
        }
    }

    private fun showLoadingOnProjectBids(isLoading: Boolean) {
        binding.progressBarOnProjectBids.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyOnProjectBids(isEmpty: Boolean, message: String) {
        binding.emptyOnProjectBids.llEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.emptyOnProjectBids.tvEmpty.text = message
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}