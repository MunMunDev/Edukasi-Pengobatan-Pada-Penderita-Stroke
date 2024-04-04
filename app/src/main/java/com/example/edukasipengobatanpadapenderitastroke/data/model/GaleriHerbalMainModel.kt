package com.example.edukasipengobatanpadapenderitastroke.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class GaleriHerbalMainModel (
    @SerializedName("id_hal_galeri_herbal")
    var id_hal_galeri_herbal: String? = null,

    @SerializedName("judul")
    var judul: String? = null,

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
        parcel.writeString(id_hal_galeri_herbal)
        parcel.writeString(judul)
        parcel.writeString(deskripsi)
        parcel.writeString(gambar)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GaleriHerbalMainModel> {
        override fun createFromParcel(parcel: Parcel): GaleriHerbalMainModel {
            return GaleriHerbalMainModel(parcel)
        }

        override fun newArray(size: Int): Array<GaleriHerbalMainModel?> {
            return arrayOfNulls(size)
        }
    }
}