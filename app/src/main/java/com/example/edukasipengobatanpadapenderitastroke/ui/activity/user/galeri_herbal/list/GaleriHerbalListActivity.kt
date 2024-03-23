package com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.galeri_herbal.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edukasipengobatanpadapenderitastroke.adapter.GaleriHerbalListAdapter
import com.example.edukasipengobatanpadapenderitastroke.data.model.GaleriHerbalListModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ActivityGaleriHerbalListBinding
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.galeri_herbal.detail.GaleriHerbalDetailActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.main.MainActivity
import com.example.edukasipengobatanpadapenderitastroke.utils.OnClickItem
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GaleriHerbalListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGaleriHerbalListBinding
    private val viewModel: GaleriHerbalListViewModel by viewModels()
    private lateinit var adapter: GaleriHerbalListAdapter
    private var judulGaleriHerbal: String? = null
    private var id_hal_galeri_herbal: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGaleriHerbalListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBack()
        fetchDataSebelumnya()
        fetchGaleriHerbal()
        getGaleriHerbal()
    }

    private fun setBack() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun fetchDataSebelumnya() {
        val extras = intent.extras
        if(extras != null) {
            judulGaleriHerbal = extras.getString("judul")
            id_hal_galeri_herbal = extras.getString("id_hal_galeri_herbal")

            binding.titleHeader.text = judulGaleriHerbal
        }
    }

    private fun fetchGaleriHerbal() {
        viewModel.fetchGaleriHerbalList(id_hal_galeri_herbal!!)
    }

    private fun getGaleriHerbal(){
        viewModel.getGaleriHerbalList().observe(this@GaleriHerbalListActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureGaleriHerbal(result.message)
                is UIState.Success-> setSuccessGaleriHerbal(result.data)
            }
        }
    }

    private fun setFailureGaleriHerbal(message: String) {
        setStopShimmer()
        Toast.makeText(this@GaleriHerbalListActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessGaleriHerbal(data: ArrayList<GaleriHerbalListModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapter(data)
        } else{
            Toast.makeText(this@GaleriHerbalListActivity, "Data Kosong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: ArrayList<GaleriHerbalListModel>) {
        adapter = GaleriHerbalListAdapter(data, object : OnClickItem.ClickGaleriHerbalList{
            override fun clickItem(galeriHerbalList: GaleriHerbalListModel, it: View) {
                var i = Intent(this@GaleriHerbalListActivity, GaleriHerbalDetailActivity::class.java)
                i.putExtra("galeri_herbal", galeriHerbalList)
                startActivity(i)
            }
        })

        binding.apply {
            rvGaleriHerbalList.layoutManager = GridLayoutManager(this@GaleriHerbalListActivity, 2)
            rvGaleriHerbalList.adapter = adapter
        }
    }

    private fun setStopShimmer(){
        binding.apply {
            rvGaleriHerbalList.visibility = View.VISIBLE

            smGaleriHerbalList.stopShimmer()
            smGaleriHerbalList.visibility = View.GONE
        }
    }
    private fun setStartShimmer(){
        binding.apply {
            smGaleriHerbalList.startShimmer()
            smGaleriHerbalList.visibility = View.VISIBLE

            rvGaleriHerbalList.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@GaleriHerbalListActivity, MainActivity::class.java))
        finish()
    }
}