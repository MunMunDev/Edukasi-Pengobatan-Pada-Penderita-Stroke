package com.example.edukasipengobatanpadapenderitastroke.data.model

import com.google.gson.annotations.SerializedName

class PenyebabStrokeModel (
    @SerializedName("id_penyebab_stroke")
    var id_penyebab_stroke: String? = null,

    @SerializedName("penyebab_stroke")
    var penyebab_stroke: String? = null,

    @SerializedName("penjelasan")
    var penjelasan: String? = null
)