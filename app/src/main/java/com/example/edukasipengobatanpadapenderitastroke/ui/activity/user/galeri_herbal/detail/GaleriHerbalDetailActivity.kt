package com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.galeri_herbal.detail

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.edukasipengobatanpadapenderitastroke.R
import com.example.edukasipengobatanpadapenderitastroke.data.model.GaleriHerbalListModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ActivityGaleriHerbalDetailBinding
import com.example.edukasipengobatanpadapenderitastroke.utils.Constant
import com.example.edukasipengobatanpadapenderitastroke.utils.Youtube

class GaleriHerbalDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGaleriHerbalDetailBinding
    private var listGaleriHerbalDetail : GaleriHerbalListModel? = null
    private var youtube = Youtube()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGaleriHerbalDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchDataSebelumnya()
        setButton()
        setData()

        Log.d("GaleriHerbalDetailTAG", "onCreate: ${listGaleriHerbalDetail!!.id_val_galeri_herbal}")
    }

    private fun fetchDataSebelumnya() {
        val extras = intent.extras
        if(extras != null) {
            listGaleriHerbalDetail = intent.getParcelableExtra("galeri_herbal")
        }
    }

    private fun setButton() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }
            btnVideoYoutube.setOnClickListener {
                youtube.setToYoutubeVideo(this@GaleriHerbalDetailActivity, listGaleriHerbalDetail!!.youtube!!)
            }
        }
    }

    private fun setData() {
        binding.apply {
            titleHeader.text = listGaleriHerbalDetail!!.nama
            tvNamaGaleriHerbalDetail.text = listGaleriHerbalDetail!!.nama
            tvDeskripsiGaleriHerbalDetail.text = listGaleriHerbalDetail!!.deskripsi
            tvTataCaraPengolahan.text = listGaleriHerbalDetail!!.tata_cara_pengolahan
            Glide.with(this@GaleriHerbalDetailActivity)
                .load("${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}${listGaleriHerbalDetail!!.gambar}")
                .error(R.drawable.gambar_error_image)
                .into(binding.ivGaleriHerbalDetail)

            youtube.setImageFromYoutube(this@GaleriHerbalDetailActivity, listGaleriHerbalDetail!!.youtube!!, binding.ivYoutube)
        }

    }

}