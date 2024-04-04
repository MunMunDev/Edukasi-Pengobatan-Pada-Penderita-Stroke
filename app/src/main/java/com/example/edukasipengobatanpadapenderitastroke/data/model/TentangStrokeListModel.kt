package com.example.edukasipengobatanpadapenderitastroke.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class TentangStrokeListModel (
    @SerializedName("id_hal_tentang_stroke")
    var id_hal_tentang_stroke: String? = null,

    @SerializedName("halaman")
    var halaman: String? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id_hal_tentang_stroke)
        parcel.writeString(halaman)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TentangStrokeListModel> {
        override fun createFromParcel(parcel: Parcel): TentangStrokeListModel {
            return TentangStrokeListModel(parcel)
        }

        override fun newArray(size: Int): Array<TentangStrokeListModel?> {
            return arrayOfNulls(size)
        }
    }
}