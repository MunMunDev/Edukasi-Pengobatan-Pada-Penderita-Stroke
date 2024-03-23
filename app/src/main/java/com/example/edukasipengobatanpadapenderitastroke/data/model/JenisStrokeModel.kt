package com.example.edukasipengobatanpadapenderitastroke.data.model

import com.google.gson.annotations.SerializedName

class JenisStrokeModel (
    @SerializedName("id_jenis_stroke")
    var id_jenis_stroke: String? = null,

    @SerializedName("nama_stroke")
    var nama_stroke: String? = null,

    @SerializedName("penjelasan")
    var penjelasan: String? = null
)