package com.entre.gethub.ui.project.chat

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.R
import com.entre.gethub.databinding.ActivityChatBinding
import com.entre.gethub.ui.models.MessageType
import com.entre.gethub.ui.models.TextMessage
import com.entre.gethub.utils.FirestoreUtil
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import java.util.Calendar

class ChatActivity : AppCompatActivity() {

    private val binding by lazy { ActivityChatBinding.inflate(layoutInflater) }
    private lateinit var messagesListenerRegistration: ListenerRegistration
    private lateinit var currentChannelId: String
    private var ownerId: String = ""
    private var freelancerId: String = ""
    private var chatroomId: String = ""
    private var shouldInitRecyclerView = true
    private lateinit var messagesSection: Section

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        ownerId = intent.getStringExtra(EXTRA_OWNER_ID).toString()
        freelancerId = intent.getStringExtra(EXTRA_FREELANCER_ID).toString()
        chatroomId = intent.getStringExtra(EXTRA_CHANNEL_ID).toString()

        binding.iconBack.setOnClickListener {
            finish()
        }

        FirestoreUtil.getOrCreateChannel(
            ownerUserId = ownerId,
            freelancerId = freelancerId,
            chatroomId = chatroomId,
        ) { channelId ->
            Log.d("ChatActivity", "Channelid: $channelId")
            currentChannelId = channelId
            messagesListenerRegistration =
                FirestoreUtil.addChatMessageListener(
                    channelId,
                    this,
                    freelancerId,
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

    override fun onDestroy() {
        super.onDestroy()
        FirestoreUtil.removeListener(messagesListenerRegistration)
        shouldInitRecyclerView = true
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

    companion object {
        const val EXTRA_OWNER_ID = "extra_owner_id"
        const val EXTRA_FREELANCER_ID = "extra_freelancer_id"
        const val EXTRA_CHANNEL_ID = "extra_channel_id"
    }
}