    package com.entre.gethub.ui.detailpartner

    import android.net.Uri
    import android.os.Bundle
    import android.view.View
    import android.widget.Toast
    import androidx.activity.viewModels
    import androidx.appcompat.app.AppCompatActivity
    import com.bumptech.glide.Glide
    import com.entre.gethub.R
    import com.entre.gethub.data.Result
    import com.entre.gethub.databinding.ActivityPartnerDetailBinding
    import com.entre.gethub.utils.ViewModelFactory
    import com.google.android.material.textfield.TextInputLayout

    class DetailPartnerActivity : AppCompatActivity() {
        private val binding: ActivityPartnerDetailBinding by lazy {
            ActivityPartnerDetailBinding.inflate(layoutInflater)
        }
        private val detailPartnerViewModel by viewModels<DetailPartnerViewModel> {
            ViewModelFactory.getInstance(this)
        }
        private var currentImageUri: Uri? = null
        private var imageUrl: String? = null

        private lateinit var edFullname: TextInputLayout
        private lateinit var edProfession: TextInputLayout
        private lateinit var edPhone: TextInputLayout
        private lateinit var edEmail: TextInputLayout
        private lateinit var edWebsite: TextInputLayout
        private lateinit var edAddress: TextInputLayout
        private lateinit var edAbout: TextInputLayout

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(binding.root)

            edFullname = binding.nameTextField
            edProfession = binding.profesiTextField
            edPhone = binding.phoneTextField
            edEmail = binding.emailTextField
            edWebsite = binding.websiteTextField
            edAddress = binding.alamatTextField

            // Set EditText fields to not editable
            disableEditing(edFullname)
            disableEditing(edProfession)
            disableEditing(edPhone)
            disableEditing(edEmail)
            disableEditing(edWebsite)
            disableEditing(edAddress)

            val partnerId = intent.getStringExtra("PARTNER_ID")
            if (partnerId != null) {
                setupView(partnerId)
            } else {
                showToast("Partner ID is missing")
            }
        }

        private fun disableEditing(textInputLayout: TextInputLayout) {
            textInputLayout.editText?.apply {
                isFocusable = false
                isFocusableInTouchMode = false
                isClickable = false
            }
        }

        private fun setupView(partnerId: String) {
            // Observe the user profile data
            detailPartnerViewModel.getPartnerList().observe(this@DetailPartnerActivity) { result ->
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        val partnerList = result.data.data
                        val partner = partnerList.find { it.id == partnerId }
                        if (partner != null) {
                            edFullname.editText?.setText(partner.fullName ?: "")
                            edProfession.editText?.setText(partner.profession ?: "")
                            edPhone.editText?.setText(partner.phone ?: "")
                            edEmail.editText?.setText(partner.email ?: "")
                            edWebsite.editText?.setText(partner.website ?: "")
                            edAddress.editText?.setText(partner.address ?: "")

                            // Load the profile picture using Glide
                            partner.photo?.let { imageUrl ->
                                Glide.with(this@DetailPartnerActivity)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.profilepic1) // Add a placeholder image
                                    .into(binding.ivProfilePic)
                            }
                        } else {
                            showToast("No partner data available for the given ID")
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

        private fun showLoading(isLoading: Boolean) {
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        private fun showToast(message: String) {
            Toast.makeText(this@DetailPartnerActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
