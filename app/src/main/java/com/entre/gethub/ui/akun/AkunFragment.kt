package com.entre.gethub.ui.akun

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.databinding.FragmentAkunBinding
import com.entre.gethub.ui.auth.LoginActivity
import com.entre.gethub.utils.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AkunFragment : Fragment() {

    private var _binding: FragmentAkunBinding? = null
    private val binding get() = _binding!!
    private lateinit var akunViewModel: AkunViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAkunBinding.inflate(inflater, container, false)
        val root: View = binding.root

        akunViewModel.getToken().observe(viewLifecycleOwner) { token ->
            Log.d(TAG, "Token: $token")
        }

        setupView()
        setupVisibilitySwitch()
        getUserData()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        with(binding) {
            framesettingkeluar.setOnClickListener {
                showDialog(requireContext(), "Keluar", "Apakah Anda yakin ingin keluar?")
            }
        }
    }

    private fun setupVisibilitySwitch() {
        binding.switchVisibilty.setOnCheckedChangeListener { _, isChecked ->
            akunViewModel.setVisibility(isChecked).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Success -> {
                        showToast("Visibility updated successfully")
                    }
                    is Result.Error -> {
                        showToast(result.error)
                    }
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    else -> {
                        showLoading(false)
                    }
                }
            }
        }

        akunViewModel.getVisibility().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    binding.switchVisibilty.isChecked = result.data
                }
                is Result.Error -> {
                    showToast(result.error)
                }
                is Result.Loading -> {
                    showLoading(true)
                }
                else -> {
                    showLoading(false)
                }
            }
        }
    }

    private fun getUserData() {
        akunViewModel.getUserProfile().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    val user = result.data.data
                    showLoading(false)
                    println("Photo URL: ${user?.photo}")
                    with(binding) {
                        Glide.with(requireContext())
                            .load(user?.photo)
                            .placeholder(R.drawable.profilepic1)
                            .into(ivUserProfilePicture)
                        tvUserFullname.text = user?.fullName
                        tvUserProfession.text = user?.profession
                    }
                }
                is Result.Error -> {
                    showLoading(false)
                    showToast(result.error)
                }
                else -> {
                    showLoading(false)
                    showToast(getString(R.string.something_went_wrong))
                }
            }
        }
    }

    private fun initViewModel() {
        val factory = ViewModelFactory.getInstance(requireContext())
        akunViewModel = ViewModelProvider(requireActivity(), factory)[AkunViewModel::class.java]
    }

    private fun showDialog(context: Context, title: String, message: String) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Logout") { _, _ ->
                logout()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .setOnDismissListener {
                it.dismiss()
            }
            .show()
    }

    private fun logout() {
        akunViewModel.logout().apply {
            navigateToLoginActivity()
        }
    }

    private fun navigateToLoginActivity() {
        startActivity(
            Intent(requireContext(), LoginActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    private fun navigateToActivity(activity: AppCompatActivity) {
        startActivity(Intent(requireContext(), activity::class.java))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "AkunFragment"
    }
}
