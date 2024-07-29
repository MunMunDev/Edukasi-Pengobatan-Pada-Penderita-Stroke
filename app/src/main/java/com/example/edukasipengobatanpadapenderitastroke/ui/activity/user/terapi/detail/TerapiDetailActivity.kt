package com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.terapi.detail

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.edukasipengobatanpadapenderitastroke.R
import com.example.edukasipengobatanpadapenderitastroke.data.model.GaleriHerbalListModel
import com.example.edukasipengobatanpadapenderitastroke.data.model.TerapiModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ActivityTerapiDetailBinding
import com.example.edukasipengobatanpadapenderitastroke.utils.Constant
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TerapiDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTerapiDetailBinding
    private var listTerapi : TerapiModel? = null
    private var listData : TerapiModel? = null
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
            listData = intent.getParcelableExtra("terapi")
        }
    }

    private fun setButton() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }
            btnCopy.setOnClickListener {
                if(listData != null){
                    var data = ""
                    data += "${listData!!.nama_terapi} \n"
                    data += "${listData!!.deskripsi} \n"
                    copyData(data)
                }
            }
        }
    }

    private fun copyData(data: String) {
        try{
            val clipBoard: ClipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData: ClipData = ClipData.newPlainText("Copied Text", data)
            clipBoard.setPrimaryClip(clipData)
            Toast.makeText(this@TerapiDetailActivity, "Berhasil Copy", Toast.LENGTH_SHORT).show()
        } catch (ex: Exception){
            Toast.makeText(this@TerapiDetailActivity, "Gagal copy : ${ex.message}", Toast.LENGTH_SHORT).show()
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