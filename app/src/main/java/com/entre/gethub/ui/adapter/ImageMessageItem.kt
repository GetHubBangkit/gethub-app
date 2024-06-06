package com.entre.gethub.ui.adapter

import android.content.Context
import android.view.Gravity
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.databinding.ItemChatImageBinding
import com.entre.gethub.ui.models.ImageMessage
import com.entre.gethub.utils.StorageUtil
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import java.text.SimpleDateFormat
import java.util.Locale

class ImageMessageItem(val message: ImageMessage, val context: Context, val senderId: String) :
    Item() {

    override fun getLayout(): Int = R.layout.item_chat_image

    override fun isSameAs(other: com.xwray.groupie.Item<*>): Boolean {
        if (other !is ImageMessageItem) return false
        if (other.message != message) return false
        return true
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            val viewBinding = ItemChatImageBinding.bind(this)
            Glide.with(context)
                .load(StorageUtil.pathToReference(message.imagePath))
                .placeholder(R.drawable.ic_image)
                .into(viewBinding.ivMessageImage)
            setTimeText(viewBinding)
            setMessageRootGravity(viewBinding)
        }
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as ImageMessageItem)
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }

    private fun setTimeText(viewBinding: ItemChatImageBinding) {
        val dateFormat = SimpleDateFormat("HH:mm dd MMM yy", Locale("id", "ID"))
        viewBinding.tvTime.text = dateFormat.format(message.time)
    }

    private fun setMessageRootGravity(viewBinding: ItemChatImageBinding) {
        if (message.senderId == senderId) {
            viewBinding.messageRoot.apply {
                setBackgroundResource(R.drawable.sender_chat_background)
                viewBinding.tvTime.setTextColor(context.getColor(R.color.white))
                (layoutParams as FrameLayout.LayoutParams).gravity = Gravity.END
            }
        } else {
            viewBinding.messageRoot.apply {
                setBackgroundResource(R.drawable.receiver_chat_background)
                viewBinding.tvTime.setTextColor(context.getColor(R.color.black))
                (layoutParams as FrameLayout.LayoutParams).gravity = Gravity.START
            }
        }
    }

}