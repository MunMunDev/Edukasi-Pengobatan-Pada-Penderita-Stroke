package com.example.edukasipengobatanpadapenderitastroke.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.edukasipengobatanpadapenderitastroke.R
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.akun.AdminAkunActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.galeri_herbal.halaman_galeri_herbal.AdminHalamanGaleriHerbalActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.main.AdminMainActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.menu_sehat.AdminMenuSehatActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.tentang_stroke.halaman_tentang_stroke.AdminHalamanTentangStrokeActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.terapi.AdminTerapiActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.admin.testimoni.AdminTestimoniActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.login.LoginActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.akun.AkunActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.galeri_herbal.main.GaleriHerbalMainActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.main.MainActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.menu_sehat.MenuSehatActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.tentang_stroke.list.TentangStrokeListActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.terapi.list.TerapiListActivity
import com.example.edukasipengobatanpadapenderitastroke.ui.activity.user.testimoni.TestimoniActivity

class KontrolNavigationDrawer(var context: Context) {
    var sharedPreferences = SharedPreferencesLogin(context)
    fun cekSebagai(navigation: com.google.android.material.navigation.NavigationView){
        if(sharedPreferences.getSebagai() == "user"){
            navigation.menu.clear()
            navigation.inflateMenu(R.menu.nav_menu_user)
        }
        else if(sharedPreferences.getSebagai() == "admin"){
            navigation.menu.clear()
            navigation.inflateMenu(R.menu.nav_menu_admin)
        }
    }
    @SuppressLint("ResourceAsColor")
    fun onClickItemNavigationDrawer(navigation: com.google.android.material.navigation.NavigationView, navigationLayout: DrawerLayout, igNavigation:ImageView, activity: Activity){
        navigation.setNavigationItemSelectedListener {
            if(sharedPreferences.getSebagai() == "user"){
                when(it.itemId){
                    R.id.userNavDrawerHome -> {
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.userNavDrawerTentangStroke -> {
                        val intent = Intent(context, TentangStrokeListActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.userNavDrawerGaleriHerbal -> {
                        val intent = Intent(context, GaleriHerbalMainActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.userNavDrawerTerapi -> {
                        val intent = Intent(context, TerapiListActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.userNavDrawerMenuSehat -> {
                        val intent = Intent(context, MenuSehatActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.userNavDrawerTestimoni -> {
                        val intent = Intent(context, TestimoniActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.userNavDrawerAkun -> {
                        val intent = Intent(context, AkunActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.userBtnKeluar ->{
                        logout(activity)
                    }
                }
            }
            else if(sharedPreferences.getSebagai() == "admin"){
                when(it.itemId){
                    R.id.adminNavDrawerHome -> {
                        val intent = Intent(context, AdminMainActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerTentangStroke -> {
                        val intent = Intent(context, AdminHalamanTentangStrokeActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerGaleriHerbal -> {
                        val intent = Intent(context, AdminHalamanGaleriHerbalActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerTerapi -> {
                        val intent = Intent(context, AdminTerapiActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerMenuSehat -> {
                        val intent = Intent(context, AdminMenuSehatActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerTestimoni -> {
                        val intent = Intent(context, AdminTestimoniActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerAkun -> {
                        val intent = Intent(context, AdminAkunActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminBtnKeluar ->{
                        logout(activity)
                    }
                }

            }
            navigationLayout.setBackgroundColor(R.color.white)
            navigationLayout.closeDrawer(GravityCompat.START)
            true
        }
        // garis 3 navigasi
        igNavigation.setOnClickListener {
            navigationLayout.openDrawer(GravityCompat.START)
        }
    }

    fun logout(activity: Activity){
        sharedPreferences.setLogin(0, "", "","", "","")
        context.startActivity(Intent(context, LoginActivity::class.java))
        activity.finish()

    }
}