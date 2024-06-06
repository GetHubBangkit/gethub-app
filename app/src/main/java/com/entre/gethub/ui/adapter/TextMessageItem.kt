package com.entre.gethub.ui.adapter

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.data.remote.response.profiles.UserProfileResponse
import com.entre.gethub.data.remote.response.projects.AcceptedProjectBidResponse
import com.entre.gethub.databinding.ItemChatSenderBinding
import com.entre.gethub.ui.models.TextMessage
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import java.text.SimpleDateFormat
import java.util.Locale

class TextMessageItem(
    val message: TextMessage,
    val context: Context,
    val senderId: String,
    val senderName: String,
    val senderPhoto: String,
    val ownerName: String,
    val ownerPhoto: String,
) :
    Item() {
    override fun getLayout(): Int = R.layout.item_chat_sender

    override fun isSameAs(other: com.xwray.groupie.Item<*>): Boolean {
        if (other !is TextMessageItem) return false
        if (other.message != message) return false
        return true
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            val viewBinding = ItemChatSenderBinding.bind(this)
            viewBinding.tvMessage.text = message.text
            setTimeText(viewBinding)
            setMessageRootGravity(viewBinding)
        }
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as TextMessageItem)
    }

    private fun setTimeText(viewBinding: ItemChatSenderBinding) {
        val dateFormat = SimpleDateFormat("HH:mm dd MMM yy", Locale("id", "ID"))
        viewBinding.tvChatDate.text = dateFormat.format(message.time)
    }

    private fun setMessageRootGravity(viewBinding: ItemChatSenderBinding) {
        if (message.senderId == senderId) {
            with(viewBinding) {
                ivSenderAvatar.visibility = View.VISIBLE
                ivReceiverAvatar.visibility = View.GONE
                tvSenderName.visibility = View.VISIBLE
                chatBg.setBackgroundResource(R.drawable.sender_chat_background)
                tvMessage.setTextColor(context.getColor(R.color.white))
                tvChatDate.setTextColor(context.getColor(R.color.white))
                tvSenderName.text = senderName
                Glide.with(root.context)
                    .load(senderPhoto)
                    .placeholder(R.drawable.profilepic1)
                    .into(ivSenderAvatar)
            }
            viewBinding.messageRoot.apply {
                (layoutParams as FrameLayout.LayoutParams).gravity = Gravity.END
            }
        } else {
            with(viewBinding) {
                ivReceiverAvatar.visibility = View.VISIBLE
                ivSenderAvatar.visibility = View.GONE
                tvSenderName.visibility = View.VISIBLE
                chatBg.setBackgroundResource(R.drawable.receiver_chat_background)
                tvMessage.setTextColor(context.getColor(R.color.black))
                tvChatDate.setTextColor(context.getColor(R.color.black))
                Glide.with(root.context)
                    .load(ownerPhoto)
                    .placeholder(R.drawable.profilepic1)
                    .into(ivSenderAvatar)
                viewBinding.tvSenderName.text = ownerName
            }
            viewBinding.messageRoot.apply {
                (layoutParams as FrameLayout.LayoutParams).gravity = Gravity.START
            }
        }
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }
}