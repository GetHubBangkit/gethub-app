package com.entre.gethub.ui.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import com.entre.gethub.R
import com.entre.gethub.databinding.ItemChatReceiverBinding
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
    val receiverName: String,
    val receiverPhoto: String,
) : Item() {

    private val isSender = message.senderId == senderId

    override fun getLayout(): Int {
        return if (isSender) {
            R.layout.item_chat_sender
        } else {
            R.layout.item_chat_receiver
        }
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        if (isSender) {
            val viewBinding = ItemChatSenderBinding.bind(viewHolder.itemView)
            viewBinding.tvMessage.text = message.text
            setTimeText(viewBinding.tvChatDate)
            viewBinding.tvSenderName.text = senderName
            viewBinding.tvMessage.setTextColor(context.getColor(R.color.white))
            viewBinding.tvChatDate.setTextColor(context.getColor(R.color.white))
//            Glide.with(context).load(senderPhoto).placeholder(R.drawable.profilepic1).into(viewBinding.ivSenderAvatar)
        } else {
            val viewBinding = ItemChatReceiverBinding.bind(viewHolder.itemView)
            viewBinding.tvMessage.text = message.text
            setTimeText(viewBinding.tvChatDate)
            viewBinding.tvSenderName.text = receiverName
            viewBinding.tvMessage.setTextColor(context.getColor(R.color.black))
            viewBinding.tvChatDate.setTextColor(context.getColor(R.color.black))
//            Glide.with(context).load(receiverPhoto).placeholder(R.drawable.profilepic1).into(viewBinding.ivReceiverAvatar)
        }
    }

    private fun setTimeText(textView: TextView) {
        val dateFormat = SimpleDateFormat("HH:mm dd MMM yy", Locale("id", "ID"))
        textView.text = dateFormat.format(message.time)
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>): Boolean {
        if (other !is TextMessageItem) return false
        if (other.message != message) return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as TextMessageItem)
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }
}