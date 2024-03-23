package com.example.edukasipengobatanpadapenderitastroke.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class TerapiModel (
    @SerializedName("id_terapi")
    var id_terapi: String? = null,

    @SerializedName("nama_terapi")
    var nama_terapi: String? = null,

    @SerializedName("deskripsi")
    var deskripsi: String? = null,

    @SerializedName("gambar")
    var gambar: String? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id_terapi)
        parcel.writeString(nama_terapi)
        parcel.writeString(deskripsi)
        parcel.writeString(gambar)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TerapiModel> {
        override fun createFromParcel(parcel: Parcel): TerapiModel {
            return TerapiModel(parcel)
        }

        override fun newArray(size: Int): Array<TerapiModel?> {
            return arrayOfNulls(size)
        }
    }
}