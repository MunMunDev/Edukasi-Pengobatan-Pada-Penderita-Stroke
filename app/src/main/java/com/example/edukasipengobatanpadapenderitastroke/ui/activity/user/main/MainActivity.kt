package com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.main

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.edukasipengobatanpadapenderitastroke.R
import com.example.edukasipengobatanpadapenderitastroke.adapter.TestimoniAdapter
import com.example.edukasipengobatanpadapenderitastroke.data.model.TestimoniModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ActivityMainBinding
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.galeri_herbal.main.GaleriHerbalMainActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.terapi.list.TerapiListActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.fragment.user.galeri_herbal.GaleriHerbalFragment
import com.example.edukasipengobatanpadapenderitastroke.utils.KontrolNavigationDrawer
import com.example.edukasipengobatanpadapenderitastroke.utils.KontrolNavigationDrawerFragment
import com.example.edukasipengobatanpadapenderitastroke.utils.SharedPreferencesLogin
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: TestimoniAdapter
    private lateinit var sharedPreferencesLogin: SharedPreferencesLogin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        fragmentHome()
//        setNavigationDrawerFragment()

        setSharedPreferencesLogin()
        setNavigationDrawer()
        setButton()
        fetchTestimoni()
        getTestimoni()
    }

//    private fun setNavigationDrawerFragment() {
//        kontrolNavigationDrawerFragment= KontrolNavigationDrawerFragment(this@MainActivity)
//        binding.apply {
//            kontrolNavigationDrawer.cekSebagai(navView)
//            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@MainActivity)
//        }
//    }
//
//    private fun fragmentHome() {
//        replaceFragment(GaleriHerbalFragment())
//    }
//
//    private fun replaceFragment(fragment: Fragment){
//        var fragmentManager : FragmentManager = supportFragmentManager
//        var fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.flMain, fragment)
//        fragmentTransaction.commit()
//    }

    @SuppressLint("SetTextI18n")
    private fun setSharedPreferencesLogin() {
        sharedPreferencesLogin = SharedPreferencesLogin(this@MainActivity)
        binding.tvNama.text = "Hy, ${sharedPreferencesLogin.getNama()}"
    }

    private fun setNavigationDrawer() {
        kontrolNavigationDrawer = KontrolNavigationDrawer(this@MainActivity)
        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@MainActivity)
        }
    }

    private fun setButton() {
        binding.apply {
            btnTentangStroke.setOnClickListener {

            }
            btnGaleriHerbal.setOnClickListener {
                startActivity(Intent(this@MainActivity, GaleriHerbalMainActivity::class.java))
                finish()
            }
            btnTerapi.setOnClickListener {
                startActivity(Intent(this@MainActivity, TerapiListActivity::class.java))
                finish()
            }
            btnMenuSehat.setOnClickListener {

            }

            btnTestimoni.setOnClickListener {

            }
        }
    }

    private fun fetchTestimoni() {
        viewModel.fetchTestimoni()
    }

    private fun getTestimoni() {
        viewModel.getTestimoni().observe(this@MainActivity){result->
            when(result){
                is UIState.Loading-> setStartShimmer()
                is UIState.Failure-> setFailureTestimoni(result.message)
                is UIState.Success-> setSuccessTestimoni(result.data)
            }
        }
    }

    private fun setFailureTestimoni(message: String) {
        setStopShimmer()
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setSuccessTestimoni(data: ArrayList<TestimoniModel>) {
        setAdapter(data)
    }

    private fun setAdapter(data: ArrayList<TestimoniModel>) {
        adapter = TestimoniAdapter(data, sharedPreferencesLogin.getIdUser().toString(), true)
        binding.apply {
            rvTestimoni.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            rvTestimoni.adapter = adapter
        }
        setStopShimmer()
    }

    private fun setStopShimmer(){
        binding.apply {
            rvTestimoni.visibility = View.VISIBLE

            smTestimoni.stopShimmer()
            smTestimoni.visibility = View.GONE
        }
    }
    private fun setStartShimmer(){
        binding.apply {
            smTestimoni.startShimmer()
            smTestimoni.visibility = View.VISIBLE

            rvTestimoni.visibility = View.GONE
        }
    }
}