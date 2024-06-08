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
import com.entre.gethub.ui.akun.membership.MembershipActivity
import com.entre.gethub.ui.akun.paymenthistory.PaymentHistoryActivity
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
            framePremium.setOnClickListener {
                startActivity(Intent(requireContext(), MembershipActivity::class.java))
            }

            framesettingkeluar.setOnClickListener {
                showDialog(requireContext(), "Keluar", "Apakah Anda yakin ingin keluar?")
            }

            framePaymentHistory.setOnClickListener {
                startActivity(Intent(requireContext(), PaymentHistoryActivity::class.java))
            }
        }
    }

    private fun setupVisibilitySwitch() {
        // Get the SharedPreferences instance
        val sharedPreferences = requireContext().getSharedPreferences("visibility", Context.MODE_PRIVATE)

        // Get the last saved visibility state, default is false
        val lastVisibilityState = sharedPreferences.getBoolean("visibility_state", false)

        // Set the initial state of the switch based on the last saved state
        binding.switchVisibilty.isChecked = lastVisibilityState

        // Boolean to track if the switch is changed by the user
        var userChangedSwitch = false

        // Observe the visibility state first
        akunViewModel.getVisibility().observe(viewLifecycleOwner) { result ->
            when {
                // If the switch is changed by the user, update the switch state
                userChangedSwitch -> {
                    when (result) {
                        is Result.Success -> {
                            showLoading(false)
                            binding.switchVisibilty.isChecked = result.data
                            // Reset the flag after updating the switch state
                            userChangedSwitch = false
                        }

                        is Result.Error -> {
                            showLoading(false)
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
                // If the switch is not changed by the user, only update the switch state
                else -> {
                    when (result) {
                        is Result.Success -> {
                            showLoading(false)
                            binding.switchVisibilty.isChecked = result.data
                        }

                        is Result.Error -> {
                            showLoading(false)
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
        }

        // Set the listener for the switch
        binding.switchVisibilty.setOnCheckedChangeListener { _, isChecked ->
            // Set the flag to true to indicate user interaction
            userChangedSwitch = true
            // Save the state of switchVisibility locally
            sharedPreferences.edit().putBoolean("visibility_state", isChecked).apply()
            // Post setVisibility only when the switch is checked/unchecked
            akunViewModel.setVisibility(isChecked)
                .observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Result.Success -> {
                            showLoading(false)
                            showToast("Visibility updated successfully")
                        }

                        is Result.Error -> {
                            showLoading(false)
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
    }






    private fun getUserData() {
        akunViewModel.getUserProfile().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    val user = result.data.data
                    showLoading(false)
                    with(binding) {
                        Glide.with(requireContext())
                            .load(user?.photo)
                            .placeholder(R.drawable.profilepic1)
                            .into(ivUserProfilePicture)
                        tvUserFullname.text = user?.fullName
                        tvUserProfession.text = user?.profession
                        if (user.isPremium == true) {
                            binding.llPremium.visibility = View.VISIBLE
                            tvExpiredDate.text = "Expired ${user.premiumExpiredDate}"
                        }
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
