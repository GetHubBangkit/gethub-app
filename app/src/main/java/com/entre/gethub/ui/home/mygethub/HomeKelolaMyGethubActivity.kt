package com.entre.gethub.ui.home.mygethub

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.ui.MainActivity
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.products.Product
import com.entre.gethub.databinding.ActivityHomeKelolaMyGetHubBinding
import com.entre.gethub.ui.adapter.HomeGethubLinkAdapter
import com.entre.gethub.ui.adapter.HomeProdukJasaAdapter
import com.entre.gethub.ui.home.mygethub.link.HomeKelolaMyGethubTambahLinkActivity
import com.entre.gethub.ui.home.mygethub.product.HomeKelolaMyGethubEditProdukActivity
import com.entre.gethub.ui.home.mygethub.product.HomeKelolaMyGethubTambahProdukActivity
import com.entre.gethub.ui.models.GethubLink
import com.entre.gethub.utils.ViewModelFactory

class HomeKelolaMyGethubActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHomeKelolaMyGetHubBinding.inflate(layoutInflater) }

    private lateinit var homeKelolaMyGetHubViewModel: HomeKelolaMyGethubViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViewModel()
        getLinkList()
        getProductList()

        setupRecyclerViewHomeGethubLink()

        binding.iconBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.gantithema.setOnClickListener {
            startActivity(Intent(this, HomeKelolaMyGethubGantiDesignActivity::class.java))
        }

        binding.editgethublink.setOnClickListener {
            startActivity(Intent(this, HomeKelolaMyGethubTambahLinkActivity::class.java))
        }

        binding.editprodukjasa.setOnClickListener {
            startActivity(Intent(this, HomeKelolaMyGethubTambahProdukActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        getProductList()
        getLinkList()
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
                        showToast(result.error)
                    }

                    is Result.Empty -> {
                        showLoadingOnProduct(false)
                        binding.emptyOnProduct.apply {
                            llEmpty.visibility = View.VISIBLE
                            tvEmpty.text = result.emptyError
                        }
                    }

                    else -> {
                        showLoadingOnProduct(false)
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
                homeKelolaMyGetHubViewModel.deleteLink(linkId)
                    .observe(this@HomeKelolaMyGethubActivity) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> showLoadingOnLink(true)
                                is Result.Success -> {
                                    showLoadingOnLink(false)
                                    showToast(result.data.message.toString())
                                    getLinkList()
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
        )
        binding.recyclerViewHomeGethubLink.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = adapter
        }
    }

    private fun setupRecyclerViewHomeProdukJasa(listProdukJasa: List<Product>) {
        val adapter = HomeProdukJasaAdapter(listProdukJasa) { produkjasa, position ->
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

    private fun deleteProduct(id: String) {
        homeKelolaMyGetHubViewModel.deleteProduct(id).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoadingOnProduct(true)
                    is Result.Success -> {
                        showLoadingOnProduct(false)
                        showToast(result.data.message.toString())
                        getProductList()
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
                            val drawableRes = when (it.category) {
                                "Shopee" -> R.drawable.kelola_shopee
                                "Tiktok" -> R.drawable.kelola_tiktok
                                else -> R.drawable.kelola_tiktok // Default image if no match
                            }
                            GethubLink(
                                it.id ?: "",
                                it.link ?: "",
                                drawableRes
                            ) // Handle nullability
                        })
                }

                is Result.Error -> {
                    Toast.makeText(
                        this,
                        "Error fetching links: ${result.error}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
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

    private fun showLoadingOnProduct(isLoading: Boolean) {
        binding.progressBarOnProductList.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showLoadingOnLink(isLoading: Boolean) {
        binding.progressBarLink.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyErrorOnLink(isError: Boolean, message: String) {
        binding.tvEmptyLink.text = message
        binding.tvEmptyLink.visibility = if (isError) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}