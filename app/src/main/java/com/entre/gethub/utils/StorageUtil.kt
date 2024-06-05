package com.entre.gethub.utils

import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

object StorageUtil {
    private val instance: FirebaseStorage by lazy { FirebaseStorage.getInstance() }

    fun uploadMessageImage(userId: String, imageBytes: ByteArray, onSuccess: (imagePath: String) -> Unit) {
        val ref = instance.reference.child(userId).child(
            "messages/${UUID.nameUUIDFromBytes(imageBytes)}"
        )
        ref.putBytes(imageBytes)
            .addOnSuccessListener {
                onSuccess(ref.path)
            }
    }

    fun pathToReference(path: String) = instance.getReference(path)
}