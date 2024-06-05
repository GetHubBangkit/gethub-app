package com.entre.gethub.ui.userpublicprofile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.PostCardViewersResponse
import com.entre.gethub.data.remote.response.UserPublicProfileResponse
import com.entre.gethub.databinding.ActivityUserPublicProfileBinding
import com.entre.gethub.ui.MainActivity
import com.entre.gethub.ui.adapter.HomeGethubLinkPublicUserAdapter
import com.entre.gethub.ui.adapter.HomeProdukJasaAdapter
import com.entre.gethub.ui.adapter.HomeProdukJasaPublicUserAdapter
import com.entre.gethub.ui.adapter.HomeProjectsPublicUserAdapter
import com.entre.gethub.ui.adapter.HomeSertifikasiAdapter
import com.entre.gethub.ui.adapter.HomeSertifikasiPublicUserAdapter
import com.entre.gethub.ui.models.GethubLink
import com.entre.gethub.utils.ViewModelFactory
import com.entre.gethub.utils.generateQR

class UserPublicProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserPublicProfileBinding
    private lateinit var userPublicProfileViewModel: UserPublicProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserPublicProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.iconBack.setOnClickListener {
            finish()
        }

        val viewModelFactory = ViewModelFactory.getInstance(applicationContext)
        userPublicProfileViewModel = ViewModelProvider(this, viewModelFactory).get(UserPublicProfileViewModel::class.java)

        val username = intent.getStringExtra("username") ?: ""
        setupPublicProfile(username)
        getLinks(username)
        getProductList(username)
        getCertificationList(username)
        getProjectsList(username)
        postCardViewers(username) // Call postCardViewers when the activity is created
    }

    private fun setupPublicProfile(username: String) {
        userPublicProfileViewModel.getPublicProfile(username).observe(this, Observer { userProfileResult ->
            when (userProfileResult) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val user = userProfileResult.data?.data
                    user?.let {
                        with(binding) {
                            cardBaseItem.apply {
                                tvGethubName.text = it.fullName
                                tvGethubProfession.text = it.profession
                            }
                        }
                    }
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, userProfileResult.error, Toast.LENGTH_SHORT).show()
                }
                else -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }



    private fun getLinks(username: String) {
        userPublicProfileViewModel.getLinks(username).observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    showLoadingOnLink(true)
                }
                is Result.Empty -> {
                    showLoadingOnLink(false)
                    showEmptyErrorOnLink(true, result.emptyError)
                }
                is Result.Success -> {
                    showLoadingOnLink(false)
                    result.data?.let { links ->
                        setupRecyclerViewHomeGethubLink(links)
                    }
                }
                is Result.Error -> {
                    showLoadingOnLink(false)
                    Toast.makeText(this, "${result.error}", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    showLoadingOnLink(false)
                    Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun getProductList(username: String) {
        userPublicProfileViewModel.getProducts(username).observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoadingOnProduct(true)
                is Result.Success -> {
                    showLoadingOnProduct(false)
                    result.data?.let { products ->
                        setupRecyclerViewHomeProdukJasa(products)
                    }
                }
                is Result.Empty -> {
                    showLoadingOnProduct(false)
                    binding.emptyOnProduct.apply {
                        llEmpty.visibility = View.VISIBLE
                        tvEmpty.text = result.emptyError
                    }
                }
                is Result.Error -> {
                    showLoadingOnProduct(false)
                    showToast(result.error)
                }
                else -> {
                    showLoadingOnProduct(false)
                    showToast("Terjadi kesalahan")
                }
            }
        }
    }

    private fun getCertificationList(username: String) {
        userPublicProfileViewModel.getCertifications(username).observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoadingOnCertification(true)
                is Result.Success -> {
                    showLoadingOnCertification(false)
                    result.data?.let { certifications ->
                        setupRecyclerViewHomeCertfication(certifications)
                    }
                }
                is Result.Empty -> {
                    showLoadingOnCertification(false)
                }
                is Result.Error -> {
                    showLoadingOnProduct(false)
                    showToast(result.error)
                }
                else -> {
                    showLoadingOnProduct(false)
                    showToast("Terjadi kesalahan")
                }
            }
        }
    }

    private fun getProjectsList(username: String) {
        userPublicProfileViewModel.getProjects(username).observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoadingOnCertification(true)
                is Result.Success -> {
                    showLoadingOnCertification(false)
                    result.data?.let { projects ->
                        setupRecyclerViewHomeProjects(projects)
                    }
                }
                is Result.Empty -> {
                    showLoadingOnProjects(false)
                }
                is Result.Error -> {
                    showLoadingOnProduct(false)
                    showToast(result.error)
                }
                else -> {
                    showLoadingOnProduct(false)
                    showToast("Terjadi kesalahan")
                }
            }
        }
    }

    private fun postCardViewers(username: String) {
        userPublicProfileViewModel.postCardViewers(username).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    // Handle loading state if needed
                }
                is Result.Success -> {
                    showToast("Card viewers has been recorded successfully")
                }
                is Result.Error -> {
                    showToast(result.error)
                }
                else -> {
                    showToast("An error occurred")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setupRecyclerViewHomeGethubLink(links: List<UserPublicProfileResponse.Data.Link>) {
        val adapter = HomeGethubLinkPublicUserAdapter(
            links.map {
                val drawableRes = when (it.category) {
                    "shopee" -> R.drawable.kelola_shopee
                    "tiktok" -> R.drawable.kelola_tiktok
                    else -> R.drawable.kelola_tiktok // Default image if no match
                }
                GethubLink(
                    it.id ?: "",
                    it.link ?: "",
                    drawableRes
                )
            }.toMutableList(), // Ensure it's a mutable list
            { gethublink, _ ->
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(gethublink.link)
                }
                startActivity(intent)
            }
        )
        binding.recyclerViewHomeGethubLink.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = adapter
        }
    }

    private fun setupRecyclerViewHomeProdukJasa(products: List<UserPublicProfileResponse.Data.Product>) {
        val adapter = HomeProdukJasaPublicUserAdapter(products.toMutableList()) { produkjasa, position ->
            // Handle product click
        }
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = adapter
        }
    }

    private fun setupRecyclerViewHomeProjects(projects: List<UserPublicProfileResponse.Data.Projects>) {
        val adapter = HomeProjectsPublicUserAdapter(projects.toMutableList()) { projects , position ->
            // Handle product click
        }
        binding.rvProjectDiselesaikan.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = adapter
        }
    }

    private fun setupRecyclerViewHomeCertfication(certifications: List<UserPublicProfileResponse.Data.Certifications>) {
        val adapter = HomeSertifikasiPublicUserAdapter(certifications.toMutableList()) { sertifikasi, position ->
            // Handle product click
        }
        binding.rvCertification.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = adapter
        }
    }

    private fun showLoadingOnProduct(isLoading: Boolean) {
        binding.progressBarOnProductList.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showLoadingOnProjects(isLoading: Boolean) {
        binding.progressBarProjectDiselesaikan.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showLoadingOnCertification(isLoading: Boolean) {
        binding.progressBaronCertification.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showLoadingOnLink(isLoading: Boolean) {
        binding.progressBarLink.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyOnCertification(isLoading: Boolean) {
        binding.tvEmptyCertification.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showEmptyErrorOnLink(isError: Boolean, message: String) {
        binding.tvEmptyLink.text = message
        binding.tvEmptyLink.visibility = if (isError) View.VISIBLE else View.GONE
    }
}
