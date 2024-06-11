package com.entre.gethub.ui.gethub

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.partners.GetHubPartner
import com.entre.gethub.databinding.FragmentGethubBinding
import com.entre.gethub.ui.adapter.GethubPartnerAdapter
import com.entre.gethub.ui.adapter.SponsorAdapter
import com.entre.gethub.ui.detailpartner.DetailPartnerActivity
import com.entre.gethub.utils.ViewModelFactory
import com.entre.gethub.utils.generateQR
import java.io.ByteArrayOutputStream

class GethubFragment : Fragment() {

    private var _binding: FragmentGethubBinding? = null
    private val binding get() = _binding!!
    private lateinit var sponsorAdapter: SponsorAdapter
    private lateinit var gethubViewModel: GethubViewModel
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGethubBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupGethubCard()
        setupRecyclerView()
        getPartnerList()
        setupView()

        return root
    }

    override fun onStart() {
        super.onStart()
        getPartnerList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViewModel() {
        val factory = ViewModelFactory.getInstance(requireContext())
        gethubViewModel = ViewModelProvider(requireActivity(), factory)[GethubViewModel::class.java]
    }

    private fun setupGethubCard() {
        gethubViewModel.getUserProfile().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoadingOnCard(true)
                    is Result.Success -> {
                        showLoadingOnCard(false)
                        val user = result.data.data
                        username = user?.username // Mendapatkan username
                        with(binding) {
                            cardBaseItem.apply {
                                tvGethubName.text = user?.fullName
                                tvGethubProfession.text = user?.profession
                                tvGethubEmail.text = user?.email
                                tvGethubAddress.text = user?.address
                                tvGethubPhone.text = user?.phone
                                tvGethubWebsite.text = user?.web
                            }
                        }
                    }

                    is Result.Error -> {
                        showLoadingOnCard(false)
                        showToast(result.error)
                    }

                    else -> {
                        showLoadingOnCard(false)
                        showToast("Terjadi kesalahan")
                    }
                }
            }
        }
    }

    private fun getPartnerList() {
        gethubViewModel.getPartnerList().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoadingOnPartnerList(true)
                    is Result.Success -> {
                        val gethubPartnerList = result.data.data
                        showLoadingOnPartnerList(false)
                        binding.clEmptyGethubPartner.visibility = View.GONE
                        setupRecyclerViewGethubPartner(gethubPartnerList)
                    }

                    is Result.Error -> {
                        showLoadingOnPartnerList(false)
                    }

                    is Result.Empty -> {
                        showEmptyErrorOnGethubPartner(true, result.emptyError)
                    }

                    else -> {
                        showLoadingOnPartnerList(false)
                    }
                }
            }
        }
    }

    private fun setupRecyclerViewGethubPartner(gethubPartnerList: List<GetHubPartner>) {
        binding.rvGethubPartner.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = GethubPartnerAdapter(gethubPartnerList) { gethubpartner, position ->
                gethubpartner.id?.let { partnerId ->
                    navigateToDetailPartner(partnerId)
                } ?: run {
                    showToast("Partner ID is missing")
                }
            }
        }
    }


    private fun showLoadingOnPartnerList(isLoading: Boolean) {
        binding.progressBarOnGethubPartnerList.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupRecyclerView() {
        sponsorAdapter = SponsorAdapter(emptyList()) { sponsor, _ ->
            sponsor.link?.let { openLink(it) }
        }
        binding.rvGethubSponsor.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = sponsorAdapter
        }

        // Observe sponsor data from ViewModel
        gethubViewModel.sponsors.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> showLoadingOnSponsor(true)

                is Result.Success -> {
                    showLoadingOnSponsor(false)
                    val sponsorData = result.data.data ?: emptyList()
                    sponsorAdapter.updateData(sponsorData)
                }

                is Result.Error -> {
                    showLoadingOnSponsor(false)
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT)
                        .show()
                }

                is Result.Empty -> {
                    showLoadingOnSponsor(false)
                    showEmptyErrorOnGethubSponsor(true, result.emptyError)
                }

                else -> {
                    // Handle other cases, like Result.Loading
                }
            }
        }

        // Fetch sponsors
        gethubViewModel.getSponsors()
    }

    private fun openLink(link: String) {
        val uri = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun setupView() {
        gethubViewModel.getUserQRCode().observe(viewLifecycleOwner) { qrContent ->
            Log.d(TAG, "QR Code: $qrContent")
            generateQRCode(qrContent)
        }

        binding.ivShare.setOnClickListener {
            shareCard()
        }

        binding.tvSeeAllPartner.setOnClickListener {
            startActivity(Intent(requireActivity(), GethubPartnerListActivity::class.java))
        }

        binding.ivAddPartner.setOnClickListener {
            startActivity(Intent(requireActivity(), GethubAddPartnerActivity::class.java))
        }

    }

    private fun generateQRCode(content: String) {
        generateQR(binding.cardBaseItem.qrCode, content)
    }

    private fun shareCard() {
        val bitmap = createBitmapFromView(binding.cardBaseItem.root)
        val uri = getImageUri(bitmap)

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_TEXT, "https://gethub-webporto-kot54pmj3q-et.a.run.app/$username") // Menambahkan username ke URL
            type = "image/jpeg"
        }

        startActivity(Intent.createChooser(shareIntent, "Share Card"))
    }

    private fun createBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun getImageUri(image: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            requireContext().contentResolver,
            image,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    private fun showLoadingOnCard(isLoading: Boolean) {
        binding.progressBarOnCard.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyErrorOnGethubPartner(isError: Boolean, message: String) {
        binding.clEmptyGethubPartner.visibility = if (isError) View.VISIBLE else View.GONE
    }
    private fun showEmptyErrorOnGethubSponsor(isError: Boolean, message: String) {
        binding.clEmptyGethubSponsor.visibility = if (isError) View.VISIBLE else View.GONE
    }

    private fun showLoadingOnSponsor(isLoading: Boolean) {
        binding.progressBarOnGethubSponsor.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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


    companion object {
        const val TAG = "GetHubFragment"
    }
}

//    private fun setupConstraintLayout() {
//        val clEmptyGethubPartner = binding.clEmptyGethubPartner
//        val gethubSponsor = binding.GethubSponsor
//
//        // Menentukan constraint berdasarkan visibilitas clEmptyGethubPartner
//        if (clEmptyGethubPartner.visibility == View.VISIBLE) {
//            val params = gethubSponsor.layoutParams as ConstraintLayout.LayoutParams
//            params.topToBottom = R.id.clEmptyGethubPartner
//            gethubSponsor.layoutParams = params
//        } else {
//            val params = gethubSponsor.layoutParams as ConstraintLayout.LayoutParams
//            params.topToBottom = R.id.rvGethubPartner
//            gethubSponsor.layoutParams = params
//        }
//    }

