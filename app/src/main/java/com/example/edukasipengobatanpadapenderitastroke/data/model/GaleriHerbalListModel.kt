package com.example.edukasipengobatanpadapenderitastroke.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class GaleriHerbalListModel (
    @SerializedName("id_val_galeri_herbal")
    var id_val_galeri_herbal: String? = null,

    @SerializedName("id_hal_galeri_herbal")
    var id_hal_galeri_herbal: String? = null,

    @SerializedName("nama")
    var nama: String? = null,

    @SerializedName("deskripsi")
    var deskripsi: String? = null,

    @SerializedName("tata_cara_pengolahan")
    var tata_cara_pengolahan: String? = null,

    @SerializedName("gambar")
    var gambar: String? = null,

    @SerializedName("youtube")
    var youtube: String? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id_val_galeri_herbal)
        parcel.writeString(id_hal_galeri_herbal)
        parcel.writeString(nama)
        parcel.writeString(deskripsi)
        parcel.writeString(tata_cara_pengolahan)
        parcel.writeString(gambar)
        parcel.writeString(youtube)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GaleriHerbalListModel> {
        override fun createFromParcel(parcel: Parcel): GaleriHerbalListModel {
            return GaleriHerbalListModel(parcel)
        }

        override fun newArray(size: Int): Array<GaleriHerbalListModel?> {
            return arrayOfNulls(size)
        }
    }
}