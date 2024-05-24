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
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.partners.GetHubPartner
import com.entre.gethub.databinding.FragmentGethubBinding
import com.entre.gethub.ui.adapter.GethubPartnerAdapter
import com.entre.gethub.ui.adapter.SponsorAdapter
import com.entre.gethub.utils.ViewModelFactory
import com.entre.gethub.utils.generateQR
import java.io.ByteArrayOutputStream

class GethubFragment : Fragment() {

    private var _binding: FragmentGethubBinding? = null
    private val binding get() = _binding!!
    private lateinit var sponsorAdapter: SponsorAdapter
    private lateinit var gethubViewModel: GethubViewModel

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
                        setupRecyclerViewGethubPartner(gethubPartnerList)
                    }

                    is Result.Error -> {
                        showLoadingOnPartnerList(false)
                    }

                    is Result.Empty -> {
                        binding.tvEmptyPartner.apply {
                            visibility = View.VISIBLE
                            text = result.emptyError
                        }
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
                Toast.makeText(
                    requireContext(),
                    "Clicked on actor: ${gethubpartner.fullName}",
                    Toast.LENGTH_SHORT
                ).show()
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
                    binding.empty.apply {
                        llEmpty.visibility = View.VISIBLE
                        tvEmpty.text = result.emptyError
                    }
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
            putExtra(Intent.EXTRA_TEXT, "Halo ges ini tes ya ")
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

    private fun showLoadingOnSponsor(isLoading: Boolean) {
        binding.progressBarOnGethubSponsor.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "GetHubFragment"
    }
}
