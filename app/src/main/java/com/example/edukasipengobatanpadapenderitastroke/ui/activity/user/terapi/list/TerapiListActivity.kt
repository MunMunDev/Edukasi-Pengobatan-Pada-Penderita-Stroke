package com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.terapi.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edukasipengobatanpadapenderitastroke.adapter.TerapiAdapter
import com.example.edukasipengobatanpadapenderitastroke.data.model.TerapiModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ActivityTerapiListBinding
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.main.MainActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.terapi.detail.TerapiDetailActivity
import com.example.edukasipengobatanpadapenderitastroke.utils.KontrolNavigationDrawer
import com.example.edukasipengobatanpadapenderitastroke.utils.OnClickItem
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TerapiListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTerapiListBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private val viewModel: TerapiListViewModel by viewModels()
    private lateinit var adapter: TerapiAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTerapiListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        fetchTerapi()
        getTerapi()
    }

    private fun setKontrolNavigationDrawer() {
        kontrolNavigationDrawer = KontrolNavigationDrawer(this@TerapiListActivity)
        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@TerapiListActivity)
        }
    }

    private fun fetchTerapi() {
        viewModel.fetchTerapi()
    }

    private fun getTerapi(){
        viewModel.getTerapi().observe(this@TerapiListActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureTerapi(result.message)
                is UIState.Success-> setSuccessTerapi(result.data)
            }
        }
    }

    private fun setFailureTerapi(message: String) {
        setStopShimmer()
        Toast.makeText(this@TerapiListActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessTerapi(data: ArrayList<TerapiModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapter(data)
        } else{
            Toast.makeText(this@TerapiListActivity, "Data Kosong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: ArrayList<TerapiModel>) {
        adapter = TerapiAdapter(data, object : OnClickItem.ClickTerapi{
            override fun clickItem(terapi: TerapiModel, it: View) {
                val i = Intent(this@TerapiListActivity, TerapiDetailActivity::class.java)
                i.putExtra("terapi", terapi)
                startActivity(i)
            }

        })

        binding.apply {
            rvTerapi.layoutManager = LinearLayoutManager(this@TerapiListActivity, LinearLayoutManager.VERTICAL, false)
            rvTerapi.adapter = adapter
        }
    }

    private fun setStopShimmer(){
        binding.apply {
            rvTerapi.visibility = View.VISIBLE

            smTerapi.stopShimmer()
            smTerapi.visibility = View.GONE
        }
    }
    private fun setStartShimmer(){
        binding.apply {
            smTerapi.startShimmer()
            smTerapi.visibility = View.VISIBLE

            rvTerapi.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@TerapiListActivity, MainActivity::class.java))
        finish()
    }
}