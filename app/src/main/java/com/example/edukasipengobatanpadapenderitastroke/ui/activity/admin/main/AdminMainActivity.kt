package com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.edukasipengobatanpadapenderitastroke.R
import com.example.edukasipengobatanpadapenderitastroke.databinding.ActivityAdminMainBinding
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.akun.AdminAkunActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.galeri_herbal.halaman_galeri_herbal.AdminHalamanGaleriHerbalActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.menu_sehat.AdminMenuSehatActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.tentang_stroke.halaman_tentang_stroke.AdminHalamanTentangStrokeActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.terapi.AdminTerapiActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.testimoni.AdminTestimoniActivity
import com.example.edukasipengobatanpadapenderitastroke.utils.KontrolNavigationDrawer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminMainBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setKontrolNavigationDrawer()
        setButton()
    }

    private fun setKontrolNavigationDrawer() {
        kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminMainActivity)
        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminMainActivity)
        }
    }

    private fun setButton() {
        binding.apply {
            btnTentangStroke.setOnClickListener {
                val i = Intent(this@AdminMainActivity, AdminHalamanTentangStrokeActivity::class.java)
                startActivity(i)
                finish()
            }
            btnGaleriHerbal.setOnClickListener {
                val i = Intent(this@AdminMainActivity, AdminHalamanGaleriHerbalActivity::class.java)
                startActivity(i)
                finish()
            }
            btnTerapi.setOnClickListener {
                val i = Intent(this@AdminMainActivity, AdminTerapiActivity::class.java)
                startActivity(i)
                finish()
            }
            btnMenuSehat.setOnClickListener {
                val i = Intent(this@AdminMainActivity, AdminMenuSehatActivity::class.java)
                startActivity(i)
                finish()
            }
            btnTestimoni.setOnClickListener {
                val i = Intent(this@AdminMainActivity, AdminTestimoniActivity::class.java)
                startActivity(i)
                finish()
            }
            btnAkun.setOnClickListener {
                val i = Intent(this@AdminMainActivity, AdminAkunActivity::class.java)
                startActivity(i)
                finish()
            }
        }
    }

    private var tapDuaKali = false
    override fun onBackPressed() {
        if (tapDuaKali){
            super.onBackPressed()
        }
        tapDuaKali = true
        Toast.makeText(this@AdminMainActivity, "Tekan Sekali Lagi untuk keluar", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            tapDuaKali = false
        }, 2000)
    }
}