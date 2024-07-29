package com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.tentang_stroke.detail

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edukasipengobatanpadapenderitastroke.adapter.user.TentangStrokeDetailAdapter
import com.example.edukasipengobatanpadapenderitastroke.data.model.TentangStrokeDetailModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ActivityTentangStrokeDetailBinding
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TentangStrokeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTentangStrokeDetailBinding
    private val viewModel: TentangStrokeDetailViewModel by viewModels()
    private lateinit var adapter: TentangStrokeDetailAdapter
    private lateinit var idHalTentangStroke: String
    private var listData : ArrayList<TentangStrokeDetailModel> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTentangStrokeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setDataSebelumnya()
        setButton()
        fetchTentangStroke()
        getTentangStroke()
    }

    private fun setDataSebelumnya() {
        val extras = intent.extras
        if(extras != null) {
            idHalTentangStroke = extras.getString("id_hal_tentang_stroke")!!
            val halaman = extras.getString("halaman")!!
            binding.titleHeader.text = halaman
        }
    }

    private fun setButton() {
        binding.apply {
            btnBack.setOnClickListener {
                finish()
            }
            btnCopy.setOnClickListener{
                if(listData.isNotEmpty()){
                    var data = ""
                    for(value in listData){
                        data += "${value.judul} \n"
                        data += "${value.deskripsi} \n"
                        data += "\n"
                    }
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
            Toast.makeText(this@TentangStrokeDetailActivity, "Berhasil Copy", Toast.LENGTH_SHORT).show()
        } catch (ex: Exception){
            Toast.makeText(this@TentangStrokeDetailActivity, "Gagal copy : ${ex.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchTentangStroke() {
        viewModel.fetchTentangStrokeDetail(idHalTentangStroke)
    }

    private fun getTentangStroke(){
        viewModel.getTentangStrokeDetail().observe(this@TentangStrokeDetailActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureTentangStroke(result.message)
                is UIState.Success-> setSuccessTentangStroke(result.data)
            }
        }
    }

    private fun setFailureTentangStroke(message: String) {
        setStopShimmer()
        Toast.makeText(this@TentangStrokeDetailActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessTentangStroke(data: ArrayList<TentangStrokeDetailModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            listData.addAll(data)
            setAdapter(data)
        } else{
            Toast.makeText(this@TentangStrokeDetailActivity, "Data Kosong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: ArrayList<TentangStrokeDetailModel>) {
        adapter = TentangStrokeDetailAdapter(data)

        binding.apply {
            tvTentangStrokeDetail.layoutManager = LinearLayoutManager(this@TentangStrokeDetailActivity, LinearLayoutManager.VERTICAL, false)
            tvTentangStrokeDetail.adapter = adapter
        }
    }

    private fun setStopShimmer(){
        binding.apply {
            tvTentangStrokeDetail.visibility = View.VISIBLE

            smTentangStrokeDetail.stopShimmer()
            smTentangStrokeDetail.visibility = View.GONE
        }
    }
    private fun setStartShimmer(){
        binding.apply {
            smTentangStrokeDetail.startShimmer()
            smTentangStrokeDetail.visibility = View.VISIBLE

            tvTentangStrokeDetail.visibility = View.GONE
        }
    }
}