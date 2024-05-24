package com.entre.gethub.ui.models

import android.os.Parcel
import android.os.Parcelable

class ProjectBid(
    var rekomendasiprofilename: String,
    var profilepic2: Int,
    var rekomendasiprofiledesc: String,
    var rekrutproject: String,
    var rekrutprojectdesc: String,
    var rekrutprice: String,
    var rekrutprojectdeadline: String,
    var rekrutprojecttotal: String,
    var rekrutprojectdate: String,
    var tanggalawalakhir: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(rekomendasiprofilename)
        parcel.writeInt(profilepic2)
        parcel.writeString(rekomendasiprofiledesc)
        parcel.writeString(rekrutproject)
        parcel.writeString(rekrutprojectdesc)
        parcel.writeString(rekrutprice)
        parcel.writeString(rekrutprojectdeadline)
        parcel.writeString(rekrutprojecttotal)
        parcel.writeString(rekrutprojectdate)
        parcel.writeString(tanggalawalakhir)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProjectBid> {
        override fun createFromParcel(parcel: Parcel): ProjectBid {
            return ProjectBid(parcel)
        }

        override fun newArray(size: Int): Array<ProjectBid?> {
            return arrayOfNulls(size)
        }
    }
}
