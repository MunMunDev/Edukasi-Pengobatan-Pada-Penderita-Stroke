package com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.menu_sehat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edukasipengobatanpadapenderitastroke.adapter.user.MenuSehatAdapter
import com.example.edukasipengobatanpadapenderitastroke.data.model.MenuSehatModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ActivityMenuSehatBinding
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.main.MainActivity
import com.example.edukasipengobatanpadapenderitastroke.utils.KontrolNavigationDrawer
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuSehatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuSehatBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private val viewModel: MenuSehatViewModel by viewModels()
    private lateinit var adapter: MenuSehatAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuSehatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        fetchTentangStroke()
        getTentangStroke()
    }
    private fun setKontrolNavigationDrawer() {
        kontrolNavigationDrawer = KontrolNavigationDrawer(this@MenuSehatActivity)
        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@MenuSehatActivity)
        }
    }

    private fun fetchTentangStroke() {
        viewModel.fetchMenuSehat()
    }

    private fun getTentangStroke(){
        viewModel.getMenuSehat().observe(this@MenuSehatActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureTentangStroke(result.message)
                is UIState.Success-> setSuccessTentangStroke(result.data)
            }
        }
    }

    private fun setFailureTentangStroke(message: String) {
        setStopShimmer()
        Toast.makeText(this@MenuSehatActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessTentangStroke(data: ArrayList<MenuSehatModel>) {
        setStopShimmer()
        if(data.isNotEmpty()){
            setAdapter(data)
        } else{
            Toast.makeText(this@MenuSehatActivity, "Data Kosong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter(data: ArrayList<MenuSehatModel>) {
        adapter = MenuSehatAdapter(data)

        binding.apply {
            tvMenuSehat.layoutManager = LinearLayoutManager(this@MenuSehatActivity, LinearLayoutManager.VERTICAL, false)
            tvMenuSehat.adapter = adapter
        }
    }

    private fun setStopShimmer(){
        binding.apply {
            tvMenuSehat.visibility = View.VISIBLE

            smMenuSehat.stopShimmer()
            smMenuSehat.visibility = View.GONE
        }
    }
    private fun setStartShimmer(){
        binding.apply {
            smMenuSehat.startShimmer()
            smMenuSehat.visibility = View.VISIBLE

            tvMenuSehat.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@MenuSehatActivity, MainActivity::class.java))
        finish()
    }
}