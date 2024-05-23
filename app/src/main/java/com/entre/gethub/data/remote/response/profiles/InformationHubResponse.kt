package com.entre.gethub.data.remote.response.profiles

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class InformationHubResponse(
    @SerializedName("success")
    val success: Boolean? = null,

    @SerializedName("data")
    val data: List<Data>? = null,

    @SerializedName("message")
    val message: String? = null
) {
    data class Data(
        @SerializedName("id")
        val id: String? = null,

        @SerializedName("title")
        val title: String? = null,

        @SerializedName("description")
        val description: String? = null,

        @SerializedName("image_url")
        val imageUrl: String? = null,

        @SerializedName("is_active")
        val isActive: Boolean? = null,

        @SerializedName("category") // Properti category ditambahkan
        val category: String? = null // Properti category ditambahkan
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
            parcel.readString() // Membaca category dari parcel
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(id)
            parcel.writeString(title)
            parcel.writeString(description)
            parcel.writeString(imageUrl)
            parcel.writeValue(isActive)
            parcel.writeString(category)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Data> {
            override fun createFromParcel(parcel: Parcel): Data {
                return Data(parcel)
            }

            override fun newArray(size: Int): Array<Data?> {
                return arrayOfNulls(size)
            }
        }
    }
}
