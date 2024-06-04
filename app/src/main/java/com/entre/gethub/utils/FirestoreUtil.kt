package com.entre.gethub.utils

import android.content.Context
import android.util.Log
import com.entre.gethub.data.remote.response.profiles.UserProfileResponse
import com.entre.gethub.data.remote.response.projects.AcceptedProjectBidResponse
import com.entre.gethub.ui.adapter.TextMessageItem
import com.entre.gethub.ui.models.Message
import com.entre.gethub.ui.models.MessageType
import com.entre.gethub.ui.models.TextMessage
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.kotlinandroidextensions.Item

object FirestoreUtil {
    private val instance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val chatChannelsCollectionRef = instance.collection("chatChannels")

    fun removeListener(registration: ListenerRegistration) = registration.remove()

    fun getOrCreateChannel(
        ownerUserId: String,
        freelancerId: String,
        chatroomId: String,
        onComplete: (channelId: String) -> Unit
    ) {

        val newChannel = chatChannelsCollectionRef.document(chatroomId)
        newChannel.set(ChatChannel(mutableListOf(freelancerId, ownerUserId)))

        onComplete(newChannel.id)

    }

    fun addChatMessageListener(
        channelId: String,
        context: Context,
        senderId: String,
        senderName: String,
        senderPhoto: String,
        ownerName: String,
        ownerPhoto: String,
        onListen: (List<Item>) -> Unit
    ): ListenerRegistration {
        return chatChannelsCollectionRef.document(channelId)
            .collection("messages")
            .orderBy("time")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("Firestore", "ChatMessages listener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = mutableListOf<Item>()
                querySnapshot?.documents?.forEach {
                    if (it["type"] == MessageType.TEXT)
                        items.add(
                            TextMessageItem(
                                message = it.toObject(TextMessage::class.java)!!,
                                context = context,
                                senderId = senderId,
                                senderName = senderName,
                                senderPhoto = senderPhoto,
                                ownerName = ownerName,
                                ownerPhoto = ownerPhoto,
                            )
                        )
//                    else
//                        items.add(
//                            ImageMessageItem(
//                                it.toObject(ImageMessage::class.java)!!,
//                                context
//                            )
//                        )
                }

                onListen(items)
            }
    }

    fun sendMessage(message: Message, channelId: String) {
        chatChannelsCollectionRef.document(channelId)
            .collection("messages")
            .add(message)
    }

}

data class ChatChannel(val userIds: MutableList<String>) {
    constructor() : this(mutableListOf())
}