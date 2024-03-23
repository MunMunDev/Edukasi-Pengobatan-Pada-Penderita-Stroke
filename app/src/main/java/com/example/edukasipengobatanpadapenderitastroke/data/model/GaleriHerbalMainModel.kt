package com.example.edukasipengobatanpadapenderitastroke.data.model

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
)