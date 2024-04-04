package com.example.edukasipengobatanpadapenderitastroke.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class MenuSehatModel (
    @SerializedName("id_menu_sehat")
    var id_menu_sehat: String? = null,

    @SerializedName("judul")
    var judul: String? = null,

    @SerializedName("deskripsi")
    var deskripsi: String? = null
)