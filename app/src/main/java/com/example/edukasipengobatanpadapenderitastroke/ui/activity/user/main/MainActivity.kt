package com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.main

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.edukasipengobatanpadapenderitastroke.R
import com.example.edukasipengobatanpadapenderitastroke.adapter.user.TestimoniAdapter
import com.example.edukasipengobatanpadapenderitastroke.data.model.TestimoniModel
import com.example.edukasipengobatanpadapenderitastroke.databinding.ActivityMainBinding
import com.example.edukasipengobatanpadapenderitastroke.databinding.AlertDialogShowImageBinding
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.galeri_herbal.main.GaleriHerbalMainActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.menu_sehat.MenuSehatActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.tentang_stroke.list.TentangStrokeListActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.terapi.list.TerapiListActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.testimoni.TestimoniActivity
import com.example.edukasipengobatanpadapenderitastroke.utils.Constant
import com.example.edukasipengobatanpadapenderitastroke.utils.KontrolNavigationDrawer
import com.example.edukasipengobatanpadapenderitastroke.utils.OnClickItem
import com.example.edukasipengobatanpadapenderitastroke.utils.SharedPreferencesLogin
import com.example.edukasipengobatanpadapenderitastroke.utils.network.UIState
import dagger.hilt.android.AndroidEntryPoint
import kotlin.collections.ArrayList

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
                startActivity(Intent(this@MainActivity, TentangStrokeListActivity::class.java))
                finish()
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
                startActivity(Intent(this@MainActivity, MenuSehatActivity::class.java))
                finish()
            }

            btnTestimoni.setOnClickListener {
                startActivity(Intent(this@MainActivity, TestimoniActivity::class.java))
                finish()
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
        val testimoniSendiri: ArrayList<TestimoniModel> = arrayListOf()
        val testimoniOrangLain: ArrayList<TestimoniModel> = arrayListOf()
        val testimoniSemua: ArrayList<TestimoniModel> = arrayListOf()
        for (value in data){
            if(value.id_user!!.trim().toInt() == sharedPreferencesLogin.getIdUser()){
                testimoniSendiri.add(value)
            } else{
                testimoniOrangLain.add(value)
            }
        }
        testimoniSemua.addAll(testimoniSendiri)
        testimoniSemua.addAll(testimoniOrangLain)
        setAdapter(testimoniSemua)

//        setAdapter(data)
    }

    private fun setAdapter(data: ArrayList<TestimoniModel>) {
        adapter = TestimoniAdapter(data, sharedPreferencesLogin.getIdUser().toString(), true, object : OnClickItem.ClickTestimoni{
            override fun clickGambar(gambar: String, nama: String, it: View) {

//                val view = AlertDialogShowImageBinding.inflate(layoutInflater)
//
//                val alertDialog = AlertDialog.Builder(this@MainActivity)
//                alertDialog.setView(view.root)
//                    .setCancelable(false)
//                val dialogInputan = alertDialog.create()
//                dialogInputan.show()
//
//                view.apply {
//                    tvTitle.text = nama
//                    btnClose.setOnClickListener {
//                        dialogInputan.dismiss()
//                    }
//                }
//
//                Glide.with(this@MainActivity)
//                    .load("${Constant.BASE_URL}${Constant.LOCATION_GAMBAR}$gambar") // URL Gambar
//                    .error(R.drawable.gambar_error_image)
//                    .into(view.ivShowImage) // imageView mana yang akan diterapkan

            }

        })
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

    private var tapDuaKali = false
    override fun onBackPressed() {
        if (tapDuaKali){
            super.onBackPressed()
        }
        tapDuaKali = true
        Toast.makeText(this@MainActivity, "Tekan Sekali Lagi untuk keluar", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            tapDuaKali = false
        }, 2000)
    }
}