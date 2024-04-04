package com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.tentang_stroke.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edukasipengobatanpadapenderitastroke.adapter.user.TentangStrokeAdapter
import com.example.edukasipengobatanpadapenderitastroke.data.model.TentangStrokeListModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ActivityTentangStrokeListBinding
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.main.MainActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.tentang_stroke.detail.TentangStrokeDetailActivity
import com.example.edukasipengobatanpadapenderitastroke.utils.KontrolNavigationDrawer
import com.example.edukasipengobatanpadapenderitastroke.utils.OnClickItem
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TentangStrokeListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTentangStrokeListBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private val viewModel: TentangStrokeListViewModel by viewModels()
    private lateinit var adapter: TentangStrokeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTentangStrokeListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        fetchTentangStroke()
        getTentangStroke()
    }

    private fun setKontrolNavigationDrawer() {
        kontrolNavigationDrawer = KontrolNavigationDrawer(this@TentangStrokeListActivity)
        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@TentangStrokeListActivity)
        }
    }

    private fun fetchTentangStroke() {
        viewModel.fetchTentangStrokeList()
    }

    private fun getTentangStroke(){
        viewModel.getTentangStrokeList().observe(this@TentangStrokeListActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureTentangStroke(result.message)
                is UIState.Success-> setSuccessTentangStroke(result.data)
            }
        }
    }

    private fun setFailureTentangStroke(message: String) {
        setStopShimmer()
        Toast.makeText(this@TentangStrokeListActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessTentangStroke(data: ArrayList<TentangStrokeListModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapter(data)
        } else{
            Toast.makeText(this@TentangStrokeListActivity, "Data Kosong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: ArrayList<TentangStrokeListModel>) {
        adapter = TentangStrokeAdapter(data, object : OnClickItem.ClickTentangStroke{
            override fun clickItem(id_hal_tentang_stroke: String, halaman:String, it: View) {
                val i = Intent(this@TentangStrokeListActivity, TentangStrokeDetailActivity::class.java)
                i.putExtra("id_hal_tentang_stroke", id_hal_tentang_stroke)
                i.putExtra("halaman", halaman)
                startActivity(i)
            }

        })

        binding.apply {
            rvTentangStroke.layoutManager = LinearLayoutManager(this@TentangStrokeListActivity, LinearLayoutManager.VERTICAL, false)
            rvTentangStroke.adapter = adapter
        }
    }

    private fun setStopShimmer(){
        binding.apply {
            rvTentangStroke.visibility = View.VISIBLE

            smTentangStroke.stopShimmer()
            smTentangStroke.visibility = View.GONE
        }
    }
    private fun setStartShimmer(){
        binding.apply {
            smTentangStroke.startShimmer()
            smTentangStroke.visibility = View.VISIBLE

            rvTentangStroke.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@TentangStrokeListActivity, MainActivity::class.java))
        finish()
    }
}