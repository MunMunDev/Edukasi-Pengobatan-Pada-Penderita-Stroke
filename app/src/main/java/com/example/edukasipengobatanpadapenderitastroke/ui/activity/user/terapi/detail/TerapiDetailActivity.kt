package com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.terapi.detail

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.edukasipengobatanpadapenderitastroke.R
import com.example.edukasipengobatanpadapenderitastroke.data.model.TerapiModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ActivityTerapiDetailBinding
import com.example.edukasipengobatanpadapenderitastroke.utils.Constant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TerapiDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTerapiDetailBinding
    private var listTerapi : TerapiModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTerapiDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchDataSebelumnya()
        setButton()
        setData()
    }

    private fun fetchDataSebelumnya() {
        val extras = intent.extras
        if(extras != null) {
            listTerapi = intent.getParcelableExtra("terapi")
        }
    }

    private fun setButton() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun setData() {
        binding.apply {
            titleHeader.text = listTerapi!!.nama_terapi
            tvNamaTerapi.text = listTerapi!!.nama_terapi
            tvDeskripsiTerapi.text = listTerapi!!.deskripsi
            Glide.with(this@TerapiDetailActivity)
                .load("${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}${listTerapi!!.gambar}")
                .error(R.drawable.gambar_error_image)
                .into(binding.ivTerapi)

        }

    }

}