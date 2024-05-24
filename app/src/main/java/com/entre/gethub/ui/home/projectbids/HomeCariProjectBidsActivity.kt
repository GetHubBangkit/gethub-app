package com.entre.gethub.ui.home.projectbids

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.R
import com.entre.gethub.databinding.ActivityHomeCariProjectBidsBinding
import com.entre.gethub.ui.adapter.HomeProjectBidsAdapter
import com.entre.gethub.ui.models.ProjectBid

class HomeCariProjectBidsActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHomeCariProjectBidsBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupRecyclerViewProjectBid()

        binding.iconBack.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerViewProjectBid() {
        binding.recyclerViewRekomendasiProjectBid.apply {
            layoutManager = LinearLayoutManager(
                this@HomeCariProjectBidsActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = HomeProjectBidsAdapter(createProjectBidList()) { projectBid, position ->
                // Handling item click event
                val intent = Intent(
                    this@HomeCariProjectBidsActivity,
                    HomeDetailProjectBidsActivity::class.java
                )
                intent.putExtra(
                    "project_bid",
                    projectBid
                ) // Passing ProjectBid object to detail activity
                startActivity(intent)
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
                "Dibutuhkan UI UX Designer yg memiliki jiwa seni untuk membuat layout Aplikasi sesuai dengan recruitment yg telah saya tentukan dengan thema Coffee Shop",
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