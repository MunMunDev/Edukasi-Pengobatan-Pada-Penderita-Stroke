package com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.galeri_herbal.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edukasipengobatanpadapenderitastroke.adapter.GaleriHerbalMainAdapter
import com.example.edukasipengobatanpadapenderitastroke.data.model.GaleriHerbalMainModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ActivityGaleriHerbalMainBinding
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.galeri_herbal.list.GaleriHerbalListActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.main.MainActivity
import com.example.edukasipengobatanpadapenderitastroke.utils.KontrolNavigationDrawer
import com.example.edukasipengobatanpadapenderitastroke.utils.OnClickItem
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GaleriHerbalMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGaleriHerbalMainBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private val viewModel: GaleriHerbalMainViewModel by viewModels()
    private lateinit var adapter: GaleriHerbalMainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGaleriHerbalMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        fetchGaleriHerbal()
        getGaleriHerbal()
    }

    private fun setKontrolNavigationDrawer() {
        kontrolNavigationDrawer = KontrolNavigationDrawer(this@GaleriHerbalMainActivity)
        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@GaleriHerbalMainActivity)
        }
    }

    private fun fetchGaleriHerbal() {
        viewModel.fetchGaleriHerbal()
    }

    private fun getGaleriHerbal(){
        viewModel.getGaleriHerbal().observe(this@GaleriHerbalMainActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureGaleriHerbal(result.message)
                is UIState.Success-> setSuccessGaleriHerbal(result.data)
            }
        }
    }

    private fun setFailureGaleriHerbal(message: String) {
        setStopShimmer()
        Toast.makeText(this@GaleriHerbalMainActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessGaleriHerbal(data: ArrayList<GaleriHerbalMainModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapter(data)
        } else{
            Toast.makeText(this@GaleriHerbalMainActivity, "Data Kosong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: ArrayList<GaleriHerbalMainModel>) {
        adapter = GaleriHerbalMainAdapter(data, object : OnClickItem.ClickObatHerbal{
            override fun clickItem(id: String, judul: String, it: View) {
                val i = Intent(this@GaleriHerbalMainActivity, GaleriHerbalListActivity::class.java)
                i.putExtra("id_hal_galeri_herbal", id)
                i.putExtra("judul", judul)
                startActivity(i)
            }

        })

        binding.apply {
            rvGaleriHerbal.layoutManager = LinearLayoutManager(this@GaleriHerbalMainActivity, LinearLayoutManager.VERTICAL, false)
            rvGaleriHerbal.adapter = adapter
        }
    }

    private fun setStopShimmer(){
        binding.apply {
            rvGaleriHerbal.visibility = View.VISIBLE

            smGaleriHerbal.stopShimmer()
            smGaleriHerbal.visibility = View.GONE
        }
    }
    private fun setStartShimmer(){
        binding.apply {
            smGaleriHerbal.startShimmer()
            smGaleriHerbal.visibility = View.VISIBLE

            rvGaleriHerbal.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@GaleriHerbalMainActivity, MainActivity::class.java))
        finish()
    }

}