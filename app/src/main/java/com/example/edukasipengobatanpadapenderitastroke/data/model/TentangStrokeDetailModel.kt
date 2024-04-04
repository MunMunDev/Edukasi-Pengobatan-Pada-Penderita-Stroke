package com.example.edukasipengobatanpadapenderitastroke.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class TentangStrokeDetailModel (
    @SerializedName("id_val_tentang_stroke")
    var id_val_tentang_stroke: String? = null,

    @SerializedName("id_hal_tentang_stroke")
    var id_hal_tentang_stroke: String? = null,

    @SerializedName("hal_tentang_stroke")
    var hal_tentang_stroke: String? = null,

    @SerializedName("judul")
    var judul: String? = null,

    @SerializedName("deskripsi")
    var deskripsi: String? = null
)