package com.entre.gethub.ui.project

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.ui.models.ProjectBid
import com.entre.gethub.R
import com.entre.gethub.ui.models.TopTalent
import com.entre.gethub.databinding.FragmentProjectBinding
import com.entre.gethub.ui.adapter.HomeProjectBidsAdapter
import com.entre.gethub.ui.adapter.TopTalentAdapter

class ProjectFragment : Fragment() {

    private var _binding: FragmentProjectBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val projectViewModel =
            ViewModelProvider(this).get(ProjectViewModel::class.java)

        _binding = FragmentProjectBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textProject
        projectViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
        }
        // Panggil setupRecyclerView() di sini
        setupRecyclerView()

        setupRecyclerViewProjectBid()



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

//    private fun setupRecyclerViewProjectBid() {
//        binding.recyclerViewRekomendasiProjectBid.apply {
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//            adapter = RekomendasiProjectBidAdapter(createProjectBidList()) { projectbid, position ->
//                Toast.makeText(
//                    this@ProjectFragment.requireContext(), // Gunakan requireContext() untuk mendapatkan Context yang benar
//                    "Clicked on actor: ${projectbid.rekomendasiprofilename}",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }

//    private fun createProjectBidList(): ArrayList<ProjectBid> {
//        return arrayListOf<ProjectBid>(
//            ProjectBid(
//
//                "Ajay Devgan",
//                R.drawable.profilepic1,
//                "Software Enginer",
//                "UI UX Designer",
//                "Dibutuhkan UI UX Designer yg memiliki jiwa seni untuk membuat layout Aplikasi sesuai dengan recruitment yg telah saya tentukan dengan thema Coffee Shop ",
//                "Rp 3,000,000 - 4,500,000",
//                "Deadline : 10 Days",
//                "Total User Bids : 10 Users",
//                "Di Post : 20-04-2024"
//
//
//            ),
//            ProjectBid(
//                "Ajay Devgan",
//                R.drawable.profilepic1,
//                "Software Enginer",
//                "Butuh Konten Kreator   ",
//                "Sedang Mencari Konten Kreator untuk pembuatan konten makanan dan untuk promosi warung rumah makanan yg baru buka di butuhkan segera",
//                "Rp 3,000,000 - 4,500,000",
//                "Deadline : 10 Days",
//                "Total User Bids : 10 Users",
//                "Di Post : 20-04-2024"
//
//            )
//        )
//    }

    private fun setupRecyclerViewProjectBid() {
        binding.recyclerViewRekomendasiProjectBid.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = HomeProjectBidsAdapter(createProjectBidList()) { projectBid, position ->
                // Handling item click event
                // Di sini, Anda dapat menangani klik item proyek sesuai dengan kebutuhan
                // Misalnya, menampilkan pesan toast atau melakukan aksi tertentu
                Toast.makeText(requireContext(), "Clicked on project: ${projectBid.rekomendasiprofilename}", Toast.LENGTH_SHORT).show()
            }
        }
    }




    private fun createProjectBidList(): ArrayList<ProjectBid> {
        return arrayListOf<ProjectBid>(
            ProjectBid(
                "Ajay Devgan",
                R.drawable.profilepic1,
                "Software Engineer",
                "UI UX Designer",
                "Dibutuhkan UI UX Designer yg memiliki jiwa seni untuk membuat layout Aplikasi sesuai dengan recruitment yg telah saya tentukan dengan thema Coffee Shop ",
                "Rp 3,000,000 - 4,500,000", // Harga rekrut
                "Deadline : 10 Days", // Deadline proyek
                "Total User Bids : 5 Users", // Total proyek
                "Di Post : 20-04-2024", // Tanggal proyek
                "2 Mei 2024 - 12 Mei 2024" // Tanggal awal-akhir
            ),
            ProjectBid(
                "Michael",
                R.drawable.profilepic1,
                "Software Engineer",
                "Butuh Konten Kreator",
                "Sedang Mencari Konten Kreator untuk pembuatan konten makanan dan untuk promosi warung rumah makanan yg baru buka di butuhkan segera",
                "Rp 400,000 - 500,000", // Harga rekrut
                "Deadline : 5 Days", // Deadline proyek
                "Total User Bids : 5 Users", // Total proyek
                "Di Post : 20-04-2024", // Tanggal proyek
                "2 Mei 2024 - 12 Mei 2024" // Tanggal awal-akhir
            )
        )
    }
}