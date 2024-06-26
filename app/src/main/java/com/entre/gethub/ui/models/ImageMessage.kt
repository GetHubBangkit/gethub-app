package com.entre.gethub.ui.models

import java.util.Date

data class ImageMessage(
    val imagePath: String,
    override val time: Date,
    override val senderId: String,
    override val type: String = MessageType.IMAGE
) : Message {
    constructor() : this("", Date(0), "")
}
