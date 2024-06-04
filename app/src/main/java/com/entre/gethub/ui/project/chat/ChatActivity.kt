package com.entre.gethub.ui.project.chat

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.databinding.ActivityChatBinding
import com.entre.gethub.ui.models.ImageMessage
import com.entre.gethub.ui.models.MessageType
import com.entre.gethub.ui.models.TextMessage
import com.entre.gethub.utils.FirestoreUtil
import com.entre.gethub.utils.StorageUtil
import com.entre.gethub.utils.ViewModelFactory
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import java.io.ByteArrayOutputStream
import java.util.Calendar

class ChatActivity : AppCompatActivity() {

    private val binding by lazy { ActivityChatBinding.inflate(layoutInflater) }
    private val chatViewModel by viewModels<ChatViewModel> { ViewModelFactory.getInstance(this) }
    private lateinit var messagesListenerRegistration: ListenerRegistration
    private lateinit var currentChannelId: String
    private var ownerId: String = ""
    private var freelancerId: String = ""
    private var chatroomId: String = ""
    private var senderName: String = ""
    private var senderPhoto: String = ""
    private var ownerName: String = ""
    private var ownerPhoto: String = ""
    private var shouldInitRecyclerView = true
    private lateinit var messagesSection: Section
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getUserProfile()

        ownerId = intent.getStringExtra(EXTRA_OWNER_ID).toString()
        freelancerId = intent.getStringExtra(EXTRA_FREELANCER_ID).toString()
        chatroomId = intent.getStringExtra(EXTRA_CHANNEL_ID).toString()
        ownerName = intent.getStringExtra(EXTRA_OWNER_NAME).toString()
        ownerPhoto = intent.getStringExtra(EXTRA_OWNER_PHOTO).toString()

        binding.iconBack.setOnClickListener {
            finish()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        FirestoreUtil.removeListener(messagesListenerRegistration)
        shouldInitRecyclerView = true
    }

    private fun getUserProfile() {
        chatViewModel.getUserProfile().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Success -> {
                        senderName = result.data.data.fullName.toString()
                        senderPhoto = result.data.data.photo.toString()
                        Log.d("ChatActivity", "senderName: $senderName, senderPhoto: $senderPhoto")
                        FirestoreUtil.getOrCreateChannel(
                            ownerUserId = ownerId,
                            freelancerId = freelancerId,
                            chatroomId = chatroomId,
                        ) { channelId ->
                            Log.d("ChatActivity", "Channelid: $channelId")
                            currentChannelId = channelId
                            messagesListenerRegistration =
                                FirestoreUtil.addChatMessageListener(
                                    channelId = channelId,
                                    context = this,
                                    senderId = freelancerId,
                                    senderName = senderName,
                                    senderPhoto = senderPhoto,
                                    ownerName = ownerName,
                                    ownerPhoto = ownerPhoto,
                                    this::updateRecyclerView
                                )

                            binding.cvSendChat.setOnClickListener {
                                val messageToSend = TextMessage(
                                    binding.etMessage.text.toString(),
                                    Calendar.getInstance().time,
                                    freelancerId,
                                    MessageType.TEXT
                                )
                                binding.etMessage.setText("")
                                FirestoreUtil.sendMessage(messageToSend, channelId)
                            }
                        }
                    }

                    else -> {
                        //
                    }
                }
            }
        }
    }

    private fun updateRecyclerView(messages: List<Item>) {
        fun init() {
            binding.rvChat.apply {
                adapter = GroupAdapter<GroupieViewHolder>().apply {
                    messagesSection = Section(messages)

                    this.add(messagesSection)
                }
                layoutManager =
                    LinearLayoutManager(this@ChatActivity)
            }
            shouldInitRecyclerView = false
        }

        fun updateItem() {
            messagesSection.update(messages)
        }

        if (shouldInitRecyclerView)
            init()
        else
            updateItem()

        binding.rvChat.scrollToPosition(binding.rvChat.adapter!!.itemCount - 1)
    }

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data?.data != null) {
                    currentImageUri = result.data?.data

                    val selectedImageBmp =
                        MediaStore.Images.Media.getBitmap(contentResolver, currentImageUri)
                    val outputStream = ByteArrayOutputStream()
                    selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                    val selectedImageBytes = outputStream.toByteArray()

                    StorageUtil.uploadMessageImage(freelancerId, selectedImageBytes) { imagePath ->
                        val messageToSend = ImageMessage(
                            imagePath,
                            Calendar.getInstance().time,
                            senderId = freelancerId,
                            MessageType.IMAGE
                        )
                        FirestoreUtil.sendMessage(messageToSend, currentChannelId)
                    }
                } else {
                    showToast(getString(R.string.image_unavailable))
                }
            }
        }

    private fun showImageIntent() {
        val pickPhotoIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImage.launch(pickPhotoIntent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_OWNER_ID = "extra_owner_id"
        const val EXTRA_FREELANCER_ID = "extra_freelancer_id"
        const val EXTRA_CHANNEL_ID = "extra_channel_id"
        const val EXTRA_OWNER_NAME = "extra_owner_name"
        const val EXTRA_OWNER_PHOTO = "extra_owner_photo"
    }
}