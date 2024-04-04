package com.example.edukasipengobatanpadapenderitastroke.data.model

import com.google.gson.annotations.SerializedName

class TestimoniModel (
    @SerializedName("id_testimoni")
    var id_testimoni: String? = null,

    @SerializedName("id_user")
    var id_user: String? = null,

    @SerializedName("nama")
    var nama: String? = null,

    @SerializedName("testimoni")
    var testimoni: String? = null,

    @SerializedName("bintang")
    var bintang: String? = null,

    @SerializedName("gambar")
    var gambar: String? = null,

    @SerializedName("tanggal")
    var tanggal: String? = null
)