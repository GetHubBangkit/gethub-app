package com.entre.gethub.ui.home.mygethub

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.entre.gethub.ui.MainActivity
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.Category
import com.entre.gethub.data.remote.response.certifications.Certification
import com.entre.gethub.data.remote.response.products.Product
import com.entre.gethub.databinding.ActivityHomeKelolaMyGetHubBinding
import com.entre.gethub.ui.adapter.HomeGethubLinkAdapter
import com.entre.gethub.ui.adapter.HomeProdukJasaAdapter
import com.entre.gethub.ui.adapter.HomeSertifikasiAdapter
import com.entre.gethub.ui.akun.AkunViewModel
import com.entre.gethub.ui.home.mygethub.certification.HomeKelolaMyGethubEditSertifikasiActivity
import com.entre.gethub.ui.home.mygethub.certification.HomeKelolaMyGethubTambahSertifikasiActivity
import com.entre.gethub.ui.home.mygethub.link.HomeKelolaMyGethubTambahLinkActivity
import com.entre.gethub.ui.home.mygethub.product.HomeKelolaMyGethubEditProdukActivity
import com.entre.gethub.ui.home.mygethub.product.HomeKelolaMyGethubTambahProdukActivity
import com.entre.gethub.ui.home.mygethub.scanktp.HomeKelolaMyGethubScanKTPActivity
import com.entre.gethub.ui.home.mygethub.scanktp.HomeKelolaMyGethubScanKTPMenungguVerifikasiActivity
import com.entre.gethub.ui.home.mygethub.scanktp.HomeKelolaMyGethubScanKTPTerverifikasiActivity
import com.entre.gethub.ui.home.mygethub.tentangsaya.HomeKelolaMyGethubEditTentangSayaActivity
import com.entre.gethub.ui.home.mygethub.tentangsaya.HomeKelolaMyGethubEditTentangSayaViewModel
import com.entre.gethub.ui.models.GethubLink
import com.entre.gethub.utils.ViewModelFactory

class HomeKelolaMyGethubActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHomeKelolaMyGetHubBinding.inflate(layoutInflater) }
    private lateinit var homeKelolaMyGetHubViewModel: HomeKelolaMyGethubViewModel

    // Declare categories variable
    private var categories: List<Category>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViewModel()

        getUserData()
        getLinkList()
        getCategories()
        getProductList()
        getCertificationList()

        setupRecyclerViewHomeGethubLink()

        binding.iconBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.framegantitema.setOnClickListener {
            startActivity(Intent(this, HomeKelolaMyGethubGantiDesignActivity::class.java))
        }

        binding.edittentangsaya.setOnClickListener {
            startActivity(Intent(this, HomeKelolaMyGethubEditTentangSayaActivity::class.java))
        }
        binding.editgethublink.setOnClickListener {
            startActivity(Intent(this, HomeKelolaMyGethubTambahLinkActivity::class.java))
        }

        binding.editprodukjasa.setOnClickListener {
            startActivity(Intent(this, HomeKelolaMyGethubTambahProdukActivity::class.java))
        }
        binding.editSertifikasi.setOnClickListener {
            startActivity(Intent(this, HomeKelolaMyGethubTambahSertifikasiActivity::class.java))
        }
        binding.framektp.setOnClickListener {
            homeKelolaMyGetHubViewModel.getUserProfile().observe(this) { result ->
                if (result is Result.Success) {
                    val user = result.data.data
                    user?.let {
                        when {
                            it.isVerifKtp == true -> {
                                startActivity(Intent(this, HomeKelolaMyGethubScanKTPTerverifikasiActivity::class.java))
                            }
                            it.isVerifKtp == false && it.isVerifKtpUrl == null -> {
                                startActivity(Intent(this, HomeKelolaMyGethubScanKTPActivity::class.java))
                            }
                            it.isVerifKtp == false && it.isVerifKtpUrl != null -> {
                                startActivity(Intent(this, HomeKelolaMyGethubScanKTPMenungguVerifikasiActivity::class.java))
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getProductList()
        getLinkList()
        getCertificationList()
    }
    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
    }



    private fun getUserData() {
        homeKelolaMyGetHubViewModel.getUserProfile().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoadingOnAbout(true)
                    is Result.Success -> {
                        val user = result.data.data
                        showLoadingOnAbout(false)
                        binding.tvAbout.text = user?.about as? String ?: ""

                        // Check the value of isVerifKtp
                        user?.isVerifKtp?.let { isVerifKtp ->
                            user?.isVerifKtpUrl?.let { isVerifKtpUrl ->
                                if (isVerifKtp) {
                                    // Jika isVerifKtp true
                                    binding.framektp.setOnClickListener {
                                        startActivity(
                                            Intent(
                                                this,
                                                HomeKelolaMyGethubScanKTPTerverifikasiActivity::class.java
                                            )
                                        )
                                    }
                                } else if (isVerifKtp == false && isVerifKtpUrl == null) {
                                    // Jika isVerifKtp false dan isVerifKtpUrl null
                                    binding.framektp.setOnClickListener {
                                        startActivity(
                                            Intent(
                                                this,
                                                HomeKelolaMyGethubScanKTPActivity::class.java
                                            )
                                        )
                                    }
                                } else if (isVerifKtp == false && isVerifKtpUrl != null) {
                                    // Jika isVerifKtp false dan isVerifKtpUrl tidak null
                                    binding.framektp.setOnClickListener {
                                        startActivity(
                                            Intent(
                                                this,
                                                HomeKelolaMyGethubScanKTPMenungguVerifikasiActivity::class.java
                                            )
                                        )
                                    }
                                }
                            } ?: run {
                                if (isVerifKtp == false) {
                                    // Jika isVerifKtp false dan isVerifKtpUrl null
                                    binding.framektp.setOnClickListener {
                                        startActivity(
                                            Intent(
                                                this,
                                                HomeKelolaMyGethubScanKTPActivity::class.java
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    is Result.Empty -> {
                        showLoadingOnCertification(false)
                        showEmptyErrorOnAbout(true, result.emptyError)
                    }
                    is Result.Error -> {
                        showLoadingOnAbout(false)
                        // showToast(result.error)
                    }
                    else -> {
                        showLoadingOnAbout(false)
                        showToast(getString(R.string.something_went_wrong))
                    }
                }
            }
        }
    }



    private fun initViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        homeKelolaMyGetHubViewModel =
            ViewModelProvider(this, factory)[HomeKelolaMyGethubViewModel::class.java]
    }

    private fun getProductList() {
        homeKelolaMyGetHubViewModel.getProductList().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoadingOnProduct(true)
                    is Result.Success -> {
                        showLoadingOnProduct(false)
                        setupRecyclerViewHomeProdukJasa(result.data.data)
                    }
                    is Result.Error -> {
                        showLoadingOnProduct(false)
//                        showToast(result.error)
                    }
                    is Result.Empty -> {
                        showLoadingOnProduct(false)
                        showEmptyErrorOnProduct(true, result.emptyError)
                    }
                    else -> {
                        showLoadingOnProduct(false)
                        showToast("Terjadi kesalahan")
                    }
                }
            }
        }
    }

    private fun getCategories() {
        homeKelolaMyGetHubViewModel.getCategories().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> { /* Show loading indicator if needed */ }
                    is Result.Success -> {
                        categories = result.data.data
                    }
                    is Result.Error -> {
//                        showToast(result.error)
                    }
                    else -> {
                        showToast("Terjadi kesalahan")
                    }
                }
            }
        }
    }

    private fun getCertificationList() {
        homeKelolaMyGetHubViewModel.getCertificationList().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoadingOnCertification(true)
                    is Result.Success -> {
                        showLoadingOnCertification(false)
                        setupRecyclerViewHomeSertifikasi(result.data.data.toMutableList())
                    }
                    is Result.Error -> {
                        showLoadingOnCertification(false)
//                        showToast(result.error)
                    }
                    is Result.Empty -> {
                        showLoadingOnCertification(false)
                        showEmptyErrorOnCertification(true, result.emptyError)
                    }
                    else -> {
                        showLoadingOnCertification(false)
                        showToast("Terjadi kesalahan")
                    }
                }
            }
        }
    }


    private fun setupRecyclerViewHomeGethubLink() {
        val adapter = HomeGethubLinkAdapter(
            arrayListOf(),
            { gethublink, _ ->
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(gethublink.link)
                }
                startActivity(intent)
            },
            { linkId ->
                deleteLink(linkId)
            }
        )
        binding.recyclerViewHomeGethubLink.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = adapter
        }
    }

    private fun setupRecyclerViewHomeProdukJasa(listProdukJasa: List<Product>) {
        val adapter = HomeProdukJasaAdapter(listProdukJasa.toMutableList()) { produkjasa, position ->
            val intent = Intent(
                this@HomeKelolaMyGethubActivity,
                HomeKelolaMyGethubEditProdukActivity::class.java
            )
            intent.putExtra(HomeKelolaMyGethubEditProdukActivity.EXTRA_PRODUCT_ID, produkjasa.id)
            startActivity(intent)
        }
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = adapter
            adapter.setOnItemClickCallback(object : HomeProdukJasaAdapter.OnItemClickCallback {
                override fun onDeleteProductItem(product: Product, position: Int) {
                    deleteProduct(product.id.toString())
                }
            })
        }
    }



    private fun setupRecyclerViewHomeSertifikasi(listSertifikasi: MutableList<Certification>) {
        val adapter = HomeSertifikasiAdapter(listSertifikasi, categories ?: listOf()) { sertifikasi, position ->
            val intent = Intent(
                this@HomeKelolaMyGethubActivity,
                HomeKelolaMyGethubEditSertifikasiActivity::class.java
            )
            intent.putExtra(HomeKelolaMyGethubEditSertifikasiActivity.EXTRA_CERTIFICATION_ID, sertifikasi.id)
            startActivity(intent)
        }
        binding.rvCertification.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = adapter
            adapter.setOnItemClickCallback(object : HomeSertifikasiAdapter.OnItemClickCallback {
                override fun onDeleteCertificationItem(certification: Certification, position: Int) {
                    deleteCertification(certification.id.toString())
                }
            })
        }
    }


    private fun deleteProduct(id: String) {
        homeKelolaMyGetHubViewModel.deleteProduct(id).observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoadingOnProduct(true)
                is Result.Success -> {
                    showLoadingOnProduct(false)
                    showToast(result.data.message.toString())
                    // Memanggil fungsi removeProduk() di adapter
                    val adapter = binding.rvProducts.adapter as? HomeProdukJasaAdapter
                    adapter?.removeProduk(id)
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


    private fun deleteLink(linkId: String) {
        homeKelolaMyGetHubViewModel.deleteLink(linkId)
            .observe(this@HomeKelolaMyGethubActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> showLoadingOnLink(true)
                        is Result.Success -> {
                            showLoadingOnLink(false)
                            showToast(result.data.message.toString())
                            // Hapus item terakhir dari daftar link yang dipegang oleh adapter
                            val adapter = binding.recyclerViewHomeGethubLink.adapter as? HomeGethubLinkAdapter
                            adapter?.removeGethubLink(linkId)

                            // Periksa apakah daftar link menjadi kosong setelah menghapus item terakhir
                            if (adapter?.itemCount == 0) {
                                showEmptyErrorOnLink(true, "Daftar link kosong")
                            }
                        }
                        is Result.Error -> {
                            showLoadingOnLink(false)
                            showToast(result.error)
                        }
                        else -> {
                            //
                        }
                    }
                }
            }
    }


    private fun deleteCertification(id: String) {
        homeKelolaMyGetHubViewModel.deleteCertification(id).observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoadingOnProduct(true)
                is Result.Success -> {
                    showLoadingOnProduct(false)
                    showToast(result.data.message.toString())
                    // Memanggil fungsi removeSertifikasi() di adapter
                    val adapter = binding.rvCertification.adapter as? HomeSertifikasiAdapter
                    adapter?.removeSertifikasi(id)
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


    private fun getLinkList() {
        homeKelolaMyGetHubViewModel.getLinks().observe(this, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    // Show loading indicator if needed
                }
                is Result.Success -> {
                    // Update the RecyclerView with new data
                    (binding.recyclerViewHomeGethubLink.adapter as? HomeGethubLinkAdapter)?.updateGethubLinks(
                        result.data.data!!.map {
                            val image = when (it.category?.toLowerCase()) {
                                "shopee" -> "https://storage.googleapis.com/gethub_bucket/link_category/logo%202/shopee2.png"
                                "tiktok" -> "https://storage.googleapis.com/gethub_bucket/link_category/logo%202/tiktok2.png"
                                "figma" -> "https://storage.googleapis.com/gethub_bucket/link_category/logo%202/figma2.png"
                                "github" -> "https://storage.googleapis.com/gethub_bucket/link_category/logo%202/github2.png"
                                "gitlab" -> "https://storage.googleapis.com/gethub_bucket/link_category/logo%202/gitlab2.png"
                                "instagram" -> "https://storage.googleapis.com/gethub_bucket/link_category/logo%202/instagram2.png"
                                "jupyternotebook" -> "https://storage.googleapis.com/gethub_bucket/link_category/logo%202/jupyternotebook2.png"
                                "linkedin" -> "https://storage.googleapis.com/gethub_bucket/link_category/logo%202/linkedin2.png"
                                "tokopedia" -> "https://storage.googleapis.com/gethub_bucket/link_category/logo%202/tokopedia2.png"
                                "youtube" -> "https://storage.googleapis.com/gethub_bucket/link_category/logo%202/youtube2.png"
                                else -> "https://storage.googleapis.com/gethub_bucket/link_category/logo%202/tiktok2.png" // Default image if no match
                            }

                            GethubLink(
                                it.id ?: "",
                                it.link ?: "",
                                    image
                            )
                        }
                    )
                }

                is Result.Error -> {
//                    Toast.makeText(
//                        this,
//                        "${result.error}",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    showEmptyErrorOnLink(true, result.error)
                }
                is Result.Empty -> {
                    showEmptyErrorOnLink(true, result.emptyError)
                }
                else -> {
                    // Handle other possible states
                }
            }
        })
    }

    private fun showLoadingOnAbout(isLoading: Boolean) {
        binding.progressBarAbout.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showLoadingOnProduct(isLoading: Boolean) {
        binding.progressBarOnProductList.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showLoadingOnCertification(isLoading: Boolean) {
        binding.progressBarCertification.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showLoadingOnLink(isLoading: Boolean) {
        binding.progressBarLink.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyErrorOnAbout(isError: Boolean, message: String) {
        binding.tvEmptyAbout.text = message
        binding.tvEmptyAbout.visibility = if (isError) View.VISIBLE else View.GONE
    }
    private fun showEmptyErrorOnLink(isError: Boolean, message: String) {
        binding.clEmptyGethubLink.visibility = if (isError) View.VISIBLE else View.GONE
    }
    private fun showEmptyErrorOnProduct(isError: Boolean, message: String) {
        binding.clEmptyProdukJasa.visibility = if (isError) View.VISIBLE else View.GONE
    }

    private fun showEmptyErrorOnCertification(isError: Boolean, message: String) {
        binding.clEmptySertifikasi.visibility = if (isError) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
